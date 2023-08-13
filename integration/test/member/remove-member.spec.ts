import { DbUtils, memberEntity } from '../../database'
import { removeMember } from '../../endpoints/app'

describe('delete /api/v1/member/{memberId}', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => {
    await dbUtils.beforeEach()
  })
  afterAll(async () => {
    await dbUtils.afterAll()
  })

  const id = '01234567-0123-0123-0123-0123456789ab'

  it('should return no content and empty body when removing member', async () => {
    expect.assertions(2)
    const entity1 = memberEntity({ id, url: 'url1', name: 'Bence Csik 1' })
    await dbUtils.addMembers([entity1])

    const response = await removeMember(entity1.id)

    expect(response.status).toBe(204)
    expect(response.data).toBe('')
  })
  it('should return no content and empty body when user tries to remove a non existent member', async () => {
    expect.assertions(2)

    const response = await removeMember('01234567-0123-0123-0123-0123456789ab')

    expect(response.status).toBe(204)
    expect(response.data).toBe('')
  })
})
