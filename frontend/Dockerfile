# 'AS build-stage'로 스테이지 이름을 지정
FROM node:18 AS build-stage  
WORKDIR /app
COPY package.json ./
COPY package-lock.json ./
RUN npm install
COPY . .
RUN npm run build

# Serve stage
FROM nginx:alpine
# Copy dist to Nginx
COPY --from=build-stage /app/dist /usr/share/nginx/html  
# Copy your Nginx config
COPY nginx.conf /etc/nginx/conf.d/default.conf 