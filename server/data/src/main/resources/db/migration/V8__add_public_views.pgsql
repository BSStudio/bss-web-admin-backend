CREATE VIEW public.member AS
SELECT id,
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
    array_remove(array_agg(video_url.url), null) AS urls,
    video.title,
    video.description,
    array_remove(array_agg(label.name), null) AS labels,
    video.shooting_date_start,
    video.shooting_date_end,
    video.created_at,
    video.updated_at
FROM video
         LEFT JOIN video_url ON video.id = video_url.video_id
         LEFT JOIN video_label ON video.id = video_label.video_id
         LEFT JOIN label ON video_label.label_id = label.id
WHERE visible
GROUP BY video.id;

CREATE VIEW public.crew AS
SELECT video_id, member_id, position
FROM crew;

COMMENT ON VIEW public.crew IS
    E'@foreignKey (video_id) references video (id)\n@foreignKey (member_id) references member (id)';

CREATE VIEW public.event AS
SELECT id,
       url,
       title,
       description,
       date_from,
       date_to,
       created_at,
       updated_at
FROM event
WHERE visible;

CREATE VIEW public.event_video AS
SELECT event_id, video_id
FROM event_video;

COMMENT ON VIEW public.event_video IS
    E'@foreignKey (event_id) references event (id)\n@foreignKey (video_id) references video (id)';
