import { DbUtils, memberEntity, videoEntity } from '../../database'
import { getPositions } from '../../endpoints/app'

describe('get /api/v1/videoCrew/position', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => {
    await dbUtils.beforeEach()
  })
  afterAll(async () => {
    await dbUtils.afterAll()
  })

  const member_id = '01234567-0123-0123-0123-0123456789ab'
  const video_id = '11234567-0123-0123-0123-0123456789ab'
  const position1 = 'position1'
  const position2 = 'position2'

  it('should return all crew members under the video after adding a position', async () => {
    expect.assertions(2)
    const memberEntity1 = memberEntity({ id: member_id, url: 'url', name: 'Bence Csik' })
    const videoEntity1 = videoEntity({ id: video_id, url: 'url', title: 'title' })
    await dbUtils.addMembers([memberEntity1])
    await dbUtils.addVideos([videoEntity1])
    await dbUtils.addVideoCrew([
      { video_id, member_id, position: position1 },
      { video_id, member_id, position: position2 },
    ])

    const response1 = await getPositions()

    expect(response1.status).toBe(200)
    expect(response1.data).toStrictEqual([position1, position2])
  })
})
