import { VideoEndpoint } from '../../endpoints/video.endpoints'
import truncateAll from '../../database/truncate-all'
import { dbUtils } from '../../database'
import { videoEntity } from '../../database/add-videos'

describe('get /api/video', () => {
  beforeEach(async () => await truncateAll())

  it('should return empty', async () => {
    expect.assertions(2)

    const response = await VideoEndpoint.getAllVideos(0, 1)

    expect(response.status).toBe(200)
    expect(response.data).toStrictEqual([])
  })

  it('should return paginated results', async () => {
    expect.assertions(4)
    await dbUtils.addVideos([
      videoEntity({ id: '01234567-0123-0123-0123-0123456789AB', url: 'url1', title: 'title1' }),
      videoEntity({ id: '11234567-0123-0123-0123-0123456789AB', url: 'url2', title: 'title2' }),
      videoEntity({ id: '21234567-0123-0123-0123-0123456789AB', url: 'url3', title: 'title3' }),
    ])

    const response1 = await VideoEndpoint.getAllVideos(0, 2)
    const response2 = await VideoEndpoint.getAllVideos(1, 2)

    expect(response1.status).toBe(200)
    expect(response1.data).toHaveLength(2)

    expect(response2.status).toBe(200)
    expect(response2.data).toHaveLength(1)
  })
})
