## Why

JobPilot AI needs to move from planning documents and visual prototypes into an implementable product baseline. This change defines the first production-ready MVP contract for a job-centered AI preparation system that starts with the computer/Internet domain while keeping the architecture open for future multi-industry expansion.

## What Changes

- Create the initial full-stack JobPilot AI application structure with backend, frontend, shared documentation, and Docker Compose deployment assets.
- Add user registration, login, JWT authentication, current-user access, and logout support.
- Add resume intake for text, text-based PDF, and DOCX files, including parsed content editing and default resume selection.
- Add job post management with industry, job direction, JD content, status, and job-detail workflows.
- Add AI battle card generation from resume + JD, including structured output, persistence, prompt versioning, AI call logging, and Redis request caching.
- Add public question bank retrieval before AI question generation, with Qdrant semantic search, tag/quality ranking, and candidate-question intake for AI-generated questions.
- Add user question practice with answer submission, AI scoring, optimized answers, follow-up questions, and mastery status management.
- Add personal knowledge base storage separated from the public question bank.
- Add application records and AI-assisted application review.
- Add development and production Docker Compose support for MySQL, Redis, Qdrant, backend, frontend, and Nginx.

## Capabilities

### New Capabilities

- `user-auth`: User registration, login, logout, JWT authentication, and current-user access.
- `resume-management`: Resume upload, text resume entry, parsing, versioning, editing, listing, deletion, and default resume selection.
- `job-management`: Job post CRUD, JD storage, industry/job-direction metadata, and job status tracking.
- `battle-card`: AI-generated job battle cards based on a resume and job JD, including structured analysis and caching.
- `question-workflow`: Public question bank retrieval, Qdrant semantic search, AI supplement generation, candidate-question handling, and user-specific question assignment.
- `practice-evaluation`: User answer practice, AI scoring, optimized answers, follow-up questions, practice history, and mastery status updates.
- `personal-knowledge-base`: Personal knowledge items created from practice, manual notes, and reviews, with categories, tags, search, and source links.
- `application-review`: Application records, job/resume/battle-card links, application status updates, results, failure reasons, and AI review suggestions.
- `deployment-operations`: Docker Compose development and production topology, environment configuration, service communication, and deployment documentation.

### Modified Capabilities

- None.

## Impact

- Backend: Java 17, Spring Boot 3, Spring Security, MyBatis-Plus, MySQL 8, Redis, Qdrant client integration, file parsing libraries, AI and embedding provider clients.
- Frontend: React, Vite, React Router, Axios, Ant Design-inspired workbench UI, auth-aware routing, and MVP pages for the complete preparation flow.
- Data: Initial MySQL schema for users, resumes, jobs, battle cards, question bank, candidate questions, user questions, practices, knowledge items, application records, and AI call logs.
- Infrastructure: Docker Compose files for development dependencies and production services, backend/frontend Dockerfiles, Nginx frontend serving and API proxying, `.env.example`, and README deployment instructions.
- Product scope: MVP intentionally excludes OCR, scanned PDF parsing, automatic job crawling, automatic applications, payment, WeChat login, browser extension, voice interviews, and complex agent orchestration.
