import { describe, expect, it, beforeEach } from 'vitest';
import type { BssApiClient, Event, DetailedEvent } from '../client/index.js';
import { createTestEvent, createTestEventUpdate, createTestApiClient } from '../utils/index.js';

describe('Event API Integration Tests', () => {
  let apiClient: BssApiClient;
  const createdEvents: string[] = [];

  beforeEach(() => {
    apiClient = createTestApiClient();
  });

  describe('Event CRUD Operations', () => {
    it('should create a new event', async () => {
      const createEvent = createTestEvent();
      
      const createdEvent = await apiClient.events.createEvent(createEvent);
      createdEvents.push(createdEvent.id);
      
      expect(createdEvent).toBeDefined();
      expect(createdEvent.id).toBeDefined();
      expect(createdEvent.title).toBe(createEvent.title);
      expect(createdEvent.url).toBe(createEvent.url);
    });

    it('should retrieve all events', async () => {
      const events = await apiClient.events.getAllEvents();
      
      expect(Array.isArray(events)).toBe(true);
      // Should include our test events
      const testEvents = events.filter(event => event.title.includes('TEST_INTEGRATION'));
      expect(testEvents.length).toBeGreaterThan(0);
    });

    it('should retrieve a specific event by ID', async () => {
      // First create an event
      const createEvent = createTestEvent();
      const createdEvent = await apiClient.events.createEvent(createEvent);
      createdEvents.push(createdEvent.id);
      
      // Then retrieve it
      const retrievedEvent = await apiClient.events.getEventById(createdEvent.id);
      
      expect(retrievedEvent).toBeDefined();
      expect(retrievedEvent.id).toBe(createdEvent.id);
      expect(retrievedEvent.title).toBe(createdEvent.title);
    });

    it('should update an existing event', async () => {
      // First create an event
      const createEvent = createTestEvent();
      const createdEvent = await apiClient.events.createEvent(createEvent);
      createdEvents.push(createdEvent.id);
      
      // Then update it
      const updateEvent = createTestEventUpdate({
        title: `Updated ${createEvent.title}`,
        description: 'This event has been updated'
      });
      
      const updatedEvent = await apiClient.events.updateEvent(createdEvent.id, updateEvent);
      
      expect(updatedEvent).toBeDefined();
      expect(updatedEvent.id).toBe(createdEvent.id);
      expect(updatedEvent.title).toBe(updateEvent.title);
      expect(updatedEvent.description).toBe(updateEvent.description);
      expect(updatedEvent.visible).toBe(updateEvent.visible);
    });

    it('should delete an event', async () => {
      // First create an event
      const createEvent = createTestEvent();
      const createdEvent = await apiClient.events.createEvent(createEvent);
      
      // Then delete it
      await expect(apiClient.events.deleteEvent(createdEvent.id)).resolves.toBeUndefined();
      
      // Verify it's gone (should throw 404)
      await expect(apiClient.events.getEventById(createdEvent.id)).rejects.toThrow();
    });
  });

  describe('Event Validation', () => {
    it('should handle invalid event ID', async () => {
      const invalidId = '00000000-0000-0000-0000-000000000000';
      
      await expect(apiClient.events.getEventById(invalidId)).rejects.toThrow();
    });

    it('should handle malformed event ID', async () => {
      const malformedId = 'not-a-uuid';
      
      await expect(apiClient.events.getEventById(malformedId)).rejects.toThrow();
    });
  });
});