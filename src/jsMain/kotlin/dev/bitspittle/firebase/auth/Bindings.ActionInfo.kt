package dev.bitspittle.firebase.auth

import dev.bitspittle.firebase.externals.auth.ActionCodeSettings
import dev.bitspittle.firebase.util.jsonWithoutNullValues

class ActionCodeSettings(
    android: Android,
    dynamicLinkDomain: String?,
    override val handleCodeInApp: Boolean,
    iOS: Ios,
    override val url: String
): ActionCodeSettings {
    class Android(val installApp: Boolean?, val minimumVersion: String?, val packageName: String)
    class Ios(val bundleId: String)

    override val android: dynamic = jsonWithoutNullValues(
        "installApp" to android.installApp,
        "minimumVersion" to android.minimumVersion,
        "packageName" to android.packageName,
    )

    override val dynamicLinkDomain: String? = dynamicLinkDomain ?: undefined // null != undefined

    override val iOS: dynamic = jsonWithoutNullValues(
        "bundleId" to iOS.bundleId
    )
}