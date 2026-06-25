# JobPilot AI

JobPilot AI is a job-centered AI preparation workbench. The MVP focuses on computer and Internet roles, while keeping the data model generic enough to expand into other industries later.

Core loop:

1. Parse or paste a resume.
2. Create a target job post with JD content.
3. Generate an AI battle card from resume + JD.
4. Retrieve public interview questions first.
5. Generate AI supplement questions only when the public bank is insufficient.
6. Practice answers and receive AI evaluation.
7. Save strong answers and notes into the personal knowledge base.
8. Track application outcomes and generate review suggestions.

## Project Structure

```text
jobpilot-ai
├─ backend                 Spring Boot 3 backend
├─ frontend                React + Vite frontend
├─ deploy                  Deployment and infrastructure files
├─ docs                    Product and design documentation
├─ openspec                OpenSpec changes and project specs
├─ docker-compose.dev.yml  Development dependencies
├─ .env.example            Environment variable template
└─ README.md
```

## Local Development

The intended development setup is:

- Run MySQL, Redis, and Qdrant through Docker Compose.
- Run the backend locally from `backend`.
- Run the frontend locally from `frontend`.

```bash
docker compose -f docker-compose.dev.yml up -d
cd backend && mvn spring-boot:run
cd frontend && npm install && npm run dev
```

If your server uses Docker Compose v1, replace `docker compose` with `docker-compose`.

The current machine must have Java 17 and Docker installed before the backend and Compose checks can pass.

More detail:

- [Development guide](docs/development.md)
- [Deployment guide](docs/deployment.md)

## Environment

Copy `.env.example` to `.env` and fill the AI provider settings before enabling real AI calls.

```bash
cp .env.example .env
```

## MVP Boundaries

Included:

- User registration, login, JWT authentication, and logout.
- Text PDF, DOCX, and pasted text resume intake.
- Job post and JD management.
- Battle-card generation with Redis caching.
- Public question-bank retrieval before AI generation.
- Candidate-question workflow for AI-generated questions.
- Practice, evaluation, personal knowledge base, and application review.

Excluded from MVP:

- OCR and scanned PDF parsing.
- Automatic job crawling or auto-apply.
- Payment, WeChat login, browser extension, voice interview, and complex agent orchestration.
