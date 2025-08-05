import { inject } from 'vitest';
import { createBssApiClient, type BssApiClient } from '../client/index.js';

/**
 * Creates a configured API client for use in tests
 * Uses the base URL provided by the global setup
 */
export function createTestApiClient(): BssApiClient {
  const baseURL = inject('app');
  
  return createBssApiClient({
    baseURL,
    timeout: 15000, // Increased timeout for integration tests
  });
}