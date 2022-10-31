CREATE EXTENSION pgcrypto;

CREATE SCHEMA bss_web;

CREATE TABLE bss_web.status
(
    name VARCHAR(250) PRIMARY KEY
);

INSERT INTO bss_web.status
VALUES ('ALUMNI'),
       ('ACTIVE_ALUMNI'),
       ('MEMBER'),
       ('MEMBER_CANDIDATE'),
       ('MEMBER_CANDIDATE_CANDIDATE');

CREATE TABLE bss_web.member
(
    id          UUID PRIMARY KEY             DEFAULT gen_random_uuid(),
    url         VARCHAR(250) UNIQUE NOT NULL,
    name        VARCHAR(250) UNIQUE NOT NULL,
    description VARCHAR(2000)       NOT NULL DEFAULT '',
    image_url   VARCHAR(250)        NOT NULL DEFAULT '',
    joined_at   DATE                NOT NULL DEFAULT current_date,
    role        VARCHAR(250)        NOT NULL DEFAULT '',
    status      VARCHAR(250)        NOT NULL DEFAULT 'MEMBER_CANDIDATE_CANDIDATE' REFERENCES bss_web.status (name),
    archived    BOOLEAN             NOT NULL DEFAULT FALSE,
    CONSTRAINT url_not_empty CHECK (trim(url) != '') ,
    CONSTRAINT name_not_empty CHECK (trim(name) != '')
);

CREATE TABLE bss_web.video
(
    id            UUID PRIMARY KEY             DEFAULT gen_random_uuid(),
    url           VARCHAR(250) UNIQUE NOT NULL,
    title         VARCHAR(250) UNIQUE NOT NULL,
    description   VARCHAR(250)        NOT NULL DEFAULT '',
    video_url     VARCHAR(250)        NOT NULL DEFAULT '',
    thumbnail_url VARCHAR(250)        NOT NULL DEFAULT '',
    uploaded_at   DATE                NOT NULL DEFAULT current_date,
    visible       BOOLEAN             NOT NULL DEFAULT FALSE,
    CONSTRAINT url_not_empty CHECK (trim(url) != '') ,
    CONSTRAINT title_not_empty CHECK (trim(title) != '')
);

CREATE TABLE bss_web.crew
(
    video_id  UUID REFERENCES bss_web.video (id),
    member_id UUID REFERENCES bss_web.member (id),
    position  VARCHAR(250) NOT NULL,
    PRIMARY KEY (video_id, member_id, position),
    CONSTRAINT position_not_empty CHECK (trim(position) != '')
);

CREATE TABLE bss_web.event
(
    id          UUID PRIMARY KEY             DEFAULT gen_random_uuid(),
    url         VARCHAR(250) UNIQUE NOT NULL,
    title       VARCHAR(250) UNIQUE NOT NULL,
    description VARCHAR(250)        NOT NULL DEFAULT '',
    date        DATE                NOT NULL DEFAULT current_date,
    visible     BOOLEAN             NOT NULL DEFAULT FALSE,
    CONSTRAINT url_not_empty CHECK (trim(url) != '') ,
    CONSTRAINT title_not_empty CHECK (trim(title) != '')
);

CREATE TABLE bss_web.event_video
(
    event_id UUID REFERENCES bss_web.event (id),
    video_id UUID REFERENCES bss_web.video (id),
    PRIMARY KEY (event_id, video_id)
);
