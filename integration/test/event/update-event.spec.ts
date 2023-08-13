import { DbUtils, eventEntity } from '../../database'
import { DetailedEvent, updateEvent, UpdateEvent } from '../../endpoints/app'

describe('put /api/v1/event/{eventId}', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => {
    await dbUtils.beforeEach()
  })
  afterAll(async () => {
    await dbUtils.afterAll()
  })

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

    const updateEventBody: UpdateEvent = {
      url: 'updateUrl',
      title: 'updateTitle',
      description: 'updatedDescription',
      date: '1997-01-01',
      visible: true,
    }
    const response = await updateEvent(id, updateEventBody)

    const detailedEvent: DetailedEvent = { id, ...updateEventBody, videos: [] }
    expect(response.status).toBe(200)
    expect(response.data).toStrictEqual(detailedEvent)
  })
})
