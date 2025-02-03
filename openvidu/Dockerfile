### Stage 2: Build the backend
FROM node:20 AS build-backend

USER node

WORKDIR /app/backend

# First copy package files
COPY --chown=node:node app/package*.json ./

RUN npm install

# Copy the rest of the application
COPY --chown=node:node app/ ./

# Create dist/src directory and copy serve.js to server.js
RUN mkdir -p dist/src && \
    cp serve.js dist/src/server.js

### Stage 3: Final production image
FROM node:20-alpine AS production

WORKDIR /opt/openvidu-complete-app

COPY --from=build-backend /app/backend/dist ./dist
COPY --from=build-backend /app/backend/package*.json ./

RUN npm install --production && npm cache clean --force

COPY ./entrypoint.sh /usr/local/bin/entrypoint.sh

RUN chmod +x /usr/local/bin/entrypoint.sh && \
    chown -R node:node /opt/openvidu-complete-app

EXPOSE $SERVER_PORT

CMD ["/usr/local/bin/entrypoint.sh"]