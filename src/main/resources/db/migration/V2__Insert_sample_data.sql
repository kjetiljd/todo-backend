-- Insert sample todos for demonstration
INSERT INTO todos (title, description, completed, priority, due_date, created_at, updated_at) VALUES 
(
    'Set up development environment',
    'Install Node.js, Java, and configure IDE for the project',
    1,
    'HIGH',
    DATE('now', '-5 days'),
    DATETIME('now', '-7 days'),
    DATETIME('now', '-5 days')
),
(
    'Design API endpoints',
    'Define REST API structure for todo CRUD operations',
    1,
    'HIGH',
    DATE('now', '-3 days'),
    DATETIME('now', '-6 days'),
    DATETIME('now', '-3 days')
),
(
    'Implement frontend components',
    'Create Vue.js components for todo list, form, and item display',
    0,
    'HIGH',
    DATE('now', '+2 days'),
    DATETIME('now', '-4 days'),
    DATETIME('now', '-4 days')
),
(
    'Add sorting functionality',
    'Implement sorting service for different todo ordering options',
    0,
    'MEDIUM',
    DATE('now', '+3 days'),
    DATETIME('now', '-3 days'),
    DATETIME('now', '-3 days')
),
(
    'Write unit tests',
    'Add comprehensive test coverage for backend services',
    0,
    'MEDIUM',
    DATE('now', '+5 days'),
    DATETIME('now', '-2 days'),
    DATETIME('now', '-2 days')
),
(
    'Set up CI/CD pipeline',
    'Configure automated build and deployment process',
    0,
    'LOW',
    DATE('now', '+7 days'),
    DATETIME('now', '-1 days'),
    DATETIME('now', '-1 days')
),
(
    'Update documentation',
    'Document API endpoints and deployment instructions',
    0,
    'LOW',
    NULL,
    DATETIME('now'),
    DATETIME('now')
);