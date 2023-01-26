import { rimraf } from 'rimraf'
import jestTempDir from './jest-temp-dir'

export default async function () {
  // remove temp folder
  await rimraf(jestTempDir)
  // stop containers
  await globalThis.dockerComposeEnvironment.down()
}
