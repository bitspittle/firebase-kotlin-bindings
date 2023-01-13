@file:JsModule("firebase/util")
@file:JsNonModule
package dev.bitspittle.firebase.externals.util

external interface FirebaseError {
    val code: String
    val message: String
}