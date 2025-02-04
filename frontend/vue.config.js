// vue.config.js
const fs = require('fs');
const path = require('path');
const { defineConfig } = require('@vue/cli-service');

const devConfig = {
  transpileDependencies: true,
  devServer: {
    host: '0.0.0.0',
    allowedHosts: 'all',
    https: {
      key: fs.readFileSync(path.join(__dirname, 'certs/localhost.key')),
      cert: fs.readFileSync(path.join(__dirname, 'certs/localhost.crt')),
    },
    port: 5173,
  },
};

const prodConfig = {
  transpileDependencies: true,
  // 프로덕션 환경 설정
};

module.exports = defineConfig(
  process.env.NODE_ENV === 'development' ? devConfig : prodConfig
);

