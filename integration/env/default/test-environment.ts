import NodeEnvironment from 'jest-environment-node'
import('dotenv/config')

export default class TestEnvironment extends NodeEnvironment {
  async setup(): Promise<void> {
    await super.setup()

    this.global.baseUrl = {
      app: process.env.APP_URL || 'http://127.0.0.1:8080',
      db: process.env.DB_URL || 'postgresql://postgres:postgres@127.0.0.1:5432/postgres?currentSchema=bss_web',
      fileApi: process.env.FILE_API_URL || 'http://127.0.0.1:8888',
    }
  }
}
