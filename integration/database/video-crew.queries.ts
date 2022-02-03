export interface CrewEntity {
  video_id: string
  position: string
  member_id: string
}

export function insertCrewQuery(crews: CrewEntity[]) {
  return (
    'INSERT INTO bss_web.crew (video_id, member_id, position) VALUES ' +
    crews.map((crew) => `('${crew.video_id}', '${crew.member_id}', '${crew.position}')`).join(',')
  )
}
