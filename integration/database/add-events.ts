import { Client } from 'pg'

export interface EventEntity {
  id: string
  name: string
  description: string
  date: string
  archived: boolean
}

export default async function (events: EventEntity[]) {
  const client = new Client({ connectionString: globalThis.baseUrl.db })
  await client.connect()
  // todo
  await Promise.all(events.map((event) => client.query(`${event}`)))
  await client.end()
}
