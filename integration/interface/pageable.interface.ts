export type Direction = 'ASC' | 'DESC'
export type NullHandling = 'NATIVE' | 'NULLS_FIRST' | 'NULLS_LAST'
export interface Order {
  ascending: boolean
  descending: boolean
  direction: Direction
  ignoreCase: boolean
  nullHandling: NullHandling
  property: string
}
export type Sort = Order[]
export interface Pageable {
  offset: number
  pageNumber: number
  pageSize: number
  paged: boolean
  sort: Sort
  unpaged: boolean
}
export interface Page<T> {
  content: T[]
  empty: boolean
  first: boolean
  last: boolean
  number: number
  numberOfElements: number
  pageable: Pageable
  size: number
  sort: Sort
  totalElements: number
  totalPages: number
}
export interface PageableRequestParam {
  size?: number
  page?: number
  sort?: string
}
