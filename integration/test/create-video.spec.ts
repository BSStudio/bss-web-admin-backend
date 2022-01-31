import { CreateVideo, videoEndpoints } from './endpoints/video.endpoints'
import truncateAll from './database/truncate-all'
import { dbUtils } from './database'
import { videoEntity } from './database/add-videos'

describe('post /api/video', () => {
  beforeEach(async () => await truncateAll())

  const id = '01234567-0123-0123-0123-0123456789AB'
  const url = 'url'
  const title = 'title'

  it('should create video', async () => {
    expect.assertions(6)

    const createVideo: CreateVideo = { url, title }
    const response = await videoEndpoints.createVideo(createVideo)

    expect(response.status).toBe(201)
    expect(typeof response.data.id).toBe('string')
    expect(response.data.url).toStrictEqual(url)
    expect(response.data.title).toStrictEqual(title)
    expect(response.data.visible).toBe(false)
    expect(response.data.title).toStrictEqual(title)
  })
  it('should not create video if url already exist', async () => {
    expect.assertions(1)
    await dbUtils.addVideos([videoEntity(id, url, 'otherTitle')])
    const createVideo: CreateVideo = { url, title }

    const response = await videoEndpoints.createVideo(createVideo)

    expect(response.status).toBe(500)
  })
  it('should not create video if title already exist', async () => {
    expect.assertions(1)
    await dbUtils.addVideos([videoEntity(id, 'otherUrl', title)])
    const createVideo: CreateVideo = { url, title }

    const response = await videoEndpoints.createVideo(createVideo)

    expect(response.status).toBe(500)
  })
})
