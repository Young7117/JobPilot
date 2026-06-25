# Development Guide

## Prerequisites

- Java 17
- Maven 3.9+
- Node.js 22+
- Docker with Compose v2

## Start Dependencies

```bash
docker compose -f docker-compose.dev.yml up -d
```

For Docker Compose v1:

```bash
docker-compose -f docker-compose.dev.yml up -d
```

Services:

- MySQL: `localhost:3306`
- Redis: `localhost:6379`
- Qdrant: `localhost:6333`

## Backend

```bash
cd backend
mvn spring-boot:run
```

The backend reads database, Redis, Qdrant, JWT, AI, and embedding settings from environment variables. See `.env.example`.

## Frontend

```bash
cd frontend
npm install
npm run dev
```

The frontend runs on `http://localhost:5173` and proxies `/api` to the backend during local development.

## Reset Local Data

Stop Compose and remove volumes:

```bash
docker compose -f docker-compose.dev.yml down -v
```

For Docker Compose v1:

```bash
docker-compose -f docker-compose.dev.yml down -v
```
