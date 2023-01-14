@file:Suppress("LocalVariableName") // We use _ prefixed variables to indicate external targets

package dev.bitspittle.firebase.auth

import kotlin.js.json

abstract class AuthProvider internal constructor(internal val wrapped: dev.bitspittle.firebase.externals.auth.AuthProvider) {
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

abstract class FederatedAuthProvider internal constructor(
    private val _provider: dev.bitspittle.firebase.externals.auth.FederatedAuthProvider
) : AuthProvider(_provider) {
    fun setCustomParameters(params: OAuthCustomParameters): AuthProvider {
        val setValues = listOf(
            "accessType" to params.accessType?.name?.lowercase(),
            "hd" to params.hd,
            "includeGrantedScopes" to params.includeGrantedScopes,
            "loginHint" to params.loginHint,
            "prompt" to params.prompt?.name?.lowercase(),
            "state" to params.state,
        ).filter { it.second != null }

        return object : AuthProvider(_provider.setCustomParameters(json(*setValues.toTypedArray()))) {}
    }
}

abstract class BaseOAuthProvider internal constructor(
    private val _provider: dev.bitspittle.firebase.externals.auth.BaseOAuthProvider
): FederatedAuthProvider(_provider) {
    fun addScope(scope: String) = object : AuthProvider(_provider.addScope(scope)) {}
    fun getScopes() = _provider.getScopes()
}


// https://firebase.google.com/docs/reference/js/auth.googleauthprovider
 class GoogleAuthProvider internal constructor(
     private val _provider: dev.bitspittle.firebase.externals.auth.GoogleAuthProvider
 ) : BaseOAuthProvider(_provider)

