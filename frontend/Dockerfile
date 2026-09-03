# frontend/Dockerfile
FROM node:18 AS build
WORKDIR /app

# instalacja zależności (cache layer)
COPY package.json package-lock.json ./
RUN npm ci

# kopiujemy resztę BEZ node_modules
COPY . .

# build vite
RUN npm run build

# nginx stage
FROM nginx:stable
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx-ssl.conf /etc/nginx/conf.d/default.conf

EXPOSE 3000
CMD ["nginx", "-g", "daemon off;"]
