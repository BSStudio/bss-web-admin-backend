import { client } from './client'
import { DetailedEvent } from '../../api'

export class EventVideoEndpoint {
  private static client = client
  static addEventToVideo(eventId: string, videoId: string) {
    return this.client.post<DetailedEvent>('/api/v1/eventVideo', null, { params: { eventId, videoId } })
  }
  static removeEventToVideo(eventId: string, videoId: string) {
    return this.client.delete<DetailedEvent>('/api/v1/eventVideo', { params: { eventId, videoId } })
  }
}
