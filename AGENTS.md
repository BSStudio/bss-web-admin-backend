# Guidelines for coding agents

Read [README.md](README.md) for the project and [server/ui/README.md](server/ui/README.md) before changing the admin
UI. This file lists what those don't say, or what is easy to get wrong.

## Environment

- Use JDK 25 (`.sdkmanrc`: `java=25-librca`). Shells that don't load sdkman need
  `export JAVA_HOME=$HOME/.sdkman/candidates/java/25-librca PATH=$JAVA_HOME/bin:$PATH` before Gradle.
- Run the app the way [server/ui/README.md](server/ui/README.md#local-development) describes: only the
  dependencies in Docker (`docker compose up -d`), the server with `SPRING_PROFILES_ACTIVE=local ./gradlew :server:bootRun`.
  `server/src/main/resources/application-local.yml` is git-ignored; a fresh worktree needs a copy of it. Don't build
  the app image to try a change.
- `bootRun` serves templates, CSS and JS from the build output, so restart it after changing them.
- Don't run Gradle builds or tests while `bootRun` is up from the same checkout: they rewrite the UI module's jar under
  the running server, which then fails every page with `ZipFile invalid LOC header (bad signature)`. Restart `bootRun`
  if that happens.
- The local login is a mock: any username logs in, and a member's URL logs in as that member.
- Tests that use Testcontainers need Docker running.
- **Never run `./gradlew integrationTest` against a database that holds data you want to keep.** The integration
  tests connect to `localhost:5432` (the compose Postgres) and call `deleteAll()` on every table before each test. They
  also need the app running on `localhost:8080`. Leave them to CI, or run them only after a fresh
  `docker compose down -v && docker compose up -d`.

## Before you push

- `./gradlew :server:ui:check` runs the UI module's tests, the 100% JaCoCo instruction coverage rule, detekt and
  Spotless. `./gradlew spotlessApply` fixes formatting, Markdown included. `./gradlew build` checks everything.
- A new migration is a `.pgsql` file, linted by Squawk and SQLFluff; see
  [server/data/README.md](server/data/README.md#flyway-migrations). Never edit a deployed one.
- The linters that aren't Gradle's (actionlint, Squawk, SQLFluff) are pinned in `lint/pyproject.toml`; run them with
  `uv run` from `lint/`.
- Controller tests compare rendered markup as strings. A change to a template's classes or attribute order changes
  them; update the expected strings from the rendered page, not by guessing.
- Commit messages follow Conventional Commits (`feat(ui): …`, `fix(ui): …`); semantic-release builds the release
  notes from them.
- SonarCloud fails the pull request unless new code is rated A for reliability. Its HTML analysis can't read
  Thymeleaf attributes, so:
  - give every field a literal `id`, and every label a literal `for`, even where `th:id` or `th:for` replace them;
  - give `role="switch"` a literal `aria-checked` next to `th:aria-checked`;
  - don't use `autofocus` on page load (`th:autofocus` that restores focus after an htmx swap is fine).
- CodeRabbit, SonarCloud and Codecov comment on every pull request by themselves.

## Admin UI

The look is IBM Carbon with a BSS theme. The full rules are in
[server/ui/README.md, Look and feel](server/ui/README.md#look-and-feel); in short:

- **Carbon comes from npm, as CSS.** The `carbonStyles` Gradle task downloads `@carbon/styles` (version in
  `gradle/libs.versions.toml`). Don't add a CDN link, a web font or a Node build: the Content-Security-Policy allows
  styles, scripts and fonts only from the app itself.
- **Use Carbon's markup and classes** (`cds--btn`, `cds--data-table`, `cds--form-item` …) as in Carbon's own
  examples. Put overrides in `admin.css`: Carbon sits in a cascade layer, so a plain rule there wins without matching
  Carbon's specificity.
- **Colours are tokens.** Use Carbon's `--cds-*` tokens or the `--bss-*` ones, each defined once on `:root` with
  `light-dark()`. Never write a literal colour into a component rule. Check every change in both colour schemes.
- **The brand is one gradient used one way.** `--bss-edge` (orange to blue, blended in OKLCH) appears only as a 2–3px
  line along an edge. It is never a fill, never on text. When an edge appears on hover on a Carbon component, set its
  `background-size` and `background-position` in the resting state: Carbon transitions the whole `background`, and
  the gradient would sweep over the label.
- **Ink, not blue, for primary actions.** One primary button per page; blue is for links, focus and selection.
- **Square corners and flat surfaces.** Radius 0, except the BSS badge and the switch track. Shadows only on things
  that float (dialogs, toasts, menus).
- **Browser support is Baseline 2026.** Anything newer may only be an enhancement that nothing depends on. Check
  support with the MDN MCP server (`.mcp.json`) before using a CSS or HTML feature.
- **Checkboxes are written out, not `th:field`.** Its hidden `_<field>` marker would come between the box and its
  label, which Carbon's stylesheet needs side by side. Put the marker before the box.
- **`th:replace` runs before `th:if`.** To include a fragment conditionally, put `th:if` on a `th:block` around it.
- **Messages.** Success is a toast (a `Flash` after a redirect; a row switch's response sets the `toast` model
  attribute and adds it with `hx-swap-oob="beforeend:#toasts"`). Errors stay in the page as an inline notification.
- **Every text is in both languages**: `messages.properties` and `messages_hu.properties` (`MessagesTest` checks).
- **htmx 4, no inline script.** Don't use `hx-on` (it needs `eval`, which the CSP blocks); add behaviour to
  `admin.js`.
- **Look at the result.** Check a UI change in a browser in light and dark, and at phone width (375px).
