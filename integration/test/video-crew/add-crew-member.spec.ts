import { DbUtils } from '../../database'
import { VideoCrewEndpoint } from '../../endpoints/video-crew.endpoint'
import { memberEntity } from '../../database/member.queries'
import { videoEntity } from '../../database/video.queries'

describe('post /api/videoCrew', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => await dbUtils.beforeEach())
  afterAll(async () => await dbUtils.afterAll())

  const memberId = '01234567-0123-0123-0123-0123456789ab'
  const videoId = '11234567-0123-0123-0123-0123456789ab'
  const position1 = 'position1'
  const position2 = 'position2'

  it('should add a crew member to a video', async () => {
    expect.assertions(2)
    await dbUtils.addMembers([memberEntity({ id: memberId, url: 'url', name: 'Bence Csik' })])
    await dbUtils.addVideos([videoEntity({ id: videoId, url: 'url', title: 'title' })])

    const response = await VideoCrewEndpoint.addVideoCrewMember(videoId, memberId, position1)

    expect(response.status).toBe(200)
    expect(response.data).toStrictEqual([{ memberId, position: position1 }])
  }, 8000)

  it('should return all crew members under the video after adding a position', async () => {
    expect.assertions(4)
    await dbUtils.addMembers([memberEntity({ id: memberId, url: 'url', name: 'Bence Csik' })])
    await dbUtils.addVideos([videoEntity({ id: videoId, url: 'url', title: 'title' })])

    const response1 = await VideoCrewEndpoint.addVideoCrewMember(videoId, memberId, position1)
    const response2 = await VideoCrewEndpoint.addVideoCrewMember(videoId, memberId, position2)

    expect(response1.status).toBe(200)
    expect(response1.data).toStrictEqual([{ memberId, position: position1 }])
    expect(response2.status).toBe(200)
    expect(response2.data).toStrictEqual([
      { memberId, position: position1 },
      { memberId, position: position2 },
    ])
  }, 8000)
})
