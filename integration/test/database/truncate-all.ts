import { Client } from 'pg'

export default async function () {
  const client = new Client({ connectionString: globalThis.baseUrl.db })
  await client.connect()
  await Promise.all([
    client.query('TRUNCATE bss_web.videos CASCADE'),
    client.query('TRUNCATE bss_web.members CASCADE'),
    client.query('TRUNCATE bss_web.events CASCADE'),
  ])
  await client.end()
}
