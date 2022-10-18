import { DbUtils, memberEntity } from '../../database'
import { Member, MemberEndpoint, UpdateMember } from '../../endpoints'

describe('put /api/v1/member/{memberId}', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => await dbUtils.beforeEach())
  afterAll(async () => await dbUtils.afterAll())

  const id = '01234567-0123-0123-0123-0123456789ab'

  it('should update member', async () => {
    expect.assertions(2)
    const entity1 = memberEntity({ id, url: 'url1', name: 'Bence Csik 1' })
    await dbUtils.addMembers([entity1])

    const updateMember: UpdateMember = {
      url: 'updatedUrl',
      name: 'updatedName',
      description: 'updatedDescription',
      imageUrl: 'updatedImageUrl',
      joinedAt: '1997-01-01',
      role: 'updatedRole',
      status: 'ALUMNI',
      archived: true,
    }
    const response = await MemberEndpoint.updateMember(entity1.id, updateMember)

    const expectedMember: Member = { id, ...updateMember }
    expect(response.status).toBe(200)
    expect(response.data).toStrictEqual(expectedMember)
  })
})
