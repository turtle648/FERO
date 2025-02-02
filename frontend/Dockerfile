FROM node:18
WORKDIR /app
COPY package.json ./
COPY package-lock.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine
# Copy dist to Nginx
COPY --from=build-stage /app/dist /usr/share/nginx/html  
# Copy your Nginx config
COPY nginx.conf /etc/nginx/conf.d/default.conf  