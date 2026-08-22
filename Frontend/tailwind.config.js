/** @type {import('tailwindcss').Config} */
export default {
  darkMode: 'class',
  content: ['./index.html', './src/**/*.{js,jsx}'],
  theme: {
    extend: {
      colors: {
        ink: {
          950: '#0A0F18',
          900: '#0E1420',
          800: '#161D2E',
          700: '#212B40',
          border: '#283350',
          text: '#E9ECF1',
          muted: '#8A93A6',
        },
        paper: {
          50: '#F6F1E7',
          100: '#FFFDF8',
          border: '#E3DCC9',
          text: '#181611',
          muted: '#726C5C',
        },
        amber: {
          DEFAULT: '#FFB020',
          dim: '#C98A18',
        },
        teal: {
          DEFAULT: '#3FBFAD',
          dim: '#2B8E80',
        },
        alert: {
          DEFAULT: '#E5484D',
          dim: '#B23639',
        },
      },
      fontFamily: {
        display: ['Fraunces', 'ui-serif', 'Georgia', 'serif'],
        body: ['Inter', 'ui-sans-serif', 'system-ui', 'sans-serif'],
        mono: ['"JetBrains Mono"', 'ui-monospace', 'monospace'],
      },
      letterSpacing: {
        wire: '0.14em',
      },
      keyframes: {
        ticker: {
          '0%': { transform: 'translateX(0%)' },
          '100%': { transform: 'translateX(-50%)' },
        },
        blink: {
          '0%, 100%': { opacity: 1 },
          '50%': { opacity: 0.2 },
        },
      },
      animation: {
        ticker: 'ticker 42s linear infinite',
        blink: 'blink 1.1s step-start infinite',
      },
    },
  },
  plugins: [],
}
