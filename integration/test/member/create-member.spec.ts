import { DbUtils, memberEntity } from '../../database'
import { CreateMember, Member, MemberEndpoint } from '../../endpoints/app'
import { UUID_REGEX, dateToday } from '../../util'
import { FileEndpoint } from '../../endpoints/file-api/file.endpoint'

describe('post /api/v1/member', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => Promise.all([FileEndpoint.resetMocks(), dbUtils.beforeEach()]))
  afterAll(async () => Promise.all([FileEndpoint.resetMocks(), dbUtils.afterAll()]))

  const id = '01234567-0123-0123-0123-0123456789ab'
  const url = 'url'
  const name = 'name'

  it('should create a new member', async () => {
    expect.assertions(3)
    await FileEndpoint.mockCreateMemberFolder()

    const createMember: CreateMember = { url, name }
    const response = await MemberEndpoint.createMember(createMember)
    const mockCalls = await FileEndpoint.verifyCreateMemberFolder()

    expect(mockCalls).toBe(1)
    expect(response.status).toBe(201)
    expect(response.data).toMatchObject<Member>({
      id: expect.stringMatching(UUID_REGEX) as string,
      url,
      name,
      description: '',
      joinedAt: dateToday(),
      role: '',
      status: 'MEMBER_CANDIDATE_CANDIDATE',
      archived: false,
    })
  })
  it('should not create a new member when url already exist', async () => {
    expect.assertions(2)
    await FileEndpoint.mockCreateMemberFolder()
    await dbUtils.addMembers([memberEntity({ id, url, name })])
    const createMember: CreateMember = { url, name }

    const response = await MemberEndpoint.createMember(createMember)
    const mockCalls = await FileEndpoint.verifyCreateMemberFolder()

    expect(mockCalls).toBe(0)
    expect(response.status).toBe(500)
  })
})
