import { Client } from 'pg'

export type MemberStatus = 'ALUMNI' | 'ACTIVE_ALUMNI' | 'MEMBER' | 'MEMBER_CANDIDATE' | 'MEMBER_CANDIDATE_CANDIDATE'

export interface MemberEntity {
  id: string
  name: string
  imageUrl: string | null
  joinedAt: string
  role: string
  status: MemberStatus
  archived: boolean
}

export default async function (members: MemberEntity[]) {
  const client = new Client({ connectionString: globalThis.baseUrl.db })
  await client.connect()
  await Promise.all(members.map((member) => client.query(`${member}`)))
  await client.end()
}
