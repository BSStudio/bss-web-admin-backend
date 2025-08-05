import type { UUID } from './common.js';

export interface CreateLabel {
  name: string;
  description: string;
}

export interface Label extends CreateLabel {
  id: UUID;
}
