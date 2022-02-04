export interface EventVideoEntity {
  event_id: string
  video_id: string
}

export function insertEventVideoQuery(eventVideos: EventVideoEntity[]) {
  return (
    'INSERT INTO bss_web.event_video (event_id, video_id) VALUES ' +
    eventVideos.map((eventVideo) => `('${eventVideo.event_id}', '${eventVideo.video_id}')`).join(',')
  )
}
