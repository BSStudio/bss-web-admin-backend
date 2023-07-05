import { DbUtils, videoEntity } from '../../database'
import { VideoEndpoint } from '../../endpoints/app'

describe('get /api/v1/video', () => {
  const dbUtils = new DbUtils()
  const videoEntity1 = videoEntity({ id: '01234567-0123-0123-0123-0123456789ab', url: 'url1', title: 'title1' })
  const videoEntity2 = videoEntity({ id: '11234567-0123-0123-0123-0123456789ab', url: 'url2', title: 'title2' })
  beforeEach(async () => await dbUtils.beforeEach())
  afterAll(async () => await dbUtils.afterAll())

  it('should return ok and empty array', async () => {
    expect.assertions(2)

    const response = await VideoEndpoint.getAllVideos()

    expect(response.status).toBe(200)
    expect(response.data).toStrictEqual([])
  })

  it('should return ok and video array', async () => {
    expect.assertions(2)
    await dbUtils.addVideos([videoEntity1, videoEntity2])

    const response = await VideoEndpoint.getAllVideos()

    expect(response.status).toBe(200)
    expect(response.data).toStrictEqual([
      {
        id: '01234567-0123-0123-0123-0123456789ab',
        title: 'title1',
        url: 'url1',
        uploadedAt: '2022-01-01',
        visible: false,
      },
      {
        id: '11234567-0123-0123-0123-0123456789ab',
        title: 'title2',
        url: 'url2',
        uploadedAt: '2022-01-01',
        visible: false,
      },
    ])
  })
})
