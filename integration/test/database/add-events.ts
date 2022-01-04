import {Client} from "pg"

export default async function (events: {}[]) {
    const client = new Client({connectionString: globalThis.baseUrl.db})
    await client.connect();
    await Promise.all(events.map((event) => client.query("")))
    await client.end()
}
