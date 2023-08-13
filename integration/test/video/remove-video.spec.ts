import { DbUtils, videoEntity } from '../../database'
import { removeVideo } from '../../endpoints/app'

describe('delete /api/v1/video/{videoId}', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => {
    await dbUtils.beforeEach()
  })
  afterAll(async () => {
    await dbUtils.afterAll()
  })

  const id = '01234567-0123-0123-0123-0123456789ab'
  it('should return no content and empty body when video was removed', async () => {
    expect.assertions(2)
    await dbUtils.addVideos([videoEntity({ id, url: 'url', title: 'title' })])

    const response = await removeVideo(id)

    expect(response.status).toBe(204)
    expect(response.data).toBe('')
  })
  it('should return no content and empty body when removing a non-existent video', async () => {
    expect.assertions(2)

    const response = await removeVideo(id)

    expect(response.status).toBe(204)
    expect(response.data).toBe('')
  })
})
