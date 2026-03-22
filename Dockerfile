# ==============================================================
# SkillConnect — Single Container (Frontend + Backend)
# ==============================================================

# --- Stage 1: Build React Frontend ---
FROM node:18-alpine AS frontend-build
WORKDIR /app/frontend

# Install pnpm
RUN corepack enable && corepack prepare pnpm@10.12.1 --activate

# Install dependencies first (for layer caching)
COPY frontend/package.json frontend/pnpm-lock.yaml ./
RUN pnpm install --frozen-lockfile

# Copy source and build
COPY frontend/ ./
RUN pnpm build


# --- Stage 2: Build Java Spring Boot Backend ---
FROM eclipse-temurin:21-jdk-alpine AS backend-build
WORKDIR /app/backend

# Copy Maven wrapper and pom.xml first (for layer caching)
COPY backend/.mvn .mvn
COPY backend/mvnw backend/pom.xml ./
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline

# Copy source and build
COPY backend/src ./src
RUN ./mvnw clean package -DskipTests


# --- Stage 3: Final Runtime Image ---
FROM eclipse-temurin:21-jre

# Install Nginx and Supervisor
RUN apt-get update && \
    apt-get install -y --no-install-recommends nginx supervisor && \
    rm -rf /var/lib/apt/lists/*

# Copy built frontend → Nginx web root
COPY --from=frontend-build /app/frontend/dist /var/www/html

# Copy built backend JAR
COPY --from=backend-build /app/backend/target/backend-0.0.1-SNAPSHOT.jar /app/backend.jar

# Copy Nginx config
COPY nginx-default.conf /etc/nginx/sites-available/default

# Copy Supervisor config
COPY supervisord.conf /etc/supervisor/conf.d/supervisord.conf

# Environment variables for Neon DB (pass at runtime via docker run -e)
ENV DB_URL=""
ENV DB_USERNAME=""
ENV DB_PASSWORD=""

EXPOSE 80

CMD ["/usr/bin/supervisord", "-c", "/etc/supervisor/conf.d/supervisord.conf"]
