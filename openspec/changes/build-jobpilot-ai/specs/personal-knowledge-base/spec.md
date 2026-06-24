## ADDED Requirements

### Requirement: Users can create personal knowledge items
The system SHALL allow authenticated users to create personal knowledge items with title, content, category, tags, and mastery level.

#### Scenario: Create manual knowledge item
- **WHEN** a user submits a knowledge item manually
- **THEN** the system stores it as a user-owned personal knowledge item

### Requirement: Users can save practice outputs to knowledge base
The system SHALL allow users to save a practiced question, user answer, AI optimized answer, and follow-up questions into the personal knowledge base.

#### Scenario: Save evaluated answer
- **WHEN** a user saves an evaluated practice answer to knowledge base
- **THEN** the system creates a personal knowledge item linked to the source question and practice context

### Requirement: Personal knowledge is searchable
The system SHALL allow users to search and filter their knowledge items by keyword, category, tags, and mastery level.

#### Scenario: Filter knowledge by category
- **WHEN** a user filters knowledge items by category
- **THEN** the system returns only matching items owned by that user

### Requirement: Public and personal knowledge are separated
The system MUST NOT store user personal answers or interview notes in the public question bank.

#### Scenario: Personal answer remains private
- **WHEN** a user saves an optimized answer from practice
- **THEN** the system stores it in personal knowledge only and not in public question-bank records
