## ADDED Requirements

### Requirement: System retrieves public questions before AI generation
The system SHALL retrieve relevant public question-bank questions before generating AI supplement questions.

#### Scenario: Public retrieval is sufficient
- **WHEN** public question retrieval returns enough high-quality questions covering the battle-card tags and interview focus
- **THEN** the system creates a user-specific question list without calling AI question generation

#### Scenario: Public retrieval is insufficient
- **WHEN** public question retrieval does not cover enough required ability points
- **THEN** the system calls AI to generate supplement questions for the missing points

### Requirement: Public questions are searched semantically
The system SHALL use Qdrant semantic search combined with tags, difficulty, and quality score to rank public question-bank results.

#### Scenario: Semantic retrieval
- **WHEN** the system receives battle-card-derived query text and tags
- **THEN** it queries Qdrant and returns ranked public questions

### Requirement: AI-generated questions enter candidate workflow
The system SHALL store AI-generated questions as candidate questions before public promotion.

#### Scenario: Candidate question stored
- **WHEN** AI generates supplement questions
- **THEN** the system stores them in candidate-question records with quality score, duplicate score, status, source, and raw AI result

### Requirement: User-specific questions link to context
The system SHALL assign retrieved and generated questions to the user with links to job post and battle card.

#### Scenario: Create user question list
- **WHEN** question generation for a job completes
- **THEN** the system creates user-question records linked to the user, job post, battle card, and question source
