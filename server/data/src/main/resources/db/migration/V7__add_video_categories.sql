-- create label table with name and description
CREATE TABLE label
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(64)  NOT NULL UNIQUE CHECK ( TRIM(name) != '' ),
    description VARCHAR(256) NOT NULL        CHECK ( TRIM(description) != '' )
);

-- create video category table
-- a video only can have one of the same category
CREATE TABLE video_label
(
    label_id UUID REFERENCES label (id),
    video_id UUID REFERENCES video (id),
    PRIMARY KEY (label_id, video_id)
);
