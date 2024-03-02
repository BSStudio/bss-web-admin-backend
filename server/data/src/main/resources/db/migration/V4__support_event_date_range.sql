-- update event.date column to date_from
ALTER TABLE event
    RENAME COLUMN date TO date_from;

-- add date_to column, default to date_from
ALTER TABLE event
    ADD COLUMN date_to DATE NOT NULL DEFAULT current_date;
