import { DbUtils, eventEntity } from '../../database'
import { Event, EventEndpoint } from '../../endpoints'

describe('get /api/event', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => await dbUtils.beforeEach())
  afterAll(async () => await dbUtils.afterAll())

  it('should return ok and all events', async () => {
    expect.assertions(2)
    const eventEntity1 = eventEntity({ id: '01234567-0123-0123-0123-0123456789ab', url: 'url1', title: 'title1' })
    const eventEntity2 = eventEntity({ id: '11234567-0123-0123-0123-0123456789ab', url: 'url2', title: 'title2' })
    await dbUtils.addEvents([eventEntity1, eventEntity2])

    const response = await EventEndpoint.getAllEvents()

    const expectedEvent1: Event = { ...eventEntity1 }
    const expectedEvent2: Event = { ...eventEntity2 }
    expect(response.status).toBe(200)
    expect(response.data).toStrictEqual([expectedEvent1, expectedEvent2])
  })

  it('should return ok and empty array', async () => {
    expect.assertions(2)

    const response = await EventEndpoint.getAllEvents()

    expect(response.status).toBe(200)
    expect(response.data).toStrictEqual([])
  })
})
