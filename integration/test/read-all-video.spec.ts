import { videoEndpoints } from './endpoints/video.endpoints'
import truncateAll from './database/truncate-all'
import { dbUtils } from './database'
import { createVideoEntity } from './database/add-videos'

describe('get /api/video', () => {
  beforeEach(async () => {
    await truncateAll()
  })

  it('should return empty', async () => {
    expect.assertions(2)

    const response = await videoEndpoints.getAllVideos(0, 1)

    expect(response.status).toBe(200)
    expect(response.data).toStrictEqual([])
  })

  it('should return paginated results', async () => {
    expect.assertions(4)
    await dbUtils.addVideos([
      createVideoEntity('id1', 'title1', 'vidUrl1', 'thumbUrl1'),
      createVideoEntity('id2', 'title2', 'vidUrl2', 'thumbUrl2'),
      createVideoEntity('id3', 'title3', 'vidUrl3', 'thumbUrl3'),
    ])

    const response1 = await videoEndpoints.getAllVideos(0, 2)
    const response2 = await videoEndpoints.getAllVideos(1, 2)

    expect(response1.status).toBe(200)
    expect(response1.data).toHaveLength(2)

    expect(response2.status).toBe(200)
    expect(response2.data).toHaveLength(1)
  })
})
