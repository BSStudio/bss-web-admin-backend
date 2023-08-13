import { client } from './client'
import { Page, PageableRequestParam } from '../../interface'

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
  uploadedAt: string
  visible: boolean
}
export interface DetailedVideo {
  id: string
  url: string
  title: string
  description: string
  uploadedAt: string
  visible: boolean
  crew: VideoCrew[]
}
interface VideoCrew {
  videoId: string
  position: string
  member: SimpleMember
}

interface SimpleMember {
  id: string
  name: string
  nickname: string
}

export function getAllVideos() {
  return client.get<Video[]>('/api/v1/video/all')
}
export function getAllVideosPaginated(pageable?: PageableRequestParam) {
  return client.get<Page<Video>>('/api/v1/video', { params: { ...pageable } })
}
export function createVideo(createVideo: CreateVideo) {
  return client.post<Video>('/api/v1/video', createVideo)
}
export function changeVideoVisibility(videoIds: string[], visible: boolean) {
  const params = { videoIds: videoIds.join(','), visible: `${visible}` }
  return client.put<string[]>('/api/v1/video/visible', null, { params })
}
export function updateVideo(videoId: string, updateVideo: UpdateVideo) {
  return client.put<DetailedVideo>(`/api/v1/video/${videoId}`, updateVideo)
}
export function getVideo(videoId: string) {
  return client.get<DetailedVideo>(`/api/v1/video/${videoId}`)
}
export function removeVideo(videoId: string) {
  return client.delete(`/api/v1/video/${videoId}`)
}
