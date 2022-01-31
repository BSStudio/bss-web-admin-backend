import { DetailedVideo, videoEndpoints } from './endpoints/video.endpoints'
import truncateAll from './database/truncate-all'
import { dbUtils } from './database'
import { videoEntity } from './database/add-videos'

describe('get /api/video/{videoId}', () => {
  beforeEach(async () => await truncateAll())

  const id = '01234567-0123-0123-0123-0123456789ab'
  const url = 'url'
  const title = 'title'

  it('should return empty', async () => {
    expect.assertions(2)

    const response = await videoEndpoints.getVideo(id)

    expect(response.status).toBe(404)
    expect(response.data).toBe('')
  })

  it('should return detailed video', async () => {
    expect.assertions(2)
    await dbUtils.addVideos([videoEntity(id, url, title)])

    const response1 = await videoEndpoints.getVideo(id)

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
