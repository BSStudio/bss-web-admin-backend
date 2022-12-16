import { client } from './client'
import { CreateMember, Member, UpdateMember } from '../../api'

export class MemberEndpoint {
  private static client = client
  static getAllMembers() {
    return this.client.get<Member[]>('/api/v1/member')
  }
  static createMember(createMember: CreateMember) {
    return this.client.post<Member>('/api/v1/member', createMember)
  }
  static archiveMembers(memberIds: string[], archive: boolean) {
    return this.client.put<string[]>('/api/v1/member/archive', null, {
      params: { memberIds: memberIds.join(','), archive: `${archive}` },
    })
  }
  static updateMember(memberId: string, updateMember: UpdateMember) {
    return this.client.put<Member>(`/api/v1/member/${memberId}`, updateMember)
  }
  static getMember(memberId: string) {
    return this.client.get<Member>(`/api/v1/member/${memberId}`)
  }
  static removeMember(memberId: string) {
    return this.client.delete(`/api/v1/member/${memberId}`)
  }
}
