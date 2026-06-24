## ADDED Requirements

### Requirement: Users can practice assigned questions
The system SHALL allow users to view assigned questions and submit practice answers.

#### Scenario: Submit answer
- **WHEN** a user submits an answer for an assigned question
- **THEN** the system stores a practice record linked to that user and question

### Requirement: AI evaluates user answers
The system SHALL evaluate user answers using question content, reference answer, user answer, job direction, and battle-card focus.

#### Scenario: Successful evaluation
- **WHEN** a user requests evaluation for a submitted answer
- **THEN** the system returns and stores score, feedback, optimized answer, and follow-up questions

### Requirement: Users can manage mastery status
The system SHALL allow users to mark question mastery status as not practiced, needs review, mastered, or high-priority.

#### Scenario: Update mastery status
- **WHEN** a user updates the mastery status for one of their assigned questions
- **THEN** the system stores the new status

### Requirement: Practice history is available
The system SHALL allow users to view practice history for questions they own.

#### Scenario: Fetch practice history
- **WHEN** a user requests practice history for one of their assigned questions
- **THEN** the system returns previous answers and AI evaluations for that user
