# 'AS build-stage'로 스테이지 이름을 지정
FROM node:18 AS build-stage  
WORKDIR /app
COPY package.json ./
COPY package-lock.json ./
RUN npm install
COPY . .
RUN npm run build --no-cache

# Serve stage
FROM nginx:alpine
# Copy dist to Nginx
COPY --from=build-stage /app/dist /usr/share/nginx/html  