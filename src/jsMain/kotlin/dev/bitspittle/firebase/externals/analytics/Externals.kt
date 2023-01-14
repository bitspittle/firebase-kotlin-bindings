@file:JsModule("firebase/analytics")
@file:JsNonModule
package dev.bitspittle.firebase.externals.analytics

import dev.bitspittle.firebase.externals.app.FirebaseApp
import kotlin.js.Json

// https://firebase.google.com/docs/reference/js/analytics.analytics
internal external interface Analytics {
    val app: FirebaseApp
}

// https://firebase.google.com/docs/reference/js/analytics.analyticscalloptions
internal external interface AnalyticsCallOptions {
    val global: Boolean
}

// https://firebase.google.com/docs/reference/js/analytics.md#getanalytics
internal external fun getAnalytics(app: FirebaseApp): Analytics

// https://firebase.google.com/docs/reference/js/analytics.md#logevent
// Note: There are a bunch of logevent varieties, but we just use a single one here to catch all.
// We'll leave it up to the bindings layer to provide more interesting APIs.
internal external fun logEvent(analytics: Analytics, name: String, params: Json?, options: AnalyticsCallOptions?)