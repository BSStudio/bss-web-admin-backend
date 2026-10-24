# UI module

This module holds the admin UI: server-rendered pages from the root, `/`. The REST API (`/api`), the actuator,
the Swagger UI (`/swagger-ui.html`) and the OpenAPI document keep their own addresses.
It uses [Thymeleaf][thymeleaf] templates, [htmx][htmx] for partial page updates and [IBM Carbon][carbon]'s styles
with the BSS theme on top. htmx comes in as a WebJar, and Gradle takes Carbon's precompiled stylesheet from npm
(`carbonStyles` in `build.gradle.kts`).

Controllers call the services directly, the same way the `web` module does.
Keep business logic in the `service` module.

## Layout

|                    Path                    |                          What it holds                          |
|--------------------------------------------|-----------------------------------------------------------------|
| `<feature>/page/*PageController.kt`        | One controller per section, returning template names            |
| `<feature>/page/*Form.kt`                  | A form's fields, their validation and mapping to the model      |
| `ui/config/ConditionalOnAdminUi.kt`        | Turns every UI bean on or off together                          |
| `ui/config/ApiPaths.kt`                    | The API's addresses, which the UI's language and URL rules skip |
| `ui/config/UiSecurityConfig.kt`            | Browser login and security headers for every UI address         |
| `ui/config/UiWebConfig.kt`                 | Languages and locale interceptors for every UI address          |
| `ui/config/AdminLocaleResolver.kt`         | Language cookie for the UI, Accept-Language for the API         |
| `ui/config/LanguageLinksAdvice.kt`         | Language links that keep the current address                    |
| `ui/flash/Flash.kt`                        | One-off messages shown after a redirect                         |
| `ui/paging/`                               | Paging, search and sorting of lists, and a table's state        |
| `resources/templates/admin/layout.html`    | Page shell: head, top bar, flash message, language switch       |
| `resources/templates/admin/fragments.html` | Shared pieces: search, sort, pagination, dates, dialogs         |
| `resources/templates/admin/<section>/`     | `list.html`, `new.html`, `edit.html` of a section               |
| `resources/static/css/admin.css`           | Carbon in a layer, the BSS theme tokens, page layout            |
| `resources/static/js/admin.js`             | Unsaved changes, select all, toasts; our only script            |
| `resources/messages*.properties`           | UI text in English (default) and Hungarian                      |

## Logo and icons

The logo artwork lives in `../../tools/admin-icons/logo.svg` (square) and `../../tools/admin-icons/logo-horizontal.svg`
(rectangular BSS lockup). Both use Roboto lettering outlined as paths and the BSS orange-to-blue frame. Device icons are
generated from the square artwork with [RealFaviconGenerator's CLI][rfg]. The checked-in icon set includes an SVG and ICO
for desktop browsers, Apple's 180px touch icon, and Android's 192px and 512px web-app icons and manifest.

The generation command is in `tools/admin-icons/generate-icons.sh`. It uses the pinned `realfavicon` CLI through `npx` and does not add a JavaScript package to the application. CI pins Node via `.node-version`, reruns the generator, and fails if the checked-in icon set changes. `AdminIconsTest` checks the icon files and their dimensions.

[rfg]: https://realfavicongenerator.net/favicon-generator/node-cli

## Page patterns

| Section |                              List                              |                Edit page                |
|---------|----------------------------------------------------------------|-----------------------------------------|
| Videos  | Newest first; visibility switch; bulk publish/hide             | Details, URLs, labels; crew panel       |
| Events  | Newest first; video count; visibility switch                   | Details; videos panel (search, add)     |
| Members | By name, archived last; archived switch; bulk archive, status  | Profile, status, joined date, archived  |
| Labels  | One page: video count, add form in a side panel, delete dialog | No edit page; the service has no update |

Videos is the reference section; new sections follow its structure. The event and member services return every
item at once, so their lists are paged in memory with `ui/paging/Paging.kt`.

None of the services can search, so search filters the full lists in memory (`ui/paging/Search.kt`): every word
must appear, ignoring case and accents. Videos stay paged by the database until there is a search. With a few
thousand rows a search takes well under a second; move it into the database when that changes.

- **Layout.** Every page starts with `th:replace="~{admin/layout :: layout(~{::title}, ~{::main}, 'videos')}"`.
  The third argument marks the active item in the top bar. Add new sections to the top bar in `layout.html`.
  The layout's parameters are prefixed with `layout` so they never hide page data such as `title`.
- **Model contract.** Each page's `<main>`, and each fragment, starts with `th:assert` listing the model attributes
  it needs, e.g. `th:assert="${page != null}, ${q != null}, ${sorting != null}"`. A controller that forgets one
  fails with a clear message instead of a half-rendered page.
- **Top bar.** Below 48rem the sections fold into a menu: the ☰ button opens the list as a popover, so no script
  is needed. The signed-in person's menu (language, *Log out*) opens on hover, or with its button for keyboard and
  touch. At login, `CurrentUserLoginHandler` looks for the member they are, once per session: their username
  (`preferred_username`) as a member's URL, else the only member with their full name. Their initials then lead to
  that member's page.
- **List.** `GET /<section>?q=&page=&size=&sort=` shows a toolbar (search, *New*), a compact table, and
  a footer with the page size, the item range, a page list and previous / next. Searching, sorting and paging load
  the list into `#results` with htmx and update the address, so reloading or sharing it keeps the view.
  For an htmx request the controller renders only the `results` fragment (htmx-spring-boot's `HtmxRequest`).
  Row actions that change one value (the visibility switch) use htmx and swap only their own table cell.
- **Dates** print in the page's language with `admin/fragments :: dates(from, to)`, inside `<time datetime>`.
- **Selection.** Bulk actions are a plain form; the row checkboxes join it with `form="bulk"`, and so do the hidden
  `q`, `page`, `size` and `sort` fields under the table, which the controller binds as `TableQuery` and passes to
  `redirect.backTo(table)`. While any row is selected, CSS (`:has`) swaps the toolbar for the selection bar; its
  *Cancel* is a reset button. The header checkbox (`admin/fragments :: selectAll`) selects the whole page.
- **Sorting.** A header made with `admin/fragments :: sortable` links to `sort=<column>,asc`, then `,desc`.
  Each controller lists its sortable columns in a `TableSort`, with a comparator for each; other columns, or none,
  get the default order, which also breaks ties. Videos sort in the database unless there is a search; the other
  lists sort in memory. Text sorts the Hungarian way. Events can also be sorted by their video count, which the database counts as it loads them.
- **New.** `GET /<section>/new` asks only for the required fields, then opens the edit page.
- **Edit.** `GET /<section>/{id}` is a full page: the form on the left, related lists (crew) in a side
  panel on the right. *Save* posts the form and redirects to the list with a flash message.
- **Validation.** Every form, the create forms too, is a `*Form` class with Bean Validation: `@NotBlank`, `@Size`
  from the column lengths, and `@AssertTrue datesInOrder` where there is a date range. The controller takes
  `@Valid @ModelAttribute("form") form, binding: BindingResult` and shows the form again while there are errors.
  In the template, `admin/fragments :: formError` goes at the top of the form and `fieldError('<field>')` under
  each field, which gets `aria-invalid` and `aria-describedby="<field>-error"`. A unique constraint violation
  becomes a field error where the field is known (`binding.rejectValue("url", "member.error.duplicateUrl")`), and
  a form error otherwise. The messages are generic, by constraint (`NotBlank`, `Size`, …), in the message files.
  A value the form can't send (not a date, an unknown status) leaves no form to show, so it answers 400.
- **Sub-lists** such as the crew are a fragment in the same template, re-rendered by htmx after each add or remove.
- **Choosing from a known list.** Use a `<select>`, not `<datalist>`, which can't be styled and hides options
  once something is typed. Where the browser supports `appearance: base-select` (Chromium) the open list is
  styled too. When new values must stay possible, add a last option marked `data-other` and give the select
  `data-other="<id of a text field>"`: `admin.js` shows that field while the option is chosen, as for crew positions.
- **Delete** sits in the page header (in each row for labels) and opens `admin/fragments :: deleteDialog`, a native
  `<dialog>`, with `command="show-modal"`. Its form posts to `/<section>/{id}/delete`. Invoker commands are
  Baseline only since December 2025; in older browsers `admin.js` opens and closes the dialog instead.
- **Unsaved changes.** Forms carry `th:data-unsaved="${#fields.hasAnyErrors()} ? 'pending' : 'track'"` and an empty
  `<span class="unsaved" role="status">` next to *Save*. While the values differ from the loaded ones, `admin.js`
  writes "Unsaved changes" into it, which screen readers announce, and asks before leaving the page. After a failed
  save the form counts as unsaved from the start.
- **File server failures.** Saving a video also tells the file API about its URLs, after the database change is
  committed. If that call fails, the page says the video was saved but the file server wasn't updated, instead of
  showing the error page.
- **Messages after a redirect.** Call `redirect.flash("video.flash.saved", title)`; pass `error = true` for failures.

## Look and feel

The pages use [Carbon][carbon]'s components and classes (`cds--btn`, `cds--data-table`, `cds--text-input` …), as
in Carbon's own markup. Carbon's stylesheet comes from `@carbon/styles` on npm: the `carbonStyles` task downloads the
version in `gradle/libs.versions.toml` (Renovate updates it), checks it against npm's integrity hash and serves it as
`/css/carbon.min.css`, without its `@font-face` rules, which load IBM Plex from IBM's CDN and which the
Content-Security-Policy would block. The type falls back to the system font.

- `admin.css` imports Carbon into a cascade layer, so its own rules win without matching Carbon's specificity.
- The theme is Carbon's White and Gray 100 themes as one `light-dark()` per `--cds-*` token, so the browser's colour
  scheme picks light or dark. Carbon's greys are tinted towards the brand blue, and its blues are the BSS icon's.
- The brand appears only as the orange-to-blue gradient (`--bss-edge`, blended in OKLCH) as a thin line along an
  edge: under the top bar (it runs while htmx loads), under a primary button or a dashboard tile on hover, and along
  the top of a dialog. The BSS logo's frame is the only rounded shape apart from the switches.
- Primary buttons are ink (near-black, near-white in dark) instead of Carbon's blue; blue is for links and focus.
  One primary per page; `cds--btn--secondary` for Cancel, `cds--btn--danger--ghost` for Delete.
- The top bar stays dark in both schemes (`color-scheme: dark` on it), and the selection bar takes the opposite
  scheme to the page, like Carbon's inverse surfaces.
- What Carbon does with JavaScript is done in CSS: the selection bar shows with `:has()`, the switch is Carbon's
  toggle drawn by a real checkbox's `:checked`, and the delete dialog is Carbon's modal in a `<dialog>`.
- Write checkboxes out instead of `th:field`: its hidden `_<field>` marker would come between the box and its label,
  which Carbon's stylesheet needs side by side. Put the marker before the box.
- Success messages are toasts (`#toasts`, top right, gone after five seconds unless hovered or focused); errors stay
  in the page as an inline notification. A row switch's response adds its toast out of band
  (`hx-swap-oob="beforeend:#toasts"`).
- Features up to Baseline 2026 are used freely (anchor positioning, invoker commands, `light-dark()`,
  `field-sizing`). Newer ones only where nothing depends on them: a dialog's fade out (Chromium) and
  `appearance: base-select`, which makes long select lists scroll where the browser supports it.

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
- When an htmx update fails, the error page replaces the whole page (`hx-status:4xx` and `hx-status:5xx` on
  `<body>` in `layout.html`). When the session has expired, htmx loads the page it was on
  (htmx-spring-boot's `HxRedirectToPageAuthenticationEntryPoint`), which leads to the login.
- The admin filter chain serves `/error` and permits it, so the page also renders after a session has expired.

## Images

Video thumbnails and member pictures are not in the admin yet: uploading them to the file API comes in a later
change. The video and member edit pages already have a panel for the image, with a placeholder
(`admin/fragments :: imagePlaceholder`).

## Keyboard and focus

- The first Tab on every page reaches *Skip to content*, which jumps to `<main id="main">`. Keep `id="main"` on new pages.
- The *New* pages focus their only field (`autofocus`), so you can type straight away. Edit pages start at the top,
  since you often open them to read.
- The focus ring is the browser's own.
- A required field left empty gets a red border once you move on (`:user-invalid`). Errors from the server set
  `aria-invalid="true"` on the field and point `aria-describedby` at its message; the alert at the top of the form
  is read out as the page shows it.
- htmx swaps keep your place. htmx puts focus back on an element with the same `id` (the visibility switch), and
  focuses an `autofocus` element in swapped-in content (the crew member list after adding or removing crew).
- Dialogs open with the safe choice (*Cancel*) focused; Esc closes them. Esc also hides the profile menu that a
  mouse over the profile opened.

## htmx

The UI uses htmx 4.

- Inheritance is explicit: an attribute applies to the children only with `:inherited`, e.g.
  `hx-target:inherited="#results"` on the pagination `<nav>`, and the CSRF header on `<body>`:
  `th:hx-headers:inherited`. htmx forms need no hidden CSRF field.
- htmx 4 swaps error responses too. `hx-status:4xx:inherited` and `hx-status:5xx:inherited` on `<body>` make the
  error page replace the whole page, without changing the address.
- `htmx-config` in `layout.html` turns off htmx's indicator styles, which would need an inline `<style>` and an
  exception in the Content-Security-Policy, and makes going back reload the page, since list requests get only
  the list.
- The admin pages send a strict `Content-Security-Policy` (`UiSecurityConfig`): scripts, styles and images from the
  app itself only, no inline script or style, no `eval`. Keep templates free of `style=""`, `<script>` and `on*`
  attributes; htmx's `hx-on` needs `eval`, so don't use it.

## Languages

English is the default. `?lang=hu` on any admin URL switches to Hungarian and stores the choice in the
`bss-admin-lang` cookie; `?lang=en` switches back. Unknown languages fall back to English. The cookie only counts on
admin pages: the API keeps reading `Accept-Language` (`AdminLocaleResolver`). The language links keep the current
search, sort and page (`LanguageLinksAdvice`; `admin.js` takes the address htmx last pushed).

Never write UI text into a template; use `#{key}` and add the key to `messages.properties` and
`messages_hu.properties`. Keys follow `<section>.<area>.<name>`, for example `video.flash.saved`.
`MessagesTest` fails when the two files differ in keys or placeholders, when a message is empty, when a template
or a controller uses a key that doesn't exist, or when nothing uses a key. Keys built at runtime
(`'nav.' + section`, `'member.status.' + status`, `'error.title.'`, `'error.lead.'`) and the validation codes are
on its allow-list.

The UI keeps its settings in code rather than in `application.yml`, so they don't change the API: its own
message source in `UiWebConfig`, and `@DateTimeFormat(iso = DATE)` on the forms' date fields.

Messages that take arguments go through `MessageFormat`, so an ASCII apostrophe must be doubled (`''`).
Prefer the typographic apostrophe (’), which needs no escaping.

## Login

The app has two filter chains. The `web` module's comes first and takes the REST API (`/api`), the Swagger UI,
the OpenAPI document and the actuator: it accepts only an opaque token, keeps no session, and so ignores the admin's
session cookie. The admin UI's chain takes every other address, with OAuth2 login (authorization code flow) and
session cookies.

The UI is enabled only when a client registration is configured, and logs in with it. Production calls it
`authentik`, as in the table below, but the id is up to the deployment: configure one registration, with any id.
Every UI bean (the filter chain, the controllers, the error details) carries `@ConditionalOnAdminUi`, so without it
nothing of the UI exists. The `web` module then adds a catch-all chain that asks for a token everywhere, so `/`
answers `401` without one and has no admin page to serve with one.

**Who can use it.** Anyone who can finish the Authentik login can change every video, event and member; the app
itself checks no group. Restrict the login with the Authentik application's policy bindings (e.g. a group
membership policy), and keep them that way. A group check in the app is a possible later addition.

**Behind a proxy.** The redirect URI is built from `{baseUrl}`, so the app has to know the address the browser
used: see [Behind a proxy](../README.md#behind-a-proxy) in the server module.

|   Property (`spring.security.oauth2.client.…`)    |                         Value                         |
|---------------------------------------------------|-------------------------------------------------------|
| `registration.authentik.client-id`                | Client ID of the Authentik application                |
| `registration.authentik.client-secret`            | Client secret of the Authentik application            |
| `registration.authentik.authorization-grant-type` | `authorization_code`                                  |
| `registration.authentik.redirect-uri`             | `{baseUrl}/login/oauth2/code/{registrationId}`        |
| `registration.authentik.scope`                    | `openid,profile,email`                                |
| `provider.authentik.issuer-uri`                   | `https://login.bsstudio.hu/application/o/<app-slug>/` |
| `provider.authentik.user-name-attribute`          | `preferred_username`                                  |

In Authentik, register `https://<admin host>/login/oauth2/code/authentik` as the redirect URI, and
`https://<admin host>/` as the one to return to after logging out.

## Local development

Locally you log in through the `mock-oidc` WireMock's login form, where any username logs in; see
[Mock login](../../README.md#mock-login). A username that is a member's URL logs in as that member. Locally the
login is plain OAuth2 (scope `profile`), because the mock has no ID tokens to sign; production uses OIDC through
Authentik.

Start the dependencies, then the server with Gradle:

```shell
docker compose up -d
SPRING_PROFILES_ACTIVE=local ./gradlew :server:bootRun
```

Put the `local` profile in `server/src/main/resources/application-local.yml` (git ignores it):

```yaml
bss:
  file-api:
    url: http://localhost:8888
    username: username
    password: password
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/bss?currentSchema=private
    username: user
    password: password
  flyway:
    default-schema: private
    schemas: "private,public"
  security:
    oauth2:
      resourceserver:
        opaquetoken:
          introspection-uri: http://localhost:8998/o/introspect
          client-id: client-id
          client-secret: client-secret
      client:
        registration:
          authentik:
            client-id: admin-ui
            client-secret: secret
            authorization-grant-type: authorization_code
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
            scope: profile
        provider:
          authentik:
            authorization-uri: http://localhost:8998/login/authorize
            token-uri: http://localhost:8998/login/token
            user-info-uri: http://localhost:8998/login/userinfo
            user-name-attribute: sub
```

Or run the app container. The integration tests use it without the UI, so the UI's login settings for it are
in a separate file, `docker-compose.ui.yml`:

```shell
docker compose -f docker-compose.yml -f docker-compose.ui.yml --profile app up -d --build
```

Then open <http://localhost:8080>.

## Testing

`UiTestConfig` creates a Spring environment for the `@WebMvcTest` tests in this module.
Import `UiSecurityConfig` in each test so the real login chain is used, and log in with `oauth2Login()`.
The `application.yml` in `test/resources` holds a client registration that needs no network access.

`server/src/test/resources/application.yml` has a client registration too, so every `@SpringBootTest` in `server`
starts with the UI on. That tests the two filter chains together, the way production runs.

Browser checks (Playwright screenshots, axe, Lighthouse, HTML and CSS linting) are planned in
[#976](https://github.com/BSStudio/bss-web-admin-backend/issues/976).

[thymeleaf]: https://www.thymeleaf.org/
[htmx]: https://htmx.org/
[carbon]: https://carbondesignsystem.com/

