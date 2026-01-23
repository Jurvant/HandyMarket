CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS categories (
    category_id VARCHAR(10) PRIMARY KEY,
    parent_id VARCHAR(10),
    category_name VARCHAR(64) NOT NULL,
    CONSTRAINT fk_parent
    FOREIGN KEY (parent_id) REFERENCES categories(category_id)
);

CREATE TABLE IF NOT EXISTS announcements (
    announcement_id UUID PRIMARY KEY DEFAULT uuid_generate_V4(),
    user_id UUID NOT NULL,
    title VARCHAR(64) NOT NULL,
    description VARCHAR(2000) NOT NULL,
    price DECIMAL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    category_id VARCHAR(10),
    status SMALLINT NOT NULL CHECK (status IN (0,1,2)),

    CONSTRAINT fk_category
    FOREIGN KEY (category_id)
    REFERENCES categories(category_id)
    ON DELETE SET NULL
);