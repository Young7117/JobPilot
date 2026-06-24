## ADDED Requirements

### Requirement: Users can manage application records
The system SHALL allow users to create, list, view, and update application records linked to job posts.

#### Scenario: Create application record
- **WHEN** a user creates an application record for a job post they own
- **THEN** the system stores status, apply date, linked job, linked resume, and linked battle card when provided

### Requirement: Application records track outcomes
The system SHALL store application status, interview date, result, failure reason, and notes.

#### Scenario: Update application outcome
- **WHEN** a user records an interview result and failure reason
- **THEN** the system persists the outcome fields

### Requirement: AI can generate application review suggestions
The system SHALL generate review suggestions from the application outcome, linked job, linked resume, and linked battle card.

#### Scenario: Generate review suggestion
- **WHEN** a user requests an application review for a record with outcome details
- **THEN** the system returns and stores actionable review suggestions

### Requirement: Application data is user-scoped
The system SHALL prevent users from accessing application records owned by other users.

#### Scenario: Deny cross-user application access
- **WHEN** a user requests an application record owned by another user
- **THEN** the system denies access
