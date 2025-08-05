import type { AxiosInstance } from 'axios';
import type { Member, CreateMember, UpdateMember, UUID } from '../../types/index.js';

export function createMemberService(client: AxiosInstance) {
  const basePath = '/api/v1/member';

  return {
    async getAllMembers(): Promise<Member[]> {
      const response = await client.get<Member[]>(basePath);
      return response.data;
    },

    async createMember(createMember: CreateMember): Promise<Member> {
      const response = await client.post<Member>(basePath, createMember);
      return response.data;
    },

    async updateMember(memberId: UUID, updateMember: UpdateMember): Promise<Member> {
      const response = await client.put<Member>(`${basePath}/${memberId}`, updateMember);
      return response.data;
    },

    async archiveMembers(memberIds: UUID[], archive = true): Promise<UUID[]> {
      // Create URLSearchParams for proper array handling
      const params = new URLSearchParams();
      memberIds.forEach(id => params.append('memberIds', id));
      params.append('archive', archive.toString());
      
      const response = await client.put<UUID[]>(`${basePath}/archive?${params.toString()}`);
      return response.data;
    },

    async getMemberById(memberId: UUID): Promise<Member> {
      const response = await client.get<Member>(`${basePath}/${memberId}`);
      return response.data;
    },

    async deleteMember(memberId: UUID): Promise<void> {
      await client.delete(`${basePath}/${memberId}`);
    },
  };
}