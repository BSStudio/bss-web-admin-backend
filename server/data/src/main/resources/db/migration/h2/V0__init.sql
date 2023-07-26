CREATE TABLE status
(
    name VARCHAR(250) PRIMARY KEY
);

INSERT INTO status
VALUES ('ALUMNI'),
       ('ACTIVE_ALUMNI'),
       ('MEMBER'),
       ('MEMBER_CANDIDATE'),
       ('MEMBER_CANDIDATE_CANDIDATE');

CREATE TABLE member
(
    id          UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    url         VARCHAR(250) NOT NULL,
    name        VARCHAR(250) NOT NULL,
    nickname    VARCHAR(250) NOT NULL DEFAULT '',
    description VARCHAR(2000) NOT NULL DEFAULT '',
    joined_at   DATE NOT NULL DEFAULT CURRENT_DATE,
    role        VARCHAR(250) NOT NULL DEFAULT '',
    status      VARCHAR(250) NOT NULL DEFAULT 'MEMBER_CANDIDATE_CANDIDATE',
    archived    BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT member_url_not_empty CHECK (trim(url) <> ''),
    CONSTRAINT member_name_not_empty CHECK (trim(name) <> '')
);

CREATE TABLE video
(
    id            UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    url           VARCHAR(250) NOT NULL,
    title         VARCHAR(250) NOT NULL,
    description   VARCHAR(2000) NOT NULL DEFAULT '',
    uploaded_at   DATE NOT NULL DEFAULT CURRENT_DATE,
    visible       BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT video_url_not_empty CHECK (trim(url) <> ''),
    CONSTRAINT video_title_not_empty CHECK (trim(title) <> '')
);

CREATE TABLE crew
(
    video_id  UUID,
    member_id UUID,
    position  VARCHAR(250) NOT NULL,
    PRIMARY KEY (video_id, member_id, position),
    CONSTRAINT position_not_empty CHECK (trim(position) <> ''),
    FOREIGN KEY (video_id) REFERENCES video (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE event
(
    id          UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    url         VARCHAR(250) NOT NULL,
    title       VARCHAR(250) NOT NULL,
    description VARCHAR(2000) NOT NULL DEFAULT '',
    date        DATE NOT NULL DEFAULT CURRENT_DATE,
    visible     BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT url_not_empty CHECK (trim(url) <> ''),
    CONSTRAINT title_not_empty CHECK (trim(title) <> '')
);

CREATE TABLE event_video
(
    event_id UUID,
    video_id UUID,
    PRIMARY KEY (event_id, video_id),
    FOREIGN KEY (event_id) REFERENCES event (id),
    FOREIGN KEY (video_id) REFERENCES video (id)
);
