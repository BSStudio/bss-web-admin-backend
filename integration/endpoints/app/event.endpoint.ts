import { client } from './client'
import { Video } from './video.endpoint'

export interface Event {
  id: string
  url: string
  title: string
  description: string
  date: string
  visible: boolean
}

export interface DetailedEvent {
  id: string
  url: string
  title: string
  description: string
  date: string
  visible: boolean
  videos: Video[]
}

export interface CreateEvent {
  url: string
  title: string
}

export interface UpdateEvent {
  url: string
  title: string
  description: string
  date: string
  visible: boolean
}

export class EventEndpoint {
  private static client = client
  static getAllEvents() {
    return this.client.get<Event[]>('/api/v1/event')
  }
  static createEvent(createEvent: CreateEvent) {
    return this.client.post<Event>('/api/v1/event', createEvent)
  }
  static updateEvent(eventId: string, updateEvent: UpdateEvent) {
    return this.client.put<DetailedEvent>(`/api/v1/event/${eventId}`, updateEvent)
  }
  static getEvent(eventId: string) {
    return this.client.get<DetailedEvent>(`/api/v1/event/${eventId}`)
  }
  static removeEvent(eventId: string) {
    return this.client.delete(`/api/v1/event/${eventId}`)
  }
}