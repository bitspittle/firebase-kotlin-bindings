This project is a collection of bindings for working with various Firebase services in Kotlin/JS.

*<span style="color:red">THIS PROJECT IS STILL VERY EXPERIMENTAL AND NOT READY FOR PUBLIC USE.</span>*

The goal of these bindings are to provide a clean, Kotlin-idiomatic view of Firebase web services:
* JavaScript methods that return `Promise`s are converted to `suspend fun`s in Kotlin
* Class design is updated for an API that is more object-oriented.
* Some methods which take in / return JSON-objects in JavaScript are changed to typed objects in Kotlin.
* Constructor (factory) methods are added when possible to make constructing JavaScript interfaces with optional
  parameters easier.

## Gradle

At the moment, this library is not published anywhere. To use it, clone this project, and then run

```bash
$ ./gradlew publishToLocalMaven
```

Then, in your own project:

```kotlin
// build.gradle.kts

repositories {
  /* ... other repositories ... */
  mavenLocal()
}

kotlin {
  js(IR) { /* ... */ }
  sourceSets {
    val jsMain by getting {
      dependencies {
        // TODO: Replace with a real version when this library gets more mature
        implementation("dev.bitspittle:firebase-kotlin-bindings:+")
      }
    }
  }
}
```

## Background

This project contains bindings I need for using various Firebase services, wrapping API calls from
[the web API](https://firebase.google.com/docs/reference/js) and porting them to Kotlin.

Honestly, I probably should have used an existing solution (for example,
[GitLiveApp/firebase-kotlin-sdk](https://github.com/GitLiveApp/firebase-kotlin-sdk)), but I wanted to see what it's like
to wrap a JS API with a Kotlin one. This is, at the moment, a learning project.

## Usage

This library provides a handcrafted Kotlin layer backed by external JS APIs that it delegates to.

These custom classes are provided instead of the underlying JS APIs as those are designed somewhat inconsistently and
occasionally using features that don't map cleanly to Kotlin concepts.

To get started, initialize a `FirebaseApp` class, and use that to access the remaining APIs.

So for example, this JavaScript code (taken from
[the tutorials](https://firebase.google.com/docs/database/web/read-and-write#basic_write)):

```javascript
import { initializeApp } from "firebase/app";
import { getDatabase, ref, set } from "firebase/database";

const firebaseOptions = { /* ... */ };
const app = initializeApp(firebaseOptions)

function writeUserData(userId, name, email, imageUrl) {
  const db = getDatabase(app);
  set(ref(db, 'users/' + userId), {
    username: name,
    email: email,
    profile_picture : imageUrl
  });
}
```

translates to the following Kotlin code:

```kotlin
val app = FirebaseApp.initialize(FirebaseOptions(/*...*/))

fun writeUserData(userId: String, name: String, email: String, imageUrl: String) {
    val db = app.getDatabase()
    db.ref("users/$userId").set(json(
        "username" to name,
        "email" to email,
        "profile_picture" to imageUrl
    ))
}
```

## API support

- @firebase/analytics (<span style="color:yellow">low</span>)
- @firebase/app (<span style="color:yellow">low</span>)
- @firebase/app-check (<span style="color:red">none</span>) 
- @firebase/auth (<span style="color:yellow">low</span>)
- @firebase/database (<span style="color:yellow">low</span>)
- @firebase/firestore (<span style="color:red">none</span>)
- @firebase/functions (<span style="color:red">none</span>)
- @firebase/installations (<span style="color:red">none</span>)
- @firebase/messaging (<span style="color:red">none</span>)
- @firebase/performance (<span style="color:red">none</span>)
- @firebase/remote-config (<span style="color:red">none</span>)
- @firebase/storage (<span style="color:red">none</span>)