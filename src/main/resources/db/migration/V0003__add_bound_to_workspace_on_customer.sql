ALTER TABLE customer ADD IF NOT EXISTS workspace_id UUID NOT NULL;

ALTER TABLE customer ADD CONSTRAINT FK_CUSTOMER_ON_WORKSPACE FOREIGN KEY (workspace_id) REFERENCES workspace (id);