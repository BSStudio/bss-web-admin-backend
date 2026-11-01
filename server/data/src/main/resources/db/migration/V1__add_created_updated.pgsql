ALTER TABLE member ADD COLUMN created_at TIMESTAMP DEFAULT current_timestamp;
ALTER TABLE member ADD COLUMN updated_at TIMESTAMP DEFAULT current_timestamp;
ALTER TABLE video ADD COLUMN created_at TIMESTAMP DEFAULT current_timestamp;
ALTER TABLE video ADD COLUMN updated_at TIMESTAMP DEFAULT current_timestamp;
ALTER TABLE event ADD COLUMN created_at TIMESTAMP DEFAULT current_timestamp;
ALTER TABLE event ADD COLUMN updated_at TIMESTAMP DEFAULT current_timestamp;
