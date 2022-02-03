import { pool } from './pool'

export interface EventEntity {
  id: string
  url: string
  title: string
  description: string
  date: string
  visible: boolean
}

export interface CreateEventEntity {
  id: string
  url: string
  title: string
  description?: string
  date?: string
  visible?: boolean
}

export function eventEntity(createEventEntity: CreateEventEntity): EventEntity {
  return {
    id: createEventEntity.id,
    url: createEventEntity.url,
    title: createEventEntity.title,
    description: createEventEntity.description || '',
    date: createEventEntity.date || '2022-01-01',
    visible: createEventEntity.visible || false,
  }
}

const insertEventQuery = (events: EventEntity[]) =>
  'INSERT INTO bss_web.event (id, url, title, description, date, visible) VALUES ' +
  events
    .map(
      (event) =>
        `('${event.id}', '${event.url}', '${event.title}', '${event.description}', '${event.date}', '${event.visible}')`
    )
    .join(',')

export default async function (events: EventEntity[]) {
  const client = await pool.connect()
  await client.query(insertEventQuery(events))
  await client.release()
}
