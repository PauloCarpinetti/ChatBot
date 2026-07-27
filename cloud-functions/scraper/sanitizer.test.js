const { sanitizeHtml, MAX_TEXT_LENGTH } = require('./sanitizer');

describe('HTML Sanitization', () => {
    it('should remove script and style tags', async () => {
        const html = `
            <html>
                <head>
                    <style>body { color: red; }</style>
                    <script>alert("test");</script>
                </head>
                <body>
                    <p>Important content.</p>
                </body>
            </html>
        `;
        const text = await sanitizeHtml(html);
        expect(text).toBe('Important content.');
    });

    it('should remove header, footer, nav, aside', async () => {
        const html = `
            <body>
                <header>Header content</header>
                <nav>Navigation links</nav>
                <aside>Sidebar content</aside>
                <article>Main article content</article>
                <footer>Footer content</footer>
            </body>
        `;
        const text = await sanitizeHtml(html);
        expect(text).toBe('Main article content');
    });

    it('should truncate text that exceeds the max length', async () => {
        const veryLongText = 'a'.repeat(MAX_TEXT_LENGTH + 100);
        const html = `<body><p>${veryLongText}</p></body>`;
        
        const text = await sanitizeHtml(html);
        
        expect(text.length).toBe(MAX_TEXT_LENGTH + 3); // +3 for "..."
        expect(text.endsWith('...')).toBe(true);
    });

    it('should normalize whitespace', async () => {
        const html = `
            <body>
                <p>Hello      World</p>
                <p>This    is \n\n   a test.</p>
            </body>
        `;
        const text = await sanitizeHtml(html);
        expect(text).toBe('Hello World This is a test.');
    });
});
