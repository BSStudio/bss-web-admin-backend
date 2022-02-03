import { Pool } from 'pg'

export const pool = new Pool({ connectionString: globalThis.baseUrl.db })

void process.on('beforeExit', () => pool.end())
