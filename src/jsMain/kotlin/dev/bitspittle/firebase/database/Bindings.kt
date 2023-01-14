@file:Suppress("LocalVariableName") // We use _ prefixed variables to indicate external targets

package dev.bitspittle.firebase.database

import kotlinx.coroutines.await
import kotlin.js.json

class Database internal constructor(private val _db: dev.bitspittle.firebase.externals.database.Database) {
    fun ref(path: String? = null) =
        DatabaseReference(dev.bitspittle.firebase.externals.database.ref(_db, path))
}

abstract class Query internal constructor(private val _query: dev.bitspittle.firebase.externals.database.Query) {
    abstract val ref: DatabaseReference

    override fun equals(other: Any?): Boolean {
        return other is Query && _query.isEqual(other._query)
    }

    override fun hashCode(): Int {
        return _query.hashCode()
    }

    suspend fun get() = DataSnapshot(
        dev.bitspittle.firebase.externals.database.get(_query).await()
    )

    fun query(vararg queryConstraints: QueryConstraint): Query {
        val self = this
        return object : Query(
            dev.bitspittle.firebase.externals.database.query(
                _query,
                queryConstraints.map { it.wrapped }.toTypedArray()
            )
        ) {
            override val ref = self.ref
        }
    }

    override fun toString() = _query.toString()
}


class DatabaseReference internal constructor(
    private val _ref: dev.bitspittle.firebase.externals.database.DatabaseReference
) : Query(_ref) {
    override val ref: DatabaseReference = this

    val key = _ref.key
    val parent = _ref.parent?.let { DatabaseReference(it) }
    val root = DatabaseReference(_ref.root)

    fun child(path: String) = DatabaseReference(dev.bitspittle.firebase.externals.database.child(_ref, path))
    fun push() = DatabaseReference(dev.bitspittle.firebase.externals.database.push(_ref))
    suspend fun remove() = dev.bitspittle.firebase.externals.database.remove(_ref).await()
    suspend fun set(value: Any) =
        dev.bitspittle.firebase.externals.database.set(_ref, value).await()
    suspend fun update(vararg values: Pair<String, Any>) =
        dev.bitspittle.firebase.externals.database.update(_ref, json(*values)).await()

    suspend fun runTransaction(
        transactionUpdate: (currentData: Any) -> Any?,
        options: TransactionOptions? = null
    ) = TransactionResult(
        dev.bitspittle.firebase.externals.database.runTransaction(
            _ref,
            transactionUpdate = { transactionUpdate(it as Any) },
            options?.wrapped
        ).await()
    )
}

class DataSnapshot internal constructor(_snapshot: dev.bitspittle.firebase.externals.database.DataSnapshot) {
    val key = _snapshot.key
    val priority = Priority.from(_snapshot.priority)
    val ref = DatabaseReference(_snapshot.ref)
    val size = _snapshot.size
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

class QueryConstraint internal constructor(internal val wrapped: dev.bitspittle.firebase.externals.database.QueryConstraint) {
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

class TransactionOptions internal constructor(internal val wrapped: dev.bitspittle.firebase.externals.database.TransactionOptions) {
    val applyLocally = wrapped.applyLocally
}

class TransactionResult internal constructor(_result: dev.bitspittle.firebase.externals.database.TransactionResult) {
    val committed = _result.committed
    val snapshot = DataSnapshot(_result.snapshot)
}

object ServerValue {
    fun increment(delta: Number) = dev.bitspittle.firebase.externals.database.increment(delta)
}
