CREATE VIEW public.member AS
SELECT *
FROM member;

CREATE VIEW public.video AS
SELECT video.*,
       array_remove(array_agg(video_url.url), null) AS urls,
       array_remove(array_agg(label.name), null) AS labels
FROM video
         LEFT JOIN video_url ON video.id = video_url.video_id
         LEFT JOIN video_label ON video.id = video_label.video_id
         LEFT JOIN label ON video_label.label_id = label.id
WHERE visible = true
GROUP BY video.id;

CREATE VIEW public.crew AS
SELECT *
FROM crew;

COMMENT ON VIEW public.crew IS
    E'@foreignKey (video_id) references video (id)\n@foreignKey (member_id) references member (id)';

CREATE VIEW public.event AS
SELECT *
FROM event
WHERE visible = true;

CREATE VIEW public.event_video AS
SELECT *
FROM event_video;

COMMENT ON VIEW public.event_video IS
    E'@foreignKey (event_id) references event (id)\n@foreignKey (video_id) references video (id)';
