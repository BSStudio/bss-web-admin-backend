import { Client } from 'pg'

export default async function () {
  const client = new Client({ connectionString: globalThis.baseUrl.db })
  await client.connect()
  await Promise.all([
    client.query('TRUNCATE bss_web.video CASCADE'),
    client.query('TRUNCATE bss_web.member CASCADE'),
    client.query('TRUNCATE bss_web.event CASCADE'),
  ])
  await client.end()
}
