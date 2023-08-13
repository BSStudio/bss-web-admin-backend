import { client } from './client'
import { DetailedVideo } from './video.endpoint'

export function getPositions() {
  return client.get<string[]>('/api/v1/videoCrew/position')
}
export function addVideoCrewMember(videoId: string, memberId: string, position: string) {
  return client.put<DetailedVideo>('/api/v1/videoCrew', null, { params: { videoId, memberId, position } })
}
export function removeVideoCrewMember(videoId: string, memberId: string, position: string) {
  return client.delete<DetailedVideo>('/api/v1/videoCrew', { params: { videoId, memberId, position } })
}
