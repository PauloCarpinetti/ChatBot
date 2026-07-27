const axios = require('axios');
const mysql = require('mysql2/promise');
const functions = require('@google-cloud/functions-framework');

const { processScrapingJobs } = require('./index.js');

jest.mock('axios');
jest.mock('mysql2/promise');

describe('Scraping Cloud Function - Retries and Resiliency', () => {
    let mockConnection;

    beforeEach(() => {
        mockConnection = {
            execute: jest.fn(),
            end: jest.fn(),
        };
        mysql.createConnection.mockResolvedValue(mockConnection);
        jest.clearAllMocks();
    });

    it('should exponentially backoff when axios fails', async () => {
        // Arrange
        const mockJob = {
            id: 'job-123',
            tenant_id: 'tenant-456',
            url: 'http://example.com/fail',
            retry_count: 1
        };

        // Simulate DB finding a pending job
        mockConnection.execute.mockResolvedValueOnce([[mockJob]]);
        
        // Simulate DB UPDATE status = 'PROCESSING'
        mockConnection.execute.mockResolvedValueOnce([]);

        // Force Axios to throw an error (e.g. 429 Too Many Requests or 500)
        axios.get.mockRejectedValue(new Error('500 Internal Server Error'));

        // Simulate DB UPDATE for exponential backoff retry
        mockConnection.execute.mockResolvedValueOnce([]);

        // Mock req/res for Cloud Function
        const req = {};
        const res = {
            status: jest.fn().mockReturnThis(),
            send: jest.fn()
        };

        // Act
        await processScrapingJobs(req, res);

        // Assert
        expect(res.status).toHaveBeenCalledWith(500);
        
        // Verify exponential backoff logic (since it was retry_count=1, next should be 5 mins)
        expect(mockConnection.execute).toHaveBeenNthCalledWith(3,
            expect.stringContaining("UPDATE scraping_jobs SET status = 'PENDING_RETRY'"),
            [2, '500 Internal Server Error', 5, 'job-123']
        );
    });

    it('should fail permanently after MAX_RETRIES', async () => {
        // Arrange
        const mockJob = {
            id: 'job-123',
            tenant_id: 'tenant-456',
            url: 'http://example.com/fail-max',
            retry_count: 2 // Assuming MAX_RETRIES = 3, so next is 3 -> FAIL
        };

        mockConnection.execute.mockResolvedValueOnce([[mockJob]]);
        mockConnection.execute.mockResolvedValueOnce([]);
        axios.get.mockRejectedValue(new Error('Timeout'));

        const req = {};
        const res = {
            status: jest.fn().mockReturnThis(),
            send: jest.fn()
        };

        await processScrapingJobs(req, res);

        expect(mockConnection.execute).toHaveBeenNthCalledWith(3,
            expect.stringContaining("UPDATE scraping_jobs SET status = 'FAILED'"),
            [3, 'Timeout', 'job-123']
        );
    });
});
