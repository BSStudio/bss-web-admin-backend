import type { AxiosInstance } from 'axios';
import { type ApiClientConfig, createApiClient } from './base-client.js';
import { createEventService } from './services/event-service.js';
import { createLabelService } from './services/label-service.js';
import { createMemberService } from './services/member-service.js';
import { createVideoService } from './services/video-service.js';

export interface BssApiClient {
  client: AxiosInstance;
  events: ReturnType<typeof createEventService>;
  videos: ReturnType<typeof createVideoService>;
  members: ReturnType<typeof createMemberService>;
  labels: ReturnType<typeof createLabelService>;
}

export function createBssApiClient(config: ApiClientConfig): BssApiClient {
  const client = createApiClient(config);

  return {
    client,
    events: createEventService(client),
    videos: createVideoService(client),
    members: createMemberService(client),
    labels: createLabelService(client),
  };
}
