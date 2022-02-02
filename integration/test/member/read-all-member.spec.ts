import { dbUtils } from '../../database'
import { Member, MemberEndpoint } from '../../endpoints/member.endpoints'
import { memberEntity } from '../../database/add-members'

describe('get /api/member', () => {
  beforeEach(async () => await dbUtils.truncateAll())

  it('should read all members', async () => {
    expect.assertions(4)
    const entity1 = memberEntity({ id: '01234567-0123-0123-0123-0123456789ab', url: 'url1', name: 'Bence Csik 1' })
    const entity2 = memberEntity({ id: '11234567-0123-0123-0123-0123456789ab', url: 'url2', name: 'Bence Csik 2' })
    await dbUtils.addMembers([entity1, entity2])

    const response = await MemberEndpoint.getAllMembers()

    const expectedMember1: Member = {
      id: entity1.id,
      url: entity1.url,
      name: entity1.name,
      description: entity1.description,
      imageUrl: entity1.image_url,
      joinedAt: entity1.joined_at,
      role: entity1.role,
      status: entity1.status,
      archived: entity1.archived,
    }
    const expectedMember2: Member = {
      id: entity2.id,
      url: entity2.url,
      name: entity2.name,
      description: entity2.description,
      imageUrl: entity2.image_url,
      joinedAt: entity2.joined_at,
      role: entity2.role,
      status: entity2.status,
      archived: entity2.archived,
    }
    expect(response.status).toBe(200)
    expect(response.data).toHaveLength(2)
    expect(response.data[0]).toStrictEqual(expectedMember1)
    expect(response.data[1]).toStrictEqual(expectedMember2)
  })
  it('should return empty array if empty', async () => {
    expect.assertions(2)

    const response = await MemberEndpoint.getAllMembers()

    expect(response.status).toBe(200)
    expect(response.data).toStrictEqual([])
  })
})
