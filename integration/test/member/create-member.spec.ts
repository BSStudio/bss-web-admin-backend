import { DbUtils, memberEntity } from '../../database'
import { CreateMember, MemberEndpoint } from '../../endpoints'
import { DATE_TODAY, UUID_REGEX } from '../../util'

describe('post /api/member', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => await dbUtils.beforeEach())
  afterAll(async () => await dbUtils.afterAll())

  const id = '01234567-0123-0123-0123-0123456789ab'
  const url = 'url'
  const name = 'name'

  it('should create a new member', async () => {
    expect.assertions(2)

    const createMember: CreateMember = { url, name }
    const response = await MemberEndpoint.createMember(createMember)

    expect(response.status).toBe(201)
    expect(response.data).toMatchObject({
      id: expect.stringMatching(UUID_REGEX) as string,
      url,
      name,
      description: '',
      imageUrl: '',
      joinedAt: DATE_TODAY,
      role: '',
      status: 'MEMBER_CANDIDATE_CANDIDATE',
      archived: false,
    })
  })
  it('should not create a new member when url already exist', async () => {
    expect.assertions(1)
    await dbUtils.addMembers([memberEntity({ id, url, name })])
    const createMember: CreateMember = { url, name }

    const response = await MemberEndpoint.createMember(createMember)

    expect(response.status).toBe(500)
  })
})
