import { client } from './client'
import { BssMetrics } from '../../api'

export class MetricsEndpoint {
  private static client = client
  static getMetrics() {
    return this.client.get<BssMetrics>('/api/v1/metrics')
  }
}
