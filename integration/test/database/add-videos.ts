import {Client} from "pg"

export default async function (videos: {}[]) {
    const client = new Client({connectionString: globalThis.baseUrl.db})
    await client.connect();
    await Promise.all(videos.map((video) => client.query("")))
    await client.end()
}
