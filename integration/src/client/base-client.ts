import axios, { type AxiosInstance, type AxiosResponse, type AxiosError } from 'axios';
import type { ApiError } from '../types/index.js';

export class ApiClientError extends Error {
  public readonly status: number;
  public readonly response?: AxiosResponse;

  constructor(message: string, status: number, response?: AxiosResponse) {
    super(message);
    this.name = 'ApiClientError';
    this.status = status;
    this.response = response;
  }
}

export interface ApiClientConfig {
  baseURL: string;
  timeout?: number;
}

export function createApiClient({ baseURL, timeout = 10000 }: ApiClientConfig): AxiosInstance {
  const client = axios.create({
    baseURL,
    timeout,
    headers: {
      'Content-Type': 'application/json',
    },
  });

  // Request interceptor for logging
  client.interceptors.request.use(
    (config: any) => {
      console.log(`🔵 ${config.method?.toUpperCase()} ${config.url}`);
      return config;
    },
    (error: any) => {
      console.error('❌ Request error:', error);
      return Promise.reject(error);
    }
  );

  // Response interceptor for error handling
  client.interceptors.response.use(
    (response: any) => {
      console.log(`✅ ${response.status} ${response.config.url}`);
      return response;
    },
    (error: AxiosError<ApiError>) => {
      const message = error.response?.data?.message || error.message;
      const status = error.response?.status || 500;
      
      console.error(`❌ ${status} ${error.config?.url}: ${message}`);
      
      throw new ApiClientError(message, status, error.response);
    }
  );

  return client;
}