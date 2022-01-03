import rimraf from 'rimraf'
import jestTempDir from './jest-temp-dir'

export default async function () {
  // remove temp folder
  rimraf.sync(jestTempDir)
  // stop containers
  await globalThis.dockerComposeEnvironment.down()
}
