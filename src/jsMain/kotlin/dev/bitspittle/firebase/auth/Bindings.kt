package dev.bitspittle.firebase.auth

import dev.bitspittle.firebase.util.jsonWithoutNullValues
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

    fun isSignInLink(emailLink: String) = runUnsafe {
        dev.bitspittle.firebase.externals.auth.isSignInWithEmailLink(wrapped, emailLink)
    }

    suspend fun sendSignInLinkToEmail(email: String, actionCodeSettings: ActionCodeSettings) = runUnsafe {
        dev.bitspittle.firebase.externals.auth.sendSignInLinkToEmail(
            auth = wrapped,
            email = email,
            actionCodeSettings = object : dev.bitspittle.firebase.externals.auth.ActionCodeSettings {
                override val android: dynamic = actionCodeSettings.android?.let { android ->
                    jsonWithoutNullValues(
                        "installApp" to android.installApp,
                        "minimumVersion" to android.minimumVersion,
                        "packageName" to android.packageName,
                    )
                } ?: undefined
                override val dynamicLinkDomain: String? = actionCodeSettings.dynamicLinkDomain ?: undefined
                override val handleCodeInApp: Boolean = actionCodeSettings.handleCodeInApp
                override val iOS: dynamic = actionCodeSettings.iOs?.let { ios ->
                    jsonWithoutNullValues("bundleId" to ios.bundleId)
                } ?: undefined
                override val url: String = actionCodeSettings.url
            }
        ).await()
    }

    suspend fun signInWithEmailAndPassword(email: String, password: String) = runUnsafe {
        UserCredential(
            dev.bitspittle.firebase.externals.auth.signInWithEmailAndPassword(wrapped, email, password).await()
        )
    }

    suspend fun signInWithEmailLink(email: String, emailLink: String?) = runUnsafe {
        UserCredential(
            dev.bitspittle.firebase.externals.auth.signInWithEmailLink(wrapped, email, emailLink).await()
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

    /**
     * Returns a JSON Web Token (JWT) used to identify the user to a Firebase service.
     * Returns the current token if it has not expired. Otherwise, this will refresh the token and return a new one.
     */
    suspend fun getIdToken(forceRefresh: Boolean? = null) = wrapped.getIdToken(forceRefresh).await()

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
