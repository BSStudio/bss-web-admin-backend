import { client } from './client'
import { Page, PageableRequestParam } from '../interface'

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

export class VideoEndpoint {
  private static client = client
  static getAllVideos(pageable?: PageableRequestParam) {
    return this.client.get<Page<Video>>('/api/v1/video', { params: { ...pageable } })
  }
  static createVideo(createVideo: CreateVideo) {
    return this.client.post<Video>('/api/v1/video', createVideo)
  }
  static changeVideoVisibility(videoIds: string[], visible: boolean) {
    return this.client.put<string[]>('/api/v1/video/visible', null, {
      params: { videoIds: videoIds.join(','), visible: `${visible}` },
    })
  }
  static updateVideo(videoId: string, updateVideo: UpdateVideo) {
    return this.client.put<DetailedVideo>(`/api/v1/video/${videoId}`, updateVideo)
  }
  static getVideo(videoId: string) {
    return this.client.get<DetailedVideo>(`/api/v1/video/${videoId}`)
  }
  static removeVideo(videoId: string) {
    return this.client.delete(`/api/v1/video/${videoId}`)
  }
}
