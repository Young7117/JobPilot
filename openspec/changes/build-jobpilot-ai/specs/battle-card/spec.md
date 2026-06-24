## ADDED Requirements

### Requirement: Users can generate battle cards
The system SHALL generate a structured battle card from a selected resume and job post owned by the authenticated user.

#### Scenario: Generate battle card
- **WHEN** a user requests battle-card generation with a valid resume and job post
- **THEN** the system returns and stores structured fields including match score, core requirements, skill breakdown, matched points, weak points, resume suggestions, interview focus, plans, and risk tips

### Requirement: Battle card AI output is cached
The system SHALL cache battle-card results using resume content, JD content, and prompt version as request hash inputs.

#### Scenario: Cache hit for repeated generation
- **WHEN** a user requests generation for the same resume content, JD content, and prompt version
- **THEN** the system returns the cached battle-card result without calling the AI provider again

### Requirement: Battle card AI calls are logged
The system SHALL log AI call metadata for battle-card generation.

#### Scenario: Log battle-card AI call
- **WHEN** the system calls the AI provider for battle-card generation
- **THEN** it records scene, request hash, model name, token usage when available, cost when available, and user id

### Requirement: Battle cards are linked to source records
The system SHALL link each battle card to its user, resume, and job post.

#### Scenario: Fetch battle card by job
- **WHEN** a user requests the battle card for a job post they own
- **THEN** the system returns the battle card linked to that job if one exists
