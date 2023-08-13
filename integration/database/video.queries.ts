export interface VideoEntity {
  id: string
  url: string
  title: string
  description: string
  uploaded_at: string
  visible: boolean
}

interface CreateVideoEntity {
  id: string
  url: string
  title: string
  description?: string
  uploaded_at?: string
  visible?: boolean
}

export function videoEntity(createEntity: CreateVideoEntity): VideoEntity {
  return {
    id: createEntity.id,
    url: createEntity.url,
    title: createEntity.title,
    description: createEntity.description ?? '',
    uploaded_at: createEntity.uploaded_at ?? '2022-01-01',
    visible: createEntity.visible ?? false,
  }
}

export function insertVideoQuery(videos: VideoEntity[]) {
  return (
    'INSERT INTO bss_web.video (id, url, title, description, uploaded_at, visible) VALUES ' +
    videos
      .map(
        (video) =>
          `('${video.id}', '${video.url}', '${video.title}', '${video.description}', '${video.uploaded_at}', '${video.visible}')`,
      )
      .join(',')
  )
}
