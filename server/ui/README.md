# UI module

This module holds the admin UI: server-rendered pages under `/admin`.
It uses [Thymeleaf][thymeleaf] templates, [htmx][htmx] for partial page updates and [Pico CSS][pico] for styling.
There is no JavaScript build: htmx and Pico come in as WebJars.

Controllers call the services directly, the same way the `web` module does.
Keep business logic in the `service` module.

## Layout

|                    Path                    |                       What it holds                        |
|--------------------------------------------|------------------------------------------------------------|
| `<feature>/page/*PageController.kt`        | One controller per section, returning template names       |
| `<feature>/page/*Form.kt`                  | Form fields of an edit page and their mapping to the model |
| `ui/config/UiSecurityConfig.kt`            | Browser login for `/admin/**`                              |
| `ui/config/UiWebConfig.kt`                 | Language switching and the `/admin/webjars` handler        |
| `ui/flash/Flash.kt`                        | One-off messages shown after a redirect                    |
| `resources/templates/admin/layout.html`    | Page shell: head, top bar, flash message, language switch  |
| `resources/templates/admin/fragments.html` | Shared pieces: pagination, form error                      |
| `resources/templates/admin/<section>/`     | `list.html`, `new.html`, `edit.html` of a section          |
| `resources/static/admin/css/admin.css`     | Design tokens and compact component styles on top of Pico  |
| `resources/static/admin/js/admin.js`       | The unsaved-changes marker; the only script of our own     |
| `resources/messages*.properties`           | UI text in English (default) and Hungarian                 |

## Page patterns

| Section |                            List                             |                Edit page                |
|---------|-------------------------------------------------------------|-----------------------------------------|
| Videos  | Paged; visibility switch; bulk publish/hide                 | Details, URLs, labels; crew panel       |
| Events  | Newest first; visibility switch                             | Details; videos panel (add and remove)  |
| Members | By name, archived last; archived switch; bulk archive       | Profile, status, joined date, archived  |
| Labels  | One page: table, add form in a side panel, confirmed delete | No edit page; the service has no update |

Videos is the reference section; new sections follow its structure. The event and member services return every
item at once, so their lists are paged in memory with `ui/paging/Paging.kt`.

- **Layout.** Every page starts with `th:replace="~{admin/layout :: layout(~{::title}, ~{::main}, 'videos')}"`.
  The third argument marks the active item in the top bar. Add new sections to the top bar in `layout.html`.
  The layout's parameters are prefixed with `layout` so they never hide page data such as `title`.
- **List.** `GET /admin/<section>` shows a toolbar with the *New* button, a compact table, and a footer with
  the item range and previous / next. Row actions that change one value (the visibility switch) use htmx and swap
  only their own table cell. Bulk actions are a plain form; the row checkboxes join it with `form="bulk"`.
  While any row is selected, CSS (`:has`) swaps the toolbar for the selection bar; its *Cancel* is a reset button.
- **New.** `GET /admin/<section>/new` asks only for the required fields, then opens the edit page.
- **Edit.** `GET /admin/<section>/{id}` is a full page: the form on the left, related lists (crew) in a side
  panel on the right. *Save* posts the form and redirects to the list with a flash message. Unique constraint
  violations re-render the form with `admin/fragments :: error`.
- **Sub-lists** such as the crew are a fragment in the same template, re-rendered by htmx after each add or remove.
- **Delete** sits in the page header and opens a native `<dialog>` with `command="show-modal"`; no JavaScript is
  needed. The dialog's form posts to `/admin/<section>/{id}/delete`.
- **Unsaved changes.** Edit forms carry `th:data-unsaved="${error} ? 'pending' : 'track'"` and an
  `<span class="unsaved">` next to *Save*. `admin.js` shows it while the values differ from the loaded ones and
  asks before leaving the page; after a failed save the form counts as unsaved from the start.
- **File server failures.** Saving a video also tells the file API about its URLs, after the database change is
  committed. If that call fails, the page says the video was saved but the file server wasn't updated, instead of
  showing the error page.
- **Messages after a redirect.** Call `redirect.flash("video.flash.saved", title)`; pass `error = true` for failures.

## Look and feel

Compact and data-dense, using IBM's Carbon design system as a reference for density and component patterns,
not for its exact look:

- 14px text, 32px inputs, buttons and table rows, spacing in steps of 4px.
- A navy top bar; the current section is underlined in orange.
- Orange for the one primary action on a screen, blue for links and focus, red only for destructive actions.
- Buttons: primary (default), `secondary`, `outline`, `ghost`, `danger`, `danger ghost`.
- Every colour is a `--c-*` token in `admin.css`, defined for light and dark mode and fed into Pico's variables.
  Add new colours as tokens, never as literals in component rules.

## Errors

Spring Boot renders `templates/error.html` for every HTML error, in the admin layout.
On admin pages, `AdminErrorAttributes` adds the details for logged-in users: the request, the error message,
a short reference, the start of the stack trace for server errors, and a *Report on GitHub* link. Anonymous visitors
and API responses keep Spring's defaults, without messages.

- The reference is logged with the full stack trace (`Admin UI error <reference> on <request>`), so a reported
  reference leads straight to the log entry.
- *Report on GitHub* opens [`.github/ISSUE_TEMPLATE/admin-ui-error.yml`](../../.github/ISSUE_TEMPLATE/admin-ui-error.yml)
  with the page, reference and details filled in. Issue form fields are pre-filled by their `id`, so keep the ids
  in the form and the parameter names in `AdminErrorAttributes` the same.
- When an htmx update fails, the error page replaces the whole page (`htmx-config` in `layout.html`).
  When the session has expired, htmx reloads the page, which leads to the login.
- The admin filter chain serves `/error` and permits it, so the page also renders after a session has expired.

## Images

Video thumbnails and member profile pictures are uploaded to the file API, which converts each upload to AVIF, WebP
and JPEG in three sizes (`/v/{id}/thumbnail/{sd,hd,fhd}.*`, `/m/{id}/profile/{m,l,xl}.*`). The file API only writes
these files; another host publishes them.

- `bss.media.url` is the base URL of that host. Lists show small thumbnails and avatars, and the edit pages show
  the current image. Without the property, pages show no images, but uploads still work.
- Uploads go through `MediaService` and swap only the image panel (htmx), so unsaved form changes stay.
  If the video or member has no folder on the file API yet (created before the file API existed), the folder is
  created and the upload retried once.
- `admin.js` replaces an image that does not exist yet with the panel's "No image yet" text.
- Uploads can be up to 20 MB (`spring.servlet.multipart.*`).

To try uploads with real images locally, run the real file API and the static host for its files:

```shell
FILE_API_URL=http://file-api:8000 MEDIA_URL=http://localhost:8890 docker compose --profile app --profile files up -d --build
```

Without these, the app uses the WireMock mock of the file API, which accepts uploads but stores nothing.

## Keyboard and focus

- The first Tab on every page reaches *Skip to content*, which jumps to `<main id="main">`. Keep `id="main"` on new pages.
- The first field of a form has `autofocus`, so you can type straight away.
- Everything reachable with Tab gets a 2px focus ring. The field being edited is also tinted, and its label turns blue.
- A required field left empty turns red once you move on (`:user-invalid`). Errors from the server set
  `aria-invalid="true"` on the field and point `aria-describedby` at the `form-error` message.
- htmx swaps keep your place. htmx puts focus back on an element with the same `id` (the visibility switch), and
  focuses an `autofocus` element in swapped-in content (the crew member list after adding or removing crew).
- Dialogs open with the safe choice (*Cancel*) focused; Esc closes them.

htmx requests send the CSRF token from the `hx-headers` attribute on `<body>`, so htmx forms need no hidden field.

## Languages

English is the default. `?lang=hu` on any admin URL switches to Hungarian and stores the choice in the
`bss-admin-lang` cookie; `?lang=en` switches back. Unknown languages fall back to English.

Never write UI text into a template; use `#{key}` and add the key to `messages.properties` and
`messages_hu.properties`. Keys follow `<section>.<area>.<name>`, for example `video.flash.saved`.
`MessagesTest` fails when a key is missing from either file.

Messages that take arguments go through `MessageFormat`, so an ASCII apostrophe must be doubled (`''`).
Prefer the typographic apostrophe (’), which needs no escaping.

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

