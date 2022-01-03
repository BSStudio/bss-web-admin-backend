import type { Config } from '@jest/types'

const config: Config.InitialOptions = {
  reporters: ['default', ['jest-junit', { outputDirectory: 'out' }]],
  preset: 'ts-jest',
  testEnvironment: require.resolve('./env/ci/test-environment.ts'),
  globalSetup: require.resolve('./env/ci/global-setup.ts'),
  globalTeardown: require.resolve('./env/ci/global-teardown.ts'),
}

export default config
