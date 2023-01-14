@file:Suppress("LocalVariableName") // We use _ prefixed variables to indicate external targets

package dev.bitspittle.firebase.analytics

import dev.bitspittle.firebase.util.jsonWithoutNullValues
import kotlin.js.Json
import kotlin.js.json

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
    }

    fun log(event: Event, options: AnalyticsCallOptions? = null) {
        dev.bitspittle.firebase.externals.analytics.logEvent(wrapped, event.name, event.toParams(), options?.let { options ->
            object : dev.bitspittle.firebase.externals.analytics.AnalyticsCallOptions {
                override val global = options.global
            }
        })
    }
}

data class AnalyticsCallOptions(val global: Boolean)
