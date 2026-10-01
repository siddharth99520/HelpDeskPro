INSERT INTO roles (name) VALUES ('EMPLOYEE'), ('ENGINEER'), ('MANAGER'), ('ADMIN');

INSERT INTO users (id, name, role) VALUES 
('u1', 'Alice Employee', 'EMPLOYEE'),
('u2', 'Bob Engineer', 'ENGINEER'),
('u3', 'Carol Manager', 'MANAGER'),
('u4', 'Dave Admin', 'ADMIN');

INSERT INTO categories (id, name) VALUES 
('c1', 'Hardware'),
('c2', 'Software'),
('c3', 'Network');

INSERT INTO sla_policies (priority, response_time_minutes, resolution_time_minutes) VALUES 
('LOW', 480, 2880),
('MEDIUM', 240, 1440),
('HIGH', 60, 480),
('CRITICAL', 15, 120);

INSERT INTO assets (id, name, status) VALUES 
('a1', 'MacBook Pro 14', 'AVAILABLE'),
('a2', 'Dell XPS 15', 'AVAILABLE');
