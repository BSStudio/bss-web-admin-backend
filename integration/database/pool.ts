import { Pool } from 'pg'

export const pool = new Pool({ connectionString: globalThis.baseUrl.db })

void process.on('exit', () => pool.end())
