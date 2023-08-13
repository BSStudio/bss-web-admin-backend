import { client } from './client'

export interface BssMetrics {
  videoCount: number
  eventCount: number
  memberCount: number
}

export function getMetrics() {
  return client.get<BssMetrics>('/api/v1/metrics')
}
