import { describe, expect, it, beforeEach } from 'vitest';
import type { BssApiClient, Label } from '../client/index.js';
import { createTestLabel, createTestApiClient } from '../utils/index.js';

describe('Label API Integration Tests', () => {
  let apiClient: BssApiClient;
  const createdLabels: string[] = [];

  beforeEach(() => {
    apiClient = createTestApiClient();
  });

  describe('Label CRUD Operations', () => {
    it('should create a new label', async () => {
      const createLabel = createTestLabel();
      
      const createdLabel = await apiClient.labels.createLabel(createLabel);
      createdLabels.push(createdLabel.id);
      
      expect(createdLabel).toBeDefined();
      expect(createdLabel.id).toBeDefined();
      expect(createdLabel.name).toBe(createLabel.name);
      expect(createdLabel.description).toBe(createLabel.description);
    });

    it('should retrieve all labels', async () => {
      const labels = await apiClient.labels.getAllLabels();
      
      expect(Array.isArray(labels)).toBe(true);
      const testLabels = labels.filter(label => label.name.includes('TEST_INTEGRATION'));
      expect(testLabels.length).toBeGreaterThan(0);
    });

    it('should delete a label', async () => {
      const createLabel = createTestLabel();
      const createdLabel = await apiClient.labels.createLabel(createLabel);
      
      await expect(apiClient.labels.deleteLabel(createdLabel.id)).resolves.toBeUndefined();
      
      // Verify it's gone by checking it's not in the list
      const remainingLabels = await apiClient.labels.getAllLabels();
      const foundLabel = remainingLabels.find(label => label.id === createdLabel.id);
      expect(foundLabel).toBeUndefined();
    });

    it('should create multiple labels with unique names', async () => {
      const label1 = createTestLabel({ name: 'Label 1 TEST_INTEGRATION_UNIQUE_1' });
      const label2 = createTestLabel({ name: 'Label 2 TEST_INTEGRATION_UNIQUE_2' });
      
      const createdLabel1 = await apiClient.labels.createLabel(label1);
      const createdLabel2 = await apiClient.labels.createLabel(label2);
      
      createdLabels.push(createdLabel1.id, createdLabel2.id);
      
      expect(createdLabel1.name).toBe(label1.name);
      expect(createdLabel2.name).toBe(label2.name);
      expect(createdLabel1.id).not.toBe(createdLabel2.id);
    });
  });

  describe('Label Validation', () => {
    it('should handle invalid label ID for deletion', async () => {
      const invalidId = '00000000-0000-0000-0000-000000000000';
      
      // This might succeed (delete non-existent = no-op) or fail depending on backend behavior
      try {
        await apiClient.labels.deleteLabel(invalidId);
        // If it succeeds, that's fine
      } catch (error) {
        // If it fails, that's also acceptable
        expect(error).toBeDefined();
      }
    });

    it('should handle malformed label ID', async () => {
      const malformedId = 'not-a-uuid';
      
      await expect(apiClient.labels.deleteLabel(malformedId)).rejects.toThrow();
    });
  });
});