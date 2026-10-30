CREATE TABLE video_url
(
    url      VARCHAR(255) PRIMARY KEY,
    video_id UUID REFERENCES video (id)
);

-- migrate all current video.url to video_url.url
INSERT INTO video_url (url, video_id)
SELECT url, id
FROM video;

-- remove url from video
ALTER TABLE video DROP COLUMN url;
