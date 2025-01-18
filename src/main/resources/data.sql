-- Insert 6 employees into the 'employee' table
INSERT INTO employee (employee_id, full_name, job_title, department, hire_date, employment_status, contact_info, address)
VALUES 
('E12345', 'John Doe', 'Software Engineer', 'IT', STR_TO_DATE('2022-06-15', '%Y-%m-%d'), 'Full-time', 'john.doe@example.com', '1234 Elm Street'),
('E12346', 'Jane Smith', 'Project Manager', 'IT', STR_TO_DATE('2021-03-22', '%Y-%m-%d'), 'Full-time', 'jane.smith@example.com', '5678 Oak Avenue'),
('E12347', 'Mike Johnson', 'HR Specialist', 'HR', STR_TO_DATE('2020-08-11', '%Y-%m-%d'), 'Full-time', 'mike.johnson@example.com', '9101 Pine Road'),
('E12348', 'Emily Davis', 'Software Engineer', 'IT', STR_TO_DATE('2023-01-10', '%Y-%m-%d'), 'Full-time', 'emily.davis@example.com', '1234 Maple Street'),
('E12349', 'David Brown', 'Marketing Manager', 'Marketing', STR_TO_DATE('2022-11-18', '%Y-%m-%d'), 'Full-time', 'david.brown@example.com', '5678 Birch Lane'),
('E12350', 'Sarah Wilson', 'Sales Executive', 'Sales', STR_TO_DATE('2021-07-25', '%Y-%m-%d'), 'Full-time', 'sarah.wilson@example.com', '9101 Cedar Circle');
-- Insert roles into the 'roles' table
INSERT INTO role (name) VALUES ('ROLE_HR');
INSERT INTO role (name) VALUES ('ROLE_MANAGER');
INSERT INTO role (name) VALUES ('ROLE_ADMIN');

-- Insert users into the 'users' table with their roles (Note: password should be hashed in a real scenario)
INSERT INTO user (username, password) VALUES ('admin', 'admin123');
INSERT INTO user (username, password) VALUES ('hr_user', 'hrpassword');
INSERT INTO user (username, password) VALUES ('manager_user', 'managerpassword');

-- Assign roles to users
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM user u
JOIN role r ON r.name = 'ROLE_ADMIN'
WHERE u.username = 'admin';

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM user u
JOIN role r ON r.name = 'ROLE_HR'
WHERE u.username = 'hr_user';

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM user u
JOIN role r ON r.name = 'ROLE_MANAGER'
WHERE u.username = 'manager_user';

-- Insert audit log records
INSERT INTO audit_log (action, performed_by, employee_id)
VALUES ('Created new employee', 'admin', 1);

INSERT INTO audit_log (action, performed_by, employee_id)
VALUES ('Updated employee details', 'hr_user', 2);

