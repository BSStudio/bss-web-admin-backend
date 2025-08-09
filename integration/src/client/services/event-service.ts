import type { AxiosInstance } from 'axios';
import type {
  CreateEvent,
  DetailedEvent,
  Event,
  UpdateEvent,
  UUID,
} from '../../types/index.js';

export function createEventService(client: AxiosInstance) {
  const basePath = '/api/v1/event';

  return {
    async getAllEvents(): Promise<Event[]> {
      const response = await client.get<Event[]>(basePath);
      return response.data;
    },

    async createEvent(createEvent: CreateEvent): Promise<Event> {
      const response = await client.post<Event>(basePath, createEvent);
      return response.data;
    },

    async getEventById(eventId: UUID): Promise<DetailedEvent> {
      const response = await client.get<DetailedEvent>(
        `${basePath}/${eventId}`,
      );
      return response.data;
    },

    async updateEvent(
      eventId: UUID,
      updateEvent: UpdateEvent,
    ): Promise<DetailedEvent> {
      const response = await client.put<DetailedEvent>(
        `${basePath}/${eventId}`,
        updateEvent,
      );
      return response.data;
    },

    async deleteEvent(eventId: UUID): Promise<void> {
      await client.delete(`${basePath}/${eventId}`);
    },
  };
}
