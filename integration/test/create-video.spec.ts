import axios from "axios";

interface CreateVideo {
    id: string,
    title: string,
}

interface Video {
    id: string,
    title: string,
    uploadedAt: string,
    visible: boolean,
    archived: boolean
}

describe('POST /api/video', () => {
    const appBaseURL = globalThis.baseUrl.app
    const client = axios.create({
        baseURL: appBaseURL
    });

    function createVideo(createVideo: CreateVideo) {
        return client.post<Video>('/api/video', createVideo)
    }

    test.todo('should create video')

});
