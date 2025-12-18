# =========================
# 1️⃣ Build stage (Vite build)
# =========================
FROM node:20-alpine AS build
WORKDIR /app

# 1) 의존성 파일만 먼저 복사 (캐시 최적화)
COPY package.json package-lock.json ./
RUN npm ci

# 2) 소스 복사 후 빌드
COPY . .
RUN npm run build

# =========================
# 2️⃣ Run stage (Nginx serve)
# =========================
FROM nginx:alpine

# Vite 빌드 결과(dist)를 nginx 정적 경로로 복사
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
