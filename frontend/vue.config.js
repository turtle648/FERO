const { defineConfig } = require('@vue/cli-service');
const fs = require('fs');
const path = require('path');

const devConfig = {
  transpileDependencies: true,
  devServer: process.env.NODE_ENV === 'development' ? {
    host: '0.0.0.0',
    allowedHosts: 'all',
    https: {
      key: fs.readFileSync(path.join(__dirname, 'certs/localhost.key')),
      cert: fs.readFileSync(path.join(__dirname, 'certs/localhost.crt')),
    },
    port: 5173,
  } : {},
  pwa: {
    name: 'FERO',
    themeColor: '#2D90A6',
    msTileColor: '#000000',
    manifestOptions: {
      start_url: '.',
      display: 'standalone',
      icons: [
        {
          src: '/android-icon-192x192.png',
          sizes: '192x192',
          type: 'image/png',
        },
        {
          src: '/android-icon-144x144.png',
          sizes: '144x144',
          type: 'image/png',
        },
      ],
    },
  },
};

const prodConfig = {
  transpileDependencies: true,
  pwa: {
    name: 'FERO',
    themeColor: '#2D90A6',
    msTileColor: '#000000',
    manifestOptions: {
      start_url: '.',
      display: 'standalone',
      icons: [
        {
          src: '/android-icon-192x192.png',
          sizes: '192x192',
          type: 'image/png',
        },
        {
          src: '/android-icon-144x144.png',
          sizes: '144x144',
          type: 'image/png',
        },
      ],
    },
  },
};

module.exports = defineConfig(
  process.env.NODE_ENV === 'development' ? devConfig : prodConfig
);
