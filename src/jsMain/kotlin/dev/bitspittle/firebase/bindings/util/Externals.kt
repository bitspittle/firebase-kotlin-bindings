@file:JsModule("firebase/util")
@file:JsNonModule
package dev.bitspittle.firebase.bindings.util

external interface FirebaseError {
    val code: String
    val message: String
}