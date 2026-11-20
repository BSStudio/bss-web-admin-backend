-- A video is visible only while every event it belongs to is visible:
-- * hiding an event hides its videos,
-- * adding a video to a hidden event hides the video,
-- * showing a video that is in a hidden event fails (check_violation, constraint video_in_hidden_event).
-- Showing an event again leaves its videos hidden: which of them were visible before isn't recorded.
-- The triggers stamp updated_at with clock_timestamp(), the time of the change, not the transaction's start.

-- Make the rule hold from the start: hide the visible videos of events that are hidden already.
UPDATE video
SET visible    = FALSE,
    updated_at = CURRENT_TIMESTAMP
WHERE visible
  AND id IN (SELECT event_video.video_id
             FROM event_video
                      JOIN event ON event.id = event_video.event_id
             WHERE NOT event.visible);

CREATE FUNCTION hide_videos_of_hidden_event() RETURNS TRIGGER
    LANGUAGE plpgsql AS
$$
BEGIN
    UPDATE video
    SET visible    = FALSE,
        updated_at = clock_timestamp()
    WHERE visible
      AND id IN (SELECT video_id FROM event_video WHERE event_id = NEW.id);
    RETURN NULL;
END;
$$;

CREATE TRIGGER hide_videos_of_hidden_event
    AFTER UPDATE OF visible
    ON event
    FOR EACH ROW
    WHEN (OLD.visible AND NOT NEW.visible)
EXECUTE FUNCTION hide_videos_of_hidden_event();

CREATE FUNCTION hide_video_added_to_hidden_event() RETURNS TRIGGER
    LANGUAGE plpgsql AS
$$
BEGIN
    UPDATE video
    SET visible    = FALSE,
        updated_at = clock_timestamp()
    WHERE id = NEW.video_id
      AND visible
      AND EXISTS (SELECT FROM event WHERE id = NEW.event_id AND NOT visible);
    RETURN NULL;
END;
$$;

CREATE TRIGGER hide_video_added_to_hidden_event
    AFTER INSERT
    ON event_video
    FOR EACH ROW
EXECUTE FUNCTION hide_video_added_to_hidden_event();

CREATE FUNCTION refuse_visible_video_in_hidden_event() RETURNS TRIGGER
    LANGUAGE plpgsql AS
$$
BEGIN
    IF EXISTS (SELECT
               FROM event_video
                        JOIN event ON event.id = event_video.event_id
               WHERE event_video.video_id = NEW.id
                 AND NOT event.visible) THEN
        RAISE EXCEPTION 'video_in_hidden_event: video % is in a hidden event, so it can''t be shown', NEW.id
            USING ERRCODE = 'check_violation', CONSTRAINT = 'video_in_hidden_event';
    END IF;
    RETURN NEW;
END;
$$;

CREATE TRIGGER refuse_visible_video_in_hidden_event
    BEFORE UPDATE OF visible
    ON video
    FOR EACH ROW
    WHEN (NEW.visible AND NOT OLD.visible)
EXECUTE FUNCTION refuse_visible_video_in_hidden_event();
