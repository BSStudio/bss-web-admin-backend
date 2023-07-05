import axios from 'axios'

export const client = axios.create({
  baseURL: globalThis.baseUrl.app,
  auth: { username: 'user', password: 'password' },
  validateStatus: null,
})
