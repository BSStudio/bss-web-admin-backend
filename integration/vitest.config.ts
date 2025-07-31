import { defineConfig } from 'vitest/config'

export default defineConfig({
  test: {
    globals: true,
    environment: 'node',
    testTimeout: 60000, // 60 seconds for integration tests
    hookTimeout: 60000,
    teardownTimeout: 60000,
    pool: 'forks', // Better isolation for integration tests
    poolOptions: {
      forks: {
        singleFork: true // Run tests sequentially to avoid resource conflicts
      }
    },
    include: ['tests/**/*.{test,spec}.{js,mjs,cjs,ts,mts,cts,jsx,tsx}'],
    exclude: ['node_modules', 'dist']
  }
})