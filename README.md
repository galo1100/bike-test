# bike-test

A single-screen Android app that displays a bike telemetry snapshot: battery, session, power and
diagnostics. The API is mocked locally, so it runs offline with no backend.

## Run

Open in Android Studio and hit run, or:

```bash
./gradlew :app:assembleDebug
```

`minSdk` 26, `compileSdk` 37.

### Requirements

This uses a recent toolchain (AGP 9.3.1, Gradle 9.5.0, Kotlin 2.4.10), so you need:

- **Android Studio** new enough to sync AGP 9.3 — Quail (2026.1.x) works.
- **JDK 17+** (CI uses 21).
- **SDK Platform 37** and **Build Tools 36.0.0** installed.

## Checks

```bash
./gradlew ktlintCheck              # lint (ktlintFormat to autofix)
./gradlew :app:testDebugUnitTest   # unit tests
./gradlew :app:connectedDebugAndroidTest   # UI tests, needs a device
```

CI runs lint first, then build + unit tests + instrumented-test compilation.

## Architecture

Three layers under `galo.db.biketest`, wired with Hilt:

- **`data`** — Ktor client, DTOs, and mappers to domain. Failures are mapped to a
  `BikeTelemetryError` and returned as `Result`.
- **`domain`** — plain models and the `GetBikeTelemetry` use case. Units are value classes
  (`Percent`, `Celsius`, `Kilometers`) so they can't be mixed up.
- **`presentation`** — Compose UI. The ViewModel exposes a `BikeTelemetryState` (`Loading` /
  `Content` / `Error`) as a `StateFlow`; a mapper turns domain models into preformatted UI models,
  keeping formatting out of composables.

## Mock API

There is no real backend. `MockBikeTelemetryEngineFactory` installs a Ktor `MockEngine` that serves
`app/src/main/assets/telemetry_snapshot.json` after a short delay.

To exercise the error states, change the scenario in `NetworkModule.provideMockBikeTelemetryConfig()`:

| Scenario | Result |
| --- | --- |
| `SUCCESS` | the bundled snapshot (default) |
| `HTTP_ERROR` | 503, surfaced as `BikeTelemetryError.Unavailable` |
| `MALFORMED` | truncated JSON, surfaced as `BikeTelemetryError.InvalidSnapshot` |

## Libraries

Compose + Material 3, Hilt, Ktor, kotlinx.serialization, coroutines, kotlinx-collections-immutable,
Coil 3. Versions live in `gradle/libs.versions.toml`; ktlint rules live in `.editorconfig`.
