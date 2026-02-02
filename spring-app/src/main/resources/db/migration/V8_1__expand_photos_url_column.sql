-- Ensure the `photos.url` column can store large URLs/data URIs.
-- JPA maps it as LONGTEXT (see PhotoEntity), but the old migration created it as VARCHAR(255).

ALTER TABLE photos
  MODIFY COLUMN url LONGTEXT NOT NULL;
