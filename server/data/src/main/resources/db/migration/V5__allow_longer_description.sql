-- update event.description column to accept characters with 8192 length
ALTER TABLE event
    ALTER COLUMN description TYPE VARCHAR(8192);
