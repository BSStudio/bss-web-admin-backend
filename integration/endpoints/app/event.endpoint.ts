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

export function getAllEvents() {
  return client.get<Event[]>('/api/v1/event')
}
export function createEvent(createEvent: CreateEvent) {
  return client.post<Event>('/api/v1/event', createEvent)
}
export function updateEvent(eventId: string, updateEvent: UpdateEvent) {
  return client.put<DetailedEvent>(`/api/v1/event/${eventId}`, updateEvent)
}
export function getEvent(eventId: string) {
  return client.get<DetailedEvent>(`/api/v1/event/${eventId}`)
}
export function removeEvent(eventId: string) {
  return client.delete(`/api/v1/event/${eventId}`)
}
