export interface Event {
  id: string
  url: string
  title: string
  description: string
  date: string
  visible: boolean
}

export interface DetailedEvent {
  id: string
  url: string
  title: string
  description: string
  date: string
  visible: boolean
  videos: Video[]
}

export interface CreateEvent {
  url: string
  title: string
}

export interface UpdateEvent {
  url: string
  title: string
  description: string
  date: string
  visible: boolean
}

export type MemberStatus = 'ALUMNI' | 'ACTIVE_ALUMNI' | 'MEMBER' | 'MEMBER_CANDIDATE' | 'MEMBER_CANDIDATE_CANDIDATE'

export interface Member {
  id: string
  url: string
  name: string
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
  description: string
  joinedAt: string
  role: string
  status: MemberStatus
  archived: boolean
}

export interface BssMetrics {
  videoCount: number
  eventCount: number
  memberCount: number
}

export interface Video {
  id: string
  url: string
  title: string
  uploadedAt: string
  visible: boolean
}

export interface CreateVideo {
  url: string
  title: string
}

export interface UpdateVideo {
  url: string
  title: string
  description: string
  uploadedAt: string
  visible: boolean
}

export interface DetailedVideo {
  id: string
  url: string
  title: string
  description: string
  uploadedAt: string
  visible: boolean
  crew: VideoCrew[]
}

interface VideoCrew {
  videoId: string
  position: string
  member: SimpleMember
}

interface SimpleMember {
  id: string
  name: string
}
