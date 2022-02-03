import { DbUtils } from '../../database'
import { VideoCrewEndpoint } from '../../endpoints/video-crew.endpoint'
import { memberEntity } from '../../database/member.queries'
import { videoEntity } from '../../database/video.queries'

describe('delete /api/videoCrew', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => await dbUtils.beforeEach())
  afterAll(async () => await dbUtils.afterAll())

  const member_id = '01234567-0123-0123-0123-0123456789ab'
  const video_id = '11234567-0123-0123-0123-0123456789ab'
  const position = 'position'
  const otherPosition = 'otherPosition'

  it('should remove a crew member from a video', async () => {
    expect.assertions(2)
    await dbUtils.addMembers([memberEntity({ id: member_id, url: 'url', name: 'Bence Csik' })])
    await dbUtils.addVideos([videoEntity({ id: video_id, url: 'url', title: 'title' })])
    await dbUtils.addVideoCrew([{ video_id, member_id, position }])
    await dbUtils.addVideoCrew([{ video_id, member_id, position: otherPosition }])

    const response = await VideoCrewEndpoint.removeVideoCrewMember(video_id, member_id, position)

    expect(response.status).toBe(200)
    expect(response.data).toStrictEqual([{ memberId: member_id, position: otherPosition }])
  })
})
