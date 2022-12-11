import { DbUtils, memberEntity, videoEntity } from '../../database'
import { DetailedVideo, VideoCrewEndpoint } from '../../endpoints'

describe('post /api/v1/videoCrew', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => await dbUtils.beforeEach())
  afterAll(async () => await dbUtils.afterAll())

  const memberId = '01234567-0123-0123-0123-0123456789ab'
  const videoId = '11234567-0123-0123-0123-0123456789ab'
  const position1 = 'position1'
  const position2 = 'position2'

  it('should return all crew members under the video after adding a position', async () => {
    expect.assertions(4)
    const memberEntity1 = memberEntity({ id: memberId, url: 'url', name: 'Bence Csik' })
    const videoEntity1 = videoEntity({ id: videoId, url: 'url', title: 'title' })
    await dbUtils.addMembers([memberEntity1])
    await dbUtils.addVideos([videoEntity1])

    const response1 = await VideoCrewEndpoint.addVideoCrewMember(videoId, memberId, position1)
    const response2 = await VideoCrewEndpoint.addVideoCrewMember(videoId, memberId, position2)

    const expected: DetailedVideo = {
      id: videoEntity1.id,
      url: videoEntity1.url,
      title: videoEntity1.title,
      description: videoEntity1.description,
      uploadedAt: videoEntity1.uploaded_at,
      visible: videoEntity1.visible,
      crew: [
        {
          videoId: videoEntity1.id,
          position: position1,
          member: { id: memberEntity1.id, name: memberEntity1.name },
        },
      ],
    }
    expect(response1.status).toBe(200)
    expect(response1.data).toStrictEqual<DetailedVideo>(expected)
    expect(response2.status).toBe(200)
    expect(response2.data).toStrictEqual<DetailedVideo>({
      ...expected,
      crew: [
        {
          videoId: videoEntity1.id,
          position: position1,
          member: { id: memberEntity1.id, name: memberEntity1.name },
        },
        {
          videoId: videoEntity1.id,
          position: position2,
          member: { id: memberEntity1.id, name: memberEntity1.name },
        },
      ],
    })
  })
})
