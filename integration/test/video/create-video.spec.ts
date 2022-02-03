import { CreateVideo, VideoEndpoint } from '../../endpoints/video.endpoint'
import truncateAll from '../../database/truncate-all'
import { dbUtils } from '../../database'
import { videoEntity } from '../../database/add-videos'

describe('post /api/video', () => {
  beforeEach(async () => await truncateAll())

  const id = '01234567-0123-0123-0123-0123456789ab'
  const url = 'url'
  const title = 'title'

  it('should return ok and created video', async () => {
    expect.assertions(7)

    const createVideo: CreateVideo = { url, title }
    const response = await VideoEndpoint.createVideo(createVideo)

    expect(response.status).toBe(201)
    expect(typeof response.data.id).toBe('string')
    expect(response.data.url).toStrictEqual(url)
    expect(response.data.title).toStrictEqual(title)
    expect(response.data.visible).toBe(false)
    expect(response.data.uploadedAt).toStrictEqual(new Date().toISOString().split('T')[0])
    expect(response.data.title).toStrictEqual(title)
  })
  it('should return internal server error when url already exist', async () => {
    expect.assertions(1)
    await dbUtils.addVideos([videoEntity({ id, url, title: 'otherTitle' })])
    const createVideo: CreateVideo = { url, title }

    const response = await VideoEndpoint.createVideo(createVideo)

    expect(response.status).toBe(500)
  })
  it('should return internal server error when title already exist', async () => {
    expect.assertions(1)
    await dbUtils.addVideos([videoEntity({ id, url: 'otherUrl', title })])
    const createVideo: CreateVideo = { url, title }

    const response = await VideoEndpoint.createVideo(createVideo)

    expect(response.status).toBe(500)
  })
})
