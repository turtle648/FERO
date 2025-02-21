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
        bounce: {
          "0%, 100%": { transform: "translateY(0)" },
          "50%": { transform: "translateY(-25%)" },
        },
        tamagotchi: {
          "0%, 100%": { transform: "scale(1)" },
          "50%": { transform: "scale(1.2)" },
        },
        "wave-text": {
          "0%, 100%": { transform: "translateY(0em)" },
          "60%": { transform: "translateY(-0.6em)" },
        },
        "pixel-wave": {
          "0%, 100%": { transform: "translateY(0px)" },
          "20%": { transform: "translateY(-4px)" },
          "40%": { transform: "translateY(-8px)" },
          "60%": { transform: "translateY(-4px)" },
        },
      },
      animation: {
        blink: "blink 1.5s infinite",
        "bounce-text": "bounce 1s ease infinite",
        tamagotchi: "tamagotchi 2s ease-in-out infinite",
        "wave-text": "wave-text 1s ease-in-out infinite",
        "pixel-wave": "pixel-wave 1s steps(4) infinite",
      },
      fontFamily: {
        pixel: ["PixelFont", "monospace"],
        dgm: ["DungGeunMo", "sans-serif"],
      },
      colors: {
        "custom-blue": "#1E40AF",
        "custom-yellow": "#EDAE0A",
      },
      zIndex: {
        100: 100,
        9000: 9000,
      },
    },
  },
  plugins: [],
}
