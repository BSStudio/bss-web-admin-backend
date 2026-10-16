# UI module

This module holds the admin UI: server-rendered pages under `/admin`.
It uses [Thymeleaf][thymeleaf] templates, [htmx][htmx] for partial page updates and [Pico CSS][pico] for styling.
There is no JavaScript build: htmx and Pico come in as WebJars.

Controllers call the services directly, the same way the `web` module does.
Keep business logic in the `service` module.

## Layout

|                  Path                   |                     What it holds                     |
|-----------------------------------------|-------------------------------------------------------|
| `<feature>/page/*PageController.kt`     | One controller per feature, returning template names  |
| `ui/config/UiSecurityConfig.kt`         | Browser login for `/admin/**`                         |
| `ui/config/UiWebConfig.kt`              | Language switching and the `/admin/webjars` handler   |
| `resources/templates/admin/layout.html` | Page shell: head, top bar, language switch, logout    |
| `resources/templates/admin/*.html`      | Pages; each one fills the layout's title and `<main>` |
| `resources/static/admin/css/admin.css`  | BSS colours on top of Pico                            |
| `resources/messages*.properties`        | UI text in English (default) and Hungarian            |

A new page needs a controller method, a template that starts with
`th:replace="~{admin/layout :: layout(~{::title}, ~{::main})}"`,
and its text keys in **both** message files.

## Languages

English is the default. `?lang=hu` on any admin URL switches to Hungarian and stores the choice in the
`bss-admin-lang` cookie; `?lang=en` switches back. Unknown languages fall back to English.

Never write UI text into a template; use `#{key}` and add the key to `messages.properties` and
`messages_hu.properties`.

## Login

The admin UI uses its own filter chain with OAuth2 login (authorization code flow) and session cookies.
The REST API under `/api` keeps its opaque-token filter chain from the `web` module.

The UI is enabled only when the `authentik` client registration is configured.
Without it, `/admin` falls through to the API filter chain and answers `401`.

|   Property (`spring.security.oauth2.client.…`)    |                         Value                         |
|---------------------------------------------------|-------------------------------------------------------|
| `registration.authentik.client-id`                | Client ID of the Authentik application                |
| `registration.authentik.client-secret`            | Client secret of the Authentik application            |
| `registration.authentik.authorization-grant-type` | `authorization_code`                                  |
| `registration.authentik.redirect-uri`             | `{baseUrl}/login/oauth2/code/{registrationId}`        |
| `registration.authentik.scope`                    | `openid,profile,email`                                |
| `provider.authentik.issuer-uri`                   | `https://login.bsstudio.hu/application/o/<app-slug>/` |
| `provider.authentik.user-name-attribute`          | `preferred_username`                                  |

In Authentik, register `https://<admin host>/login/oauth2/code/authentik` as the redirect URI.

## Local development

`docker compose up -d` starts `mock-login`, an OIDC provider with a login form on <http://localhost:8999>.
Type any username to log in.

With `docker compose --profile app up`, the app container is already configured for it:
open <http://localhost:8080/admin>.

With `./gradlew bootRun`, add this to `server/src/main/resources/application-local.yml` and run with the
`local` profile:

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          authentik:
            client-id: admin-ui
            client-secret: secret
            authorization-grant-type: authorization_code
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
            scope: openid,profile,email
        provider:
          authentik:
            issuer-uri: http://localhost:8999/default
            user-name-attribute: sub
```

## Testing

`UiTestConfig` creates a Spring environment for the `@WebMvcTest` tests in this module.
Import `UiSecurityConfig` in each test so the real login chain is used, and log in with `oauth2Login()`.
The `application.yml` in `test/resources` holds a client registration that needs no network access.

[thymeleaf]: https://www.thymeleaf.org/
[htmx]: https://htmx.org/
[pico]: https://picocss.com/

