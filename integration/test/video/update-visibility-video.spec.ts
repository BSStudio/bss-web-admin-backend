import { DbUtils, videoEntity } from '../../database'
import { VideoEndpoint } from '../../endpoints/app'

describe('put /api/v1/video/visible', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => await dbUtils.beforeEach())
  afterAll(async () => await dbUtils.afterAll())

  const id1 = '01234567-0123-0123-0123-0123456789ab'
  const id2 = '11234567-0123-0123-0123-0123456789ab'

  it('should return ok and updated ids when videos were changed to visible', async () => {
    expect.assertions(4)
    await dbUtils.addVideos([
      videoEntity({ id: id1, url: 'url1', title: 'title1' }),
      videoEntity({ id: id2, url: 'url2', title: 'title2' }),
    ])

    const response = await VideoEndpoint.changeVideoVisibility([id1, id2], true)
    const getResponse1 = await VideoEndpoint.getVideo(id1)
    const getResponse2 = await VideoEndpoint.getVideo(id2)

    expect(response.data).toStrictEqual([id1, id2])
    expect(response.data).toHaveLength(2)
    expect(getResponse1.data.visible).toBe(true)
    expect(getResponse2.data.visible).toBe(true)
  })
  it('should return ok and updated ids when videos were changed to invisible', async () => {
    expect.assertions(4)
    await dbUtils.addVideos([
      videoEntity({ id: id1, url: 'url1', title: 'title1', visible: false }),
      videoEntity({ id: id2, url: 'url2', title: 'title2', visible: false }),
    ])

    const response = await VideoEndpoint.changeVideoVisibility([id1, id2], false)
    const getResponse1 = await VideoEndpoint.getVideo(id1)
    const getResponse2 = await VideoEndpoint.getVideo(id2)

    expect(response.data).toStrictEqual([id1, id2])
    expect(response.data).toHaveLength(2)
    expect(getResponse1.data.visible).toBe(false)
    expect(getResponse2.data.visible).toBe(false)
  })
})
