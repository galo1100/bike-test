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

Dependencies point inward: `presentation` and `data` both depend on `domain`, never on each other.
The use case is an interface in `domain` implemented in `data` and bound via Hilt, so the ViewModel
is tested against a fake with no HTTP involved.

## Key decisions

**Ktor over Retrofit.** Retrofit is the default Android choice, but Ktor is multiplatform — if this
codebase grows a shared KMP module, the networking layer moves as-is.

**A KMP-ready core, with two deliberate exceptions.** Ktor, kotlinx.serialization, coroutines,
kotlinx-collections-immutable, Coil 3 and `kotlin.time.Instant` are all multiplatform. The two
Android/JVM-only pieces are **Hilt** (chosen for compile-time-verified DI, and because it's the
Android standard) and the **formatting in the UI mapper** — `java.time`, `java.util.Locale` and
`String.format` are all JVM-only, so that layer would move to `kotlinx-datetime` and `NumberFormat`
in a shared module. Each is confined to a single layer, so they're the known seams to cut if this
goes multiplatform.

**`Dispatchers.Default`, not `IO`.** Ktor suspends without blocking a thread, so there's no blocking
IO to get off. What costs CPU is deserialization and DTO→domain mapping, and `Default` is sized for
exactly that; `IO` would reserve a thread from the blocking-friendly pool for work that never
blocks. It's injected behind a `@DispatcherDefault` qualifier so tests substitute a
`StandardTestDispatcher` and run deterministically.

**Value classes for units.** `Percent`, `Celsius`, `Kilometers`, `KilometersPerHour` and
`Horsepower` are `@JvmInline` — no allocation, but the compiler stops you passing a range where a
speed belongs. Telemetry is exactly the domain where confusing km with km/h is a real bug.

**`ImmutableList` in UI models.** Compose treats `List` as unstable and recomposes on it.
`ImmutableList` restores stability without hand-written `@Stable` annotations.

**`rememberAsyncImagePainter` over `SubcomposeAsyncImage`** for the image shimmer.
`SubcomposeAsyncImage` reads better, but subcomposition is the slow path and this card is a
`LazyColumn` item; it also can't answer intrinsic-measurement queries.

## Mock API

There is no real backend. `MockBikeTelemetryEngineFactory` installs a Ktor `MockEngine` that serves
`app/src/main/assets/telemetry_snapshot.json` after a short delay.

To exercise the error states, change the scenario in `NetworkModule.provideMockBikeTelemetryConfig()`:

| Scenario | Result |
| --- | --- |
| `SUCCESS` | the bundled snapshot (default) |
| `HTTP_ERROR` | 503, surfaced as `BikeTelemetryError.Unavailable` |
| `MALFORMED` | truncated JSON, surfaced as `BikeTelemetryError.InvalidSnapshot` |

## Trade-offs and what I'd do differently

**`kotlin.Result` is the choice I'd revisit first.** It's stdlib and free, but its error type is
`Throwable`, so the compiler can't verify every failure is handled. The cost shows up in
`BikeTelemetryErrorMapper`:

```kotlin
is Unknown -> R.string.telemetry_error_unknown
else -> R.string.telemetry_error_unknown   // unreachable, but required
```

Every `BikeTelemetryError` case is already covered, yet `else` is still mandatory and adding a new
error type would compile silently instead of failing. A custom `sealed interface ApiResult<out T,
out E>` makes that `when` exhaustive and deletes the dead branch. Related:
domain errors extend `Exception` so they fit `Result.failure`, which conflates expected control flow
with exceptional flow. Both changes go together, a typed result plus errors as plain sealed data.

**Unit suffixes are hardcoded in the mapper.** `String.format(locale, "%.1f km", value)` localizes
the *number* but not the *unit*, and there's no imperial support. Proper i18n means moving suffixes
into string resources and using `NumberFormat`/ICU. I localized what was cheap and left one visible
seam rather than half-doing it invisibly.

**Single module.** Right at this size; drawing module boundaries before the seams are known costs
more than it returns. Past a few features I'd split `:core:network`, `:core:designsystem` and
`:feature:telemetry` for example.

**No observability.** There's no crash reporting, structured logging or network tracing, all of
which a production app needs to diagnose failures in the field.

## Libraries

Compose + Material 3, Hilt, Ktor, kotlinx.serialization, coroutines, kotlinx-collections-immutable,
Coil 3. Versions live in `gradle/libs.versions.toml`; ktlint rules live in `.editorconfig`.
