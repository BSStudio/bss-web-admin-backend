import { defineConfig } from 'vitest/config';

export default defineConfig({
  test: {
    globalSetup: './src/globalSetup/index.ts',
    clearMocks: true,
    unstubEnvs: true,
    unstubGlobals: true,
    restoreMocks: true,
  },
});
