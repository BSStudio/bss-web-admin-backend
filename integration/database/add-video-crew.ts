import { pool } from './pool'

export interface CrewEntity {
  video_id: string
  position: string
  member_id: string
}

const insertCrewQuery = (crews: CrewEntity[]) =>
  'INSERT INTO bss_web.crew (video_id, member_id, position) VALUES ' +
  crews.map((crew) => `('${crew.video_id}', '${crew.member_id}', '${crew.position}')`).join(',')

export default async function (crews: CrewEntity[]) {
  const client = await pool.connect()
  await client.query(insertCrewQuery(crews))
  await client.release()
}
