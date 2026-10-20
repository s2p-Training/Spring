-- Create companies table
CREATE TABLE IF NOT EXISTS companies (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         name VARCHAR(255) NOT NULL UNIQUE,
    logo VARCHAR(500),
    industry VARCHAR(100) NOT NULL,
    size VARCHAR(50) NOT NULL,
    rating DECIMAL(3,2) NOT NULL,
    locations VARCHAR(1000),
    founded INT NOT NULL,
    description TEXT,
    employees INT,
    website VARCHAR(500),
    created_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by  VARCHAR(20)  NOT NULL,
    updated_at  TIMESTAMP   DEFAULT NULL,
    updated_by  VARCHAR(20) DEFAULT NULL
    );

CREATE TABLE IF NOT EXISTS contacts (
    id BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    name VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'NEW',
    subject VARCHAR(255) NOT NULL,
    user_type VARCHAR(50) NOT NULL,

    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    created_by VARCHAR(255) NOT NULL,

    updated_at TIMESTAMP(6) NULL DEFAULT NULL
    ON UPDATE CURRENT_TIMESTAMP(6),
    updated_by VARCHAR(20) DEFAULT NULL,
    PRIMARY KEY (id)
    );