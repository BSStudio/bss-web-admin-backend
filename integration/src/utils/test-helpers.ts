import type { BssApiClient } from '../client/index.js';
import { getTestPrefix, isTestEntity } from './test-data-factory.js';

export interface CleanupSummary {
  events: number;
  videos: number;
  members: number;
  labels: number;
  errors: Array<{
    type: string;
    id: string;
    error: string;
  }>;
}

/**
 * Cleans up all test data created during integration tests
 * This function identifies test entities by the TEST_PREFIX in their names/titles
 */
export async function cleanupTestData(client: BssApiClient): Promise<CleanupSummary> {
  const summary: CleanupSummary = {
    events: 0,
    videos: 0,
    members: 0,
    labels: 0,
    errors: [],
  };

  console.log(`🧹 Starting cleanup of test data with prefix: ${getTestPrefix()}`);

  // Cleanup Events
  try {
    const events = await client.events.getAllEvents();
    const testEvents = events.filter(event => isTestEntity(event.title));
    
    for (const event of testEvents) {
      try {
        await client.events.deleteEvent(event.id);
        summary.events++;
        console.log(`🗑️  Deleted test event: ${event.title}`);
      } catch (error) {
        summary.errors.push({
          type: 'event',
          id: event.id,
          error: error instanceof Error ? error.message : String(error),
        });
      }
    }
  } catch (error) {
    console.error('❌ Failed to cleanup events:', error);
  }

  // Cleanup Videos
  try {
    const videos = await client.videos.getAllVideos();
    const testVideos = videos.filter(video => isTestEntity(video.title));
    
    for (const video of testVideos) {
      try {
        await client.videos.deleteVideo(video.id);
        summary.videos++;
        console.log(`🗑️  Deleted test video: ${video.title}`);
      } catch (error) {
        summary.errors.push({
          type: 'video',
          id: video.id,
          error: error instanceof Error ? error.message : String(error),
        });
      }
    }
  } catch (error) {
    console.error('❌ Failed to cleanup videos:', error);
  }

  // Cleanup Members
  try {
    const members = await client.members.getAllMembers();
    const testMembers = members.filter(member => isTestEntity(member.name));
    
    for (const member of testMembers) {
      try {
        await client.members.deleteMember(member.id);
        summary.members++;
        console.log(`🗑️  Deleted test member: ${member.name}`);
      } catch (error) {
        summary.errors.push({
          type: 'member',
          id: member.id,
          error: error instanceof Error ? error.message : String(error),
        });
      }
    }
  } catch (error) {
    console.error('❌ Failed to cleanup members:', error);
  }

  // Cleanup Labels
  try {
    const labels = await client.labels.getAllLabels();
    const testLabels = labels.filter(label => isTestEntity(label.name));
    
    for (const label of testLabels) {
      try {
        await client.labels.deleteLabel(label.id);
        summary.labels++;
        console.log(`🗑️  Deleted test label: ${label.name}`);
      } catch (error) {
        summary.errors.push({
          type: 'label',
          id: label.id,
          error: error instanceof Error ? error.message : String(error),
        });
      }
    }
  } catch (error) {
    console.error('❌ Failed to cleanup labels:', error);
  }

  const totalCleaned = summary.events + summary.videos + summary.members + summary.labels;
  console.log(`✅ Cleanup completed: ${totalCleaned} entities removed, ${summary.errors.length} errors`);

  return summary;
}

/**
 * Waits for a condition to be met, with timeout
 */
export async function waitFor<T>(
  condition: () => Promise<T>,
  timeout = 10000,
  interval = 100
): Promise<T> {
  const start = Date.now();
  
  while (Date.now() - start < timeout) {
    try {
      const result = await condition();
      if (result) {
        return result;
      }
    } catch (error) {
      // Continue waiting
    }
    
    await new Promise(resolve => setTimeout(resolve, interval));
  }
  
  throw new Error(`Condition not met within ${timeout}ms`);
}

/**
 * Retries an async operation with exponential backoff
 */
export async function retry<T>(
  operation: () => Promise<T>,
  maxAttempts = 3,
  baseDelay = 1000
): Promise<T> {
  for (let attempt = 1; attempt <= maxAttempts; attempt++) {
    try {
      return await operation();
    } catch (error) {
      if (attempt === maxAttempts) {
        throw error;
      }
      
      const delay = baseDelay * Math.pow(2, attempt - 1);
      console.log(`Attempt ${attempt} failed, retrying in ${delay}ms...`);
      await new Promise(resolve => setTimeout(resolve, delay));
    }
  }
  
  throw new Error('This should never be reached');
}