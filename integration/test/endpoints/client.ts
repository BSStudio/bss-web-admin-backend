import axios from 'axios'

export const client = axios.create({
  baseURL: globalThis.baseUrl.app,
  validateStatus: null,
})
