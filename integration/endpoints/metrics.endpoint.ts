import { client } from './client'

export interface BssMetrics {
  videoCount: number
  eventCount: number
  memberCount: number
}

export class MetricsEndpoint {
  private static client = client
  static getMetrics() {
    return this.client.get<BssMetrics>('/api/v1/metrics')
  }
}
