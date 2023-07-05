import * as fs from 'fs/promises'
import NodeEnvironment from 'jest-environment-node'
import * as path from 'path'
import jestTempDir from './jest-temp-dir'

export default class TestEnvironment extends NodeEnvironment {
  private static readTempFile(name: string): Promise<string> {
    return fs.readFile(path.join(jestTempDir, name), 'utf8')
  }

  async setup(): Promise<void> {
    await super.setup()

    // get the urls
    const app = await TestEnvironment.readTempFile('app')
    const db = await TestEnvironment.readTempFile('db')
    const fileApi = await TestEnvironment.readTempFile('fileApi')

    if (!app || !db || !fileApi) {
      throw new Error('baseUrls not found')
    }

    this.global.baseUrl = { app, db, fileApi }
  }
}
