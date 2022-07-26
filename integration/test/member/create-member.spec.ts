import { DbUtils } from '../../database'
import { CreateMember, MemberEndpoint } from '../../endpoints/member.endpoint'
import { memberEntity } from '../../database/member.queries'

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
      id: expect.stringMatching(/[\da-f]{8}-[\da-f]{4}-[\da-f]{4}-[\da-f]{4}-[\da-f]{12}/),
      url,
      name,
      description: '',
      imageUrl: '',
      joinedAt: new Date().toISOString().split('T')[0],
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
