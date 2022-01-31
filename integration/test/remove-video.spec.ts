import { videoEndpoints } from './endpoints/video.endpoints'
import truncateAll from './database/truncate-all'
import { dbUtils } from './database'
import { videoEntity } from './database/add-videos'

describe('delete /api/video/{videoId}', () => {
  beforeEach(async () => await truncateAll())

  const id = '01234567-0123-0123-0123-0123456789ab'
  it('should return 500 if it', async () => {
    expect.assertions(1)

    const response = await videoEndpoints.removeVideo(id)

    expect(response.status).toBe(500)
  })

  it('should remove video that exist', async () => {
    expect.assertions(2)
    await dbUtils.addVideos([videoEntity(id, 'url', 'title')])

    const deleteResponse = await videoEndpoints.removeVideo(id)
    const getResponse = await videoEndpoints.getVideo(id)

    expect(deleteResponse.status).toBe(200)
    expect(getResponse.status).toBe(404)
  })
})
