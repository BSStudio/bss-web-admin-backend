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
interface Sort {
  empty: boolean
  sorted: boolean
  unsorted: boolean
}
interface Pageable {
  offset: number
  pageNumber: number
  pageSize: number
  paged: boolean
  sort: Sort
}
interface Page<T> {
  content: T[]
  empty: boolean
  first: boolean
  last: boolean
  number: number
  numberOfElements: number
  pageable: Pageable
  size: number
  sort: Sort
  totalElements: number
  totalPages: number
}
interface PageableRequestParam {
  size?: number
  page?: number
  sort?: string
}

export class VideoEndpoint {
  static client = client
  static getAllVideos(pageable?: PageableRequestParam) {
    return this.client.get<Page<Video>>('/api/video', { params: { ...pageable } })
  }
  static createVideo(createVideo: CreateVideo) {
    return this.client.post<Video>('/api/video', createVideo)
  }
  static changeVideoVisibility(videoIds: string[], visible: boolean) {
    return this.client.put<string[]>('/api/video/visible', null, {
      params: { videoIds: videoIds.join(','), visible: `${visible}` },
    })
  }
  static updateVideo(videoId: string, updateVideo: UpdateVideo) {
    return this.client.put<DetailedVideo>(`/api/video/${videoId}`, updateVideo)
  }
  static getVideo(videoId: string) {
    return this.client.get<DetailedVideo>(`/api/video/${videoId}`)
  }
  static removeVideo(videoId: string) {
    return this.client.delete(`/api/video/${videoId}`)
  }
}
