# BSS Web Admin Backend Integration Tests

This package provides comprehensive integration tests for the BSS Web Admin Backend API using TypeScript, Axios, and Vitest.

## Features

- 🔧 **Functional API Client**: Function-based approach for creating API clients and services
- 🎯 **Type Safety**: Full TypeScript support with comprehensive type definitions
- 🧪 **Test Data Management**: Automated test data creation with consistent naming for easy cleanup
- 🐳 **Docker Integration**: Uses Testcontainers to spin up the application stack
- 🧹 **Automatic Cleanup**: Test data is automatically identified and cleaned up
- 📊 **Comprehensive Coverage**: Tests for all CRUD operations across Events, Videos, Members, and Labels

## Project Structure

```
src/
├── client/                    # API client library
│   ├── base-client.ts        # Core axios client with interceptors
│   ├── api-client.ts         # Main client factory
│   ├── services/             # Individual service modules
│   │   ├── event-service.ts  # Event API operations
│   │   ├── video-service.ts  # Video API operations
│   │   ├── member-service.ts # Member API operations
│   │   └── label-service.ts  # Label API operations
│   └── index.ts              # Client exports
├── types/                    # TypeScript type definitions
│   ├── common.ts             # Shared types and interfaces
│   ├── event.ts              # Event-related types
│   ├── video.ts              # Video-related types
│   ├── member.ts             # Member-related types
│   ├── label.ts              # Label-related types
│   └── index.ts              # Type exports
├── utils/                    # Test utilities
│   ├── test-data-factory.ts  # Test data creation helpers
│   ├── test-helpers.ts       # Cleanup and utility functions
│   └── index.ts              # Utility exports
├── tests/                    # Integration test suites
│   ├── event.test.ts         # Event API tests
│   ├── video.test.ts         # Video API tests
│   ├── member.test.ts        # Member API tests
│   ├── label.test.ts         # Label API tests
│   └── cleanup.test.ts       # Cleanup functionality tests
├── globalSetup/              # Test environment setup
│   └── index.ts              # Docker Compose and client setup
└── test.test.ts              # Basic connectivity tests
```

## Usage

### Running Tests

```bash
# Install dependencies
pnpm install

# Run all tests
pnpm test

# Run tests with UI
pnpm vitest --ui
```

### Using the API Client

```typescript
import { createBssApiClient } from './src/client/index.js';

// Create a client instance
const client = createBssApiClient({
  baseURL: 'http://localhost:8080',
  timeout: 10000
});

// Use the client
const events = await client.events.getAllEvents();
const newEvent = await client.events.createEvent({
  title: 'My Event',
  url: 'https://example.com/event'
});
```

### Creating Test Data

```typescript
import { 
  createTestEvent, 
  createTestVideo, 
  createTestMember, 
  createTestLabel 
} from './src/utils/index.js';

// Create test data with automatic naming
const eventData = createTestEvent();
const videoData = createTestVideo({ title: 'Custom Title' });

// All test data includes 'TEST_INTEGRATION' prefix for easy cleanup
```

### Manual Cleanup

```typescript
import { cleanupTestData } from './src/utils/index.js';

// Clean up all test data
const summary = await cleanupTestData(client);
console.log(\`Cleaned up: \${summary.events} events, \${summary.videos} videos\`);
```

## API Coverage

### Events API (`/api/v1/event`)
- ✅ GET `/` - List all events
- ✅ POST `/` - Create event
- ✅ GET `/{id}` - Get event by ID
- ✅ PUT `/{id}` - Update event
- ✅ DELETE `/{id}` - Delete event

### Videos API (`/api/v1/video`)
- ✅ GET `/all` - List all videos
- ✅ GET `/` - List videos (paginated)
- ✅ POST `/` - Create video
- ✅ PUT `/visible` - Change video visibility
- ✅ GET `/{id}` - Get video by ID
- ✅ PUT `/{id}` - Update video
- ✅ DELETE `/{id}` - Delete video

### Members API (`/api/v1/member`)
- ✅ GET `/` - List all members
- ✅ POST `/` - Create member
- ✅ PUT `/{id}` - Update member
- ✅ PUT `/archive` - Archive/unarchive members
- ✅ GET `/{id}` - Get member by ID
- ✅ DELETE `/{id}` - Delete member

### Labels API (`/api/v1/label`)
- ✅ GET `/` - List all labels
- ✅ POST `/` - Create label
- ✅ DELETE `/{id}` - Delete label

## Test Data Naming Convention

All test data is created with the prefix `TEST_INTEGRATION_<timestamp>_<uuid>` to ensure:
- Easy identification of test data
- No conflicts with production data
- Reliable cleanup after tests

Examples:
- `TEST_INTEGRATION_1703123456789_a1b2c3d4`
- `Event TEST_INTEGRATION_1703123456789_e5f6g7h8`

## Environment Setup

The tests use Docker Compose to start the application stack:
- Main application with health checks
- Database and other dependencies
- Automatic container lifecycle management

The global setup:
1. Starts Docker Compose environment
2. Waits for health checks to pass
3. Creates and configures API client
4. Provides client to all tests
5. Cleans up test data after tests complete
6. Tears down Docker environment

## Error Handling

The API client includes:
- Automatic request/response logging
- Custom error types with status codes
- Response data extraction
- Timeout handling
- Request/response interceptors

## Development Guidelines

### Adding New API Endpoints

1. **Add types** in `src/types/`
2. **Create service function** in appropriate service file
3. **Add to main client** in `src/client/api-client.ts`
4. **Create test data factory** in `src/utils/test-data-factory.ts`
5. **Write integration tests** in `src/tests/`

### Test Best Practices

- Use the test data factories for consistent data creation
- Always use the injected `apiClient` from global setup
- Test both success and error scenarios
- Verify cleanup works for new entity types
- Include validation tests for edge cases

### Debugging

- Check Docker container logs if tests fail to start
- Use `console.log` in interceptors for request/response debugging
- Verify API endpoints match the actual backend implementation
- Check test data cleanup if tests interfere with each other

## Dependencies

- **axios**: HTTP client for API requests
- **vitest**: Testing framework
- **testcontainers**: Docker container management
- **uuid**: Test data ID generation
- **typescript**: Type safety and development experience

## Configuration

The integration tests are configured via:
- `vitest.config.ts` - Test framework configuration
- `tsconfig.json` - TypeScript configuration  
- `biome.json` - Code formatting and linting
- `package.json` - Dependencies and scripts
