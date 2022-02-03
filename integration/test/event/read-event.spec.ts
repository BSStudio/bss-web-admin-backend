import { dbUtils } from '../../database'
import { eventEntity } from '../../database/add-events'
import { DetailedEvent, EventEndpoint } from '../../endpoints/event.endpoint'

describe('get /api/event/{eventId}', () => {
  beforeEach(async () => await dbUtils.truncateAll())

  const id = '01234567-0123-0123-0123-0123456789ab'

  it('should return ok and detailed event', async () => {
    expect.assertions(2)
    const eventEntity1 = eventEntity({ id, url: 'url1', title: 'title1' })
    await dbUtils.addEvents([eventEntity1])

    const response = await EventEndpoint.getEvent(id)

    const expectedEvent1: DetailedEvent = { ...eventEntity1, videos: [] }
    expect(response.status).toBe(200)
    expect(response.data).toStrictEqual(expectedEvent1)
  })
})
