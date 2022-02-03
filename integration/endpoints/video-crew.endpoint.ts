import { client } from './client'

export interface SimpleCrew {
  memberId: string
  position: string
}

export class VideoCrewEndpoint {
  static client = client
  static addVideoCrewMember(videoId: string, memberId: string, position: string) {
    return this.client.post<SimpleCrew[]>('/api/videoCrew', null, { params: { videoId, memberId, position } })
  }
  static removeVideoCrewMember(videoId: string, memberId: string, position: string) {
    return this.client.delete<SimpleCrew[]>('/api/videoCrew', { params: { videoId, memberId, position } })
  }
}
