@file:Suppress("LocalVariableName") // We use _ prefixed variables to indicate external targets

package dev.bitspittle.firebase.auth

import dev.bitspittle.firebase.util.jsonWithoutNullValues

open class AuthProvider internal constructor(
    internal open val wrapped: dev.bitspittle.firebase.externals.auth.AuthProvider
) {
    val providerId get() = wrapped.providerId
}

// See: https://developers.google.com/identity/openid-connect/openid-connect#authenticationuriparameters
class OAuthCustomParameters(
    val accessType: AccessType? = null,
    val hd: String? = null,
    val includeGrantedScopes: Boolean? = null,
    val loginHint: String? = null,
    val prompt: Prompt? = null,
    val state: String? = null,
) {
    enum class AccessType {
        Offline,
        Online,
    }
    enum class Prompt {
        None,
        Consent,
        SelectAccount,
    }
}

open class FederatedAuthProvider internal constructor(
    override val wrapped: dev.bitspittle.firebase.externals.auth.FederatedAuthProvider
) : AuthProvider(wrapped) {

    fun setCustomParameters(params: OAuthCustomParameters): AuthProvider {
        return AuthProvider(
            wrapped.setCustomParameters(
                jsonWithoutNullValues(
                    "accessType" to params.accessType?.name?.lowercase(),
                    "hd" to params.hd,
                    "includeGrantedScopes" to params.includeGrantedScopes,
                    "loginHint" to params.loginHint,
                    "prompt" to params.prompt?.name?.lowercase(),
                    "state" to params.state,
                )
            )
        )
    }
}

sealed class Scope(internal val key: String) {
    sealed class Google(key: String): Scope("https://www.googleapis.com/auth/$key") {
        object Email : Google("userinfo.email")
        object Profile : Google("userinfo.profile")
    }
}

open class BaseOAuthProvider<S: Scope> internal constructor(
    override val wrapped: dev.bitspittle.firebase.externals.auth.BaseOAuthProvider
) : FederatedAuthProvider(wrapped) {
    fun addScope(scope: S): AuthProvider = AuthProvider(wrapped.addScope(scope.key))
}

 class GoogleAuthProvider internal constructor(
     override val wrapped: dev.bitspittle.firebase.externals.auth.GoogleAuthProvider
 ) : BaseOAuthProvider<Scope.Google>(wrapped) {
     constructor() : this(dev.bitspittle.firebase.externals.auth.GoogleAuthProvider())
 }
