import { client } from './client'

export interface SimpleCrew {
  memberId: string
  position: string
}

export class VideoCrewEndpoint {
  private static client = client
  static addVideoCrewMember(videoId: string, memberId: string, position: string) {
    return this.client.post<SimpleCrew[]>('/api/v1/videoCrew', null, { params: { videoId, memberId, position } })
  }
  static removeVideoCrewMember(videoId: string, memberId: string, position: string) {
    return this.client.delete<SimpleCrew[]>('/api/v1/videoCrew', { params: { videoId, memberId, position } })
  }
}
