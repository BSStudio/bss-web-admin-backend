import axios, { Method } from 'axios'

describe('security', () => {
  const client = axios.create({
    baseURL: globalThis.baseUrl.app,
    validateStatus: null,
  })
  const endpoints: { method: Method; url: string }[] = [
    { method: 'GET', url: '/api/v1/event' },
    { method: 'POST', url: '/api/v1/event' },
    { method: 'PUT', url: '/api/v1/event/${eventId}' },
    { method: 'GET', url: '/api/v1/event/${eventId}' },
    { method: 'DELETE', url: '/api/v1/event/${eventId}' },
    { method: 'POST', url: '/api/v1/eventVideo' },
    { method: 'DELETE', url: '/api/v1/eventVideo' },
    { method: 'GET', url: '/api/v1/member' },
    { method: 'POST', url: '/api/v1/member' },
    { method: 'PUT', url: '/api/v1/member/archive' },
    { method: 'PUT', url: '/api/v1/member/${memberId}' },
    { method: 'GET', url: '/api/v1/member/${memberId}' },
    { method: 'DELETE', url: '/api/v1/member/${memberId}' },
    { method: 'GET', url: '/api/v1/metrics' },
    { method: 'GET', url: '/api/v1/video/all' },
    { method: 'GET', url: '/api/v1/video' },
    { method: 'POST', url: '/api/v1/video' },
    { method: 'PUT', url: '/api/v1/video/visible' },
    { method: 'PUT', url: '/api/v1/video/${videoId}' },
    { method: 'GET', url: '/api/v1/video/${videoId}' },
    { method: 'DELETE', url: '/api/v1/video/${videoId}' },
    { method: 'GET', url: '/api/v1/videoCrew/position' },
    { method: 'PUT', url: '/api/v1/videoCrew' },
    { method: 'DELETE', url: '/api/v1/videoCrew' },
  ]
  it.each(endpoints)('should return unauthorized for $method $url', async ({ method, url }) => {
    expect.assertions(1)
    const response = await client.request({ method, url })

    expect(response.status).toBe(401)
  })
})
