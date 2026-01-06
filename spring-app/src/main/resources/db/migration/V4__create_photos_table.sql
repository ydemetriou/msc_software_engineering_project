-- Flyway migration: create photos table
CREATE TABLE IF NOT EXISTS photos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    url VARCHAR(1024) NOT NULL
);
