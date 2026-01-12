CREATE TABLE IF NOT EXISTS photos (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  url VARCHAR(255) NOT NULL,
  step_id BIGINT NULL,
  recipe_id BIGINT NULL,
  CONSTRAINT fk_photo_step FOREIGN KEY (step_id)
    REFERENCES steps(id)
    ON DELETE SET NULL,
  CONSTRAINT fk_photo_recipe FOREIGN KEY (recipe_id)
    REFERENCES recipes(id)
    ON DELETE SET NULL
);

