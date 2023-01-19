package dev.bitspittle.firebase.analytics

import dev.bitspittle.firebase.util.jsonWithoutNullValues
import kotlin.js.Json

class Analytics internal constructor(private val wrapped: dev.bitspittle.firebase.externals.analytics.Analytics) {
    sealed class Event(val name: String) {
        internal abstract fun toParams(): Json

        /** Represents a screen within an app; one page might host multiple screens */
        // https://firebase.google.com/docs/reference/js/analytics#logevent_3
        class PageView(private val title: String? = null, private val location: String? = null, private val path: String? = null) : Event("page_view") {
            override fun toParams() = jsonWithoutNullValues(
                "title" to title,
                "location" to location,
                "path" to path,
            )
        }

        // region games

        // https://developers.google.com/analytics/devguides/collection/ga4/reference/events?client_type=gtag#level_start
        class LevelStart(private val levelName: String? = null) : Event("level_start") {
            override fun toParams() = jsonWithoutNullValues(
                "level_name" to levelName,
            )
        }

        // https://developers.google.com/analytics/devguides/collection/ga4/reference/events?client_type=gtag#level_start
        class LevelEnd(private val levelName: String? = null, private val success: Boolean? = null) : Event("level_end") {
            override fun toParams() = jsonWithoutNullValues(
                "level_name" to levelName,
                "success" to success,
            )
        }

        // endregion
    }

    fun log(event: Event, options: AnalyticsCallOptions? = null) {
        dev.bitspittle.firebase.externals.analytics.logEvent(
            wrapped,
            event.name,
            event.toParams(),
            @Suppress("NAME_SHADOWING")
            options?.let { options ->
            object : dev.bitspittle.firebase.externals.analytics.AnalyticsCallOptions {
                override val global get() = options.global
            }
        })
    }

    fun setAnalyticsCollectionEnabled(enabled: Boolean) {
        dev.bitspittle.firebase.externals.analytics.setAnalyticsCollectionEnabled(wrapped, enabled)
    }
}

data class AnalyticsCallOptions(val global: Boolean)

// TODO: Support passing in EventParams as well.
class AnalyticsSettings(internal val gtagParams: GtagConfigParams = GtagConfigParams())

// https://firebase.google.com/docs/reference/js/analytics.gtagconfigparams
data class GtagConfigParams(
    val allowAdPersonalizationSignals: Boolean? = null,
    val allowGoogleSignals: Boolean? = null,
    val cookieDomain: String? = null,
    val cookieExpires: Number? = null,
    val cookieFlags: String? = null,
    val cookiePrefix: String? = null,
    val cookieUpdate: Boolean? = null,
    val pageLocation: String? = null,
    val pageTitle: String? = null,
    val sendPageView: Boolean? = null,
) {
    internal fun toJson() = jsonWithoutNullValues(
        "allow_ad_personalization_signals" to allowAdPersonalizationSignals,
        "allow_google_signals" to allowGoogleSignals,
        "cookie_domain" to cookieDomain,
        "cookie_expires" to cookieExpires,
        "cookie_flags" to cookieFlags,
        "cookie_prefix" to cookiePrefix,
        "cookie_update" to cookieUpdate,
        "page_location" to pageLocation,
        "page_title" to pageTitle,
        "send_page_view" to sendPageView,
    )
}
