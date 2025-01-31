# 1. Vue 빌드 단계
FROM node:18 AS build-stage
WORKDIR /app
COPY package.json ./
COPY package-lock.json ./
RUN npm install
COPY . .
RUN npm run build

# 2. Nginx 서버에서 Vue 배포
FROM nginx:1.23.3
COPY --from=build-stage /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]