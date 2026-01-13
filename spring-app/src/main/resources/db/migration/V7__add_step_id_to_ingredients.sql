ALTER TABLE ingredients
  ADD COLUMN step_id BIGINT NULL,
  ADD CONSTRAINT fk_ingredient_step FOREIGN KEY (step_id)
    REFERENCES steps(id)
    ON DELETE SET NULL;

