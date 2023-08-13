import { DbUtils, memberEntity } from '../../database'
import { createMember, CreateMember, Member } from '../../endpoints/app'
import { UUID_REGEX, dateToday } from '../../util'
import { mockCreateMemberFolder, resetMocks, verifyCreateMemberFolder } from '../../endpoints/file-api/file.endpoint'

describe('post /api/v1/member', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => Promise.all([resetMocks(), dbUtils.beforeEach()]))
  afterAll(async () => Promise.all([resetMocks(), dbUtils.afterAll()]))

  const id = '01234567-0123-0123-0123-0123456789ab'
  const url = 'url'
  const name = 'name'

  it('should create a new member', async () => {
    expect.assertions(4)
    await mockCreateMemberFolder()

    const createMemberBody: CreateMember = { url, name }
    const response = await createMember(createMemberBody)
    const mockCalls = await verifyCreateMemberFolder()

    expect(mockCalls).toBe(1)
    expect(response.status).toBe(201)
    expect(response.headers.location).toBe(`${globalThis.baseUrl.app}/api/v1/member/${response.data.id}`)
    expect(response.data).toMatchObject({
      id: expect.stringMatching(UUID_REGEX) as string,
      url,
      name,
      nickname: '',
      description: '',
      joinedAt: dateToday(),
      role: '',
      status: 'MEMBER_CANDIDATE_CANDIDATE',
      archived: false,
    } as Member)
  })
  it('should not create a new member when url already exist', async () => {
    expect.assertions(2)
    await mockCreateMemberFolder()
    await dbUtils.addMembers([memberEntity({ id, url, name })])
    const createMemberBody: CreateMember = { url, name }

    const response = await createMember(createMemberBody)
    const mockCalls = await verifyCreateMemberFolder()

    expect(mockCalls).toBe(0)
    expect(response.status).toBe(500)
  })
})
