package hu.bsstudio.bssweb.video.page

import org.springframework.dao.DataIntegrityViolationException

/**
 * Whether the database refused to show a video because one of its events is hidden. The trigger in
 * `V9__hide_videos_of_hidden_events.sql` names the constraint `video_in_hidden_event` in its message.
 */
internal fun DataIntegrityViolationException.isVideoInHiddenEvent(): Boolean =
    generateSequence<Throwable>(this) { it.cause }.any { it.message?.contains(VIDEO_IN_HIDDEN_EVENT) == true }

private const val VIDEO_IN_HIDDEN_EVENT = "video_in_hidden_event"
