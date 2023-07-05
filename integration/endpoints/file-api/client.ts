import axios from 'axios'

export const client = axios.create({
  baseURL: globalThis.baseUrl.fileApi,
  validateStatus: null,
})
