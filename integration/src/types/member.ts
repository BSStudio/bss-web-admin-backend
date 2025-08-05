import type { UUID } from './common.js';

export enum Status {
  MEMBER = 'MEMBER',
  MEMBER_CANDIDATE = 'MEMBER_CANDIDATE',
  MEMBER_CANDIDATE_CANDIDATE = 'MEMBER_CANDIDATE_CANDIDATE',
}

export interface CreateMember {
  url: string;
  name: string;
}

export interface UpdateMember extends CreateMember {
  nickname: string;
  description: string;
  joinedAt: string; // ISO date string
  role: string;
  status: Status;
  archived: boolean;
}

export interface Member extends UpdateMember {
  id: UUID;
}