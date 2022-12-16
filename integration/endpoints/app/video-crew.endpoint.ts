import { client } from './client'
import { DetailedVideo } from '../../api'

export class VideoCrewEndpoint {
  private static client = client
  static getPositions() {
    return this.client.get<string[]>('/api/v1/videoCrew/position')
  }
  static addVideoCrewMember(videoId: string, memberId: string, position: string) {
    return this.client.put<DetailedVideo>('/api/v1/videoCrew', null, { params: { videoId, memberId, position } })
  }
  static removeVideoCrewMember(videoId: string, memberId: string, position: string) {
    return this.client.delete<DetailedVideo>('/api/v1/videoCrew', { params: { videoId, memberId, position } })
  }
}
