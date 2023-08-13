import { DbUtils, memberEntity } from '../../database'
import { archiveMembers } from '../../endpoints/app'

describe('put /api/v1/member/archive', () => {
  const dbUtils = new DbUtils()
  beforeEach(async () => {
    await dbUtils.beforeEach()
  })
  afterAll(async () => {
    await dbUtils.afterAll()
  })

  const id1 = '01234567-0123-0123-0123-0123456789ab'
  const id2 = '11234567-0123-0123-0123-0123456789ab'

  it('should archive members', async () => {
    expect.assertions(2)
    await dbUtils.addMembers([
      memberEntity({ id: id1, url: 'url1', name: 'name1' }),
      memberEntity({ id: id2, url: 'url2', name: 'name2' }),
    ])

    const response = await archiveMembers([id1, id2], true)

    expect(response.status).toBe(200)
    expect(response.data).toStrictEqual([id1, id2])
  })

  it('should unarchive members', async () => {
    expect.assertions(2)
    await dbUtils.addMembers([
      memberEntity({ id: id1, url: 'url1', name: 'name1', archived: true }),
      memberEntity({ id: id2, url: 'url2', name: 'name2', archived: true }),
    ])

    const response = await archiveMembers([id1, id2], false)

    expect(response.status).toBe(200)
    expect(response.data).toStrictEqual([id1, id2])
  })
})
