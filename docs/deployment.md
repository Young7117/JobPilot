# Deployment Guide

## Production-Style Docker Compose

Copy the environment template:

```bash
cp .env.example .env
```

Fill at least:

- `MYSQL_ROOT_PASSWORD`
- `MYSQL_PASSWORD`
- `JWT_SECRET`
- AI provider variables when using a real model instead of `mock`

Build and start:

```bash
docker compose --env-file .env up -d --build
```

For Docker Compose v1, run:

```bash
docker-compose up -d --build
```

Check logs:

```bash
docker compose logs -f backend
docker compose logs -f frontend
```

For Docker Compose v1:

```bash
docker-compose logs -f backend
docker-compose logs -f frontend
```

The frontend is exposed on port `80`. It serves the React app and proxies `/api` to the backend service by Docker service name.

## Data Volumes

Compose creates persistent volumes for:

- `jobpilot-mysql-data`
- `jobpilot-redis-data`
- `jobpilot-qdrant-data`

Back up MySQL before production schema changes.

## Demo Reset

For demo-only environments:

```bash
docker compose down -v
docker compose --env-file .env up -d --build
```

Do not run this against production data.
