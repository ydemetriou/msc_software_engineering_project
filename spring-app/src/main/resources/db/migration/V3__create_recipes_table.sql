CREATE TABLE IF NOT EXISTS recipes (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  category_id BIGINT,
  difficulty VARCHAR(50),
  total_time INT,
  CONSTRAINT fk_recipe_category FOREIGN KEY (category_id)
    REFERENCES categories(id)
    ON DELETE SET NULL
);

