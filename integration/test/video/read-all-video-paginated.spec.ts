import { DbUtils, videoEntity } from '../../database'
import { Video, VideoEndpoint } from '../../endpoints'

describe('get /api/video', () => {
  const dbUtils = new DbUtils()
  const videoEntity1 = videoEntity({ id: '01234567-0123-0123-0123-0123456789ab', url: 'url1', title: 'title1' })
  const videoEntity2 = videoEntity({ id: '11234567-0123-0123-0123-0123456789ab', url: 'url2', title: 'title2' })
  const videoEntity3 = videoEntity({ id: '21234567-0123-0123-0123-0123456789ab', url: 'url3', title: 'title3' })
  const defaultVideo = { uploadedAt: '2022-01-01', visible: false }
  const video1: Video = {
    id: '01234567-0123-0123-0123-0123456789ab',
    title: 'title1',
    url: 'url1',
    ...defaultVideo,
  }
  const video2: Video = {
    id: '11234567-0123-0123-0123-0123456789ab',
    title: 'title2',
    url: 'url2',
    ...defaultVideo,
  }
  const video3: Video = {
    id: '21234567-0123-0123-0123-0123456789ab',
    title: 'title3',
    url: 'url3',
    ...defaultVideo,
  }
  beforeEach(async () => await dbUtils.beforeEach())
  afterAll(async () => await dbUtils.afterAll())

  it('should return ok and empty array', async () => {
    expect.assertions(2)

    const response = await VideoEndpoint.getAllVideosPaginated({ page: 0, size: 1 })

    expect(response.status).toBe(200)
    expect(response.data).toStrictEqual({
      content: [],
      empty: true,
      first: true,
      last: true,
      number: 0,
      numberOfElements: 0,
      pageable: {
        offset: 0,
        pageNumber: 0,
        pageSize: 1,
        paged: true,
        sort: { empty: true, sorted: false, unsorted: true },
        unpaged: false,
      },
      size: 1,
      sort: { empty: true, sorted: false, unsorted: true },
      totalElements: 0,
      totalPages: 0,
    })
  })

  it('should respond without pagination params and should have page size 20 by default', async () => {
    expect.assertions(2)

    const response = await VideoEndpoint.getAllVideosPaginated()

    expect(response.status).toBe(200)
    expect(response.data).toStrictEqual({
      content: [],
      empty: true,
      first: true,
      last: true,
      number: 0,
      numberOfElements: 0,
      pageable: {
        offset: 0,
        pageNumber: 0,
        pageSize: 20,
        paged: true,
        sort: { empty: true, sorted: false, unsorted: true },
        unpaged: false,
      },
      size: 20,
      sort: { empty: true, sorted: false, unsorted: true },
      totalElements: 0,
      totalPages: 0,
    })
  })

  it('should respond without pageSize', async () => {
    expect.assertions(2)

    const response = await VideoEndpoint.getAllVideosPaginated({ page: 2 })

    expect(response.status).toBe(200)
    expect(response.data).toStrictEqual({
      content: [],
      empty: true,
      first: false,
      last: true,
      number: 2,
      numberOfElements: 0,
      pageable: {
        offset: 40,
        pageNumber: 2,
        pageSize: 20,
        paged: true,
        sort: { empty: true, sorted: false, unsorted: true },
        unpaged: false,
      },
      size: 20,
      sort: { empty: true, sorted: false, unsorted: true },
      totalElements: 0,
      totalPages: 0,
    })
  })

  it('should return ok and paginated videos', async () => {
    expect.assertions(4)
    await dbUtils.addVideos([videoEntity1, videoEntity2, videoEntity3])

    const response1 = await VideoEndpoint.getAllVideosPaginated({ page: 0, size: 2, sort: 'url,desc' })
    const response2 = await VideoEndpoint.getAllVideosPaginated({ page: 1, size: 2, sort: 'url,desc' })

    expect(response1.status).toBe(200)
    expect(response1.data).toStrictEqual({
      content: [video3, video2],
      empty: false,
      first: true,
      last: false,
      number: 0,
      numberOfElements: 2,
      pageable: {
        offset: 0,
        pageNumber: 0,
        pageSize: 2,
        paged: true,
        sort: { empty: false, sorted: true, unsorted: false },
        unpaged: false,
      },
      size: 2,
      sort: {
        empty: false,
        sorted: true,
        unsorted: false,
      },
      totalElements: 3,
      totalPages: 2,
    })

    expect(response2.status).toBe(200)
    expect(response2.data).toStrictEqual({
      content: [video1],
      empty: false,
      first: false,
      last: true,
      number: 1,
      numberOfElements: 1,
      pageable: {
        offset: 2,
        pageNumber: 1,
        pageSize: 2,
        paged: true,
        sort: { empty: false, sorted: true, unsorted: false },
        unpaged: false,
      },
      size: 2,
      sort: { empty: false, sorted: true, unsorted: false },
      totalElements: 3,
      totalPages: 2,
    })
  })

  it('should return ok and paginated videos sorted by url', async () => {
    expect.assertions(4)
    await dbUtils.addVideos([videoEntity1, videoEntity2, videoEntity3])

    const response1 = await VideoEndpoint.getAllVideosPaginated({ page: 0, size: 2 })
    const response2 = await VideoEndpoint.getAllVideosPaginated({ page: 1, size: 2 })

    expect(response1.status).toBe(200)
    expect(response1.data).toStrictEqual({
      content: [video1, video2],
      empty: false,
      first: true,
      last: false,
      number: 0,
      numberOfElements: 2,
      pageable: {
        offset: 0,
        pageNumber: 0,
        pageSize: 2,
        paged: true,
        sort: { empty: true, sorted: false, unsorted: true },
        unpaged: false,
      },
      size: 2,
      sort: { empty: true, sorted: false, unsorted: true },
      totalElements: 3,
      totalPages: 2,
    })

    expect(response2.status).toBe(200)
    expect(response2.data).toStrictEqual({
      content: [video3],
      empty: false,
      first: false,
      last: true,
      number: 1,
      numberOfElements: 1,
      pageable: {
        offset: 2,
        pageNumber: 1,
        pageSize: 2,
        paged: true,
        sort: { empty: true, sorted: false, unsorted: true },
        unpaged: false,
      },
      size: 2,
      sort: { empty: true, sorted: false, unsorted: true },
      totalElements: 3,
      totalPages: 2,
    })
  })
})
