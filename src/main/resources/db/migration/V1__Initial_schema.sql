CREATE TABLE roles (
    name VARCHAR(50) PRIMARY KEY
);

CREATE TABLE categories (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL REFERENCES roles(name)
);

CREATE TABLE sla_policies (
    priority VARCHAR(50) PRIMARY KEY,
    response_time_minutes INT NOT NULL,
    resolution_time_minutes INT NOT NULL
);

CREATE TABLE tickets (
    id VARCHAR(36) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL,
    priority VARCHAR(50) NOT NULL REFERENCES sla_policies(priority),
    created_by VARCHAR(36) NOT NULL REFERENCES users(id),
    assigned_to VARCHAR(36) REFERENCES users(id),
    category_id VARCHAR(36) NOT NULL REFERENCES categories(id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    response_due_at TIMESTAMP WITH TIME ZONE,
    resolution_due_at TIMESTAMP WITH TIME ZONE,
    first_responded_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE ticket_comments (
    id VARCHAR(36) PRIMARY KEY,
    ticket_id VARCHAR(36) NOT NULL REFERENCES tickets(id),
    user_id VARCHAR(36) NOT NULL REFERENCES users(id),
    content TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- ticket_history is the append-only workflow-status trail
CREATE TABLE ticket_history (
    id VARCHAR(36) PRIMARY KEY,
    ticket_id VARCHAR(36) NOT NULL REFERENCES tickets(id),
    changed_by VARCHAR(36) NOT NULL REFERENCES users(id),
    old_status VARCHAR(50),
    new_status VARCHAR(50) NOT NULL,
    changed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- audit_logs covers everything else worth attributing to an actor (role changes, asset actions, priority changes)
CREATE TABLE audit_logs (
    id VARCHAR(36) PRIMARY KEY,
    entity_type VARCHAR(50) NOT NULL,
    entity_id VARCHAR(36) NOT NULL,
    actor_id VARCHAR(36) NOT NULL REFERENCES users(id),
    action VARCHAR(50) NOT NULL,
    old_value TEXT,
    new_value TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE assets (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE asset_assignments (
    id VARCHAR(36) PRIMARY KEY,
    asset_id VARCHAR(36) NOT NULL REFERENCES assets(id),
    user_id VARCHAR(36) NOT NULL REFERENCES users(id),
    assigned_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    returned_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE notifications (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL REFERENCES users(id),
    message TEXT NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Indexes
CREATE UNIQUE INDEX ux_active_assignment ON asset_assignments(asset_id) WHERE returned_at IS NULL;
CREATE INDEX idx_tickets_status ON tickets(status);
CREATE INDEX idx_tickets_assigned_to ON tickets(assigned_to);
CREATE INDEX idx_tickets_priority_status ON tickets(priority, status);
CREATE INDEX idx_tickets_created_at ON tickets(created_at);
CREATE INDEX idx_assets_status ON assets(status);
CREATE INDEX idx_audit_logs_entity ON audit_logs(entity_type, entity_id);

COMMENT ON TABLE ticket_history IS 'Append-only workflow-status trail for tickets';
COMMENT ON TABLE audit_logs IS 'Covers everything else worth attributing to an actor (role changes, asset actions, priority changes)';
