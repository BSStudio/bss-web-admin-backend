import { Pool } from 'pg'
import { EventEntity, eventQueries } from './event.queries'
import { insertMemberQuery, MemberEntity } from './member.queries'
import { CrewEntity, insertCrewQuery } from './video-crew.queries'
import { insertVideoQuery, VideoEntity } from './video.queries'

export class DbUtils {
  pool = new Pool({ connectionString: globalThis.baseUrl.db })

  async addEvents(events: EventEntity[]) {
    const client = await this.pool.connect()
    await client.query(eventQueries(events))
    await client.release()
  }
  async addMembers(members: MemberEntity[]) {
    const client = await this.pool.connect()
    await client.query(insertMemberQuery(members))
    await client.release()
  }
  async addVideos(videos: VideoEntity[]) {
    const client = await this.pool.connect()
    await client.query(insertVideoQuery(videos))
    await client.release()
  }
  async addVideoCrew(crews: CrewEntity[]) {
    const client = await this.pool.connect()
    await client.query(insertCrewQuery(crews))
    await client.release()
  }
  async beforeEach() {
    const client = await this.pool.connect()
    await Promise.all([
      client.query('TRUNCATE bss_web.video CASCADE'),
      client.query('TRUNCATE bss_web.member CASCADE'),
      client.query('TRUNCATE bss_web.event CASCADE'),
    ])
    await client.release()
  }
  async afterAll() {
    await this.pool.end()
  }
}
