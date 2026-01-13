CREATE TABLE IF NOT EXISTS ingredients (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  quantity DOUBLE NULL,
  unit VARCHAR(64) NULL,
  recipe_id BIGINT,
  CONSTRAINT fk_ingredient_recipe FOREIGN KEY (recipe_id)
    REFERENCES recipes(id)
    ON DELETE CASCADE
);

