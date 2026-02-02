-- Add step_order to steps so demo data and ordering can be stored.
-- Use a nullable column first, then set a default so existing rows (if any) are safe.

ALTER TABLE steps
  ADD COLUMN step_order INT NULL;

-- Optional: default to 0 for rows where order isn't set
UPDATE steps SET step_order = 0 WHERE step_order IS NULL;

ALTER TABLE steps
  MODIFY COLUMN step_order INT NOT NULL DEFAULT 0;

-- Helpful index for ordering steps per recipe
CREATE INDEX idx_steps_recipe_order ON steps (recipe_id, step_order);
