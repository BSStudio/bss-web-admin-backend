import { DbUtils, videoEntity } from '../../database'
import { VideoEndpoint } from '../../endpoints/app'

describe('delete /api/v1/video/{videoId}', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => await dbUtils.beforeEach())
  afterAll(async () => await dbUtils.afterAll())

  const id = '01234567-0123-0123-0123-0123456789ab'
  it('should return 500 and empty body when removing a non-existent video', async () => {
    expect.assertions(1)

    const response = await VideoEndpoint.removeVideo(id)

    expect(response.status).toBe(500)
  })

  it('should return ok and empty body when video was removed', async () => {
    expect.assertions(3)
    await dbUtils.addVideos([videoEntity({ id, url: 'url', title: 'title' })])

    const deleteResponse = await VideoEndpoint.removeVideo(id)
    const getResponse = await VideoEndpoint.getVideo(id)

    expect(deleteResponse.status).toBe(200)
    expect(deleteResponse.data).toBe('')
    expect(getResponse.status).toBe(404)
  })
})
