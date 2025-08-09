import {
  DockerComposeEnvironment,
  type StartedDockerComposeEnvironment,
  Wait,
} from 'testcontainers';

import type { TestProject } from 'vitest/node';


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
  
  project.provide('app', baseURL);
  
  console.log('✅ Docker Compose environment started successfully.');
  console.log(`🔗 API Base URL: ${baseURL}`);
}

export async function teardown() {
  console.log('🧹 Starting teardown process...');
  
  if (compose) {
    await compose.down();
    console.log('✅ Docker Compose environment torn down successfully.');
  } else {
    console.warn('⚠️ Docker Compose environment not found');
  }
}

declare module 'vitest' {
  export interface ProvidedContext {
    app: string;
  }
}
