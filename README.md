NavState
========

# ‼️ WORK IN PROGRESS ‼️

NavState is simple declarative Navigation Library represent navigation history as global state

## Add library

```kotlin
// Add Jitpack to dependencies resolutions settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        maven("https://jitpack.io")
    }
}

// Add dependencies to use without UI
implementation("com.github.androidbroadcast.NavState:navstate-core:0.1.1")

// For project with Compose
implementation("com.github.androidbroadcast.NavState:navstate-compose:0.1.1")

// For project with Compose with NavHost generation
implementation("com.github.androidbroadcast.NavState:navstate-compose-annotations:0.1.1")
ksp("com.github.androidbroadcast.NavState:navstate-compose-processor:0.1.1")
```

## Compose Sample with NavHost generation

```kotlin
@Serializable
class UserListDest : NavDest

@Serializable
class ProfileDest(val userId: UserId) : NavDest

@Composable
fun RootScreen() {
    GeneratedNavHost(
        initialDestination = UserListDest()
    )
}

@Composable
@NavDest(dest = UserListDest::class)
fun UserListScreen() {
    val navigator = LocalNavigator.current
    val onItemSelected: (String) -> Unit = { id -> navgiator.enqueue(Forward(ProfileDest(id))) }
}

@Composable
@NavDest(dest = ProfileDest::class)
fun ProfileScreen(dest: ProfileDest) {
    // ...
}
```

Facts about library:

- Can work with Compose Multiplatform, SwiftUI, Android Fragments, View and any other UI framework
- Fully async using Coroutines
- Fully testable navigation without UI
