import { DbUtils, eventEntity, memberEntity, videoEntity } from '../../database'
import { BssMetrics, MetricsEndpoint } from '../../endpoints'

describe('get /api/v1/metrics', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => await dbUtils.beforeEach())
  afterAll(async () => await dbUtils.afterAll())

  it('should return metrics', async () => {
    expect.assertions(2)
    await dbUtils.addMembers([
      memberEntity({ id: '01234567-0123-0123-0123-0123456789ab', url: 'url0', name: 'name0' }),
      memberEntity({ id: '11234567-0123-0123-0123-0123456789ab', url: 'url1', name: 'name1' }),
      memberEntity({ id: '21234567-0123-0123-0123-0123456789ab', url: 'url2', name: 'name2' }),
    ])
    await dbUtils.addVideos([
      videoEntity({ id: '01234567-0123-0123-0123-0123456789ab', url: 'url0', title: 'title0' }),
      videoEntity({ id: '11234567-0123-0123-0123-0123456789ab', url: 'url1', title: 'title1' }),
    ])
    await dbUtils.addEvents([eventEntity({ id: '01234567-0123-0123-0123-0123456789ab', url: 'url0', title: 'title0' })])

    const response = await MetricsEndpoint.getMetrics()

    expect(response.status).toBe(200)
    expect(response.data).toStrictEqual<BssMetrics>({ memberCount: 3, videoCount: 2, eventCount: 1 })
  })
})
