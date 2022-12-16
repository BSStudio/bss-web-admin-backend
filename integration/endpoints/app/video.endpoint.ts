import { client } from './client'
import { Page, PageableRequestParam } from '../../interface'
import { CreateVideo, DetailedVideo, UpdateVideo, Video } from '../../api'

export class VideoEndpoint {
  private static client = client
  static getAllVideos() {
    return this.client.get<Video[]>('/api/v1/video/all')
  }
  static getAllVideosPaginated(pageable?: PageableRequestParam) {
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
