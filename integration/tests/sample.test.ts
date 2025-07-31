import { describe, it, expect, beforeAll, afterAll } from 'vitest'
import { GenericContainer, StartedTestContainer } from 'testcontainers'

describe('Sample Integration Test', () => {
  let container: StartedTestContainer

  beforeAll(async () => {
    // Example: Start a PostgreSQL container for testing
    container = await new GenericContainer('postgres:16-alpine')
      .withEnvironment({
        POSTGRES_USER: 'test',
        POSTGRES_PASSWORD: 'test',
        POSTGRES_DB: 'testdb'
      })
      .withExposedPorts(5432)
      .start()
    
    console.log(`PostgreSQL container started on port ${container.getMappedPort(5432)}`)
  })

  afterAll(async () => {
    if (container) {
      await container.stop()
    }
  })

  it('should start container successfully', () => {
    expect(container).toBeDefined()
    expect(container.getMappedPort(5432)).toBeGreaterThan(0)
  })

  it('should be able to connect to database', async () => {
    const host = container.getHost()
    const port = container.getMappedPort(5432)
    
    // This is just a basic connectivity test
    // In real tests, you would use a proper database client
    expect(host).toBeDefined()
    expect(port).toBeGreaterThan(0)
    expect(port).toBeLessThan(65536)
  })
})

describe('Simple Unit Test', () => {
  it('should demonstrate basic Vitest functionality', () => {
    const result = 2 + 2
    expect(result).toBe(4)
  })

  it('should work with async operations', async () => {
    const promise = Promise.resolve('Hello, Vitest!')
    await expect(promise).resolves.toBe('Hello, Vitest!')
  })
})