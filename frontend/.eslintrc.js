module.exports = {
  env: {
    browser: true, // 브라우저 환경
    node: true, // Node.js 환경
    jest: true, // Jest 테스트 환경 추가
  },
  extends: [
    "eslint:recommended",
    "plugin:vue/vue3-essential", // Vue 3 ESLint 규칙
  ],
  rules: {
    // 필요에 따라 커스텀 규칙 추가 가능
  },
}
