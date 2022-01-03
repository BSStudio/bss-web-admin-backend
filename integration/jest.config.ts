import type {Config} from '@jest/types'

const config: Config.InitialOptions = {
    preset: 'ts-jest',
    testEnvironment: require.resolve('./env/default/test-environment.ts'),
}

export default config
