import { DbUtils, videoEntity } from '../../database'
import { DetailedVideo, updateVideo, UpdateVideo } from '../../endpoints/app'
import { mockUpdateVideoFolder, resetMocks, verifyUpdateVideoFolder } from '../../endpoints/file-api/file.endpoint'

describe('put /api/v1/video/{videoId}', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => Promise.all([resetMocks(), dbUtils.beforeEach()]))
  afterAll(async () => Promise.all([resetMocks(), dbUtils.afterAll()]))

  const id = '01234567-0123-0123-0123-0123456789ab'
  it('should return ok and updated video', async () => {
    expect.assertions(3)
    await mockUpdateVideoFolder()
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

    const updateVideoBody: UpdateVideo = {
      url: 'updatedUrl',
      title: 'updatedTitle',
      description: 'updatedDescription',
      uploadedAt: '2000-01-01',
      visible: true,
    }
    const response = await updateVideo(id, updateVideoBody)
    const mockCalls = await verifyUpdateVideoFolder()

    expect(mockCalls).toBe(1)
    expect(response.status).toBe(200)
    expect(response.data).toStrictEqual<DetailedVideo>({
      id,
      url: updateVideoBody.url,
      title: updateVideoBody.title,
      description: updateVideoBody.description,
      uploadedAt: updateVideoBody.uploadedAt,
      visible: updateVideoBody.visible,
      crew: [],
    })
  })
})
