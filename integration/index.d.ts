import {StartedDockerComposeEnvironment} from "testcontainers";

declare global {
    var dockerComposeEnvironment: StartedDockerComposeEnvironment;
    var baseUrl: { app: string, db: string };
}
