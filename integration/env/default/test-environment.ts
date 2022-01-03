import NodeEnvironment = require('jest-environment-node')

export default class TestEnvironment extends NodeEnvironment {
  async setup(): Promise<void> {
    await super.setup()

    this.global.baseUrl = {
      app: 'http://localhost:8080',
      db: 'jdbc:postgresql://localhost:5432/postgres?currentSchema=bss_web',
    }

    return Promise.resolve()
  }
}
