import { StartedDockerComposeEnvironment } from 'testcontainers'

declare global {
  /* eslint-disable no-var */
  var dockerComposeEnvironment: StartedDockerComposeEnvironment
  var baseUrl: { app: string; db: string; fileApi: string }
  /* eslint-enable no-var */
}
