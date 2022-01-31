import { videoEndpoints } from './endpoints/video.endpoints'
import truncateAll from './database/truncate-all'
import { dbUtils } from './database'
import { videoEntity } from './database/add-videos'

describe('get /api/video', () => {
  beforeEach(async () => await truncateAll())

  it('should return empty', async () => {
    expect.assertions(2)

    const response = await videoEndpoints.getAllVideos(0, 1)

    expect(response.status).toBe(200)
    expect(response.data).toStrictEqual([])
  })

  it('should return paginated results', async () => {
    expect.assertions(4)
    await dbUtils.addVideos([
      videoEntity('01234567-0123-0123-0123-0123456789AB', 'url1', 'title1'),
      videoEntity('11234567-0123-0123-0123-0123456789AB', 'url2', 'title2'),
      videoEntity('21234567-0123-0123-0123-0123456789AB', 'url3', 'title3'),
    ])

    const response1 = await videoEndpoints.getAllVideos(0, 2)
    const response2 = await videoEndpoints.getAllVideos(1, 2)

    expect(response1.status).toBe(200)
    expect(response1.data).toHaveLength(2)

    expect(response2.status).toBe(200)
    expect(response2.data).toHaveLength(1)
  })
})
