import { DbUtils, memberEntity, videoEntity } from '../../database'
import { DetailedVideo, VideoCrewEndpoint } from '../../endpoints'

describe('delete /api/v1/videoCrew', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => await dbUtils.beforeEach())
  afterAll(async () => await dbUtils.afterAll())

  const member_id = '01234567-0123-0123-0123-0123456789ab'
  const video_id = '11234567-0123-0123-0123-0123456789ab'
  const position = 'position'
  const otherPosition = 'otherPosition'
  const memberName = 'Bence Csik'

  it('should remove a crew member from a video', async () => {
    expect.assertions(2)
    const videoEntity1 = videoEntity({ id: video_id, url: 'url', title: 'title' })
    await dbUtils.addVideos([videoEntity1])
    await dbUtils.addMembers([memberEntity({ id: member_id, url: 'url', name: memberName })])
    await dbUtils.addVideoCrew([{ video_id, member_id, position }])
    await dbUtils.addVideoCrew([{ video_id, member_id, position: otherPosition }])

    const response = await VideoCrewEndpoint.removeVideoCrewMember(video_id, member_id, position)

    expect(response.status).toBe(200)
    expect(response.data).toStrictEqual<DetailedVideo>({
      id: videoEntity1.id,
      url: videoEntity1.url,
      title: videoEntity1.title,
      description: videoEntity1.description,
      uploadedAt: videoEntity1.uploaded_at,
      visible: videoEntity1.visible,
      crew: [
        {
          videoId: videoEntity1.id,
          position: otherPosition,
          member: { id: member_id, name: memberName },
        },
      ],
    })
  })
})
