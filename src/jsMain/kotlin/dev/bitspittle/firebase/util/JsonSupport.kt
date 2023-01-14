package dev.bitspittle.firebase.util

import kotlin.js.json

internal fun Iterable<Pair<String, Any?>>.filterIfValueIsNull(): List<Pair<String, Any>> {
    @Suppress("UNCHECKED_CAST")
    return this.filter { it.second != null } as List<Pair<String, Any>>
}

internal fun List<Pair<String, Any?>>.toJson() = run {
    json(*this.toTypedArray())
}

/**
 * Create a json object from the incoming pairs after filtering out entries with a null value.
 *
 * There's no guarantee that a Firebase API treats "null" as the same thing as simply not set in the first place. So
 * when we convert from our Kotlin binding classes, which often use null to indicate "not set", we strip those results
 * from the final json passed to Firebase.
 */
internal fun jsonWithoutNullValues(vararg pairs: Pair<String, Any?>) = pairs.toList().filterIfValueIsNull().toJson()