const { defineConfig } = require("@vue/cli-service");
const fs = require("fs");
const webpack = require("webpack");
const path = require("path");

const devConfig = {
  transpileDependencies: true,
  devServer:
    process.env.APP_ENV === "development"
      ? {
          host: "0.0.0.0",
          allowedHosts: "all",
          https: {
            key: fs.readFileSync(path.join(__dirname, "certs/localhost.key")),
            cert: fs.readFileSync(path.join(__dirname, "certs/localhost.crt")),
          },
          // https: false,
          port: 5173,
        }
      : {
          host: "0.0.0.0",
          allowedHosts: "all",
          https: {
            key: fs.readFileSync(path.join(__dirname, "certs/localhost.key")),
            cert: fs.readFileSync(path.join(__dirname, "certs/localhost.crt")),
          },
          // https: false,
          port: 5173,
        },
  pwa: {
    name: "FERO",
    themeColor: "#2D90A6",
    msTileColor: "#000000",
    manifestOptions: {
      start_url: ".",
      display: "standalone",
      icons: [
        {
          src: "/android-icon-192x192.png",
          sizes: "192x192",
          type: "image/png",
        },
        {
          src: "/android-icon-144x144.png",
          sizes: "144x144",
          type: "image/png",
        },
      ],
    },
  },
  configureWebpack: {
    plugins: [
      new webpack.DefinePlugin({
        __VUE_PROD_HYDRATION_MISMATCH_DETAILS__: JSON.stringify(false), // ✅ Feature flag 명시적 정의
      }),
    ],
  },
};

const prodConfig = {
  transpileDependencies: true,
  pwa: {
    name: "FERO",
    themeColor: "#2D90A6",
    msTileColor: "#000000",
    manifestOptions: {
      start_url: "/",
      display: "standalone",
      icons: [
        {
          src: "/android-icon-192x192.png",
          sizes: "192x192",
          type: "image/png",
        },
        {
          src: "/android-icon-144x144.png",
          sizes: "144x144",
          type: "image/png",
        },
      ],
    },
  },
  configureWebpack: {
    plugins: [
      new webpack.DefinePlugin({
        __VUE_PROD_HYDRATION_MISMATCH_DETAILS__: JSON.stringify(false), // ✅ Feature flag 명시적 정의
      }),
    ],
  },
};

module.exports = defineConfig(
  process.env.NODE_ENV === "development" ? devConfig : prodConfig
);
