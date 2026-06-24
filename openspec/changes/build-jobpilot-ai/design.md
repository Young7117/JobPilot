## Context

JobPilot AI is moving from planning and prototype artifacts into a production-oriented MVP. The product is a job-centered AI preparation workbench: users create resumes, create target jobs, generate AI battle cards, retrieve public interview questions before generating new ones, practice answers, save personal knowledge, and review application outcomes.

The MVP will focus on the computer/Internet domain while keeping domain fields generic enough for future multi-industry expansion. The implementation starts from an empty repository area and must establish backend, frontend, database, AI integration, vector retrieval, caching, and Docker Compose deployment conventions.

Primary stakeholders are job seekers using the product, developers maintaining the full-stack system, and future operators who will curate the public question bank.

## Goals / Non-Goals

**Goals:**

- Deliver a full MVP loop from authentication through resume intake, job management, battle card generation, question generation, practice evaluation, knowledge capture, and application review.
- Keep public question bank data separate from user personal knowledge.
- Use public question retrieval before AI supplement generation to reduce cost and improve question quality.
- Keep AI-generated questions in a candidate-question workflow before they can become public questions.
- Persist structured AI outputs and log AI calls for observability and cost analysis.
- Support local development with backend/frontend running locally and MySQL, Redis, and Qdrant running through Docker Compose.
- Support production-style Docker Compose with backend, frontend, MySQL, Redis, Qdrant, and Nginx.

**Non-Goals:**

- OCR, scanned PDF parsing, image resumes, and legacy `.doc` parsing.
- Automatic job crawling, automatic applications, browser extension, voice interviews, payment, WeChat login, or complex agent orchestration.
- Full admin console for public question-bank moderation; MVP can support database/manual approval or a minimal endpoint.
- Multi-industry content beyond generic data model fields and configuration-ready boundaries.

## Decisions

### Decision: Use a modular Spring Boot monolith for the backend

The backend will use Java 17, Spring Boot 3, Spring Security, MyBatis-Plus, MySQL, Redis, and Qdrant integration in one deployable service. Modules will be separated by package boundaries such as `auth`, `resume`, `job`, `battlecard`, `question`, `practice`, `knowledge`, `application`, and `ai`.

Rationale: The MVP has several connected workflows but does not need distributed-service complexity. A modular monolith keeps transactions, deployment, and local debugging simple while preserving clear module ownership.

Alternative considered: Split services for AI, question bank, and user workflows. This would add operational overhead too early and slow MVP delivery.

### Decision: Use React + Vite with an Ant Design-inspired workbench UI

The frontend will use React, Vite, React Router, Axios, and a restrained workbench layout. It will include pages for login/register, dashboard, resumes, jobs, battle cards, questions, knowledge, applications, and settings.

Rationale: The product is a repeated-use preparation workspace, so dense but calm operational UI is more appropriate than a marketing-style landing page.

Alternative considered: Build a highly custom UI system first. This would delay core workflow implementation; custom polish can grow after the MVP loop works.

### Decision: Store structured AI outputs in MySQL and cache idempotent AI requests in Redis

Battle cards, generated questions, answer evaluations, knowledge formatting, and application reviews will persist structured fields plus raw AI result where useful. Redis will cache repeated battle-card and question-generation requests by request hash and prompt version.

Rationale: AI results must be inspectable, reusable, and cost-controlled. Structured persistence supports UI display and later analytics; Redis caching avoids duplicate model calls.

Alternative considered: Store AI output only as raw text. This would simplify ingestion but make UI rendering, search, and later improvement much harder.

### Decision: Use Qdrant for semantic question retrieval and duplicate detection

Public questions will be embedded and indexed in Qdrant. Question generation will first search by battle-card-derived query text and combine semantic score with tags, difficulty, and quality score. Candidate AI questions will use similarity checks before entering review.

Rationale: The core product differentiation is public question retrieval before AI supplement generation. Vector search makes the retrieval less brittle than tag-only matching.

Alternative considered: MySQL keyword/tag search only. This is simpler but weak for semantically similar questions and duplicate detection.

### Decision: Keep public question bank and personal knowledge as separate concepts

`question_bank` and `candidate_question` represent reusable shared question assets. `personal_knowledge_item` represents user-owned answers, notes, project narratives, mistakes, and reviews.

Rationale: Mixing these concepts would pollute public data with personal context and make quality control difficult.

Alternative considered: Store all learning artifacts in one generic table. This would reduce schema count but blur permissions, quality, and retrieval semantics.

### Decision: Keep industry and job direction generic from day one

Tables and UI filters will include `industry`, `job_direction`, `tags`, `question_type`, and `category`. The MVP will default to computer/Internet values, but these fields will not be hardcoded to software roles only.

Rationale: The long-term product direction is multi-industry job preparation. Generic naming avoids early schema rewrites.

Alternative considered: Hardcode Java/frontend/AI roles for the MVP. This would speed first screens but undermine future expansion.

## Risks / Trade-offs

- [AI JSON output may be malformed] → Use prompt versions, strict JSON parsing, raw result persistence, retry paths, and UI error states.
- [AI cost may grow quickly] → Use public question retrieval first, Redis request caching, user-level rate limiting, and `ai_call_log` cost tracking.
- [Question quality may degrade] → Route AI-generated questions to `candidate_question`, compute duplicate and quality scores, and require approval before public promotion.
- [Qdrant adds operational complexity] → Keep Qdrant isolated to question search and duplicate checks; provide Docker Compose defaults and graceful fallback errors.
- [Resume parsing may fail for unsupported files] → Clearly support only text-based PDF, DOCX, and pasted text; provide manual edit fallback.
- [MVP scope may expand] → Keep excluded features explicit and prioritize the end-to-end preparation loop.

## Migration Plan

1. Initialize repository structure for backend, frontend, docs, deploy files, and Docker Compose.
2. Add database migration/init SQL for the MVP schema.
3. Implement authentication and security first so all user-owned resources can be scoped by user.
4. Implement resume and job CRUD before AI workflows.
5. Add battle-card generation with Redis caching and AI call logging.
6. Seed public questions, index them in Qdrant, then add retrieval and AI supplement generation.
7. Add practice, evaluation, knowledge capture, and application review.
8. Add production Docker Compose, `.env.example`, and README deployment instructions.

Rollback strategy: because this is a new application baseline, rollback means reverting the change branch or dropping/recreating development Docker volumes. Production rollout should start with empty databases and explicit backups before schema changes.

## Open Questions

- Which AI and embedding provider will be used for the first implementation?
- Should candidate-question approval be a database/manual operation only in MVP, or should a minimal protected endpoint be included?
- What exact initial public question seed size is required for a credible demo?
- Should frontend use full Ant Design dependency immediately or a lightweight CSS implementation that visually follows the same workbench patterns?
