package dev.bitspittle.firebase.auth

class ActionCodeSettings(
    val url: String,
    val handleCodeInApp: Boolean,
    val android: AndroidConfig? = null,
    val iOs: IosConfig? = null,
    val dynamicLinkDomain: String? = null,
) {
    class AndroidConfig(
        val packageName: String,
        val installApp: Boolean? = null,
        val minimumVersion: String? = null,
    )
    class IosConfig(val bundleId: String)
}