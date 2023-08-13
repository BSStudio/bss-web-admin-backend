import { DbUtils, eventEntity } from '../../database'
import { createEvent } from '../../endpoints/app'
import { UUID_REGEX, dateToday } from '../../util'

describe('post /api/v1/event', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => {
    await dbUtils.beforeEach()
  })
  afterAll(async () => {
    await dbUtils.afterAll()
  })
  const url = 'url'
  const title = 'title'
  it('should create a new event', async () => {
    expect.assertions(3)

    const response = await createEvent({ url, title })

    expect(response.status).toBe(201)
    expect(response.headers.location).toBe(`${globalThis.baseUrl.app}/api/v1/event/${response.data.id}`)
    expect(response.data).toMatchObject({
      id: expect.stringMatching(UUID_REGEX) as string,
      url,
      title,
      description: '',
      date: dateToday(),
      visible: false,
    })
  })
  it('should not create a new event with an existing url', async () => {
    expect.assertions(1)
    await dbUtils.addEvents([eventEntity({ id: '01234567-0123-0123-0123-0123456789ab', url, title: 'otherTitle' })])

    const response = await createEvent({ url, title })

    expect(response.status).toBe(500)
  })
  it('should not create a new event with an existing title', async () => {
    expect.assertions(1)
    await dbUtils.addEvents([eventEntity({ id: '01234567-0123-0123-0123-0123456789ab', url: 'otherUrl', title })])

    const response = await createEvent({ url, title })

    expect(response.status).toBe(500)
  })
})
