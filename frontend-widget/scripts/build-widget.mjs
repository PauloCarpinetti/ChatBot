import * as esbuild from 'esbuild';
import fs from 'fs';
import path from 'path';

async function buildWidget() {
  console.log('Building widget...');
  
  try {
    await esbuild.build({
      entryPoints: ['src/widget.tsx'],
      bundle: true,
      minify: true,
      format: 'iife', // Immediately Invoked Function Expression
      outfile: 'public/embed.js',
      loader: {
        '.tsx': 'tsx',
        '.ts': 'ts',
      },
      // Configurar para carregar variáveis de ambiente
      define: {
        'process.env.NODE_ENV': '"production"',
      },
      conditions: ['style'],
    });
    
    // Lê o CSS gerado e injeta no JS para ter arquivo único
    const cssPath = 'public/embed.css';
    if (fs.existsSync(cssPath)) {
      const cssContent = fs.readFileSync(cssPath, 'utf8');
      const jsPath = 'public/embed.js';
      const jsContent = fs.readFileSync(jsPath, 'utf8');
      
      const cssInjection = `
(function(){
  var style = document.createElement('style');
  style.textContent = ${JSON.stringify(cssContent)};
  document.head.appendChild(style);
})();
`;
      fs.writeFileSync(jsPath, cssInjection + '\n' + jsContent);
      fs.unlinkSync(cssPath);
      console.log('✅ CSS inlined into embed.js');
    }
    
    console.log('✅ Widget built successfully: public/embed.js');
  } catch (err) {
    console.error('❌ Build failed:', err);
    process.exit(1);
  }
}

buildWidget();
