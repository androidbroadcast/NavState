NavState
========

# ‼️ WORK IN PROGRESS ‼️

NavState is simple declarative Navigation Library represent navigation histor as global state

## Compose Sample
```kotlin
@Composable
@Serializable
class UserListDest(): NavDest

@Serializable
class ProfileDest(val userId: UserId): NavDest

@Composable
fun RootScreen() {
    NavHost(
        initialDestination = PingPongDest.Ping(),
        onRootBack = { finish() }
    ) {
        val topEntry by rememberNavTopEntry()
        when (val dest = topEntry.destination) {
            is UserListDest -> UserListScreen(onItemSelected = { id -> navgiator.enqueue(Forward(ProfileDest(id)))})
            is ProfileDest -> ProfileScreen(userId = dest.userId)
        }
    }
}

@Composable
fun UserListScreen(onItemSelected: (String) -> Unit) {...}
```

Facts about library:
- Can work with Compose Multiplatform, SwiftUI, Android Fragments, View and any other UI framework
- Fully async using Coroutines
- Fully testable navigation without UI
