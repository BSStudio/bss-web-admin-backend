import { DbUtils, memberEntity } from '../../database'
import { Member, MemberEndpoint, UpdateMember } from '../../endpoints/app'
import { FileEndpoint } from '../../endpoints/file-api/file.endpoint'

describe('put /api/v1/member/{memberId}', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => Promise.all([FileEndpoint.resetMocks(), dbUtils.beforeEach()]))
  afterAll(async () => Promise.all([FileEndpoint.resetMocks(), dbUtils.afterAll()]))

  const id = '01234567-0123-0123-0123-0123456789ab'

  it('should update member', async () => {
    expect.assertions(3)
    await FileEndpoint.mockUpdateMemberFolder()
    const entity1 = memberEntity({ id, url: 'url1', name: 'Bence Csik 1' })
    await dbUtils.addMembers([entity1])

    const updateMember: UpdateMember = {
      url: 'updatedUrl',
      name: 'updatedName',
      nickname: 'updatedNickname',
      description: 'updatedDescription',
      joinedAt: '1997-01-01',
      role: 'updatedRole',
      status: 'ALUMNI',
      archived: true,
    }
    const response = await MemberEndpoint.updateMember(entity1.id, updateMember)
    const mockCalls = await FileEndpoint.verifyUpdateMemberFolder()

    expect(mockCalls).toBe(1)
    const expectedMember: Member = { id, ...updateMember }
    expect(response.status).toBe(200)
    expect(response.data).toStrictEqual<Member>(expectedMember)
  })
})
