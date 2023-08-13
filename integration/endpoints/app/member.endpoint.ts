import { client } from './client'
import { MemberStatus } from '../../database/member.queries'

export interface Member {
  id: string
  url: string
  name: string
  nickname: string
  description: string
  joinedAt: string
  role: string
  status: MemberStatus
  archived: boolean
}

export interface CreateMember {
  url: string
  name: string
}

export interface UpdateMember {
  url: string
  name: string
  nickname: string
  description: string
  joinedAt: string
  role: string
  status: MemberStatus
  archived: boolean
}

export function getAllMembers() {
  return client.get<Member[]>('/api/v1/member')
}
export function createMember(createMember: CreateMember) {
  return client.post<Member>('/api/v1/member', createMember)
}
export function archiveMembers(memberIds: string[], archive: boolean) {
  const params = { memberIds: memberIds.join(','), archive: `${archive}` }
  return client.put<string[]>('/api/v1/member/archive', null, { params })
}
export function updateMember(memberId: string, updateMember: UpdateMember) {
  return client.put<Member>(`/api/v1/member/${memberId}`, updateMember)
}
export function getMember(memberId: string) {
  return client.get<Member>(`/api/v1/member/${memberId}`)
}
export function removeMember(memberId: string) {
  return client.delete<never>(`/api/v1/member/${memberId}`)
}
