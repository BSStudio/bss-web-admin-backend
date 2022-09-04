import { DbUtils, videoEntity } from '../../database'
import { CreateVideo, VideoEndpoint } from '../../endpoints'
import { UUID_REGEX, dateToday } from '../../util'

describe('post /api/video', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => await dbUtils.beforeEach())
  afterAll(async () => await dbUtils.afterAll())

  const id = '01234567-0123-0123-0123-0123456789ab'
  const url = 'url'
  const title = 'title'

  it('should return ok and created video', async () => {
    expect.assertions(2)

    const createVideo: CreateVideo = { url, title }
    const response = await VideoEndpoint.createVideo(createVideo)

    expect(response.status).toBe(201)
    expect(response.data).toMatchObject({
      id: expect.stringMatching(UUID_REGEX) as string,
      url,
      title,
      visible: false,
      uploadedAt: dateToday(),
    })
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
