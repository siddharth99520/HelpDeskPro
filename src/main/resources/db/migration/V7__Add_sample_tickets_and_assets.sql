-- Add some sample tickets
INSERT INTO tickets (id, title, description, status, priority, created_by, category_id, created_at, updated_at, response_due_at, resolution_due_at, version) VALUES 
('t10', 'Network is down in building A', 'Cannot connect to any internal systems.', 'OPEN', 'CRITICAL', 'u1', 'c3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('t11', 'Need new IDE license', 'My license expired yesterday.', 'OPEN', 'MEDIUM', 'u1', 'c2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('t12', 'Laptop screen flickering', 'Screen flickers when moved.', 'ASSIGNED', 'HIGH', 'u1', 'c1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('t13', 'Database access request', 'Need read access to prod DB.', 'IN_PROGRESS', 'HIGH', 'u2', 'c2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- Assign t12 to Bob (u2)
UPDATE tickets SET assigned_to = 'u2' WHERE id = 't12';

-- Add some more assets and assign them
INSERT INTO assets (id, name, status) VALUES 
('a10', 'ThinkPad T14', 'ASSIGNED'),
('a11', 'Monitor 27"', 'ASSIGNED'),
('a12', 'iPhone 13', 'ASSIGNED'),
('a13', 'iPad Pro', 'IN_REPAIR');

-- Create asset assignments
INSERT INTO asset_assignments (id, asset_id, user_id, assigned_at) VALUES 
('aa10', 'a10', 'u1', CURRENT_TIMESTAMP),
('aa11', 'a11', 'u2', CURRENT_TIMESTAMP),
('aa12', 'a12', 'u3', CURRENT_TIMESTAMP);
