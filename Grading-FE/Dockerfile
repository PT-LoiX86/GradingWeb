# This is the newer version

FROM node:20-alpine AS build

WORKDIR /app

# Copy package files first for better caching
COPY package*.json ./
RUN npm ci --silent

# Copy source code and build
COPY . .
RUN npm run build

## run stage ##
FROM nginx:alpine

COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]