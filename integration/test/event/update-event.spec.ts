import { DbUtils } from '../../database'
import { eventEntity } from '../../database/event.queries'
import { DetailedEvent, EventEndpoint, UpdateEvent } from '../../endpoints/event.endpoint'

describe('put /api/event/{eventId}', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => await dbUtils.beforeEach())
  afterAll(async () => await dbUtils.afterAll())

  const id = '01234567-0123-0123-0123-0123456789ab'

  it('should return ok and updated detailed event', async () => {
    expect.assertions(2)
    await dbUtils.addEvents([
      eventEntity({
        id,
        url: 'url',
        title: 'title',
        description: 'description',
        date: '2022-01-01',
        visible: false,
      }),
    ])

    const updateEvent: UpdateEvent = {
      url: 'updateUrl',
      title: 'updateTitle',
      description: 'updatedDescription',
      date: '1997-01-01',
      visible: true,
    }
    const response = await EventEndpoint.updateEvent(id, updateEvent)

    const detailedEvent: DetailedEvent = { id, ...updateEvent, videos: [] }
    expect(response.status).toBe(200)
    expect(response.data).toStrictEqual(detailedEvent)
  })
})
