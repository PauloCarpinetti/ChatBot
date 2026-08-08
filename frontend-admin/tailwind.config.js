/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        background: '#0f172a',
        surface: 'rgba(30, 41, 59, 0.7)',
        primary: '#3b82f6',
        secondary: '#10b981',
        textPrimary: '#f8fafc',
        textSecondary: '#94a3b8'
      },
      backdropBlur: {
        md: '12px',
      }
    },
  },
  plugins: [],
}
