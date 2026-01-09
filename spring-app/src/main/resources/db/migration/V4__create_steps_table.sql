CREATE TABLE IF NOT EXISTS steps (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  duration BIGINT,
  recipe_id BIGINT,
  CONSTRAINT fk_step_recipe FOREIGN KEY (recipe_id)
    REFERENCES recipes(id)
    ON DELETE CASCADE
);

