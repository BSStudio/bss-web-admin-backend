import { client } from './client'

export interface Count {
  count: number
}

export class FileEndpoint {
  private static client = client

  static resetMocks() {
    return this.client.post('/__admin/reset')
  }

  static mockCreateVideoFolder() {
    return this.client.post('/__admin/mappings', {
      request: {
        method: 'POST',
        url: '/api/v1/video',
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
  }

  static verifyCreateVideoFolder() {
    return this.client
      .post<Count>('/__admin/requests/count', {
        method: 'POST',
        url: '/api/v1/video',
        bodyPatterns: [
          {
            matchesJsonPath: '$.id',
          },
          {
            matchesJsonPath: '$.url',
          },
        ],
      })
      .then(({ data }) => data)
      .then(({ count }) => count)
  }

  static mockUpdateVideoFolder() {
    return this.client.post('/__admin/mappings', {
      request: {
        method: 'PUT',
        url: '/api/v1/video',
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
  }

  static verifyUpdateVideoFolder() {
    return this.client
      .post<Count>('/__admin/requests/count', {
        method: 'PUT',
        url: '/api/v1/video',
        bodyPatterns: [
          {
            matchesJsonPath: '$.id',
          },
          {
            matchesJsonPath: '$.url',
          },
        ],
      })
      .then(({ data }) => data)
      .then(({ count }) => count)
  }

  static mockCreateMemberFolder() {
    return this.client.post('/__admin/mappings', {
      request: {
        method: 'POST',
        url: '/api/v1/member',
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
  }

  static verifyCreateMemberFolder() {
    return this.client
      .post<Count>('/__admin/requests/count', {
        method: 'POST',
        url: '/api/v1/member',
        bodyPatterns: [
          {
            matchesJsonPath: '$.id',
          },
          {
            matchesJsonPath: '$.url',
          },
        ],
      })
      .then(({ data }) => data)
      .then(({ count }) => count)
  }

  static mockUpdateMemberFolder() {
    return this.client.post('/__admin/mappings', {
      request: {
        method: 'PUT',
        url: '/api/v1/member',
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
  }

  static verifyUpdateMemberFolder() {
    return this.client
      .post<Count>('/__admin/requests/count', {
        method: 'PUT',
        url: '/api/v1/member',
        bodyPatterns: [
          {
            matchesJsonPath: '$.id',
          },
          {
            matchesJsonPath: '$.url',
          },
        ],
      })
      .then(({ data }) => data)
      .then(({ count }) => count)
  }
}
