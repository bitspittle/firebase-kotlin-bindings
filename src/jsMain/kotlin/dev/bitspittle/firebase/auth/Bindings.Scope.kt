@file:Suppress("LocalVariableName") // We use _ prefixed variables to indicate external targets

package dev.bitspittle.firebase.auth

sealed class Scope(internal val key: String) {
    companion object {
        private val scopeMapping = mutableMapOf<String, Scope>()
        fun from(key: String) = scopeMapping.getValue(key)
    }

    init {
        @Suppress("LeakingThis") // This use of it is OK
        scopeMapping[key] = this
    }

    sealed class Google(key: String): Scope("https://www.googleapis.com/auth/$key") {
        object Email : Google("userinfo.email")
        object Profile : Google("userinfo.profile")
    }
}
