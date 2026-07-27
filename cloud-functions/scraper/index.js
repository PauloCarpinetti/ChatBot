require('dotenv').config();
const functions = require('@google-cloud/functions-framework');
const mysql = require('mysql2/promise');
const axios = require('axios');
const { sanitizeHtml } = require('./sanitizer');

const DB_CONFIG = {
    host: process.env.DB_HOST || 'localhost',
    port: process.env.DB_PORT || 3307,
    user: process.env.DB_USER || 'root',
    password: process.env.DB_PASSWORD || 'root',
    database: process.env.DB_NAME || 'chat_tenant_db',
};

const BACKEND_URL = process.env.BACKEND_URL || 'http://localhost:8081/api/documents/text';
const MAX_RETRIES = 3;

functions.http('processScrapingJobs', async (req, res) => {
    let connection;
    try {
        connection = await mysql.createConnection(DB_CONFIG);
        
        // Find one job to process
        const [rows] = await connection.execute(
            `SELECT * FROM scraping_jobs 
             WHERE status IN ('PENDING', 'PENDING_RETRY') 
             AND (next_run_at IS NULL OR next_run_at <= NOW()) 
             ORDER BY created_at ASC LIMIT 1`
        );

        if (rows.length === 0) {
            return res.status(200).send('No pending jobs found.');
        }

        const job = rows[0];

        // Mark as PROCESSING
        await connection.execute(
            `UPDATE scraping_jobs SET status = 'PROCESSING' WHERE id = ?`,
            [job.id]
        );

        try {
            console.log(`Processing job ${job.id} for URL: ${job.url}`);
            
            // 1. Fetch HTML
            const response = await axios.get(job.url, { timeout: 15000 });
            
            // 2. Sanitize & Truncate
            const cleanText = await sanitizeHtml(response.data);
            
            // 3. Send to Backend
            await axios.post(BACKEND_URL, {
                tenantId: job.tenant_id,
                text: cleanText,
                sourceUrl: job.url
            });

            // 4. Mark as COMPLETED
            await connection.execute(
                `UPDATE scraping_jobs SET status = 'COMPLETED', updated_at = NOW() WHERE id = ?`,
                [job.id]
            );

            res.status(200).send(`Job ${job.id} completed successfully.`);
        } catch (error) {
            console.error(`Error processing job ${job.id}:`, error.message);
            
            const newRetryCount = job.retry_count + 1;
            
            if (newRetryCount >= MAX_RETRIES) {
                await connection.execute(
                    `UPDATE scraping_jobs SET status = 'FAILED', retry_count = ?, last_error = ?, updated_at = NOW() WHERE id = ?`,
                    [newRetryCount, error.message, job.id]
                );
            } else {
                // Exponential backoff: 1min, 5min, 15min
                const delayMinutes = newRetryCount === 1 ? 1 : (newRetryCount === 2 ? 5 : 15);
                await connection.execute(
                    `UPDATE scraping_jobs SET status = 'PENDING_RETRY', retry_count = ?, last_error = ?, next_run_at = DATE_ADD(NOW(), INTERVAL ? MINUTE), updated_at = NOW() WHERE id = ?`,
                    [newRetryCount, error.message, delayMinutes, job.id]
                );
            }
            
            res.status(500).send(`Job ${job.id} failed: ${error.message}`);
        }

    } catch (err) {
        console.error('Database connection error:', err);
        res.status(500).send('Database connection error');
    } finally {
        if (connection) {
            await connection.end();
        }
    }
});
