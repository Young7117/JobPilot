## ADDED Requirements

### Requirement: Users can register accounts
The system SHALL allow a new user to register with username, email, and password. Passwords MUST be stored as password hashes and never as plain text.

#### Scenario: Successful registration
- **WHEN** a user submits a unique username, unique email, and valid password
- **THEN** the system creates the user account and returns an authenticated session token

#### Scenario: Duplicate registration
- **WHEN** a user submits a username or email that already exists
- **THEN** the system rejects the request with a conflict error

### Requirement: Users can authenticate with JWT
The system SHALL allow registered users to log in and access protected APIs using a JWT.

#### Scenario: Successful login
- **WHEN** a registered user submits valid credentials
- **THEN** the system returns a JWT containing the user identity and token identifier

#### Scenario: Protected API without token
- **WHEN** a request to a protected API omits a valid JWT
- **THEN** the system returns an unauthorized response

### Requirement: Users can access current user information
The system SHALL expose an endpoint for authenticated users to retrieve their own profile information.

#### Scenario: Fetch current user
- **WHEN** an authenticated user requests current user information
- **THEN** the system returns the authenticated user's id, username, email, and timestamps

### Requirement: Users can log out
The system SHALL support logout by invalidating the current JWT token identifier until the token would otherwise expire.

#### Scenario: Logout invalidates token
- **WHEN** an authenticated user logs out
- **THEN** the system stores the token identifier in the Redis blacklist and rejects later requests using that token
