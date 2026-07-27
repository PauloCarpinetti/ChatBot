const mysql = require('mysql2/promise');
require('dotenv').config({ path: '../../.env' }); // root env

async function setupDatabase() {
    const DB_CONFIG = {
        host: process.env.DB_HOST || 'localhost',
        port: process.env.DB_PORT || 3307,
        user: process.env.DB_USER || 'root',
        password: process.env.DB_PASSWORD || 'Hepta2020#', // default fallback to the one in .env
        database: process.env.DB_NAME || 'chat_tenant_db',
    };

    let connection;
    try {
        console.log('Connecting to the database on port 3307...');
        connection = await mysql.createConnection(DB_CONFIG);
        
        console.log('Creating table scraping_jobs if it does not exist...');
        await connection.execute(`
            CREATE TABLE IF NOT EXISTS scraping_jobs (
                id VARCHAR(255) PRIMARY KEY,
                tenant_id VARCHAR(255) NOT NULL,
                url TEXT NOT NULL,
                status VARCHAR(50) NOT NULL,
                retry_count INT DEFAULT 0,
                last_error TEXT,
                next_run_at DATETIME,
                created_at DATETIME NOT NULL,
                updated_at DATETIME
            );
        `);
        console.log('Table scraping_jobs is ready.');

        console.log('Inserting test job...');
        await connection.execute(`
            INSERT IGNORE INTO scraping_jobs (id, tenant_id, url, status, retry_count, created_at) 
            VALUES ('job-teste-01', 'meu-tenant-1', 'https://en.wikipedia.org/wiki/Web_scraping', 'PENDING', 0, NOW());
        `);
        console.log('Test job inserted (or already exists).');
        
    } catch (err) {
        console.error('Error:', err.message);
    } finally {
        if (connection) {
            await connection.end();
        }
        console.log('Done.');
    }
}

setupDatabase();
