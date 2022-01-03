import * as os from 'os'
import * as path from 'path'

export default path.join(os.tmpdir(), 'jest_testcontainers_global_setup')
