package dev.bitspittle.firebase.auth

import dev.bitspittle.firebase.util.FirebaseError
import dev.bitspittle.firebase.util.snakeCaseToTitleCamelCase
import kotlinx.coroutines.await

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
private inline fun <T> runUnsafe(block: () -> T): T {
    try {
        return block()
    } catch (e: Throwable) {
        (e as? dev.bitspittle.firebase.externals.auth.AuthError)?.let { throw AuthError(it) }
        throw e
    }
}

class Auth internal constructor(private val wrapped: dev.bitspittle.firebase.externals.auth.Auth) {
    val config get() = Config(wrapped.config)
    val currentUser get() = wrapped.currentUser?.let { User(it) }
    val languageCode get() = wrapped.languageCode
    val name get() = wrapped.name
    val settings get() = AuthSettings(wrapped.settings)

    suspend fun setPersistence(persistence: Persistence) =
        wrapped.setPersistence(persistence.wrapped).await()

    suspend fun updateCurrentUser(user: User?) =
        wrapped.updateCurrentUser(user?.wrapped).await()

    fun useDeviceLanguage() = wrapped.useDeviceLanguage()

    fun onAuthStateChanged(handleStateChanged: (User?) -> Unit) {
        dev.bitspittle.firebase.externals.auth.onAuthStateChanged(wrapped) { user ->
            handleStateChanged(user?.let { User(it) })
        }
    }

    suspend fun createUserWithEmailAndPassword(email: String, password: String) = runUnsafe {
        UserCredential(
            dev.bitspittle.firebase.externals.auth.createUserWithEmailAndPassword(wrapped, email, password).await()
        )
    }

    suspend fun signInWithEmailAndPassword(email: String, password: String) = runUnsafe {
        UserCredential(
            dev.bitspittle.firebase.externals.auth.signInWithEmailAndPassword(wrapped, email, password).await()
        )
    }

    suspend fun signInWithPopup(provider: AuthProvider) = runUnsafe {
        UserCredential(
            dev.bitspittle.firebase.externals.auth.signInWithPopup(wrapped, provider.wrapped).await()
        )
    }

    suspend fun signOut() = dev.bitspittle.firebase.externals.auth.signOut(wrapped).await()
}

class AuthSettings internal constructor(
    internal val wrapped: dev.bitspittle.firebase.externals.auth.AuthSettings
) {
    val appVerificationDisabledForTesting get() = wrapped.appVerificationDisabledForTesting
}

class AuthError internal constructor(
    private val wrapped: dev.bitspittle.firebase.externals.auth.AuthError
) : Exception(), FirebaseError {
    val customData get() = wrapped.customData
    override val code get() = wrapped.code
    override val message get() = wrapped.message
}

// https://firebase.google.com/docs/reference/js/auth.config
 class Config internal constructor(
    internal val wrapped: dev.bitspittle.firebase.externals.auth.Config
 ) {
    val apiHost get() = wrapped.apiHost
    val apiKey get() = wrapped.apiKey
    val apiScheme get() = wrapped.apiScheme
    val authDomain get() = wrapped.authDomain
    val sdkClientVersion get() = wrapped.sdkClientVersion
    val tokenApiHost get() = wrapped.tokenApiHost
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
    val type get() = PersistenceType.from(wrapped.type)
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

class User internal constructor(
    override val wrapped: dev.bitspittle.firebase.externals.auth.User
) : UserInfo(wrapped) {
    val emailVerified get() = wrapped.emailVerified
    val isAnonymous get() = wrapped.isAnonymous
    val metadata get() = UserMetadata(wrapped.metadata)
    val providerData get() = wrapped.providerData.map { UserInfo(it) }.toTypedArray()
    val refreshToken get() = wrapped.refreshToken

    suspend fun delete() =
        dev.bitspittle.firebase.externals.auth.deleteUser(wrapped).await()

    suspend fun sendEmailVerification() =
        dev.bitspittle.firebase.externals.auth.sendEmailVerification(wrapped).await()
}

class UserCredential internal constructor(
    internal val wrapped: dev.bitspittle.firebase.externals.auth.UserCredential
) {
    val operationType get() = wrapped.operationType
    val providerId get() = wrapped.providerId
    val user get() = User(wrapped.user)
}

open class UserInfo internal constructor(
    internal open val wrapped: dev.bitspittle.firebase.externals.auth.UserInfo
) {
    val displayName get() = wrapped.displayName
    val email get() = wrapped.email
    val phoneNumber get() = wrapped.phoneNumber
    val photoURL get() = wrapped.photoURL
    val providerId get() = wrapped.providerId
    val uid get() = wrapped.uid
}

class UserMetadata internal constructor(
    internal val wrapped: dev.bitspittle.firebase.externals.auth.UserMetadata
) {
    val creationTime get() = wrapped.creationTime
    val lastSignInTime get() = wrapped.lastSignInTime
}
