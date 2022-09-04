import { DbUtils, eventEntity } from '../../database'
import { EventEndpoint } from '../../endpoints'
import { DATE_TODAY, UUID_REGEX } from '../../util'

describe('post /api/event', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => await dbUtils.beforeEach())
  afterAll(async () => await dbUtils.afterAll())
  const url = 'url'
  const title = 'title'
  it('should create a new event', async () => {
    expect.assertions(2)

    const response = await EventEndpoint.createEvent({ url, title })

    expect(response.status).toBe(201)
    expect(response.data).toMatchObject({
      id: expect.stringMatching(UUID_REGEX) as string,
      url,
      title,
      description: '',
      date: DATE_TODAY,
      visible: false,
    })
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
