import { client } from './client'

export interface Count {
  count: number
}

export class FileEndpoint {
  private static client = client

  private static mockBody = (method: string, url: string) => ({
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

  private static verifyBody = (method: string, url: string) => ({
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

  static resetMocks() {
    return this.client.post('/__admin/reset')
  }

  static mockCreateVideoFolder() {
    return this.client.post('/__admin/mappings', this.mockBody('POST', '/api/v1/video'))
  }

  static verifyCreateVideoFolder() {
    return this.client
      .post<Count>('/__admin/requests/count', this.verifyBody('POST', '/api/v1/video'))
      .then(({ data }) => data)
      .then(({ count }) => count)
  }

  static mockUpdateVideoFolder() {
    return this.client.post('/__admin/mappings', this.mockBody('PUT', '/api/v1/video'))
  }

  static verifyUpdateVideoFolder() {
    return this.client
      .post<Count>('/__admin/requests/count', this.verifyBody('PUT', '/api/v1/video'))
      .then(({ data }) => data)
      .then(({ count }) => count)
  }

  static mockCreateMemberFolder() {
    return this.client.post('/__admin/mappings', this.mockBody('POST', '/api/v1/member'))
  }

  static verifyCreateMemberFolder() {
    return this.client
      .post<Count>('/__admin/requests/count', this.verifyBody('POST', '/api/v1/member'))
      .then(({ data }) => data)
      .then(({ count }) => count)
  }

  static mockUpdateMemberFolder() {
    return this.client.post('/__admin/mappings', this.mockBody('PUT', '/api/v1/member'))
  }

  static verifyUpdateMemberFolder() {
    return this.client
      .post<Count>('/__admin/requests/count', this.verifyBody('PUT', '/api/v1/member'))
      .then(({ data }) => data)
      .then(({ count }) => count)
  }
}
