## ADDED Requirements

### Requirement: Users can manage job posts
The system SHALL allow authenticated users to create, list, view, update, and delete their own job posts.

#### Scenario: Create job post
- **WHEN** a user submits company name, position name, industry, job direction, and JD content
- **THEN** the system stores a job post owned by that user

#### Scenario: Update job post
- **WHEN** a user updates a job post they own
- **THEN** the system persists the updated job details

### Requirement: Job posts include preparation metadata
The system SHALL store industry, job direction, city, salary range, source, JD content, and status for each job post.

#### Scenario: Filter jobs by direction
- **WHEN** a user filters jobs by job direction
- **THEN** the system returns only matching jobs owned by that user

### Requirement: Job post access is user-scoped
The system SHALL prevent users from accessing or modifying job posts owned by other users.

#### Scenario: Deny cross-user job access
- **WHEN** a user requests a job post owned by another user
- **THEN** the system denies access
