@file:Suppress("LocalVariableName") // We use _ prefixed variables to indicate external targets

package dev.bitspittle.firebase.auth

import kotlin.js.json

open class AuthProvider internal constructor(
    internal open val wrapped: dev.bitspittle.firebase.externals.auth.AuthProvider
) {
    val providerId = wrapped.providerId
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
        val setValues = listOf(
            "accessType" to params.accessType?.name?.lowercase(),
            "hd" to params.hd,
            "includeGrantedScopes" to params.includeGrantedScopes,
            "loginHint" to params.loginHint,
            "prompt" to params.prompt?.name?.lowercase(),
            "state" to params.state,
        ).filter { it.second != null }

        return AuthProvider(
            wrapped.setCustomParameters(json(*setValues.toTypedArray()))
        )
    }
}

open class BaseOAuthProvider internal constructor(
    override val wrapped: dev.bitspittle.firebase.externals.auth.BaseOAuthProvider
) : FederatedAuthProvider(wrapped) {
    fun addScope(scope: String): AuthProvider = AuthProvider(wrapped.addScope(scope))
    fun getScopes() = wrapped.getScopes()
}

 class GoogleAuthProvider internal constructor(
     override val wrapped: dev.bitspittle.firebase.externals.auth.GoogleAuthProvider
 ) : BaseOAuthProvider(wrapped)
