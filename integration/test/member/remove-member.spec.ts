import { dbUtils } from '../../database'
import { Member, MemberEndpoint } from '../../endpoints/member.endpoints'
import { memberEntity } from '../../database/add-members'

describe('delete /api/member/{memberId}', () => {
  beforeEach(async () => await dbUtils.truncateAll())

  const id = '01234567-0123-0123-0123-0123456789ab'

  it('should remove member', async () => {
    expect.assertions(1)
    const entity1 = memberEntity({ id, url: 'url1', name: 'Bence Csik 1' })
    await dbUtils.addMembers([entity1])

    const response = await MemberEndpoint.removeMember(entity1.id)

    expect(response.status).toBe(200)
  })
  it('should return error when user tries to remove a non existent member', async () => {
    expect.assertions(1)

    const response = await MemberEndpoint.removeMember('01234567-0123-0123-0123-0123456789ab')

    expect(response.status).toBe(500)
  })
})
