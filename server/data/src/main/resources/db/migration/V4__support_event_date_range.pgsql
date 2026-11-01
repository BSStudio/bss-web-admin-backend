-- update event.date column to date_from
ALTER TABLE event
    RENAME COLUMN date TO date_from;

-- add date_to column, default to date_from
ALTER TABLE event
    ADD COLUMN date_to DATE NOT NULL DEFAULT current_date;

-- add constraint to ensure date_to is not before date_from
ALTER TABLE event
    ADD CONSTRAINT date_to_not_before_date_from
    CHECK (date_to >= date_from);
