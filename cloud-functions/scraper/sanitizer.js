const cheerio = require('cheerio');

const MAX_TEXT_LENGTH = 8000;

async function sanitizeHtml(html) {
    const $ = cheerio.load(html);
    
    // Remove unnecessary tags
    $('script, style, noscript, nav, footer, header, aside, iframe, svg').remove();
    
    // Extract text
    let text = $('body').text();
    
    // Clean up whitespace
    text = text.replace(/\s+/g, ' ').trim();
    
    // Truncate to context window limit
    if (text.length > MAX_TEXT_LENGTH) {
        text = text.substring(0, MAX_TEXT_LENGTH) + "...";
    }
    
    return text;
}

module.exports = { sanitizeHtml, MAX_TEXT_LENGTH };
