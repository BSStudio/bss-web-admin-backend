import { Client } from 'pg'

export type MemberStatus = 'ALUMNI' | 'ACTIVE_ALUMNI' | 'MEMBER' | 'MEMBER_CANDIDATE' | 'MEMBER_CANDIDATE_CANDIDATE'

export interface MemberEntity {
  id: string
  url: string
  name: string
  description: string
  image_url: string
  joined_at: string
  role: string
  status: MemberStatus
  archived: boolean
}

interface CreateMemberEntity {
  id: string
  url: string
  name: string
  description?: string
  image_url?: string
  joined_at?: string
  role?: string
  status?: MemberStatus
  archived?: boolean
}

export function memberEntity(createEntity: CreateMemberEntity): MemberEntity {
  return {
    id: createEntity.id || '',
    url: createEntity.url || '',
    name: createEntity.name,
    description: createEntity.description || '',
    image_url: createEntity.image_url || '',
    joined_at: createEntity.joined_at || '2022-01-01',
    role: createEntity.role || '',
    status: createEntity.status || 'MEMBER_CANDIDATE_CANDIDATE',
    archived: createEntity.archived || false,
  }
}

function createQuery(members: MemberEntity[]) {
  return (
    'INSERT INTO bss_web.member (id, url, name, description, image_url, joined_at, role, status, archived) VALUES ' +
    members
      .map(
        (member) =>
          `('${member.id}', '${member.url}', '${member.name}', '${member.description}', '${member.image_url}', '${member.joined_at}', '${member.role}', '${member.status}', '${member.archived}')`
      )
      .join(',')
  )
}

export default async function (members: MemberEntity[]) {
  const client = new Client({ connectionString: globalThis.baseUrl.db })
  await client.connect()
  await client.query(createQuery(members))
  await client.end()
}
