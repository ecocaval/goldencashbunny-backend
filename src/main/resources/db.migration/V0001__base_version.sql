-- create_account_table.sql
CREATE TABLE account (
    id UUID PRIMARY KEY,
    deleted BOOLEAN DEFAULT FALSE NOT NULL,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP,
    cpf VARCHAR(11),
    cnpj VARCHAR(14),
    user_name VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- create_address_table.sql
CREATE TABLE address (
    id UUID PRIMARY KEY,
    deleted BOOLEAN DEFAULT FALSE NOT NULL,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP,
    customer_id UUID NOT NULL UNIQUE,
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);

-- create_customer_table.sql
CREATE TABLE customer (
    id UUID PRIMARY KEY,
    deleted BOOLEAN DEFAULT FALSE NOT NULL,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP,
    cpf VARCHAR(11),
    cnpj VARCHAR(14),
    address_id UUID UNIQUE,
    company_name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),
    phone VARCHAR(255),
    social_company_name VARCHAR(255),
    FOREIGN KEY (address_id) REFERENCES address(id)
);

-- create_space_table.sql
CREATE TABLE space (
    id UUID PRIMARY KEY,
    deleted BOOLEAN DEFAULT FALSE NOT NULL,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP,
    workspace_id UUID,
    name VARCHAR(255) NOT NULL,
    FOREIGN KEY (workspace_id) REFERENCES workspace(id)
);

-- create_space_table_table.sql
CREATE TABLE space_table (
    id UUID PRIMARY KEY,
    deleted BOOLEAN DEFAULT FALSE NOT NULL,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP,
    space_id UUID,
    name VARCHAR(255) NOT NULL,
    FOREIGN KEY (space_id) REFERENCES space(id)
);

-- create_space_table_column_table.sql
CREATE TABLE space_table_column (
    id UUID PRIMARY KEY,
    deleted BOOLEAN DEFAULT FALSE NOT NULL,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP,
    space_table_id UUID,
    column_type VARCHAR(255) CHECK (column_type IN ('TEST')) NOT NULL,
    name VARCHAR(255) NOT NULL,
    FOREIGN KEY (space_table_id) REFERENCES space_table(id)
);

-- create_space_table_column_data_table.sql
CREATE TABLE space_table_column_data (
    id UUID PRIMARY KEY,
    deleted BOOLEAN DEFAULT FALSE NOT NULL,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP,
    space_table_column_id UUID,
    type VARCHAR(255) CHECK (type IN ('TEST')) NOT NULL,
    value VARCHAR(255) NOT NULL,
    FOREIGN KEY (space_table_column_id) REFERENCES space_table_column(id)
);

-- create_workspace_table.sql
CREATE TABLE workspace (
    id UUID PRIMARY KEY,
    deleted BOOLEAN DEFAULT FALSE NOT NULL,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP,
    account_id UUID,
    company_name VARCHAR(255) NOT NULL,
    social_company_name VARCHAR(255),
    FOREIGN KEY (account_id) REFERENCES account(id)
);
