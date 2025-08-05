import type { AxiosInstance } from 'axios';
import type { Label, CreateLabel, UUID } from '../../types/index.js';

export function createLabelService(client: AxiosInstance) {
  const basePath = '/api/v1/label';

  return {
    async getAllLabels(): Promise<Label[]> {
      const response = await client.get<Label[]>(basePath);
      return response.data;
    },

    async createLabel(createLabel: CreateLabel): Promise<Label> {
      const response = await client.post<Label>(basePath, createLabel);
      return response.data;
    },

    async deleteLabel(labelId: UUID): Promise<void> {
      await client.delete(`${basePath}/${labelId}`);
    },
  };
}