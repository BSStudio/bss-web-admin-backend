import { DbUtils, videoEntity } from '../../database'
import { DetailedVideo, VideoEndpoint } from '../../endpoints'

describe('get /api/video/{videoId}', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => await dbUtils.beforeEach())
  afterAll(async () => await dbUtils.afterAll())

  const id = '01234567-0123-0123-0123-0123456789ab'
  const url = 'url'
  const title = 'title'

  it('should return not found and empty body when video does not exist', async () => {
    expect.assertions(2)

    const response = await VideoEndpoint.getVideo(id)

    expect(response.status).toBe(404)
    expect(response.data).toBe('')
  })

  it('should return ok and detailed video', async () => {
    expect.assertions(2)
    await dbUtils.addVideos([videoEntity({ id, url, title })])

    const response1 = await VideoEndpoint.getVideo(id)

    const expectedDetailedVideo: DetailedVideo = {
      id,
      url,
      title,
      videoUrl: '',
      visible: false,
      description: '',
      thumbnailUrl: '',
      uploadedAt: '2022-01-01',
      crew: [],
    }
    expect(response1.status).toBe(200)
    expect(response1.data).toStrictEqual(expectedDetailedVideo)
  })
})
