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
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        TEXT NOT NULL,
    description TEXT NOT NULL    DEFAULT '',
    image_url   TEXT             DEFAULT NULL,
    role        TEXT NOT NULL    DEFAULT '',
    status      TEXT NOT NULL REFERENCES bss_web.status (name)
);

CREATE TABLE bss_web.videos
(
    id            UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    title         TEXT UNIQUE NOT NULL,
    description   TEXT        NOT NULL DEFAULT '',
    video_url     TEXT UNIQUE          DEFAULT NULL,
    thumbnail_url TEXT UNIQUE          DEFAULT NULL,
    uploaded_at   DATE        NOT NULL DEFAULT current_date,
    visible       BOOLEAN     NOT NULL DEFAULT FALSE
);

CREATE VIEW bss_web.public_videos AS
SELECT videos.id, videos.title, videos.description, videos.video_url, videos.thumbnail_url, videos.uploaded_at
FROM bss_web.videos videos
WHERE videos.visible = TRUE;

CREATE TABLE bss_web.crew
(
    video_id  UUID REFERENCES bss_web.videos (id),
    member_id UUID REFERENCES bss_web.members (id),
    position  TEXT CHECK ( trim(position) != ''
) ,
    PRIMARY KEY (video_id, member_id, position)
);

CREATE TABLE bss_web.events
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        TEXT UNIQUE NOT NULL,
    description TEXT CHECK ( trim(description) != ''
) ,
    date        DATE
);

CREATE TABLE bss_web.event_video
(
    event_id UUID REFERENCES bss_web.events (id),
    video_id UUID REFERENCES bss_web.videos (id),
    PRIMARY KEY (event_id, video_id)
);
