import {
  DockerComposeEnvironment,
  type StartedDockerComposeEnvironment,
  Wait,
} from 'testcontainers';

import type { TestProject } from 'vitest/node';
import { createBssApiClient } from '../client/index.js';
import { cleanupTestData } from '../utils/test-helpers.js';

let compose: StartedDockerComposeEnvironment;
let baseURL: string;

export async function setup(project: TestProject) {
  console.log('🚀 Starting Docker Compose environment...');
  
  const environment = new DockerComposeEnvironment('./../', [
    'docker-compose.yml',
    'docker-compose.ci.yml',
  ])
    .withProfiles('app')
    .withDefaultWaitStrategy(Wait.forHealthCheck());
  
  compose = await environment.up();
  
  baseURL = `http://${compose.getContainer('app-1').getHost()}:${compose.getContainer('app-1').getMappedPort(8080)}`;
  
  // Only provide serializable data
  project.provide('app', baseURL);
  
  console.log('✅ Docker Compose environment started successfully.');
  console.log(`🔗 API Base URL: ${baseURL}`);
  
  // Optional: Clean up any leftover test data from previous runs
  try {
    const apiClient = createBssApiClient({
      baseURL,
      timeout: 15000,
      authToken: 'token', // Mock OIDC token for integration tests
    });
    const cleanupSummary = await cleanupTestData(apiClient);
    if (cleanupSummary.events + cleanupSummary.videos + cleanupSummary.members + cleanupSummary.labels > 0) {
      console.log('🧹 Cleaned up leftover test data from previous runs');
    }
  } catch (error) {
    console.warn('⚠️  Could not clean up leftover test data (this is normal if the database is empty)');
  }
}

export async function teardown() {
  console.log('🧹 Starting teardown process...');
  
  // Clean up test data before shutting down
  if (baseURL) {
    try {
      const apiClient = createBssApiClient({
        baseURL,
        timeout: 15000,
        authToken: 'token', // Mock OIDC token for integration tests
      });
      const cleanupSummary = await cleanupTestData(apiClient);
      console.log('✅ Test data cleanup completed');
    } catch (error) {
      console.warn('⚠️  Could not clean up test data during teardown:', error);
    }
  }
  
  if (compose) {
    await compose.down();
    console.log('✅ Docker Compose environment torn down successfully.');
  }
}

declare module 'vitest' {
  export interface ProvidedContext {
    app: string;
  }
}
