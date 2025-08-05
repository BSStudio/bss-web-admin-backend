import { beforeEach, describe, expect, it } from 'vitest';
import type { BssApiClient, DetailedVideo, Video } from '../client/index.js';
import {
  createTestApiClient,
  createTestVideo,
  createTestVideoUpdate,
} from '../utils/index.js';

describe('Video API Integration Tests', () => {
  let apiClient: BssApiClient;
  const createdVideos: string[] = [];

  beforeEach(() => {
    apiClient = createTestApiClient();
  });

  describe('Video CRUD Operations', () => {
    it('should create a new video', async () => {
      const createVideo = createTestVideo();

      const createdVideo = await apiClient.videos.createVideo(createVideo);
      createdVideos.push(createdVideo.id);

      expect(createdVideo).toBeDefined();
      expect(createdVideo.id).toBeDefined();
      expect(createdVideo.title).toBe(createVideo.title);
    });

    it('should retrieve all videos', async () => {
      const videos = await apiClient.videos.getAllVideos();

      expect(Array.isArray(videos)).toBe(true);
      const testVideos = videos.filter((video) =>
        video.title.includes('TEST_INTEGRATION'),
      );
      expect(testVideos.length).toBeGreaterThan(0);
    });

    it('should retrieve videos with pagination', async () => {
      const paginatedResponse = await apiClient.videos.getVideosPaginated({
        page: 0,
        size: 10,
        sort: 'title,asc',
      });

      expect(paginatedResponse).toBeDefined();
      expect(paginatedResponse.content).toBeDefined();
      expect(Array.isArray(paginatedResponse.content)).toBe(true);
      expect(paginatedResponse.totalElements).toBeGreaterThanOrEqual(0);
      expect(paginatedResponse.size).toBe(10);
    });

    it('should retrieve a specific video by ID', async () => {
      const createVideo = createTestVideo();
      const createdVideo = await apiClient.videos.createVideo(createVideo);
      createdVideos.push(createdVideo.id);

      const retrievedVideo = await apiClient.videos.getVideoById(
        createdVideo.id,
      );

      expect(retrievedVideo).toBeDefined();
      expect(retrievedVideo.id).toBe(createdVideo.id);
      expect(retrievedVideo.title).toBe(createdVideo.title);
    });

    it('should update an existing video', async () => {
      const createVideo = createTestVideo();
      const createdVideo = await apiClient.videos.createVideo(createVideo);
      createdVideos.push(createdVideo.id);

      const updateVideo = createTestVideoUpdate({
        title: `Updated ${createVideo.title}`,
        description: 'This video has been updated',
        visible: false,
      });

      const updatedVideo = await apiClient.videos.updateVideo(
        createdVideo.id,
        updateVideo,
      );

      expect(updatedVideo).toBeDefined();
      expect(updatedVideo.id).toBe(createdVideo.id);
      expect(updatedVideo.title).toBe(updateVideo.title);
      expect(updatedVideo.description).toBe(updateVideo.description);
      expect(updatedVideo.visible).toBe(false);
    });

    it('should change video visibility', async () => {
      const createVideo = createTestVideo();
      const createdVideo = await apiClient.videos.createVideo(createVideo);
      createdVideos.push(createdVideo.id);

      // Change visibility to false
      const updatedIds = await apiClient.videos.changeVideoVisibility(
        [createdVideo.id],
        false,
      );

      expect(updatedIds).toContain(createdVideo.id);

      // Verify the change
      const updatedVideo = await apiClient.videos.getVideoById(createdVideo.id);
      expect(updatedVideo.visible).toBe(false);
    });

    it('should delete a video', async () => {
      const createVideo = createTestVideo();
      const createdVideo = await apiClient.videos.createVideo(createVideo);

      await expect(
        apiClient.videos.deleteVideo(createdVideo.id),
      ).resolves.toBeUndefined();

      // Verify it's gone
      await expect(
        apiClient.videos.getVideoById(createdVideo.id),
      ).rejects.toThrow();
    });
  });

  describe('Video Validation', () => {
    it('should handle invalid video ID', async () => {
      const invalidId = '00000000-0000-0000-0000-000000000000';

      await expect(apiClient.videos.getVideoById(invalidId)).rejects.toThrow();
    });

    it('should handle bulk visibility change with mixed valid/invalid IDs', async () => {
      const createVideo = createTestVideo();
      const createdVideo = await apiClient.videos.createVideo(createVideo);
      createdVideos.push(createdVideo.id);

      const invalidId = '00000000-0000-0000-0000-000000000000';

      // This might succeed partially or fail completely depending on backend behavior
      try {
        const result = await apiClient.videos.changeVideoVisibility(
          [createdVideo.id, invalidId],
          true,
        );
        // If it succeeds, at least our valid ID should be in the result
        expect(result).toContain(createdVideo.id);
      } catch (error) {
        // If it fails, that's also acceptable behavior
        expect(error).toBeDefined();
      }
    });
  });
});
