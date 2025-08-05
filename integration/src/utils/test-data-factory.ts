import { v4 as uuidv4 } from 'uuid';
import type { 
  CreateEvent, 
  CreateVideo, 
  CreateMember, 
  CreateLabel, 
  UpdateEvent, 
  UpdateVideo, 
  UpdateMember
} from '../types/index.js';
import { Status } from '../types/index.js';

// Test prefix for easy identification and cleanup
const TEST_PREFIX = 'TEST_INTEGRATION';

export function generateTestId(): string {
  return `${TEST_PREFIX}_${Date.now()}_${uuidv4().slice(0, 8)}`;
}

export function createTestEvent(overrides?: Partial<CreateEvent>): CreateEvent {
  const testId = generateTestId();
  return {
    url: `https://example.com/event/${testId}`,
    title: `Event ${testId}`,
    ...overrides,
  };
}

export function createTestEventUpdate(overrides?: Partial<UpdateEvent>): UpdateEvent {
  const testId = generateTestId();
  const baseEvent = createTestEvent();
  return {
    ...baseEvent,
    description: `Updated description for ${testId}`,
    dateFrom: '2024-01-01',
    dateTo: '2024-01-02',
    visible: true,
    ...overrides,
  };
}

export function createTestVideo(overrides?: Partial<CreateVideo>): CreateVideo {
  const testId = generateTestId();
  return {
    title: `Video ${testId}`,
    ...overrides,
  };
}

export function createTestVideoUpdate(overrides?: Partial<UpdateVideo>): UpdateVideo {
  const testId = generateTestId();
  const baseVideo = createTestVideo();
  return {
    ...baseVideo,
    urls: [`https://example.com/video/${testId}.mp4`],
    description: `Updated description for ${testId}`,
    shootingDateStart: '2024-01-01',
    shootingDateEnd: '2024-01-02',
    visible: true,
    labels: [`label-${testId}`],
    ...overrides,
  };
}

export function createTestMember(overrides?: Partial<CreateMember>): CreateMember {
  const testId = generateTestId();
  return {
    url: `https://example.com/member/${testId}`,
    name: `Member ${testId}`,
    ...overrides,
  };
}

export function createTestMemberUpdate(overrides?: Partial<UpdateMember>): UpdateMember {
  const testId = generateTestId();
  const baseMember = createTestMember();
  return {
    ...baseMember,
    nickname: `Nickname_${testId}`,
    description: `Description for ${testId}`,
    joinedAt: '2024-01-01',
    role: `Role_${testId}`,
    status: Status.MEMBER,
    archived: false,
    ...overrides,
  };
}

export function createTestLabel(overrides?: Partial<CreateLabel>): CreateLabel {
  const testId = generateTestId();
  return {
    name: `Label ${testId}`,
    description: `Description for ${testId}`,
    ...overrides,
  };
}

// Helper to check if an entity was created by tests
export function isTestEntity(name: string): boolean {
  return name.includes(TEST_PREFIX);
}

// Helper to get test prefix for cleanup queries
export function getTestPrefix(): string {
  return TEST_PREFIX;
}