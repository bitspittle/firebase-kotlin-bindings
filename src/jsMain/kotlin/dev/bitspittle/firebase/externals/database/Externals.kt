@file:JsModule("firebase/database")
@file:JsNonModule
package dev.bitspittle.firebase.externals.database

import dev.bitspittle.firebase.externals.app.FirebaseApp
import kotlin.js.Json
import kotlin.js.Promise

// https://firebase.google.com/docs/reference/js/database.database
internal external class Database {
    val app: FirebaseApp
}

// https://firebase.google.com/docs/reference/js/database.databasereference
internal external interface DatabaseReference : Query {
    val key: String?
    val parent: DatabaseReference?
    val root: DatabaseReference
}

// https://firebase.google.com/docs/reference/js/database.datasnapshot
internal external class DataSnapshot {
    val key: String?
    val priority: dynamic // String | Number | null
    val ref: DatabaseReference
    val size: Number

    fun child(path: String): DataSnapshot
    fun exists(): Boolean
    fun hasChild(path: String): Boolean
    fun hasChildren(): Boolean
    fun `val`(): Any?
}

// https://firebase.google.com/docs/reference/js/database.query
internal external interface Query {
    val ref: DatabaseReference
}

// https://firebase.google.com/docs/reference/js/database.queryconstraint
internal abstract external class QueryConstraint {
    val type: String // https://firebase.google.com/docs/reference/js/database#queryconstrainttype
}

// https://firebase.google.com/docs/reference/js/database.transactionoptions
internal external interface TransactionOptions {
    val applyLocally: Boolean
}

// https://firebase.google.com/docs/reference/js/database.transactionresult
internal external class TransactionResult {
    val committed: Boolean
    val snapshot: DataSnapshot
}

// https://firebase.google.com/docs/reference/js/database#getdatabase
internal external fun getDatabase(app: FirebaseApp, url: String?): Database


// https://firebase.google.com/docs/reference/js/database#child
internal external fun child(parent: DatabaseReference, path: String): DatabaseReference

// https://firebase.google.com/docs/reference/js/database#get
internal external fun get(query: Query): Promise<DataSnapshot>

// https://firebase.google.com/docs/reference/js/database#increment
internal external fun increment(delta: Number): Json

// https://firebase.google.com/docs/reference/js/database#limittofirst
internal external fun limitToFirst(limit: Number): QueryConstraint

// https://firebase.google.com/docs/reference/js/database#limittolast
internal external fun limitToLast(limit: Number): QueryConstraint

// https://firebase.google.com/docs/reference/js/database#orderbychild
internal external fun orderByChild(path: String): QueryConstraint

// https://firebase.google.com/docs/reference/js/database#push
internal external fun push(ref: DatabaseReference): DatabaseReference

// https://firebase.google.com/docs/reference/js/database#query
internal external fun query(query: Query, vararg queryConstraints: dynamic): Query

// https://firebase.google.com/docs/reference/js/database#ref
internal external fun ref(db: Database, path: String?): DatabaseReference

// https://firebase.google.com/docs/reference/js/database#remove
internal external fun remove(ref: DatabaseReference): Promise<Unit>

// https://firebase.google.com/docs/reference/js/database#runtransaction
internal external fun runTransaction(
    ref: DatabaseReference,
    transactionUpdate: (currentData: dynamic) -> dynamic,
    options: TransactionOptions?
): Promise<TransactionResult>

// https://firebase.google.com/docs/reference/js/database#set
internal external fun set(ref: DatabaseReference, value: dynamic): Promise<Unit>

// https://firebase.google.com/docs/reference/js/database#update
internal external fun update(ref: DatabaseReference, values: Json): Promise<Unit>
