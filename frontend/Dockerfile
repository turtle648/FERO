# Build stage
FROM node:18 AS build-stage
WORKDIR /app

# Clear npm cache first
RUN npm cache clean --force

# Copy package files
COPY package*.json ./
RUN npm install

# Copy project files
COPY . .

# Clean and rebuild
RUN rm -rf dist node_modules/.cache
RUN npm run build

# Serve stage
FROM nginx:alpine
# Copy dist to Nginx
COPY --from=build-stage /app/dist /usr/share/nginx/html
# Add nginx configuration if needed
# COPY nginx.conf /etc/nginx/nginx.conf
