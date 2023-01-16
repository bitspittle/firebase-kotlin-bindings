package dev.bitspittle.firebase.database

import kotlinx.coroutines.await
import kotlin.js.json

// See: https://firebase.google.com/docs/database/web/structure-data#how_data_is_structured_its_a_json_tree
private val INVALID_KEY_CHARS = setOf(
    '.',
    '$',
    '#',
    '[',
    ']',
    '/',
)

/**
 * A helper method which encodes a key, ensuring a firebase database can accept it.
 *
 * Note that slashes get encoded! So if you are specifying a full path, e.g. `a/b/c/$key_with_slashes_maybe`, be sure
 * to encode just the trailing part!
 *
 * See also the note about valid keys here:
 *   https://firebase.google.com/docs/database/web/structure-data#how_data_is_structured_its_a_json_tree
 */
fun String.encodeKey(): String {
    var encoded = this
    INVALID_KEY_CHARS.forEach { c ->
        encoded = encoded.replace(c.toString(), "%${c.code.toString(16).uppercase()}")
    }
    return encoded
}

private val HEX_STR_REGEX = Regex("""%([a-fA-F0-9]{2})""")

/**
 * A helper method which decodes a value encoded by [encodeKey].
 */
fun String.decodeKey(): String {
    return this.replace(HEX_STR_REGEX) { result ->
        Char(result.groupValues[1].toInt(16)).toString()
    }
}

class Database internal constructor(private val wrapped: dev.bitspittle.firebase.externals.database.Database) {
    fun ref(path: String? = null) =
        DatabaseReference(dev.bitspittle.firebase.externals.database.ref(wrapped, path))
}

open class Query internal constructor(
    private val wrapped: dev.bitspittle.firebase.externals.database.Query
) {
    // Note: Must be get() or else you end up with infinite recursion, since initializing a DatabaseReference
    // also initialized a Query, which initialized a DatabaseReference....
    val ref get() = DatabaseReference(wrapped.ref)

    suspend fun get() = DataSnapshot(
        dev.bitspittle.firebase.externals.database.get(wrapped).await()
    )

    fun query(vararg constraints: QueryConstraint): Query {
        return Query(
            dev.bitspittle.firebase.externals.database.query(
                wrapped,
                constraints.map { it.wrapped }.toTypedArray()
            )
        )
    }
}

class DatabaseReference internal constructor(
    private val wrapped: dev.bitspittle.firebase.externals.database.DatabaseReference
) : Query(wrapped) {
    val key get() = wrapped.key
    val parent get() = wrapped.parent?.let { DatabaseReference(it) }
    val root get() = DatabaseReference(wrapped.root)

    fun child(path: String) = DatabaseReference(dev.bitspittle.firebase.externals.database.child(wrapped, path))
    fun push() = DatabaseReference(dev.bitspittle.firebase.externals.database.push(wrapped))
    suspend fun remove() = dev.bitspittle.firebase.externals.database.remove(wrapped).await()
    suspend fun set(value: Any) =
        dev.bitspittle.firebase.externals.database.set(wrapped, value).await()
    suspend fun update(vararg values: Pair<String, Any>) =
        dev.bitspittle.firebase.externals.database.update(wrapped, json(*values)).await()

    suspend fun runTransaction(
        transactionUpdate: (currentData: Any) -> Any?,
        options: TransactionOptions? = null
    ) = TransactionResult(
        dev.bitspittle.firebase.externals.database.runTransaction(
            wrapped,
            transactionUpdate = { transactionUpdate(it as Any) },
            options?.wrapped
        ).await()
    )
}

class DataSnapshot internal constructor(
    private val wrapped: dev.bitspittle.firebase.externals.database.DataSnapshot
) {
    val key get() = wrapped.key
    val priority  get() = Priority.from(wrapped.priority)
    val ref get() = DatabaseReference(wrapped.ref)
    val size get() = wrapped.size

    fun child(path: String) = DataSnapshot(wrapped.child(path))
    fun exists() = wrapped.exists()
    fun hasChild(path: String) = wrapped.hasChild(path)
    fun hasChildren() = wrapped.hasChildren()
    fun `val`() = wrapped.`val`()
}

/**
 * A convenience method that you can call instead of [DataSnapshot.val].
 *
 * Since `val` is a reserved keyword in Kotlin, making its syntax a little strange.
 */
fun DataSnapshot.value() = `val`()

sealed interface Priority {
    companion object {
        internal fun from(priority: dynamic): Priority? {
            return when (priority) {
                null -> null
                is Number -> OfNumber(priority)
                is String -> OfString(priority)
                else -> error("Unexpected priority value: $priority")
            }
        }
    }

    val value: Any

    class OfNumber(override val value: Number) : Priority
    class OfString(override val value: String) : Priority
}

// Note: Can't be a class because it's used as a vararg parameter
class QueryConstraint internal constructor(
    internal val wrapped: dev.bitspittle.firebase.externals.database.QueryConstraint
) {
    companion object {
        fun limitToFirst(limit: Number) = QueryConstraint(dev.bitspittle.firebase.externals.database.limitToFirst(limit))
        fun limitToLast(limit: Number) = QueryConstraint(dev.bitspittle.firebase.externals.database.limitToLast(limit))
        fun orderByChild(path: String) = QueryConstraint(dev.bitspittle.firebase.externals.database.orderByChild(path))
    }

    val type get() = QueryConstraintType.from(wrapped.type)
}

enum class QueryConstraintType {
    EndAt,
    EndBefore,
    StartAt,
    StartAfter,
    LimitToFirst,
    LimitToLast,
    OrderByChild,
    OrderByKey,
    OrderByPriority,
    OrderByValue,
    EqualTo;

    companion object {
        internal fun from(typeStr: String) = run {
            val name = typeStr.capitalize()
            QueryConstraintType.values().first { it.name == name }
        }
    }
}

class TransactionOptions internal constructor(
    internal val wrapped: dev.bitspittle.firebase.externals.database.TransactionOptions
) {
    val applyLocally get() = wrapped.applyLocally
}

class TransactionResult internal constructor(
    internal val wrapped: dev.bitspittle.firebase.externals.database.TransactionResult
) {
    val committed get() = wrapped.committed
    val snapshot get() = DataSnapshot(wrapped.snapshot)
}

object ServerValue {
    fun increment(delta: Number) = dev.bitspittle.firebase.externals.database.increment(delta)
    fun timestamp() = dev.bitspittle.firebase.externals.database.serverTimestamp()
}
