package dev.bitspittle.firebase.util

interface FirebaseError<C: FirebaseError.Code> {
    interface Code {
        val text: String
    }
    val code: C
    val message: String
}