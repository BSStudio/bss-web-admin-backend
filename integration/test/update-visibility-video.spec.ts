import { videoEndpoints } from './endpoints/video.endpoints'
import { dbUtils } from './database'
import { videoEntity } from './database/add-videos'

describe('put /api/video/visible', () => {
  beforeEach(async () => await dbUtils.truncateAll())

  const id1 = '01234567-0123-0123-0123-0123456789ab'
  const id2 = '11234567-0123-0123-0123-0123456789ab'

  it('should make video visible', async () => {
    expect.assertions(4)
    await dbUtils.addVideos([videoEntity(id1, 'url1', 'title1'), videoEntity(id2, 'url2', 'title2')])

    const response = await videoEndpoints.changeVideoVisibility([id1, id2], true)
    const getResponse1 = await videoEndpoints.getVideo(id1)
    const getResponse2 = await videoEndpoints.getVideo(id2)

    expect(response.data).toStrictEqual([id1, id2])
    expect(response.data).toHaveLength(2)
    expect(getResponse1.data.visible).toBe(true)
    expect(getResponse2.data.visible).toBe(true)
  })
  it('should make video invisible', async () => {
    expect.assertions(4)
    await dbUtils.addVideos([
      videoEntity(id1, 'url1', 'title1', '', '', '', '2022-01-01', false),
      videoEntity(id2, 'url2', 'title2', '', '', '', '2022-01-01', false),
    ])

    const response = await videoEndpoints.changeVideoVisibility([id1, id2], false)
    const getResponse1 = await videoEndpoints.getVideo(id1)
    const getResponse2 = await videoEndpoints.getVideo(id2)

    expect(response.data).toStrictEqual([id1, id2])
    expect(response.data).toHaveLength(2)
    expect(getResponse1.data.visible).toBe(false)
    expect(getResponse2.data.visible).toBe(false)
  })
})
