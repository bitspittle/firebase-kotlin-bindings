@file:JsModule("firebase/auth")
@file:JsNonModule
package dev.bitspittle.firebase.externals.auth

import dev.bitspittle.firebase.externals.app.FirebaseApp
import dev.bitspittle.firebase.externals.util.FirebaseError
import kotlin.js.Json
import kotlin.js.Promise

// https://firebase.google.com/docs/reference/js/auth.auth
external interface Auth {
    val app: FirebaseApp
    val config: Config
    val currentUser: User?
    val languageCode: String?
    val name: String
    val settings: AuthSettings
    val tenantId: String?

    fun setPersistence(persistence: Persistence): Promise<Unit>
    fun updateCurrentUser(user: User?): Promise<Unit>
    fun useDeviceLanguage()
}

external interface AuthError : FirebaseError {
    val customData: Json
}

external interface AuthProvider {
    val providerId: String
}

external interface AuthSettings {
    val appVerificationDisabledForTesting: Boolean
}

// https://github.com/firebase/firebase-js-sdk/blob/master/packages/auth/src/core/providers/oauth.ts
abstract external class BaseOAuthProvider : FederatedAuthProvider {
    fun addScope(scope: String): AuthProvider
    fun getScopes(): Array<String>
}

// https://firebase.google.com/docs/reference/js/auth.config
external interface Config {
    val apiHost: String
    val apiKey: String
    val apiScheme: String
    val authDomain: String
    val sdkClientVersion: String
    val tokenApiHost: String
}

// https://firebase.google.com/docs/reference/js/auth.googleauthprovider
external class GoogleAuthProvider : BaseOAuthProvider {
    override val providerId: String
}

abstract external class FederatedAuthProvider : AuthProvider {
    fun setCustomParameters(params: Json): AuthProvider
}

// https://firebase.google.com/docs/reference/js/auth#operationtype
external enum class OperationType {
    LINK,
    REAUTHENTICATE,
    SIGN_IN
}

// https://firebase.google.com/docs/reference/js/auth.persistence
external interface Persistence {
    val type: dynamic // 'SESSION' | 'LOCAL' | 'NONE'
}

// https://firebase.google.com/docs/reference/js/auth.user
external interface User : UserInfo {
    val emailVerified: Boolean
    val isAnonymous: Boolean
    val metadata: UserMetadata
    val providerData: Array<UserInfo>
    val refreshToken: String
    val tenantId: String?
}

// https://firebase.google.com/docs/reference/js/auth.usercredential
external interface UserCredential {
    val operationType: OperationType
    val providerId: String?
    val user: User
}

// https://firebase.google.com/docs/reference/js/auth.userinfo
external interface UserInfo {
    val displayName: String?
    val email: String?
    val phoneNumber: String?
    val photoURL: String?
    val providerId: String
    val uid: String
}

// https://firebase.google.com/docs/reference/js/auth.usermetadata
external interface UserMetadata {
    val creationTime: String
    val lastSignInTime: String
}

// https://firebase.google.com/docs/reference/js/auth#getauth
internal external fun getAuth(app: FirebaseApp): Auth


// https://firebase.google.com/docs/reference/js/auth#createuserwithemailandpassword
internal external fun createUserWithEmailAndPassword(auth: Auth, email: String, password: String): Promise<UserCredential>

// https://firebase.google.com/docs/reference/js/auth#onauthstatechanged
internal external fun onAuthStateChanged(auth: Auth, handleStateChanged: (User?) -> Unit)

// https://firebase.google.com/docs/reference/js/auth#signinwithemailandpassword
internal external fun signInWithEmailAndPassword(auth: Auth, email: String, password: String): Promise<UserCredential>

// https://firebase.google.com/docs/reference/js/auth#signinwithpopup
internal external fun signInWithPopup(auth: Auth, provider: AuthProvider): Promise<UserCredential>

// https://firebase.google.com/docs/reference/js/auth#signout
internal external fun signOut(auth: Auth): Promise<Unit>

