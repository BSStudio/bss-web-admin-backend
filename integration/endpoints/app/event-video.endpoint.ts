import { client } from './client'
import { DetailedEvent } from './event.endpoint'

export function addEventToVideo(eventId: string, videoId: string) {
  return client.post<DetailedEvent>('/api/v1/eventVideo', null, { params: { eventId, videoId } })
}

export function removeEventToVideo(eventId: string, videoId: string) {
  return client.delete<DetailedEvent>('/api/v1/eventVideo', { params: { eventId, videoId } })
}
