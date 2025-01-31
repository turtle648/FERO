module.exports = {
  testEnvironment: "jest-environment-jsdom", // 브라우저 환경 설정
  moduleFileExtensions: ["js", "json", "vue"], // 처리할 파일 확장자
  transform: {
    "^.+\\.vue$": "@vue/vue3-jest", // .vue 파일 변환
    "^.+\\.js$": "babel-jest", // .js 파일 변환
  },
  transformIgnorePatterns: ["/node_modules/"], // node_modules 제외
  testEnvironmentOptions: {
    customExportConditions: ["node", "node-addons"], // Vue 3의 조건부 export 지원
  },
}
