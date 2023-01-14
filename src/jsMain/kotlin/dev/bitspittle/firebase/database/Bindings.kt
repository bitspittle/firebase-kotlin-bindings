@file:Suppress("LocalVariableName") // We use _ prefixed variables to indicate external targets

package dev.bitspittle.firebase.database

import kotlinx.coroutines.await
import kotlin.js.json

class Database internal constructor(private val wrapped: dev.bitspittle.firebase.externals.database.Database) {
    fun ref(path: String? = null) =
        DatabaseReference(dev.bitspittle.firebase.externals.database.ref(wrapped, path))
}

open class Query internal constructor(
    private val wrapped: dev.bitspittle.firebase.externals.database.Query
) {
    val ref = DatabaseReference(wrapped.ref)

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
    val key = wrapped.key
    val parent = wrapped.parent?.let { DatabaseReference(it) }
    val root = DatabaseReference(wrapped.root)

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
    val key = wrapped.key
    val priority  = Priority.from(wrapped.priority)
    val ref = DatabaseReference(wrapped.ref)
    val size = wrapped.size
}

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

    val type = QueryConstraintType.from(wrapped.type)
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
    val applyLocally = wrapped.applyLocally
}

class TransactionResult internal constructor(
    internal val wrapped: dev.bitspittle.firebase.externals.database.TransactionResult
) {
    val committed = wrapped.committed
    val snapshot = DataSnapshot(wrapped.snapshot)
}

object ServerValue {
    fun increment(delta: Number) = dev.bitspittle.firebase.externals.database.increment(delta)
}
