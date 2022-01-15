import { Client } from 'pg'

export interface VideoEntity {
  id: string
  title: string
  description: string
  video_url: string | null
  thumbnail_url: string | null
  uploaded_at: string
  visible: boolean
  archived: boolean
}

export function createVideoEntity(
  id: string,
  title: string,
  video_url: string,
  thumbnail_url: string,
  description = '',
  uploaded_at = '2022-1-1',
  visible = true,
  archived = false
): VideoEntity {
  return {
    id,
    title,
    description,
    video_url,
    thumbnail_url,
    uploaded_at,
    visible,
    archived,
  }
}

function insertVideoQuery(videos: VideoEntity[]) {
  return (
    'INSERT INTO bss_web.videos (id, title, description, video_url, thumbnail_url, uploaded_at, visible, archived) VALUES ' +
    videos
      .map(
        (video) =>
          `('${video.id}', '${video.title}', '${video.description}', '${video.video_url}', '${video.thumbnail_url}', '${video.uploaded_at}', '${video.visible}', '${video.archived}')`
      )
      .join(',')
  )
}

export default async function (videos: VideoEntity[]) {
  const client = await new Client({ connectionString: globalThis.baseUrl.db })
  await client.connect()
  await client.query(insertVideoQuery(videos))
  await client.end()
}
