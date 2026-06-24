## 1. Project Foundation

- [x] 1.1 Create `backend`, `frontend`, `docs`, and deployment directory structure
- [x] 1.2 Initialize Spring Boot 3 backend with Java 17, Maven, MyBatis-Plus, Spring Security, validation, MySQL, Redis, and test dependencies
- [x] 1.3 Initialize React + Vite frontend with React Router, Axios, Ant Design, and shared route layout
- [x] 1.4 Add root README, `.env.example`, backend env mapping, and frontend env mapping
- [x] 1.5 Add base Docker Compose file for MySQL, Redis, and Qdrant development dependencies

## 2. Database And Backend Infrastructure

- [x] 2.1 Add MySQL schema migrations or init SQL for users, resumes, jobs, battle cards, question bank, candidate questions, user questions, practices, knowledge items, application records, and AI call logs
- [x] 2.2 Configure backend datasource, MyBatis-Plus mappers, pagination, JSON field handling, and timestamp auditing
- [x] 2.3 Add common API response, error handling, validation errors, unauthorized errors, and user-scope access helpers
- [x] 2.4 Add Redis configuration for cache, rate-limit keys, and JWT blacklist keys
- [x] 2.5 Add Qdrant client configuration and health-check integration

## 3. Authentication And User Module

- [x] 3.1 Implement user registration with unique username/email validation and password hashing
- [x] 3.2 Implement login that returns a JWT with user identity and token identifier
- [x] 3.3 Implement JWT authentication filter, protected API security rules, and current-user context
- [x] 3.4 Implement `GET /api/user/me` for authenticated profile retrieval
- [x] 3.5 Implement logout that stores the token identifier in the Redis blacklist until expiry
- [x] 3.6 Add backend tests for registration, login, protected API access, and logout invalidation
- [x] 3.7 Build frontend login/register pages and authenticated route guard

## 4. Resume And Job Management

- [x] 4.1 Implement text resume creation, listing, detail, update, delete, and default resume selection APIs
- [x] 4.2 Implement PDF text extraction with PDFBox or Tika and clear unsupported-file handling
- [x] 4.3 Implement DOCX text extraction with Apache POI or Tika and clear unsupported-file handling
- [x] 4.4 Add resume user-isolation checks and tests for cross-user access denial
- [x] 4.5 Implement job post create, list, detail, update, delete, status, direction, and metadata APIs
- [x] 4.6 Add job post user-isolation checks and tests for cross-user access denial
- [x] 4.7 Build frontend resume pages for upload, pasted text creation, parsed content review, edit, and default selection
- [x] 4.8 Build frontend job pages for JD entry, filters, list, detail, edit, and delete

## 5. AI Provider And Battle Card Workflow

- [x] 5.1 Define AI provider abstraction for chat completion, embedding, prompt version, token usage, and raw response capture
- [x] 5.2 Implement battle-card prompt builder that outputs strict structured JSON
- [x] 5.3 Implement battle-card generation API using selected user-owned resume and job post
- [x] 5.4 Cache battle-card results in Redis using resume content, JD content, and prompt version request hash
- [x] 5.5 Persist battle-card structured fields, raw AI result, and source record links
- [x] 5.6 Record battle-card AI call metadata in `ai_call_log`
- [x] 5.7 Add backend tests for generation success, cache hit, malformed AI response handling, and source ownership checks
- [x] 5.8 Build frontend battle-card detail page with match score, requirements, strengths, gaps, suggestions, focus, plans, and risks

## 6. Public Question Retrieval And Candidate Questions

- [x] 6.1 Seed an initial computer-domain public question bank covering Java, Spring Boot, MySQL, Redis, Docker, React, AI application, project follow-up, scenario design, and HR questions
- [x] 6.2 Implement question embedding generation and Qdrant indexing for public questions
- [x] 6.3 Implement battle-card tag and interview-focus extraction for question retrieval queries
- [x] 6.4 Implement public question retrieval using Qdrant semantic score combined with tags, difficulty, and quality score
- [x] 6.5 Implement question-generation decision logic that skips AI when retrieved public questions are sufficient
- [x] 6.6 Implement AI supplement question generation for missing ability points
- [x] 6.7 Implement candidate-question persistence with quality score, duplicate score, status, source, and raw AI result
- [x] 6.8 Implement duplicate detection against public and candidate questions using Qdrant similarity
- [x] 6.9 Implement user-question assignment linking user, job post, battle card, and question source
- [ ] 6.10 Add backend tests for retrieval-first behavior, AI fallback behavior, candidate storage, duplicate scoring, and user-question linking
- [x] 6.11 Build frontend question list with filters for tags, type, difficulty, mastery status, and linked job

## 7. Practice Evaluation And Personal Knowledge

- [x] 7.1 Implement assigned-question detail API with public or candidate question content and reference answer access
- [x] 7.2 Implement practice answer submission and practice history APIs
- [x] 7.3 Implement AI answer evaluation prompt and parser for score, feedback, optimized answer, and follow-up questions
- [x] 7.4 Persist practice evaluation results and log AI call metadata
- [x] 7.5 Implement mastery status updates for not practiced, needs review, mastered, and high-priority
- [x] 7.6 Implement personal knowledge CRUD with category, tags, source links, and mastery level
- [x] 7.7 Implement save-to-knowledge flow from evaluated practice answer without modifying public question-bank records
- [x] 7.8 Implement knowledge search and filters by keyword, category, tags, and mastery level
- [ ] 7.9 Add backend tests for practice ownership, evaluation persistence, mastery updates, save-to-knowledge privacy, and knowledge filters
- [x] 7.10 Build frontend practice page with answer editor, evaluation result, optimized answer, follow-up questions, mastery control, and save-to-knowledge action
- [x] 7.11 Build frontend knowledge page with search, filters, list, detail, edit, and mastery controls

## 8. Application Review And Dashboard

- [x] 8.1 Implement application record create, list, detail, and update APIs linked to job, resume, and battle card
- [x] 8.2 Implement outcome fields for status, apply date, interview date, result, failure reason, and notes
- [x] 8.3 Implement AI application review generation from outcome, linked job, resume, and battle card
- [x] 8.4 Persist application review suggestions and log AI call metadata
- [ ] 8.5 Add application record user-isolation checks and backend tests
- [x] 8.6 Implement dashboard summary API for resume count, job count, battle-card count, question count, review queue, and application status statistics
- [x] 8.7 Build frontend application page for records, statuses, outcomes, notes, and AI review suggestions
- [x] 8.8 Build frontend dashboard with summary cards, next-step recommendation, review queue, and application status statistics

## 9. Deployment And Verification

- [x] 9.1 Add backend Dockerfile with production build and runtime configuration
- [x] 9.2 Add frontend Dockerfile and Nginx configuration for static serving and API proxying
- [x] 9.3 Add production Docker Compose file for frontend, backend, MySQL, Redis, Qdrant, volumes, ports, and service-name communication
- [x] 9.4 Add local development instructions for running backend/frontend locally with MySQL, Redis, and Qdrant in Docker Compose
- [x] 9.5 Add production deployment instructions for copying `.env.example`, building images, starting services, checking logs, and resetting demo data
- [ ] 9.6 Verify backend tests pass from a clean checkout
- [x] 9.7 Verify frontend build passes from a clean checkout
- [ ] 9.8 Verify Docker Compose development dependencies start successfully
- [ ] 9.9 Verify production Docker Compose stack builds and starts successfully
- [ ] 9.10 Run an end-to-end manual MVP demo from registration through application review and document the demo path
