import fs from 'fs';
import path from 'path';

// Define the source file
const sourceFile = path.resolve('public', 'embed.js');

// Define destinations
const destinations = [
  path.resolve('../Clients/laravel-biohealth/public/widget/embed.js'),
  path.resolve('../Clients/wordpress-initech/wp-content/themes/twentytwentyfour/assets/embed.js'),
  path.resolve('../Clients/nextjs-acmecorp/public/widget/embed.js')
];

function ensureDir(filePath) {
  const dir = path.dirname(filePath);
  if (!fs.existsSync(dir)) {
    fs.mkdirSync(dir, { recursive: true });
  }
}

console.log('Deploying widget to clients...');

if (!fs.existsSync(sourceFile)) {
  console.error('❌ Source file not found: ' + sourceFile);
  process.exit(1);
}

destinations.forEach(dest => {
  try {
    ensureDir(dest);
    fs.copyFileSync(sourceFile, dest);
    console.log(`✅ Copied to ${dest}`);
  } catch (err) {
    console.error(`❌ Failed to copy to ${dest}:`, err.message);
  }
});
