-- update event.date column to date_from
ALTER TABLE video
    RENAME COLUMN uploaded_at TO shooting_date_start;

-- add date_to column, default to date_from
ALTER TABLE video
    ADD COLUMN shooting_date_end DATE NOT NULL DEFAULT current_date;

-- add constraint to ensure date_to is not before date_from
ALTER TABLE video
    ADD CONSTRAINT date_to_not_before_date_from
    CHECK (shooting_date_start >= shooting_date_end);
