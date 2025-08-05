import { describe, expect, inject, it } from 'vitest';
import { createTestApiClient } from './utils/index.js';

describe('API Connection', () => {
  it('should have valid app URL', () => {
    expect(inject('app')).toMatch(/^http:\/\/localhost:\d+$/);
  });

  it('should create configured API client', () => {
    const apiClient = createTestApiClient();
    expect(apiClient).toBeDefined();
    expect(apiClient.client).toBeDefined();
    expect(apiClient.events).toBeDefined();
    expect(apiClient.videos).toBeDefined();
    expect(apiClient.members).toBeDefined();
    expect(apiClient.labels).toBeDefined();
  });
});
