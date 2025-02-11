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
    name: 'My App',
    themeColor: '#4DBA87',
    msTileColor: '#000000',
    manifestOptions: {
      start_url: '.',
      display: 'standalone',
      icons: [
        {
          src: 'img/icons/android-chrome-192x192.png',
          sizes: '192x192',
          type: 'image/png',
        },
        {
          src: 'img/icons/android-chrome-512x512.png',
          sizes: '512x512',
          type: 'image/png',
        },
      ],
    },
  },
};

const prodConfig = {
  transpileDependencies: true,
  pwa: {
    name: 'My App',
    themeColor: '#4DBA87',
    msTileColor: '#000000',
    manifestOptions: {
      start_url: '.',
      display: 'standalone',
      icons: [
        {
          src: 'img/icons/android-chrome-192x192.png',
          sizes: '192x192',
          type: 'image/png',
        },
        {
          src: 'img/icons/android-chrome-512x512.png',
          sizes: '512x512',
          type: 'image/png',
        },
      ],
    },
  },
};

module.exports = defineConfig(
  process.env.NODE_ENV === 'development' ? devConfig : prodConfig
);
