# Integration Tests

This project contains integration tests for the BSS Web Admin Backend using TypeScript, Vitest, PNPM, and Testcontainers.

## Prerequisites

- Node.js 22 or later
- PNPM
- Docker (for Testcontainers)

## Getting Started

1. Install dependencies:
   ```bash
   pnpm install
   ```

2. Run tests:
   ```bash
   pnpm test
   ```

3. Run tests in watch mode:
   ```bash
   pnpm test:watch
   ```

4. Run tests with UI:
   ```bash
   pnpm test:ui
   ```

## Project Structure

```
integration/
├── src/           # Source code for test utilities
├── tests/         # Test files
├── vitest.config.ts
├── tsconfig.json
└── package.json
```

## Writing Tests

### Basic Test Structure

```typescript
import { describe, it, expect } from 'vitest'

describe('My Feature', () => {
  it('should work correctly', () => {
    expect(true).toBe(true)
  })
})
```

### Using Testcontainers

```typescript
import { GenericContainer, StartedTestContainer } from 'testcontainers'

describe('Database Integration', () => {
  let container: StartedTestContainer

  beforeAll(async () => {
    container = await new GenericContainer('postgres:16-alpine')
      .withEnvironment({
        POSTGRES_USER: 'test',
        POSTGRES_PASSWORD: 'test',
        POSTGRES_DB: 'testdb'
      })
      .withExposedPorts(5432)
      .start()
  })

  afterAll(async () => {
    await container.stop()
  })

  // Your tests here...
})
```

## Configuration

- **TypeScript**: Configured with `@tsconfig/node22` preset
- **Vitest**: Configured for integration testing with longer timeouts
- **Testcontainers**: Ready to use with Docker containers

## Available Scripts

- `pnpm test` - Run all tests once
- `pnpm test:watch` - Run tests in watch mode
- `pnpm test:ui` - Run tests with Vitest UI
- `pnpm test:coverage` - Run tests with coverage report
- `pnpm build` - Compile TypeScript
- `pnpm dev` - Alias for test:watch