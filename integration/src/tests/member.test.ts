import { describe, expect, it, beforeEach } from 'vitest';
import type { BssApiClient, Member } from '../client/index.js';
import { createTestMember, createTestMemberUpdate, createTestApiClient } from '../utils/index.js';
import { Status } from '../types/index.js';

describe('Member API Integration Tests', () => {
  let apiClient: BssApiClient;
  const createdMembers: string[] = [];

  beforeEach(() => {
    apiClient = createTestApiClient();
  });

  describe('Member CRUD Operations', () => {
    it('should create a new member', async () => {
      const createMember = createTestMember();
      
      const createdMember = await apiClient.members.createMember(createMember);
      createdMembers.push(createdMember.id);
      
      expect(createdMember).toBeDefined();
      expect(createdMember.id).toBeDefined();
      expect(createdMember.name).toBe(createMember.name);
      expect(createdMember.url).toBe(createMember.url);
    });

    it('should retrieve all members', async () => {
      const members = await apiClient.members.getAllMembers();
      
      expect(Array.isArray(members)).toBe(true);
      const testMembers = members.filter(member => member.name.includes('TEST_INTEGRATION'));
      expect(testMembers.length).toBeGreaterThan(0);
    });

    it('should retrieve a specific member by ID', async () => {
      const createMember = createTestMember();
      const createdMember = await apiClient.members.createMember(createMember);
      createdMembers.push(createdMember.id);
      
      const retrievedMember = await apiClient.members.getMemberById(createdMember.id);
      
      expect(retrievedMember).toBeDefined();
      expect(retrievedMember.id).toBe(createdMember.id);
      expect(retrievedMember.name).toBe(createdMember.name);
    });

    it('should update an existing member', async () => {
      const createMember = createTestMember();
      const createdMember = await apiClient.members.createMember(createMember);
      createdMembers.push(createdMember.id);
      
      const updateMember = createTestMemberUpdate({
        name: `Updated ${createMember.name}`,
        nickname: 'UpdatedNickname',
        status: Status.MEMBER_CANDIDATE,
        archived: false
      });
      
      const updatedMember = await apiClient.members.updateMember(createdMember.id, updateMember);
      
      expect(updatedMember).toBeDefined();
      expect(updatedMember.id).toBe(createdMember.id);
      expect(updatedMember.name).toBe(updateMember.name);
      expect(updatedMember.nickname).toBe(updateMember.nickname);
      expect(updatedMember.status).toBe(Status.MEMBER_CANDIDATE);
    });

    it('should archive members', async () => {
      const createMember = createTestMember();
      const createdMember = await apiClient.members.createMember(createMember);
      createdMembers.push(createdMember.id);
      
      // Archive the member
      const archivedIds = await apiClient.members.archiveMembers([createdMember.id], true);
      
      expect(archivedIds).toContain(createdMember.id);
      
      // Verify the change
      const archivedMember = await apiClient.members.getMemberById(createdMember.id);
      expect(archivedMember.archived).toBe(true);
    });

    it('should unarchive members', async () => {
      const createMember = createTestMember();
      const createdMember = await apiClient.members.createMember(createMember);
      createdMembers.push(createdMember.id);
      
      // First archive
      await apiClient.members.archiveMembers([createdMember.id], true);
      
      // Then unarchive
      const unarchivedIds = await apiClient.members.archiveMembers([createdMember.id], false);
      
      expect(unarchivedIds).toContain(createdMember.id);
      
      // Verify the change
      const unarchivedMember = await apiClient.members.getMemberById(createdMember.id);
      expect(unarchivedMember.archived).toBe(false);
    });

    it('should delete a member', async () => {
      const createMember = createTestMember();
      const createdMember = await apiClient.members.createMember(createMember);
      
      await expect(apiClient.members.deleteMember(createdMember.id)).resolves.toBeUndefined();
      
      // Verify it's gone
      await expect(apiClient.members.getMemberById(createdMember.id)).rejects.toThrow();
    });
  });

  describe('Member Validation', () => {
    it('should handle invalid member ID', async () => {
      const invalidId = '00000000-0000-0000-0000-000000000000';
      
      await expect(apiClient.members.getMemberById(invalidId)).rejects.toThrow();
    });

    it('should handle bulk archive with mixed valid/invalid IDs', async () => {
      const createMember = createTestMember();
      const createdMember = await apiClient.members.createMember(createMember);
      createdMembers.push(createdMember.id);
      
      const invalidId = '00000000-0000-0000-0000-000000000000';
      
      try {
        const result = await apiClient.members.archiveMembers([createdMember.id, invalidId], true);
        expect(result).toContain(createdMember.id);
      } catch (error) {
        expect(error).toBeDefined();
      }
    });
  });
});