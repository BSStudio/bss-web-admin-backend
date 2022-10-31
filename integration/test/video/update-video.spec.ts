import { DbUtils, videoEntity } from '../../database'
import { DetailedVideo, UpdateVideo, VideoEndpoint } from '../../endpoints'

describe('put /api/v1/video/{videoId}', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => await dbUtils.beforeEach())
  afterAll(async () => await dbUtils.afterAll())

  const id = '01234567-0123-0123-0123-0123456789ab'
  it('should return ok and updated video', async () => {
    expect.assertions(2)
    await dbUtils.addVideos([
      videoEntity({
        id,
        url: 'url',
        title: 'title',
        description: 'description',
        video_url: 'videoUrl',
        thumbnail_url: 'thumbnailUrl',
        uploaded_at: '2022-01-01',
        visible: false,
      }),
    ])

    const updateVideo: UpdateVideo = {
      url: 'updatedUrl',
      title: 'updatedTitle',
      description: 'updatedDescription',
      videoUrl: 'updatedVideoUrl',
      thumbnailUrl: 'updatedThumbnailUrl',
      uploadedAt: '2000-01-01',
      visible: true,
    }
    const response = await VideoEndpoint.updateVideo(id, updateVideo)

    const expectedVideo: DetailedVideo = {
      id,
      url: updateVideo.url,
      title: updateVideo.title,
      description: updateVideo.description,
      videoUrl: updateVideo.videoUrl,
      thumbnailUrl: updateVideo.thumbnailUrl,
      uploadedAt: updateVideo.uploadedAt,
      visible: updateVideo.visible,
      crew: [],
    }
    expect(response.status).toBe(200)
    expect(response.data).toStrictEqual(expectedVideo)
  })
})
