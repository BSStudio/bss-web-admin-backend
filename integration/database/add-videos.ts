import { Client } from 'pg'

export interface VideoEntity {
  id: string
  url: string
  title: string
  description: string
  video_url: string
  thumbnail_url: string
  uploaded_at: string
  visible: boolean
}

interface CreateVideoEntity {
  id: string
  url: string
  title: string
  description?: string
  video_url?: string
  thumbnail_url?: string
  uploaded_at?: string
  visible?: boolean
}

export function videoEntity(createEntity: CreateVideoEntity): VideoEntity {
  return {
    id: createEntity.id || '',
    url: createEntity.url || '',
    title: createEntity.title || '',
    description: createEntity.description || '',
    video_url: createEntity.video_url || '',
    thumbnail_url: createEntity.thumbnail_url || '',
    uploaded_at: createEntity.uploaded_at || '2022-01-01',
    visible: createEntity.visible || false,
  }
}

function insertVideoQuery(videos: VideoEntity[]) {
  return (
    'INSERT INTO bss_web.video (id, url, title, description, video_url, thumbnail_url, uploaded_at, visible) VALUES ' +
    videos
      .map(
        (video) =>
          `('${video.id}', '${video.url}', '${video.title}', '${video.description}', '${video.video_url}', '${video.thumbnail_url}', '${video.uploaded_at}', '${video.visible}')`
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
