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
//object Firebase {
//    object Auth {
//        // See: https://developers.google.com/identity/openid-connect/openid-connect#authenticationuriparameters
//        data class CustomParameters(
//            val accessType: AccessType? = null,
//            val hd: String? = null,
//            val includeGrantedScopes: Boolean? = null,
//            val loginHint: String? = null,
//            val prompt: Prompt? = null,
//            val state: String? = null,
//        ) {
//            enum class AccessType {
//                OFFLINE,
//                ONLINE
//            }
//            enum class Prompt {
//                NONE,
//                CONSENT,
//                SELECT_ACCOUNT,
//            }
//        }
//        fun getAuth(app: FirebaseApp) = dev.bitspittle.firebase.externals.auth.getAuth(app)
//
//        suspend fun createUserWithEmailAndPassword(auth: dev.bitspittle.firebase.externals.auth.Auth, email: String, password: String) =
//            dev.bitspittle.firebase.externals.auth.createUserWithEmailAndPassword(auth, email, password).await()
//
//        fun onAuthStateChanged(auth: dev.bitspittle.firebase.externals.auth.Auth, handleStateChanged: (User?) -> Unit) =
//            dev.bitspittle.firebase.externals.auth.onAuthStateChanged(auth, handleStateChanged)
//
//        suspend fun signInWithEmailAndPassword(auth: dev.bitspittle.firebase.externals.auth.Auth, email: String, password: String) =
//            dev.bitspittle.firebase.externals.auth.signInWithEmailAndPassword(auth, email, password).await()
//
//        suspend fun signInWithPopup(auth: dev.bitspittle.firebase.externals.auth.Auth, provider: AuthProvider) =
//            dev.bitspittle.firebase.externals.auth.signInWithPopup(auth, provider).await()
//
//        suspend fun signOut(auth: dev.bitspittle.firebase.externals.auth.Auth) =
//            dev.bitspittle.firebase.externals.auth.signOut(auth).await()
//    }
//}

// region app

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
