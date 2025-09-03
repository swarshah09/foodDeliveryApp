/** @type {import('tailwindcss').Config} */
export default {
  content: [
    './index.html',
    './src/**/*.{js,jsx,ts,tsx}',
  ],
  theme: {
    extend: {
      colors: {
        brand: {
          50: '#f5f8ff',
          100: '#eaf0ff',
          200: '#cfdcff',
          300: '#a5bdff',
          400: '#7593ff',
          500: '#4f6dff',
          600: '#2c48ff',
          700: '#1d34db',
          800: '#172aa8',
          900: '#152784'
        }
      }
    },
  },
  plugins: [],
}

