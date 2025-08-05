import { beforeEach, describe, expect, it } from 'vitest';
import type { BssApiClient } from '../client/index.js';
import {
  cleanupTestData,
  createTestApiClient,
  createTestEvent,
  createTestLabel,
  createTestMember,
  createTestVideo,
  getTestPrefix,
} from '../utils/index.js';

describe('Test Data Cleanup Integration', () => {
  let apiClient: BssApiClient;

  beforeEach(() => {
    apiClient = createTestApiClient();
  });

  it('should identify and clean up test data correctly', async () => {
    // Create some test data
    const event = await apiClient.events.createEvent(createTestEvent());
    const video = await apiClient.videos.createVideo(createTestVideo());
    const member = await apiClient.members.createMember(createTestMember());
    const label = await apiClient.labels.createLabel(createTestLabel());

    // Verify they exist and have our test prefix
    expect(event.title).toContain(getTestPrefix());
    expect(video.title).toContain(getTestPrefix());
    expect(member.name).toContain(getTestPrefix());
    expect(label.name).toContain(getTestPrefix());

    // Run cleanup
    const cleanupSummary = await cleanupTestData(apiClient);

    // Verify cleanup found and removed our test data
    expect(cleanupSummary.events).toBeGreaterThanOrEqual(1);
    expect(cleanupSummary.videos).toBeGreaterThanOrEqual(1);
    expect(cleanupSummary.members).toBeGreaterThanOrEqual(1);
    expect(cleanupSummary.labels).toBeGreaterThanOrEqual(1);

    // Verify the entities are actually gone
    await expect(apiClient.events.getEventById(event.id)).rejects.toThrow();
    await expect(apiClient.videos.getVideoById(video.id)).rejects.toThrow();
    await expect(apiClient.members.getMemberById(member.id)).rejects.toThrow();

    // Labels can only be verified by checking they're not in the list
    const remainingLabels = await apiClient.labels.getAllLabels();
    const foundLabel = remainingLabels.find((l) => l.id === label.id);
    expect(foundLabel).toBeUndefined();
  });

  it('should handle empty cleanup gracefully', async () => {
    // First ensure we have a clean state by running cleanup
    await cleanupTestData(apiClient);

    // Small delay to ensure all cleanup operations are complete
    await new Promise((resolve) => setTimeout(resolve, 100));

    // Run cleanup again - should find nothing or very little
    const secondCleanup = await cleanupTestData(apiClient);

    // Check that cleanup found minimal entities (allowing for race conditions)
    expect(
      secondCleanup.events +
        secondCleanup.videos +
        secondCleanup.members +
        secondCleanup.labels,
    ).toBeLessThanOrEqual(2);
  });
});
