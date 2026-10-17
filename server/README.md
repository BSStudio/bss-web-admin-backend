# The server module

The server module bundles the backend services of the application.
The application-wide properties should be stored in the `application.yml` file.

For local development, create an `application-local.yml` file and override the properties you need.

## Behind a proxy

The app runs behind a TLS-terminating proxy, so `server.forward-headers-strategy: native` makes it trust the
proxy's `X-Forwarded-Proto` and `X-Forwarded-Host`. Addresses the app builds from the request, such as the
`{baseUrl}` of an OAuth2 login's redirect URI, then use the address the browser used (`https://…`), and cookies it
sets over https are `Secure`. The proxy must set these headers itself and overwrite any the client sends.
On Kubernetes Spring Boot already defaults to `native`; this setting makes it the same everywhere.

Don't add any file under the server module that is not related to the backend services.
