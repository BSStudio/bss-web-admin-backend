import { DbUtils } from '../../database'
import { eventEntity } from '../../database/event.queries'
import { videoEntity } from '../../database/video.queries'
import { DetailedEvent } from '../../endpoints/event.endpoint'
import { EventVideoEndpoint } from '../../endpoints/event-video.endpoint'

describe('post /api/eventVideo?eventId={eventId}&videoId={videoId}', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => await dbUtils.beforeEach())
  afterAll(async () => await dbUtils.afterAll())

  const eventId = '01234567-0123-0123-0123-0123456789ab'
  const videoId = '11234567-0123-0123-0123-0123456789ab'

  it('should return ok and detailed event with video', async () => {
    expect.assertions(2)
    const eventEntity1 = eventEntity({ id: eventId, url: 'url', title: 'title' })
    await dbUtils.addEvents([eventEntity1])
    await dbUtils.addVideos([videoEntity({ id: videoId, url: 'url', title: 'title' })])

    const response = await EventVideoEndpoint.addEventToVideo(eventId, videoId)

    const expectedDetailedEvent: DetailedEvent = {
      ...eventEntity1,
      videos: [{ id: videoId, url: 'url', title: 'title', visible: false, uploadedAt: '2022-01-01' }],
    }
    expect(response.status).toBe(200)
    expect(response.data).toStrictEqual(expectedDetailedEvent)
  })
})
