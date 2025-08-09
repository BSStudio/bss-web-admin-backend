import { inject } from 'vitest';
import { type BssApiClient, createBssApiClient } from '../client/index.js';

/**
 * Creates a configured API client for use in tests
 * Uses the base URL provided by the global setup and includes test auth token
 */
export function createTestApiClient(): BssApiClient {
  const baseURL = inject('app');

  return createBssApiClient({
    baseURL,
    timeout: 15000, // Increased timeout for integration tests
    authToken: 'token', // Mock OIDC token for integration tests
  });
}
