module.exports = {
  testEnvironment: "jsdom", // 브라우저 환경 시뮬레이션
  moduleFileExtensions: ["js", "json", "vue"], // 확장자 처리
  transform: {
    "^.+\\.vue$": "vue-jest", // .vue 파일 처리
    "^.+\\.js$": "babel-jest", // .js 파일 처리
  },
  transformIgnorePatterns: ["/node_modules/"], // node_modules 제외
}
