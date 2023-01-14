@file:Suppress("LocalVariableName") // We use _ prefixed variables to indicate external targets

package dev.bitspittle.firebase.app

import dev.bitspittle.firebase.auth.Auth
import dev.bitspittle.firebase.database.Database
import kotlin.js.json

data class FirebaseOptions(
    val apiKey: String,
    val authDomain: String,
    val databaseURL: String,
    val projectId: String,
    val storageBucket: String,
    val messagingSenderId: String,
    val appId: String,
    val measurementId: String? = null,
)

class FirebaseApp private constructor(private val wrapped: dev.bitspittle.firebase.externals.app.FirebaseApp) {
    companion object {
        fun initialize(options: FirebaseOptions, name: String? = null) = FirebaseApp(
            dev.bitspittle.firebase.externals.app.initializeApp(json(
                "apiKey" to options.apiKey,
                "authDomain" to options.authDomain,
                "databaseURL" to options.databaseURL,
                "projectId" to options.projectId,
                "storageBucket" to options.storageBucket,
                "messagingSenderId" to options.messagingSenderId,
                "appId" to options.appId,
                "measurementId" to options.measurementId,
            ),
                name
            )
        )
    }

    fun getDatabase(url: String? = null) = Database(dev.bitspittle.firebase.externals.database.getDatabase(wrapped, url))
    fun getAuth() = Auth(dev.bitspittle.firebase.externals.auth.getAuth(wrapped))
}
