@file:JsModule("firebase/util")
@file:JsNonModule
package dev.bitspittle.firebase.externals.util

internal external interface FirebaseError {
    val code: String
    val message: String
}