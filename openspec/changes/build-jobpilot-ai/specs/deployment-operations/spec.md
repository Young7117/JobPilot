## ADDED Requirements

### Requirement: Development dependencies run with Docker Compose
The project SHALL provide a development Docker Compose configuration for MySQL, Redis, and Qdrant.

#### Scenario: Start development dependencies
- **WHEN** a developer runs the development Compose command
- **THEN** MySQL, Redis, and Qdrant start with configured ports, volumes, and service names

### Requirement: Production stack runs with Docker Compose
The project SHALL provide a production-style Docker Compose configuration for frontend, backend, MySQL, Redis, Qdrant, and Nginx or frontend web serving.

#### Scenario: Start production stack
- **WHEN** an operator runs the production Compose command with a valid environment file
- **THEN** all required services start and the frontend can reach backend APIs through configured service communication

### Requirement: Environment configuration is documented
The project SHALL provide `.env.example` and README instructions for required environment variables.

#### Scenario: Configure environment
- **WHEN** a developer copies `.env.example` and fills required values
- **THEN** the application can read database, Redis, Qdrant, JWT, AI provider, and embedding provider configuration

### Requirement: Images can be built locally
The project SHALL provide Dockerfiles for backend and frontend.

#### Scenario: Build service images
- **WHEN** a developer runs Docker Compose build
- **THEN** backend and frontend images build successfully using project Dockerfiles
