export type MemberStatus = 'ALUMNI' | 'ACTIVE_ALUMNI' | 'MEMBER' | 'MEMBER_CANDIDATE' | 'MEMBER_CANDIDATE_CANDIDATE'

export interface MemberEntity {
  id: string
  url: string
  name: string
  nickname: string
  description: string
  joined_at: string
  role: string
  status: MemberStatus
  archived: boolean
}

interface CreateMemberEntity {
  id: string
  url: string
  name: string
  nickname?: string
  description?: string
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
    nickname: createEntity.nickname || '',
    description: createEntity.description || '',
    joined_at: createEntity.joined_at || '2022-01-01',
    role: createEntity.role || '',
    status: createEntity.status || 'MEMBER_CANDIDATE_CANDIDATE',
    archived: createEntity.archived || false,
  }
}

export function insertMemberQuery(members: MemberEntity[]) {
  return (
    'INSERT INTO bss_web.member (id, url, name, nickname, description, joined_at, role, status, archived) VALUES ' +
    members
      .map(
        (member) =>
          `('${member.id}', '${member.url}', '${member.name}', '${member.nickname}', '${member.description}', '${member.joined_at}', '${member.role}', '${member.status}', '${member.archived}')`
      )
      .join(',')
  )
}
