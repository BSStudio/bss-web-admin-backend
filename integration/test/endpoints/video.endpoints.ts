import {client} from "./client";

export interface Video {
    id: string,
    title: string,
    uploadedAt: string,
    visible: boolean,
    archived: boolean,
}
export interface CreateVideo {
    id: string,
    title: string,
}
export interface UpdateVideo {
    title: string,
    description: string,
    videoUrl: string | null,
    thumbnailUrl: string | null,
    uploadedAt: string,
    visible: boolean,
    archived: boolean,
}
export interface DetailedVideo {
    id: string,
    title: string,
    uploadedAt: string,
    visible: boolean,
    archived: boolean,
    description: string,
    videoUrl: string | null,
    thumbnailUrl: string | null,
}

export const videoEndpoints = {
    getAllVideos(page: number, size: number) {
        return client.get<Video[]>('/api/video', {params: {page, size}})
    },
    createVideo(createVideo: CreateVideo | null) {
        return client.post<Video>('/api/video', createVideo)
    },
    archiveVideos(videoIds: string[], unArchive: boolean) {
        return client.put<string[]>('/api/video/archive', null, {params: {videoIds, unArchive}})
    },
    changeVideoVisibility(videoIds: string[], visible: boolean) {
        return client.put<string[]>('/api/video/visible', null, {params: {videoIds, visible}})
    },
    updateVideo(videoId: string, updateVideo: UpdateVideo) {
        return client.put<DetailedVideo>(`/api/video/${videoId}`, updateVideo)
    },
    getVideo(videoId: string) {
        return client.get<DetailedVideo>(`/api/video/${videoId}`)
    },
    removeVideo(videoId: string) {
        return client.delete(`/api/video/${videoId}`)
    },
}
