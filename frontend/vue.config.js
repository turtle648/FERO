const { defineConfig } = require('@vue/cli-service');

const devConfig = {
  transpileDependencies: true,
  devServer: process.env.NODE_ENV === 'development' ? {
    host: '0.0.0.0',
    allowedHosts: 'all',
    // https: (() => {
    //   const fs = require('fs');
    //   const path = require('path');
    //   return {
    //     key: fs.readFileSync(path.join(__dirname, 'certs/localhost.key')),
    //     cert: fs.readFileSync(path.join(__dirname, 'certs/localhost.crt')),
    //   };
    // })(),
    https: false,
    port: 5173,
  } : {}
};

const prodConfig = {
  transpileDependencies: true,
};

module.exports = defineConfig(
  process.env.NODE_ENV === 'development' ? devConfig : prodConfig
);
