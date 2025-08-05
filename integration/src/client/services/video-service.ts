import type { AxiosInstance } from 'axios';
import type { 
  Video, 
  DetailedVideo, 
  CreateVideo, 
  UpdateVideo, 
  UUID, 
  PaginatedResponse 
} from '../../types/index.js';

export interface VideoQueryParams {
  page?: number;
  size?: number;
  sort?: string;
}

export function createVideoService(client: AxiosInstance) {
  const basePath = '/api/v1/video';

  return {
    async getAllVideos(): Promise<Video[]> {
      const response = await client.get<Video[]>(`${basePath}/all`);
      return response.data;
    },

    async getVideosPaginated(params?: VideoQueryParams): Promise<PaginatedResponse<Video>> {
      const response = await client.get<PaginatedResponse<Video>>(basePath, { params });
      return response.data;
    },

    async createVideo(createVideo: CreateVideo): Promise<Video> {
      const response = await client.post<Video>(basePath, createVideo);
      return response.data;
    },

    async changeVideoVisibility(videoIds: UUID[], visible: boolean): Promise<UUID[]> {
      // Create URLSearchParams for proper array handling
      const params = new URLSearchParams();
      videoIds.forEach(id => params.append('videoIds', id));
      params.append('visible', visible.toString());
      
      const response = await client.put<UUID[]>(`${basePath}/visible?${params.toString()}`);
      return response.data;
    },

    async getVideoById(videoId: UUID): Promise<DetailedVideo> {
      const response = await client.get<DetailedVideo>(`${basePath}/${videoId}`);
      return response.data;
    },

    async updateVideo(videoId: UUID, updateVideo: UpdateVideo): Promise<DetailedVideo> {
      const response = await client.put<DetailedVideo>(`${basePath}/${videoId}`, updateVideo);
      return response.data;
    },

    async deleteVideo(videoId: UUID): Promise<void> {
      await client.delete(`${basePath}/${videoId}`);
    },
  };
}