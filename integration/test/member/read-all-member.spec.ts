import { DbUtils, memberEntity } from '../../database'
import { getAllMembers, Member } from '../../endpoints/app'

describe('get /api/v1/member', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => {
    await dbUtils.beforeEach()
  })
  afterAll(async () => {
    await dbUtils.afterAll()
  })

  it('should read all members', async () => {
    expect.assertions(4)
    const entity1 = memberEntity({ id: '01234567-0123-0123-0123-0123456789ab', url: 'url1', name: 'Bence Csik 1' })
    const entity2 = memberEntity({ id: '11234567-0123-0123-0123-0123456789ab', url: 'url2', name: 'Bence Csik 2' })
    await dbUtils.addMembers([entity1, entity2])

    const response = await getAllMembers()

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
    const expectedMember2: Member = {
      id: entity2.id,
      url: entity2.url,
      name: entity2.name,
      nickname: entity2.nickname,
      description: entity2.description,
      joinedAt: entity2.joined_at,
      role: entity2.role,
      status: entity2.status,
      archived: entity2.archived,
    }
    expect(response.status).toBe(200)
    expect(response.data).toHaveLength(2)
    expect(response.data[0]).toStrictEqual<Member>(expectedMember1)
    expect(response.data[1]).toStrictEqual<Member>(expectedMember2)
  })
  it('should return empty array if empty', async () => {
    expect.assertions(2)

    const response = await getAllMembers()

    expect(response.status).toBe(200)
    expect(response.data).toStrictEqual([])
  })
})
