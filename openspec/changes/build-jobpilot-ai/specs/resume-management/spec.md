## ADDED Requirements

### Requirement: Users can create text resumes
The system SHALL allow authenticated users to create resumes by pasting text content.

#### Scenario: Create text resume
- **WHEN** a user submits resume title, target role, and text content
- **THEN** the system stores a new resume version owned by that user

### Requirement: Users can upload supported resume files
The system SHALL allow authenticated users to upload text-based PDF and DOCX resumes and parse their text content.

#### Scenario: Upload text PDF resume
- **WHEN** a user uploads a text-based PDF file
- **THEN** the system extracts text content and stores it as a resume version

#### Scenario: Upload DOCX resume
- **WHEN** a user uploads a DOCX file
- **THEN** the system extracts text content and stores it as a resume version

#### Scenario: Unsupported resume file
- **WHEN** a user uploads a scanned PDF, image resume, or legacy `.doc` file
- **THEN** the system rejects or fails parsing with a clear unsupported-file message

### Requirement: Users can manage resume versions
The system SHALL allow users to list, view, update, delete, and set a default resume among their own resumes.

#### Scenario: Set default resume
- **WHEN** a user marks one of their resumes as default
- **THEN** the system clears the previous default resume for that user and marks the selected resume as default

#### Scenario: User isolation for resumes
- **WHEN** a user requests a resume owned by another user
- **THEN** the system denies access
