import { client } from './client'

export interface Video {
  id: string
  url: string
  title: string
  uploadedAt: string
  visible: boolean
}
export interface CreateVideo {
  url: string
  title: string
}
export interface UpdateVideo {
  url: string
  title: string
  description: string
  videoUrl: string | null
  thumbnailUrl: string | null
  uploadedAt: string
  visible: boolean
}
export interface DetailedVideo {
  id: string
  url: string
  title: string
  description: string
  videoUrl: string | null
  thumbnailUrl: string | null
  uploadedAt: string
  visible: boolean
  crew: SimpleCrew[]
}

interface SimpleCrew {
  position: string
}

export const videoEndpoints = {
  getAllVideos(page: number, size: number) {
    return client.get<Video[]>('/api/video', { params: { page, size } })
  },
  createVideo(createVideo: CreateVideo | null) {
    return client.post<Video>('/api/video', createVideo)
  },
  changeVideoVisibility(videoIds: string[], visible: boolean) {
    return client.put<string[]>('/api/video/visible', null, {
      params: { videoIds: videoIds.join(','), visible: `${visible}` },
    })
  },
  updateVideo(videoId: string, updateVideo: UpdateVideo) {
    return client.put<DetailedVideo>(`/api/video/${videoId}`, updateVideo)
  },
  getVideo(videoId: string) {
    return client.get<DetailedVideo>(`/api/video/${videoId}`)
  },
  removeVideo(videoId: string) {
    return client.delete(`/api/video/${videoId}`)
  },
}
