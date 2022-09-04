import { Pool } from 'pg'
import { EventEntity, insertEventQuery } from './event.queries'
import { insertMemberQuery, MemberEntity } from './member.queries'
import { CrewEntity, insertCrewQuery } from './video-crew.queries'
import { insertVideoQuery, VideoEntity } from './video.queries'
import { EventVideoEntity, insertEventVideoQuery } from './event-video.queries'

export { eventEntity } from './event.queries'
export { memberEntity } from './member.queries'
export { videoEntity } from './video.queries'

export class DbUtils {
  pool = new Pool({ connectionString: globalThis.baseUrl.db })

  async addEvents(events: EventEntity[]) {
    const client = await this.pool.connect()
    await client.query(insertEventQuery(events))
    client.release()
  }
  async addMembers(members: MemberEntity[]) {
    const client = await this.pool.connect()
    await client.query(insertMemberQuery(members))
    client.release()
  }
  async addVideos(videos: VideoEntity[]) {
    const client = await this.pool.connect()
    await client.query(insertVideoQuery(videos))
    client.release()
  }
  async addVideoCrew(crews: CrewEntity[]) {
    const client = await this.pool.connect()
    await client.query(insertCrewQuery(crews))
    client.release()
  }
  async addEventVideos(eventVideos: EventVideoEntity[]) {
    const client = await this.pool.connect()
    await client.query(insertEventVideoQuery(eventVideos))
    client.release()
  }
  async beforeEach() {
    const client = await this.pool.connect()
    await Promise.all([
      client.query('TRUNCATE bss_web.video CASCADE'),
      client.query('TRUNCATE bss_web.member CASCADE'),
      client.query('TRUNCATE bss_web.event CASCADE'),
    ])
    client.release()
  }
  async afterAll() {
    await this.pool.end()
  }
}
