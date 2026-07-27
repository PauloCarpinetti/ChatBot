const mysql = require('mysql2/promise');
require('dotenv').config({ path: '../../.env' });

async function resetDB() {
    const c = await mysql.createConnection({ 
        host: 'localhost', 
        port: 3307, 
        user: process.env.MYSQL_USER, 
        password: process.env.MYSQL_PASSWORD, 
        database: 'chat_tenant_db' 
    });
    await c.execute(`UPDATE scraping_jobs SET url='http://example.com', status='PENDING', retry_count=0, tenant_id='123e4567-e89b-12d3-a456-426614174000', next_run_at=NULL`);
    await c.end();
    console.log('DB reset');
}

resetDB();
