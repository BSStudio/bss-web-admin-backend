import { client } from './client'

export interface Count {
  count: number
}

const mockBody = (method: string, url: string) => ({
  request: {
    method,
    url,
    bodyPatterns: [
      {
        matchesJsonPath: '$.id',
      },
      {
        matchesJsonPath: '$.url',
      },
    ],
  },
  response: {
    status: 201,
    headers: {
      'Content-Type': 'application/json',
    },
    jsonBody: {
      id: '01234567-0123-0123-0123-0123456789ab',
      url: 'url',
    },
  },
})

const verifyBody = (method: string, url: string) => ({
  method,
  url,
  bodyPatterns: [
    {
      matchesJsonPath: '$.id',
    },
    {
      matchesJsonPath: '$.url',
    },
  ],
})

export function resetMocks() {
  return client.post('/__admin/reset')
}

export function mockCreateVideoFolder() {
  return client.post('/__admin/mappings', mockBody('POST', '/api/v1/video'))
}

export function verifyCreateVideoFolder() {
  return client
    .post<Count>('/__admin/requests/count', verifyBody('POST', '/api/v1/video'))
    .then(({ data }) => data)
    .then(({ count }) => count)
}

export function mockUpdateVideoFolder() {
  return client.post('/__admin/mappings', mockBody('PUT', '/api/v1/video'))
}

export function verifyUpdateVideoFolder() {
  return client
    .post<Count>('/__admin/requests/count', verifyBody('PUT', '/api/v1/video'))
    .then(({ data }) => data)
    .then(({ count }) => count)
}

export function mockCreateMemberFolder() {
  return client.post('/__admin/mappings', mockBody('POST', '/api/v1/member'))
}

export function verifyCreateMemberFolder() {
  return client
    .post<Count>('/__admin/requests/count', verifyBody('POST', '/api/v1/member'))
    .then(({ data }) => data)
    .then(({ count }) => count)
}

export function mockUpdateMemberFolder() {
  return client.post('/__admin/mappings', mockBody('PUT', '/api/v1/member'))
}

export function verifyUpdateMemberFolder() {
  return client
    .post<Count>('/__admin/requests/count', verifyBody('PUT', '/api/v1/member'))
    .then(({ data }) => data)
    .then(({ count }) => count)
}
