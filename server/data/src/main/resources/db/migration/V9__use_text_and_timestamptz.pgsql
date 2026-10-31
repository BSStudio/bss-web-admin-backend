-- squawk-ignore-file changing-column-type, constraint-missing-not-valid
-- the tables are small, so rewriting them and checking the new constraints takes no time; the new constraints hold
-- for every row, as they keep the lengths the VARCHAR columns allowed
-- squawk-ignore-file ban-drop-view
-- a view stops its columns from changing type, so the views that read them are dropped and created again below,
-- with the same columns, in the transaction Flyway runs the migration in
DROP VIEW public.member;
DROP VIEW public.video;
DROP VIEW public.crew;
DROP VIEW public.event;

-- VARCHAR(n) becomes TEXT with a check on its length, which can change later without rewriting the table
ALTER TABLE status
ALTER COLUMN name TYPE TEXT,
ADD CONSTRAINT status_name_max_length CHECK (char_length(name) <= 250);

-- the app ran in UTC, so the TIMESTAMP columns hold UTC times
ALTER TABLE member
ALTER COLUMN url TYPE TEXT,
ALTER COLUMN name TYPE TEXT,
ALTER COLUMN nickname TYPE TEXT,
ALTER COLUMN description TYPE TEXT,
ALTER COLUMN role TYPE TEXT,
ALTER COLUMN status TYPE TEXT,
ALTER COLUMN created_at TYPE TIMESTAMP WITH TIME ZONE USING created_at AT TIME ZONE 'UTC',
ALTER COLUMN updated_at TYPE TIMESTAMP WITH TIME ZONE USING updated_at AT TIME ZONE 'UTC',
ADD CONSTRAINT member_url_max_length CHECK (char_length(url) <= 250),
ADD CONSTRAINT member_name_max_length CHECK (char_length(name) <= 250),
ADD CONSTRAINT member_nickname_max_length CHECK (char_length(nickname) <= 250),
ADD CONSTRAINT member_description_max_length CHECK (char_length(description) <= 2000),
ADD CONSTRAINT member_role_max_length CHECK (char_length(role) <= 250);

ALTER TABLE video
ALTER COLUMN title TYPE TEXT,
ALTER COLUMN description TYPE TEXT,
ALTER COLUMN created_at TYPE TIMESTAMP WITH TIME ZONE USING created_at AT TIME ZONE 'UTC',
ALTER COLUMN updated_at TYPE TIMESTAMP WITH TIME ZONE USING updated_at AT TIME ZONE 'UTC',
ADD CONSTRAINT video_title_max_length CHECK (char_length(title) <= 250),
ADD CONSTRAINT video_description_max_length CHECK (char_length(description) <= 2000);

ALTER TABLE video_url
ALTER COLUMN url TYPE TEXT,
ADD CONSTRAINT video_url_url_max_length CHECK (char_length(url) <= 255);

ALTER TABLE crew
ALTER COLUMN position TYPE TEXT,
ADD CONSTRAINT crew_position_max_length CHECK (char_length(position) <= 250);

ALTER TABLE event
ALTER COLUMN url TYPE TEXT,
ALTER COLUMN title TYPE TEXT,
ALTER COLUMN description TYPE TEXT,
ALTER COLUMN created_at TYPE TIMESTAMP WITH TIME ZONE USING created_at AT TIME ZONE 'UTC',
ALTER COLUMN updated_at TYPE TIMESTAMP WITH TIME ZONE USING updated_at AT TIME ZONE 'UTC',
ADD CONSTRAINT event_url_max_length CHECK (char_length(url) <= 250),
ADD CONSTRAINT event_title_max_length CHECK (char_length(title) <= 250),
ADD CONSTRAINT event_description_max_length CHECK (char_length(description) <= 8192);

ALTER TABLE label
ALTER COLUMN name TYPE TEXT,
ALTER COLUMN description TYPE TEXT,
ADD CONSTRAINT label_name_max_length CHECK (char_length(name) <= 64),
ADD CONSTRAINT label_description_max_length CHECK (char_length(description) <= 256);

CREATE VIEW public.member AS
SELECT
    id,
    url,
    name,
    nickname,
    description,
    joined_at,
    role,
    status,
    archived,
    created_at,
    updated_at
FROM member;

CREATE VIEW public.video AS
SELECT
    video.id,
    array_remove(array_agg(video_url.url), NULL) AS urls,
    video.title,
    video.description,
    array_remove(array_agg(label.name), NULL) AS labels,
    video.shooting_date_start,
    video.shooting_date_end,
    video.created_at,
    video.updated_at
FROM video
LEFT JOIN video_url ON video.id = video_url.video_id
LEFT JOIN video_label ON video.id = video_label.video_id
LEFT JOIN label ON video_label.label_id = label.id
WHERE video.visible
GROUP BY video.id;

CREATE VIEW public.crew AS
SELECT
    video_id,
    member_id,
    position
FROM crew;

COMMENT ON VIEW public.crew IS
E'@foreignKey (video_id) references video (id)\n@foreignKey (member_id) references member (id)';

CREATE VIEW public.event AS
SELECT
    id,
    url,
    title,
    description,
    date_from,
    date_to,
    created_at,
    updated_at
FROM event
WHERE visible;
