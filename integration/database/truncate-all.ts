import { pool } from './pool'

export default async function () {
  const client = await pool.connect()
  await Promise.all([
    client.query('TRUNCATE bss_web.video CASCADE'),
    client.query('TRUNCATE bss_web.member CASCADE'),
    client.query('TRUNCATE bss_web.event CASCADE'),
  ])
  await client.release()
}
