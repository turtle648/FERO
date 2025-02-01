# 빌드 단계만 남기고, nginx 부분 제거
FROM node:18
WORKDIR /app
COPY package.json ./
COPY package-lock.json ./
RUN npm install
COPY . .
RUN npm run build