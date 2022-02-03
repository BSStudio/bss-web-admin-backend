import { dbUtils } from '../../database'
import { Event, EventEndpoint } from '../../endpoints/event.endpoint'
import { eventEntity } from '../../database/add-events'

describe('post /api/event', () => {
  beforeEach(async () => await dbUtils.truncateAll())
  const url = 'url'
  const title = 'title'
  it('should create a new event', async () => {
    expect.assertions(7)

    const response = await EventEndpoint.createEvent({ url, title })

    expect(response.status).toBe(201)
    expect(typeof response.data.id).toBe('string')
    expect(response.data.url).toBe(url)
    expect(response.data.title).toBe(title)
    expect(response.data.description).toBe('')
    expect(response.data.date).toBe(new Date().toISOString().split('T')[0])
    expect(response.data.visible).toBe(false)
  })
  it('should not create a new event with an existing url', async () => {
    expect.assertions(1)
    await dbUtils.addEvents([eventEntity({ id: '01234567-0123-0123-0123-0123456789ab', url, title: 'otherTitle' })])

    const response = await EventEndpoint.createEvent({ url, title })

    expect(response.status).toBe(500)
  })
  it('should not create a new event with an existing title', async () => {
    expect.assertions(1)
    await dbUtils.addEvents([eventEntity({ id: '01234567-0123-0123-0123-0123456789ab', url: 'otherUrl', title })])

    const response = await EventEndpoint.createEvent({ url, title })

    expect(response.status).toBe(500)
  })
})
