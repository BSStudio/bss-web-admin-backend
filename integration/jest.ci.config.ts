import type { Config } from '@jest/types'

const config: Config.InitialOptions = {
  reporters: ['default', ['jest-junit', { outputDirectory: 'out/jest' }]],
  preset: 'ts-jest',
  testEnvironment: require.resolve('./env/ci/test-environment.ts'),
  globalSetup: require.resolve('./env/ci/global-setup.ts'),
  globalTeardown: require.resolve('./env/ci/global-teardown.ts'),
  maxWorkers: 1,
}

export default config
