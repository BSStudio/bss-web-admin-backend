import type { UUID } from './common.js';

export interface CreateEvent {
  url: string;
  title: string;
}

export interface UpdateEvent extends CreateEvent {
  description: string;
  dateFrom: string; // ISO date string
  dateTo: string; // ISO date string
  visible: boolean;
}

export interface Event extends UpdateEvent {
  id: UUID;
}

export interface DetailedEvent extends Event {
  // Additional fields from DetailedEvent if any
}
