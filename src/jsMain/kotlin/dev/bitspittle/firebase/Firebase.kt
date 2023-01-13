package dev.bitspittle.firebase

import dev.bitspittle.firebase.externals.app.FirebaseApp
import dev.bitspittle.firebase.externals.app.FirebaseOptions
import dev.bitspittle.firebase.externals.database.*
import dev.bitspittle.firebase.externals.auth.Auth
import dev.bitspittle.firebase.externals.auth.AuthProvider
import dev.bitspittle.firebase.externals.auth.FederatedAuthProvider
import dev.bitspittle.firebase.externals.auth.User
import kotlinx.coroutines.await
import kotlin.js.Json
import kotlin.js.json

/**
 * A type-safe API for accessing top level methods in the various Firebase modules.
 */
object Firebase {
    object App {
        fun FirebaseOptions(
            apiKey: String,
            authDomain: String,
            databaseURL: String,
            projectId: String,
            storageBucket: String,
            messagingSenderId: String,
            appId: String,
            measurementId: String? = null,
        ) = object : FirebaseOptions {
            override val apiKey = apiKey
            override val authDomain = authDomain
            override val databaseURL = databaseURL
            override val projectId = projectId
            override val storageBucket = storageBucket
            override val messagingSenderId = messagingSenderId
            override val appId = appId
            override val measurementId = measurementId
        }

        fun initializeApp(options: FirebaseOptions, name: String? = null) =
            dev.bitspittle.firebase.externals.app.initializeApp(
                json(
                    "apiKey" to options.apiKey,
                    "authDomain" to options.authDomain,
                    "databaseURL" to options.databaseURL,
                    "projectId" to options.projectId,
                    "storageBucket" to options.storageBucket,
                    "messagingSenderId" to options.messagingSenderId,
                    "appId" to options.appId,
                    "measurementId" to options.measurementId,
                ),
                name
            )
    }

    object Auth {
        // See: https://developers.google.com/identity/openid-connect/openid-connect#authenticationuriparameters
        data class CustomParameters(
            val accessType: AccessType? = null,
            val hd: String? = null,
            val includeGrantedScopes: Boolean? = null,
            val loginHint: String? = null,
            val prompt: Prompt? = null,
            val state: String? = null,
        ) {
            enum class AccessType {
                OFFLINE,
                ONLINE
            }
            enum class Prompt {
                NONE,
                CONSENT,
                SELECT_ACCOUNT,
            }
        }
        fun getAuth(app: FirebaseApp) = dev.bitspittle.firebase.externals.auth.getAuth(app)

        suspend fun createUserWithEmailAndPassword(auth: dev.bitspittle.firebase.externals.auth.Auth, email: String, password: String) =
            dev.bitspittle.firebase.externals.auth.createUserWithEmailAndPassword(auth, email, password).await()

        fun onAuthStateChanged(auth: dev.bitspittle.firebase.externals.auth.Auth, handleStateChanged: (User?) -> Unit) =
            dev.bitspittle.firebase.externals.auth.onAuthStateChanged(auth, handleStateChanged)

        suspend fun signInWithEmailAndPassword(auth: dev.bitspittle.firebase.externals.auth.Auth, email: String, password: String) =
            dev.bitspittle.firebase.externals.auth.signInWithEmailAndPassword(auth, email, password).await()

        suspend fun signInWithPopup(auth: dev.bitspittle.firebase.externals.auth.Auth, provider: AuthProvider) =
            dev.bitspittle.firebase.externals.auth.signInWithPopup(auth, provider).await()

        suspend fun signOut(auth: dev.bitspittle.firebase.externals.auth.Auth) =
            dev.bitspittle.firebase.externals.auth.signOut(auth).await()
    }

    object Database {
        fun TransactionOptions(applyLocally: Boolean = true) = object : TransactionOptions {
            override val applyLocally = applyLocally
        }

        fun getDatabase(app: FirebaseApp, url: String? = null) =
            dev.bitspittle.firebase.externals.database.getDatabase(app, url)


        fun child(parent: DatabaseReference, path: String) =
            dev.bitspittle.firebase.externals.database.child(parent, path)

        suspend fun get(query: Query) =
            dev.bitspittle.firebase.externals.database.get(query).await()

        fun increment(delta: Number) = dev.bitspittle.firebase.externals.database.increment(delta)

        fun limitToFirst(limit: Number) = dev.bitspittle.firebase.externals.database.limitToFirst(limit)

        fun limitToLast(limit: Number) = dev.bitspittle.firebase.externals.database.limitToLast(limit)

        fun orderByChild(path: String) = dev.bitspittle.firebase.externals.database.orderByChild(path)

        fun push(ref: DatabaseReference) =
            dev.bitspittle.firebase.externals.database.push(ref)

        fun query(query: Query, vararg constraints: QueryConstraint) =
            dev.bitspittle.firebase.externals.database.query(query, *constraints)

        fun ref(db: dev.bitspittle.firebase.externals.database.Database, path: String? = null) =
            dev.bitspittle.firebase.externals.database.ref(db, path)

        suspend fun remove(ref: DatabaseReference) {
            dev.bitspittle.firebase.externals.database.remove(ref).await()
        }

        suspend fun runTransaction(
            ref: DatabaseReference,
            transactionUpdate: (currentData: Any) -> Any?,
            options: TransactionOptions? = null,
        ) =
            dev.bitspittle.firebase.externals.database.runTransaction(
                ref,
                transactionUpdate = { transactionUpdate(it as Any) },
                options
            ).await()

        suspend fun set(ref: DatabaseReference, value: Any?) {
            dev.bitspittle.firebase.externals.database.set(ref, value).await()
        }

        suspend fun update(ref: DatabaseReference, values: Json) {
            dev.bitspittle.firebase.externals.database.update(ref, values).await()
        }
    }
}

// region app

fun FirebaseApp.getDatabase(url: String? = null) = Firebase.Database.getDatabase(this, url)
fun FirebaseApp.getAuth() = Firebase.Auth.getAuth(this)

// endregion

// region auth

suspend fun Auth.createUserWithEmailAndPassword(email: String, password: String) =
    Firebase.Auth.createUserWithEmailAndPassword(this, email, password)
fun Auth.onAuthStateChanged(handleStateChanged: (User?) -> Unit) =
    Firebase.Auth.onAuthStateChanged(this, handleStateChanged)
suspend fun Auth.signInWithEmailAndPassword(email: String, password: String) =
    Firebase.Auth.signInWithEmailAndPassword(this, email, password)
suspend fun Auth.signInWithPopup(provider: AuthProvider) = Firebase.Auth.signInWithPopup(this, provider)
suspend fun Auth.signOut() = Firebase.Auth.signOut(this)

fun FederatedAuthProvider.setCustomParameters(params: Firebase.Auth.CustomParameters) = run {
    val setValues: List<Pair<String, Any?>> = listOf(
        "accessType" to params.accessType?.name?.lowercase(),
        "hd" to params.hd,
        "includeGrantedScopes" to params.includeGrantedScopes,
        "loginHint" to params.loginHint,
        "prompt" to params.prompt?.name?.lowercase(),
        "state" to params.state,
    ).filter { it.second != null }

    setCustomParameters(json(*setValues.toTypedArray()))
}

// endregion

// region database

fun Database.ref(path: String? = null) = Firebase.Database.ref(this, path)

fun DatabaseReference.child(path: String) = Firebase.Database.child(this, path)
fun DatabaseReference.push() = Firebase.Database.push(this)
suspend fun DatabaseReference.remove() = Firebase.Database.remove(this)
suspend fun DatabaseReference.runTransation(transactionUpdate: (currentData: Any) -> Any?, transactionOptions: TransactionOptions? = null) =
    Firebase.Database.runTransaction(this, transactionUpdate, transactionOptions)
suspend fun DatabaseReference.set(value: Any) = Firebase.Database.set(this, value)
suspend fun DatabaseReference.update(values: Json) = Firebase.Database.update(this, values)

fun Query.query(vararg constraints: QueryConstraint) = Firebase.Database.query(this, *constraints)

// endregion