import type { UUID } from './common.js';

export interface CreateVideo {
  title: string;
}

export interface UpdateVideo extends CreateVideo {
  urls: string[];
  description: string;
  shootingDateStart: string; // ISO date string
  shootingDateEnd: string; // ISO date string
  visible: boolean;
  labels: string[];
}

export interface Video extends UpdateVideo {
  id: UUID;
}

export interface VideoCrew {
  videoId: UUID;
  position: string;
  member: {
    id: UUID;
    name: string;
    nickname: string;
  };
}

export interface DetailedVideo extends Video {
  crew: VideoCrew[];
}
