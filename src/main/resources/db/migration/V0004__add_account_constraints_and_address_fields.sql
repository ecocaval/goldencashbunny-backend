ALTER TABLE account ADD CONSTRAINT uc_account_user_name UNIQUE (user_name);

ALTER TABLE account ADD CONSTRAINT uc_account_user_cpf UNIQUE (cpf);

ALTER TABLE account ADD CONSTRAINT uc_account_user_cnpj UNIQUE (cnpj);

ALTER TABLE address ADD IF NOT EXISTS ibge_code VARCHAR(7);

ALTER TABLE address ADD IF NOT EXISTS name VARCHAR(255) NOT NULL;

ALTER TABLE address DROP COLUMN address;

CREATE TABLE customer_additional_email (
    id UUID NOT NULL,
    creation_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    deleted BOOLEAN DEFAULT FALSE NOT NULL,
    customer_id UUID NOT NULL,
    email VARCHAR(255) NOT NULL,
    CONSTRAINT pk_customer_additional_email PRIMARY KEY (id)
);

ALTER TABLE customer_additional_email ADD CONSTRAINT fk_accrol_on_customer_additional_email FOREIGN KEY (customer_id) REFERENCES customer (id);
