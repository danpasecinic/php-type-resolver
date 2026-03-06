# php-type-resolver

A lightweight static analysis tool that infers PHP variable types from `@var` docblock annotations.

Supports standard types, union types (`string|int`), named tags (`@var Logger $log`), and multi-tag docblocks. Falls back to `mixed` when no match is found.

## Usage

```kotlin
val type = inferTypeFromDoc(variable)
```

## Build

```
./gradlew build
```

## Test

```
./gradlew test
```

## License

MIT
