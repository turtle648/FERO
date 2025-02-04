const fs = require('fs');
const path = require('path');
const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    host: '0.0.0.0',  // 모든 IP에서의 접근 허용
    allowedHosts: 'all',  // 모든 호스트 허용
    https: {
      key: fs.readFileSync(path.join(__dirname, 'certs/localhost.key')),  // 개인 키 경로
      cert: fs.readFileSync(path.join(__dirname, 'certs/localhost.crt')),  // SSL 인증서 경로
    },
    port: 5173,  // 원하는 포트로 설정
  },
});
