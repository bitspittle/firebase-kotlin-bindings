@file:Suppress("LocalVariableName") // We use _ prefixed variables to indicate external targets

package dev.bitspittle.firebase.auth

import dev.bitspittle.firebase.util.snakeCaseToTitleCamelCase
import kotlinx.coroutines.await

class Auth internal constructor(private val _auth: dev.bitspittle.firebase.externals.auth.Auth) {
    val config = Config(_auth.config)
    val currentUser = _auth.currentUser?.let { User(it) }
    val languageCode = _auth.languageCode
    val name = _auth.name
    val settings = AuthSettings(_auth.settings)

    suspend fun setPersistence(persistence: Persistence) =
        _auth.setPersistence(persistence.wrapped).await()

    suspend fun updateCurrentUser(user: User?) =
        _auth.updateCurrentUser(user?.wrapped).await()

    fun useDeviceLanguage() = _auth.useDeviceLanguage()

    fun onAuthStateChanged(handleStateChanged: (User?) -> Unit) {
        dev.bitspittle.firebase.externals.auth.onAuthStateChanged(_auth) { _user ->
            handleStateChanged(_user?.let { User(it) })
        }
    }

    suspend fun createUserWithEmailAndPassword(email: String, password: String) = UserCredential(
        dev.bitspittle.firebase.externals.auth.createUserWithEmailAndPassword(_auth, email, password).await()
    )

    suspend fun signInWithEmailAndPassword(email: String, password: String) = UserCredential(
        dev.bitspittle.firebase.externals.auth.signInWithEmailAndPassword(_auth, email, password).await()
    )

    suspend fun signInWithPopup(provider: AuthProvider) = UserCredential(
        dev.bitspittle.firebase.externals.auth.signInWithPopup(_auth, provider.wrapped).await()
    )

    suspend fun signOut() = dev.bitspittle.firebase.externals.auth.signOut(_auth).await()
}

class AuthSettings internal constructor(_settings: dev.bitspittle.firebase.externals.auth.AuthSettings) {
    val appVerificationDisabledForTesting = _settings.appVerificationDisabledForTesting
}

// https://firebase.google.com/docs/reference/js/auth.config
 class Config internal constructor(_config: dev.bitspittle.firebase.externals.auth.Config) {
    val apiHost = _config.apiHost
    val apiKey = _config.apiKey
    val apiScheme = _config.apiScheme
    val authDomain = _config.authDomain
    val sdkClientVersion = _config.sdkClientVersion
    val tokenApiHost = _config.tokenApiHost
}

enum class OperationType {
    Link,
    Reauthenticate,
    SignIn;

    companion object {
        internal fun from(type: dynamic) = run {
            val name = type.toString().snakeCaseToTitleCamelCase()
            OperationType.values().first { it.name == name }
        }
    }
}

class Persistence internal constructor(internal val wrapped: dev.bitspittle.firebase.externals.auth.Persistence) {
    val type = PersistenceType.from(wrapped.type)
}

enum class PersistenceType {
    None,
    Local,
    Session;

    companion object {
        internal fun from(type: dynamic) = run {
            val name = type.toString().snakeCaseToTitleCamelCase()
            PersistenceType.values().first { it.name == name }
        }
    }
}

class User internal constructor(internal val wrapped: dev.bitspittle.firebase.externals.auth.User) : UserInfo(wrapped) {
    val emailVerified = wrapped.emailVerified
    val isAnonymous = wrapped.isAnonymous
    val metadata = UserMetadata(wrapped.metadata)
    val providerData = wrapped.providerData.map { UserInfo(it) }.toTypedArray()
    val refreshToken = wrapped.refreshToken
}


class UserCredential internal constructor(_credential: dev.bitspittle.firebase.externals.auth.UserCredential) {
    val operationType = _credential.operationType
    val providerId = _credential.providerId
    val user = User(_credential.user)
}


open class UserInfo internal constructor(_info: dev.bitspittle.firebase.externals.auth.UserInfo) {
    val displayName = _info.displayName
    val email = _info.email
    val phoneNumber = _info.phoneNumber
    val photoURL = _info.photoURL
    val providerId = _info.providerId
    val uid = _info.uid
}

class UserMetadata internal constructor(_metadata: dev.bitspittle.firebase.externals.auth.UserMetadata) {
    val creationTime = _metadata.creationTime
    val lastSignInTime = _metadata.lastSignInTime
}
