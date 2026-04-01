FROM node:lts-alpine AS builder
WORKDIR /build
COPY package*.json ./
RUN npm install
COPY . .
RUN npx ng build --configuration production


FROM nginx:stable-alpine3.23
WORKDIR /usr/share/nginx/html

COPY --from=builder /build/dist/frontend/browser .
COPY ./nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
