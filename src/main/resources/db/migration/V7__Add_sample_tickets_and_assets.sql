-- Add some sample tickets
INSERT INTO tickets (id, title, description, status, priority, created_by, category_id, created_at, updated_at, response_due_at, resolution_due_at, version) VALUES 
('t1', 'Network is down in building A', 'Cannot connect to any internal systems.', 'OPEN', 'CRITICAL', 'u1', 'c3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('t2', 'Need new IDE license', 'My license expired yesterday.', 'OPEN', 'MEDIUM', 'u1', 'c2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('t3', 'Laptop screen flickering', 'Screen flickers when moved.', 'ASSIGNED', 'HIGH', 'u1', 'c1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('t4', 'Database access request', 'Need read access to prod DB.', 'IN_PROGRESS', 'HIGH', 'u2', 'c2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- Assign t3 to Bob (u2)
UPDATE tickets SET assigned_to = 'u2' WHERE id = 't3';

-- Add some more assets and assign them
INSERT INTO assets (id, name, status) VALUES 
('a3', 'ThinkPad T14', 'ASSIGNED'),
('a4', 'Monitor 27"', 'ASSIGNED'),
('a5', 'iPhone 13', 'ASSIGNED'),
('a6', 'iPad Pro', 'IN_REPAIR');

-- Create asset assignments
INSERT INTO asset_assignments (id, asset_id, user_id, assigned_at) VALUES 
('aa1', 'a3', 'u1', CURRENT_TIMESTAMP),
('aa2', 'a4', 'u2', CURRENT_TIMESTAMP),
('aa3', 'a5', 'u3', CURRENT_TIMESTAMP);
