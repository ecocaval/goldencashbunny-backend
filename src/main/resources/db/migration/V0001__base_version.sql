CREATE TABLE account (
  id UUID NOT NULL,
   creation_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   last_modified_date TIMESTAMP WITHOUT TIME ZONE,
   deleted BOOLEAN DEFAULT FALSE NOT NULL,
   user_name VARCHAR(50) NOT NULL,
   email VARCHAR(255) NOT NULL,
   cpf VARCHAR(11),
   cnpj VARCHAR(14),
   password VARCHAR(255) NOT NULL,
   CONSTRAINT pk_account PRIMARY KEY (id)
);

CREATE TABLE address (
  id UUID NOT NULL,
   creation_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   last_modified_date TIMESTAMP WITHOUT TIME ZONE,
   deleted BOOLEAN DEFAULT FALSE NOT NULL,
   zip_code VARCHAR(8) NOT NULL,
   address VARCHAR(100) NOT NULL,
   number VARCHAR(6),
   neighborhood VARCHAR(50) NOT NULL,
   complement VARCHAR(50),
   city VARCHAR(255) NOT NULL,
   state VARCHAR(255) NOT NULL,
   customer_id UUID NOT NULL,
   CONSTRAINT pk_address PRIMARY KEY (id)
);

CREATE TABLE customer (
  id UUID NOT NULL,
   creation_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   last_modified_date TIMESTAMP WITHOUT TIME ZONE,
   deleted BOOLEAN DEFAULT FALSE NOT NULL,
   first_name VARCHAR(255) NOT NULL,
   last_name VARCHAR(255),
   cpf VARCHAR(11),
   cnpj VARCHAR(14),
   company_name VARCHAR(255) NOT NULL,
   social_company_name VARCHAR(255),
   email VARCHAR(255),
   phone VARCHAR(255),
   address_id UUID,
   CONSTRAINT pk_customer PRIMARY KEY (id)
);

CREATE TABLE role (
  id UUID NOT NULL,
   creation_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   last_modified_date TIMESTAMP WITHOUT TIME ZONE,
   deleted BOOLEAN DEFAULT FALSE NOT NULL,
   name VARCHAR(50) NOT NULL,
   CONSTRAINT pk_role PRIMARY KEY (id)
);

CREATE TABLE account_roles (
    account_id UUID NOT NULL,
    role_id UUID NOT NULL
);

CREATE TABLE space (
  id UUID NOT NULL,
   creation_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   last_modified_date TIMESTAMP WITHOUT TIME ZONE,
   deleted BOOLEAN DEFAULT FALSE NOT NULL,
   name VARCHAR(255) NOT NULL,
   workspace_id UUID,
   CONSTRAINT pk_space PRIMARY KEY (id)
);

CREATE TABLE space_table (
  id UUID NOT NULL,
   creation_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   last_modified_date TIMESTAMP WITHOUT TIME ZONE,
   deleted BOOLEAN DEFAULT FALSE NOT NULL,
   name VARCHAR(255) NOT NULL,
   space_id UUID,
   CONSTRAINT pk_spacetable PRIMARY KEY (id)
);

CREATE TABLE space_table_column (
  id UUID NOT NULL,
   creation_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   last_modified_date TIMESTAMP WITHOUT TIME ZONE,
   deleted BOOLEAN DEFAULT FALSE NOT NULL,
   name VARCHAR(255) NOT NULL,
   space_table_id UUID,
   column_type VARCHAR(255) NOT NULL,
   CONSTRAINT pk_spacetablecolumn PRIMARY KEY (id)
);

CREATE TABLE space_table_column_data (
  id UUID NOT NULL,
   creation_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   last_modified_date TIMESTAMP WITHOUT TIME ZONE,
   deleted BOOLEAN DEFAULT FALSE NOT NULL,
   space_table_column_id UUID,
   space_table_row_reference UUID NOT NULL,
   value VARCHAR(255) NOT NULL,
   CONSTRAINT pk_spacetablecolumndata PRIMARY KEY (id)
);

CREATE TABLE workspace (
  id UUID NOT NULL,
   creation_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   last_modified_date TIMESTAMP WITHOUT TIME ZONE,
   deleted BOOLEAN DEFAULT FALSE NOT NULL,
   company_name VARCHAR(255) NOT NULL,
   social_company_name VARCHAR(255),
   account_id UUID,
   CONSTRAINT pk_workspace PRIMARY KEY (id)
);

ALTER TABLE account ADD CONSTRAINT uc_account_email UNIQUE (email);

ALTER TABLE address ADD CONSTRAINT FK_ADDRESS_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id);

ALTER TABLE customer ADD CONSTRAINT FK_CUSTOMER_ON_ADDRESS FOREIGN KEY (address_id) REFERENCES address (id);

ALTER TABLE account_roles ADD CONSTRAINT fk_accrol_on_account FOREIGN KEY (account_id) REFERENCES account (id);

ALTER TABLE account_roles ADD CONSTRAINT fk_accrol_on_role FOREIGN KEY (role_id) REFERENCES role (id);

ALTER TABLE space ADD CONSTRAINT FK_SPACE_ON_WORKSPACE FOREIGN KEY (workspace_id) REFERENCES workspace (id);

ALTER TABLE space_table ADD CONSTRAINT FK_SPACETABLE_ON_SPACE FOREIGN KEY (space_id) REFERENCES space (id);

ALTER TABLE space_table_column ADD CONSTRAINT FK_SPACETABLECOLUMN_ON_SPACE_TABLE FOREIGN KEY (space_table_id) REFERENCES space_table (id);

ALTER TABLE space_table_column_data ADD CONSTRAINT FK_SPACETABLECOLUMNDATA_ON_SPACE_TABLE_COLUMN FOREIGN KEY (space_table_column_id) REFERENCES space_table_column (id);

ALTER TABLE workspace ADD CONSTRAINT FK_WORKSPACE_ON_ACCOUNT FOREIGN KEY (account_id) REFERENCES account (id);
