import type { Config } from 'tailwindcss'

const config: Config = {
  prefix: 'tw-chat-',
  content: [
    './src/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  theme: {
    extend: {
      colors: {
        'chat-primary': 'var(--chat-primary-color, #2563EB)',
        'chat-primary-hover': 'var(--chat-primary-hover, #1D4ED8)',
        'chat-bg': 'var(--chat-bg-color, #FFFFFF)',
        'chat-text': 'var(--chat-text-color, #1F2937)',
      }
    },
  },
  plugins: [],
}
export default config
