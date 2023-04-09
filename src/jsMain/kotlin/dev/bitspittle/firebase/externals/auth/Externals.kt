@file:JsModule("firebase/auth")
@file:JsNonModule
package dev.bitspittle.firebase.externals.auth

import dev.bitspittle.firebase.externals.app.FirebaseApp
import dev.bitspittle.firebase.externals.util.FirebaseError
import kotlin.js.Json
import kotlin.js.Promise

// https://firebase.google.com/docs/reference/js/auth.actioncodesettings.md#actioncodesettings_interface
internal external interface ActionCodeSettings {
    val android: dynamic // { installApp?: boolean; minimumVersion?: string; packageName: string; }
    val dynamicLinkDomain: String?
    val handleCodeInApp: Boolean
    val iOS: dynamic // { bundleId: string; }
    val url: String
}

// https://firebase.google.com/docs/reference/js/auth.auth
internal external interface Auth {
    val app: FirebaseApp
    val config: Config
    val currentUser: User?
    val languageCode: String?
    val name: String
    val settings: AuthSettings

    fun setPersistence(persistence: Persistence): Promise<Unit>
    fun updateCurrentUser(user: User?): Promise<Unit>
    fun useDeviceLanguage()
}

// https://firebase.google.com/docs/reference/js/auth.autherror
internal external interface AuthError : FirebaseError {
    val customData: Json
}

// https://firebase.google.com/docs/reference/js/auth.authprovider
internal external interface AuthProvider {
    val providerId: String
}

// https://firebase.google.com/docs/reference/js/auth.authsettings
internal external interface AuthSettings {
    val appVerificationDisabledForTesting: Boolean
}

// https://github.com/firebase/firebase-js-sdk/blob/master/packages/auth/src/core/providers/oauth.ts
internal abstract external class BaseOAuthProvider : FederatedAuthProvider {
    fun addScope(scope: String): AuthProvider
    fun getScopes(): Array<String>
}

// https://firebase.google.com/docs/reference/js/auth.config
internal external interface Config {
    val apiHost: String
    val apiKey: String
    val apiScheme: String
    val authDomain: String
    val sdkClientVersion: String
    val tokenApiHost: String
}

// https://firebase.google.com/docs/reference/js/auth.googleauthprovider
internal external class GoogleAuthProvider : BaseOAuthProvider {
    override val providerId: String
}

// https://github.com/firebase/firebase-js-sdk/blob/master/packages/auth/src/core/providers/federated.ts
internal abstract external class FederatedAuthProvider : AuthProvider {
    fun setCustomParameters(params: Json): AuthProvider
}

// https://firebase.google.com/docs/reference/js/auth.persistence
internal external interface Persistence {
    val type: dynamic // 'SESSION' | 'LOCAL' | 'NONE'
}

// https://firebase.google.com/docs/reference/js/auth.user
internal external interface User : UserInfo {
    val emailVerified: Boolean
    val isAnonymous: Boolean
    val metadata: UserMetadata
    val providerData: Array<UserInfo>
    val refreshToken: String

    // https://firebase.google.com/docs/reference/js/auth.user#usergetidtoken
    fun getIdToken(forceRefresh: Boolean?): Promise<String>
}

// https://firebase.google.com/docs/reference/js/auth.usercredential
internal external interface UserCredential {
    val operationType: dynamic // 'LINK' | 'REAUTHENTICATE' | 'SIGN_IN'
    val providerId: String?
    val user: User
}

// https://firebase.google.com/docs/reference/js/auth.userinfo
internal external interface UserInfo {
    val displayName: String?
    val email: String?
    val phoneNumber: String?
    val photoURL: String?
    val providerId: String
    val uid: String
}

// https://firebase.google.com/docs/reference/js/auth.usermetadata
internal external interface UserMetadata {
    val creationTime: String
    val lastSignInTime: String
}

// https://firebase.google.com/docs/reference/js/auth#getauth
internal external fun getAuth(app: FirebaseApp): Auth


// https://firebase.google.com/docs/reference/js/auth#createuserwithemailandpassword
internal external fun createUserWithEmailAndPassword(auth: Auth, email: String, password: String): Promise<UserCredential>

// https://firebase.google.com/docs/reference/js/auth#deleteuser
internal external fun deleteUser(user: User): Promise<Unit>

// https://firebase.google.com/docs/reference/js/auth#issigninwithemaillink
internal external fun isSignInWithEmailLink(auth: Auth, emailLink: String): Boolean

// https://firebase.google.com/docs/reference/js/auth#onauthstatechanged
internal external fun onAuthStateChanged(auth: Auth, handleStateChanged: (User?) -> Unit)

// https://firebase.google.com/docs/reference/js/auth#sendemailverification
internal external fun sendEmailVerification(user: User): Promise<Unit>

// https://firebase.google.com/docs/reference/js/auth#sendsigninlinktoemail
internal external fun sendSignInLinkToEmail(auth: Auth, email: String, actionCodeSettings: ActionCodeSettings): Promise<Unit>

// https://firebase.google.com/docs/reference/js/auth#signinwithemailandpassword
internal external fun signInWithEmailAndPassword(auth: Auth, email: String, password: String): Promise<UserCredential>

// https://firebase.google.com/docs/reference/js/auth#signinwithemaillink
internal external fun signInWithEmailLink(auth: Auth, email: String, emailLink: String?): Promise<UserCredential>

// https://firebase.google.com/docs/reference/js/auth#signinwithpopup
internal external fun signInWithPopup(auth: Auth, provider: AuthProvider): Promise<UserCredential>

// https://firebase.google.com/docs/reference/js/auth#signout
internal external fun signOut(auth: Auth): Promise<Unit>

