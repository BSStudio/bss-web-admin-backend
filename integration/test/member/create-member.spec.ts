import { dbUtils } from '../../database'
import { CreateMember, MemberEndpoint } from '../../endpoints/member.endpoints'
import { memberEntity } from '../../database/add-members'

describe('post /api/member', () => {
  beforeEach(async () => await dbUtils.truncateAll())

  const id = '01234567-0123-0123-0123-0123456789AB'
  const url = 'url'
  const name = 'name'

  it('should create a new member', async () => {
    expect.assertions(10)

    const createMember: CreateMember = { url, name }
    const response = await MemberEndpoint.createMember(createMember)

    expect(response.status).toBe(201)
    expect(typeof response.data.id).toBe('string')
    expect(response.data.url).toBe(url)
    expect(response.data.name).toBe(name)
    expect(response.data.description).toBe('')
    expect(response.data.imageUrl).toBe('')
    expect(response.data.joinedAt).toBe(new Date().toISOString().split('T')[0])
    expect(response.data.role).toBe('')
    expect(response.data.status).toBe('MEMBER_CANDIDATE_CANDIDATE')
    expect(response.data.archived).toBe(false)
  })
  it('should not create a new member when url already exist', async () => {
    expect.assertions(1)
    await dbUtils.addMembers([memberEntity({ id, url, name })])
    const createMember: CreateMember = { url, name }

    const response = await MemberEndpoint.createMember(createMember)

    expect(response.status).toBe(500)
  })
})
