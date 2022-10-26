import { client } from './client'
import { DetailedEvent } from './event.endpoint'

export class EventVideoEndpoint {
  private static client = client
  static addEventToVideo(eventId: string, videoId: string) {
    return this.client.post<DetailedEvent>('/api/eventVideo', null, { params: { eventId, videoId } })
  }
  static removeEventToVideo(eventId: string, videoId: string) {
    return this.client.delete<DetailedEvent>('/api/eventVideo', { params: { eventId, videoId } })
  }
}
