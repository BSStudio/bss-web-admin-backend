import { DbUtils, eventEntity, videoEntity } from '../../database'
import { DetailedEvent, removeEventToVideo } from '../../endpoints/app'

describe('delete /api/v1/eventVideo?eventId={eventId}&videoId={videoId}', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => {
    await dbUtils.beforeEach()
  })
  afterAll(async () => {
    await dbUtils.afterAll()
  })

  const eventId = '01234567-0123-0123-0123-0123456789ab'
  const videoId = '11234567-0123-0123-0123-0123456789ab'

  it('should return ok and empty body', async () => {
    expect.assertions(2)
    const eventEntity1 = eventEntity({ id: eventId, url: 'url', title: 'title' })
    await dbUtils.addEvents([eventEntity1])
    await dbUtils.addVideos([videoEntity({ id: videoId, url: 'url', title: 'title' })])
    await dbUtils.addEventVideos([{ event_id: eventId, video_id: videoId }])

    const response = await removeEventToVideo(eventId, videoId)

    const expectedDetailedEvent: DetailedEvent = {
      ...eventEntity1,
      videos: [],
    }
    expect(response.status).toBe(200)
    expect(response.data).toStrictEqual(expectedDetailedEvent)
  })
})
