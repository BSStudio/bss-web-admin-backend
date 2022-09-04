import { DbUtils, eventEntity, videoEntity } from '../../database'
import { EventVideoEndpoint } from '../../endpoints'

describe('delete /api/eventVideo?eventId={eventId}&videoId={videoId}', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => await dbUtils.beforeEach())
  afterAll(async () => await dbUtils.afterAll())

  const eventId = '01234567-0123-0123-0123-0123456789ab'
  const videoId = '11234567-0123-0123-0123-0123456789ab'

  it('should return ok and empty body', async () => {
    expect.assertions(2)
    await dbUtils.addEvents([eventEntity({ id: eventId, url: 'url', title: 'title' })])
    await dbUtils.addVideos([videoEntity({ id: videoId, url: 'url', title: 'title' })])
    await dbUtils.addEventVideos([{ event_id: eventId, video_id: videoId }])

    const response = await EventVideoEndpoint.removeEventToVideo(eventId, videoId)

    expect(response.status).toBe(200)
    expect(response.data).toBe('')
  })
})
