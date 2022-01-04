import {Client} from "pg"

export default async function (members: {}[]) {
    const client = new Client({connectionString: globalThis.baseUrl.db})
    await client.connect();
    await Promise.all(members.map((member) => client.query("")))
    await client.end()
}
