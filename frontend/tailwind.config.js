/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./index.html", "./src/**/*.{vue,js,ts,jsx,tsx}"],
  theme: {
    extend: {
      screens: {
        xs: { max: "480px" },
        sm: { max: "768px" },
      },
      keyframes: {
        blink: {
          "0%, 100%": { opacity: "1" },
          "50%": { opacity: "0" },
        },
      },
      animation: {
        blink: "blink 1.5s infinite",
      },
      fontFamily: {
        pixel: ["PixelFont", "monospace"],
        dgm: ["DungGeunMo", "sans-serif"],
      },
      colors: {
        "custom-blue": "#1E40AF",
        "custom-yellow": "#EDAE0A",
      },
    },
  },
  plugins: [],
}
