import { dbUtils } from '../../database'
import { EventEndpoint } from '../../endpoints/event.endpoint'
import { eventEntity } from '../../database/add-events'

describe('delete /api/event/{eventId}', () => {
  beforeEach(async () => await dbUtils.truncateAll())

  const id = '01234567-0123-0123-0123-0123456789ab'

  it('should return ok and empty body when remove event', async () => {
    expect.assertions(2)
    await dbUtils.addEvents([eventEntity({ id, url: 'url', title: 'title' })])

    const response = await EventEndpoint.removeEvent(id)

    expect(response.status).toBe(200)
    expect(response.data).toBe('')
  })
  it('should return internal server error when removing a non-existent event', async () => {
    expect.assertions(1)

    const response = await EventEndpoint.removeEvent(id)

    expect(response.status).toBe(500)
  })
})
