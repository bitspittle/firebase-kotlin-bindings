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

// https://firebase.google.com/docs/reference/js/analytics.analyticssettings
internal external interface AnalyticsSettings {
    val config: dynamic // EventParams | GtagConfigParams
}

// https://firebase.google.com/docs/reference/js/analytics.eventparams
internal external interface EventParams {
    val affiliation: String?
    val checkoutOption: String?
    val checkoutStep: Number?
    val contentType: String?
    val coupon: String?
    val currency: String?
    val description: String?
    val eventCategory: String?
    val eventLabel: String?
    val fatal: Boolean?
    val firebaseScreenClass: String?
    val firebaseScreen: String?
    val itemId: String?
    val itemListId: String?
    val itemListName: String?
    val items: Array<Item>?
    val method: String?
    val number: String?
    val pageLocation: String?
    val pagePath: String?
    val pageTitle: String?
    val paymentType: String?
    val promotionId: String?
    val promotionName: String?
    val promotions: Array<Promotion>?
    val screenName: String?
    val searchTerm: String?
    val shippingTier: String?
    val shipping: dynamic // Currency?
    val tax: dynamic // Currency?
    val transactionId: String?
    val value: Number?
}

// https://firebase.google.com/docs/reference/js/analytics.gtagconfigparams
internal external interface GtagConfigParams {
    val allowAdPersonalizationSignals: Boolean?
    val allowGoogleSignals: Boolean?
    val cookieDomain: String?
    val cookieExpires: Number?
    val cookieFlags: String?
    val cookiePrefix: String?
    val cookieUpdate: Boolean?
    val pageLocation: String?
    val pageTitle: String?
    val sendPageView: Boolean?
}

// https://firebase.google.com/docs/reference/js/analytics.item
internal external interface Item {
    val affiliation: String?
    val coupon: String?
    val creativeName: String?
    val creativeSlot: String?
    val discount: dynamic // Currency?
    val index: Number?
    val itemBrand: String?
    val itemCategory: String?
    val itemCategory2: String?
    val itemCategory3: String?
    val itemCategory4: String?
    val itemCategory5: String?
    val itemId: String?
    val itemListId: String?
    val itemListName: String?
    val itemName: String?
    val itemVariant: String?
    val locationId: String?
    val price: dynamic // Currency?
    val promotionId: String?
    val promotionName: String?
    val quantity: Number?
}

// https://firebase.google.com/docs/reference/js/analytics.promotion
internal external interface Promotion {
    val creativeName: String?
    val creativeSlot: String?
    val id: String?
    val name: String?
}

// https://firebase.google.com/docs/reference/js/analytics#getanalytics
internal external fun getAnalytics(app: FirebaseApp): Analytics

// https://firebase.google.com/docs/reference/js/analytics#initializeanalytics
internal external fun initializeAnalytics(app: FirebaseApp, options: AnalyticsSettings?): Analytics

// https://firebase.google.com/docs/reference/js/analytics#logevent
// Note: There are a bunch of logevent varieties, but we just use a single one here to catch all.
// We'll leave it up to the bindings layer to provide more interesting APIs.
internal external fun logEvent(analytics: Analytics, name: String, params: Json?, options: AnalyticsCallOptions?)

// https://firebase.google.com/docs/reference/js/analytics#setanalyticscollectionenabled
internal external fun setAnalyticsCollectionEnabled(analytics: Analytics, enabled: Boolean)