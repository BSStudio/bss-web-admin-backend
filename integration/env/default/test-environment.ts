import NodeEnvironment from 'jest-environment-node'
import('dotenv/config')

export default class TestEnvironment extends NodeEnvironment {
  async setup(): Promise<void> {
    await super.setup()

    this.global.baseUrl = {
      app: process.env.APP_URL || 'http://localhost:8080',
      db: process.env.DB_URL || 'postgresql://postgres:postgres@localhost:5432/postgres?currentSchema=bss_web',
    }
  }
}
