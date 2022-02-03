import { VideoEndpoint } from '../../endpoints/video.endpoint'
import { videoEntity } from '../../database/video.queries'
import { DbUtils } from '../../database'

describe('get /api/video', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => await dbUtils.beforeEach())
  afterAll(async () => await dbUtils.afterAll())

  it('should return ok and empty array', async () => {
    expect.assertions(2)

    const response = await VideoEndpoint.getAllVideos(0, 1)

    expect(response.status).toBe(200)
    expect(response.data).toStrictEqual([])
  })

  it('should return ok and paginated videos', async () => {
    expect.assertions(4)
    await dbUtils.addVideos([
      videoEntity({ id: '01234567-0123-0123-0123-0123456789ab', url: 'url1', title: 'title1' }),
      videoEntity({ id: '11234567-0123-0123-0123-0123456789ab', url: 'url2', title: 'title2' }),
      videoEntity({ id: '21234567-0123-0123-0123-0123456789ab', url: 'url3', title: 'title3' }),
    ])

    const response1 = await VideoEndpoint.getAllVideos(0, 2)
    const response2 = await VideoEndpoint.getAllVideos(1, 2)

    expect(response1.status).toBe(200)
    expect(response1.data).toHaveLength(2)

    expect(response2.status).toBe(200)
    expect(response2.data).toHaveLength(1)
  })
})
