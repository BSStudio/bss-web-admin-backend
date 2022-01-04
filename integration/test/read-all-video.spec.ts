import {videoEndpoints} from "./endpoints/video.endpoints";
import truncateAll from "./database/truncate-all";
import {dbUtils} from "./database";

describe('GET /api/video', () => {
    beforeEach(async () => {
        await truncateAll()
    })

    test("should return empty", async () => {
        expect.assertions(2)

        const response = await videoEndpoints.getAllVideos(0, 1)

        expect(response.status).toBe(200)
        expect(response.data).toStrictEqual([])
    })

    test.skip("should return paginated results", async () => {
        expect.assertions(4)
        await dbUtils.addVideos([])

        const response1 = await videoEndpoints.getAllVideos(0, 2)
        const response2 = await videoEndpoints.getAllVideos(1, 2)

        expect(response1.status).toBe(200)
        expect(response1.data).toHaveLength(2)

        expect(response2.status).toBe(200)
        expect(response2.data).toHaveLength(1)
    })
});
