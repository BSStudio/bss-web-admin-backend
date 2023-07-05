import { DbUtils, videoEntity } from '../../database'
import { DetailedVideo, UpdateVideo, VideoEndpoint } from '../../endpoints/app'
import { FileEndpoint } from '../../endpoints/file-api/file.endpoint'

describe('put /api/v1/video/{videoId}', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => Promise.all([FileEndpoint.resetMocks(), dbUtils.beforeEach()]))
  afterAll(async () => Promise.all([FileEndpoint.resetMocks(), dbUtils.afterAll()]))

  const id = '01234567-0123-0123-0123-0123456789ab'
  it('should return ok and updated video', async () => {
    expect.assertions(3)
    await FileEndpoint.mockUpdateVideoFolder()
    await dbUtils.addVideos([
      videoEntity({
        id,
        url: 'url',
        title: 'title',
        description: 'description',
        uploaded_at: '2022-01-01',
        visible: false,
      }),
    ])

    const updateVideo: UpdateVideo = {
      url: 'updatedUrl',
      title: 'updatedTitle',
      description: 'updatedDescription',
      uploadedAt: '2000-01-01',
      visible: true,
    }
    const response = await VideoEndpoint.updateVideo(id, updateVideo)
    const mockCalls = await FileEndpoint.verifyUpdateVideoFolder()

    expect(mockCalls).toBe(1)
    expect(response.status).toBe(200)
    expect(response.data).toStrictEqual<DetailedVideo>({
      id,
      url: updateVideo.url,
      title: updateVideo.title,
      description: updateVideo.description,
      uploadedAt: updateVideo.uploadedAt,
      visible: updateVideo.visible,
      crew: [],
    })
  })
})
