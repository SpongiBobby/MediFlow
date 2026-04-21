FROM node:lts-alpine AS builder
WORKDIR /build

COPY frontend/package*.json ./
RUN npm install

COPY frontend/ .
RUN npx ng build --configuration production


FROM nginx:stable-alpine
WORKDIR /usr/share/nginx/html

COPY --from=builder /build/dist/frontend/browser .
COPY nginx/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
