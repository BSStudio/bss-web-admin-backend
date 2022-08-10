import { DockerComposeEnvironment, Wait } from 'testcontainers'
import * as fs from 'fs/promises'
import mkdirp = require('mkdirp')
import * as path from 'path'
import jestTempDir from './jest-temp-dir'
import('dotenv/config')

const BUILD_CONTEXT = './..'
const COMPOSE_FILE = process.env.DOCKER_COMPOSE_FILE_NAME || 'docker-compose.ci.yml'

export default async function (): Promise<void[]> {
  const dockerComposeEnvironment = await new DockerComposeEnvironment(BUILD_CONTEXT, COMPOSE_FILE)
    .withWaitStrategy('app_1', Wait.forHealthCheck())
    .withWaitStrategy('db_1', Wait.forHealthCheck())
    .withWaitStrategy('influx_1', Wait.forHealthCheck())
    .up()

  globalThis.dockerComposeEnvironment = dockerComposeEnvironment

  const app = dockerComposeEnvironment.getContainer('app-1')
  const db = dockerComposeEnvironment.getContainer('db-1')

  const baseUrl = {
    app: `http://${app.getHost()}:${app.getMappedPort(8080)}`,
    db: `postgresql://postgres:postgres@${db.getHost()}:${db.getMappedPort(5432)}/postgres`,
  }

  // use the file system to expose the baseUrls for TestEnvironments
  mkdirp.sync(jestTempDir)
  return Promise.all(Object.entries(baseUrl).map(([filename, content]) => writeToTempFile(filename, content)))
}

function writeToTempFile(fileName: string, content: string): Promise<void> {
  const filePath = path.join(jestTempDir, fileName)
  return fs.writeFile(filePath, content)
}
