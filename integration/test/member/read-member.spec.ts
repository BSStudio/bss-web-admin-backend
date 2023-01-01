import { DbUtils, memberEntity } from '../../database'
import { Member, MemberEndpoint } from '../../endpoints/app'

describe('get /api/v1/member/{memberId}', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => await dbUtils.beforeEach())
  afterAll(async () => await dbUtils.afterAll())

  const id = '01234567-0123-0123-0123-0123456789ab'

  it('should find member', async () => {
    expect.assertions(2)
    const entity1 = memberEntity({ id, url: 'url1', name: 'Bence Csik 1' })
    await dbUtils.addMembers([entity1])

    const response = await MemberEndpoint.getMember(entity1.id)

    const expectedMember1: Member = {
      id: entity1.id,
      url: entity1.url,
      name: entity1.name,
      nickname: entity1.nickname,
      description: entity1.description,
      joinedAt: entity1.joined_at,
      role: entity1.role,
      status: entity1.status,
      archived: entity1.archived,
    }
    expect(response.status).toBe(200)
    expect(response.data).toStrictEqual<Member>(expectedMember1)
  })
  it('should not find member', async () => {
    expect.assertions(1)

    const response = await MemberEndpoint.getMember('01234567-0123-0123-0123-0123456789ab')

    expect(response.status).toBe(404)
  })
})
