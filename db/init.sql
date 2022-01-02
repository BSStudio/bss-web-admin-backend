CREATE SCHEMA bss_web;

CREATE TABLE bss_web.status
(
    name TEXT PRIMARY KEY
);

INSERT INTO bss_web.status
VALUES ('ALUMNI'),
       ('ACTIVE_ALUMNI'),
       ('MEMBER'),
       ('MEMBER_CANDIDATE'),
       ('MEMBER_CANDIDATE_CANDIDATE');

CREATE TABLE bss_web.members
(
    id          TEXT PRIMARY KEY CHECK (trim(id) != ''),
    name        TEXT UNIQUE NOT NULL,
    description TEXT        NOT NULL DEFAULT '',
    image_url   TEXT                 DEFAULT NULL,
    joined_at   DATE        NOT NULL DEFAULT current_date,
    role        TEXT        NOT NULL DEFAULT '',
    status      TEXT        NOT NULL DEFAULT 'MEMBER_CANDIDATE_CANDIDATE' REFERENCES bss_web.status (name),
    archived    BOOLEAN     NOT NULL DEFAULT FALSE
);

CREATE TABLE bss_web.videos
(
    id            TEXT PRIMARY KEY CHECK (trim(id) != ''),
    title         TEXT UNIQUE NOT NULL,
    description   TEXT        NOT NULL DEFAULT '',
    video_url     TEXT UNIQUE          DEFAULT NULL,
    thumbnail_url TEXT UNIQUE          DEFAULT NULL,
    uploaded_at   DATE        NOT NULL DEFAULT current_date,
    visible       BOOLEAN     NOT NULL DEFAULT FALSE,
    archived      BOOLEAN     NOT NULL DEFAULT FALSE
);

CREATE VIEW bss_web.public_videos AS
SELECT videos.id, videos.title, videos.description, videos.video_url, videos.thumbnail_url, videos.uploaded_at
FROM bss_web.videos videos
WHERE videos.visible = TRUE;

CREATE TABLE bss_web.crew
(
    video_id  TEXT REFERENCES bss_web.videos (id),
    member_id TEXT REFERENCES bss_web.members (id),
    position  TEXT CHECK (trim(position) != ''),
    PRIMARY KEY (video_id, member_id, position)
);

CREATE TABLE bss_web.events
(
    id          TEXT PRIMARY KEY,
    name        TEXT UNIQUE NOT NULL,
    description TEXT        NOT NULL,
    date        DATE        NOT NULL DEFAULT current_date,
    archived    BOOLEAN     NOT NULL DEFAULT FALSE
);

CREATE TABLE bss_web.event_video
(
    event_id TEXT REFERENCES bss_web.events (id),
    video_id TEXT REFERENCES bss_web.videos (id),
    PRIMARY KEY (event_id, video_id)
);
