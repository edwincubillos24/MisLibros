package com.cubidevs.mislibros.data

import android.util.Log
import com.cubidevs.mislibros.model.Book
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class BookRepository {

    private var db = Firebase.firestore
    private var auth: FirebaseAuth = Firebase.auth

    suspend fun createBook(book: Book): ResourceRemote<String?> {
        return try {
            val path = auth.uid?.let { db.collection("users").document(it).collection("books") }
            val documentBook = path?.document()
            book.id = documentBook?.id
            documentBook?.id?.let { path.document(it).set(book).await() }
            ResourceRemote.Success(data = book.id)
        } catch (e: FirebaseFirestoreException) {
            Log.e("createBook", e.localizedMessage)
            ResourceRemote.Error(message = e.localizedMessage)
        } catch (e: FirebaseNetworkException) {
            Log.e("FirebaseNetwork", e.localizedMessage)
            ResourceRemote.Error(message = e.localizedMessage)
        }
    }

    suspend fun searchBook(): ResourceRemote<QuerySnapshot?> {
        return try {
            val docRef = auth.uid?.let { db.collection("users").document(it).collection("books") }
            val result = docRef?.get()?.await()
            ResourceRemote.Success(data = result)
        } catch (e: FirebaseFirestoreException) {
            ResourceRemote.Error(message = e.localizedMessage)
        } catch (e: FirebaseNetworkException) {
            ResourceRemote.Error(message = e.localizedMessage)
        }
    }

    suspend fun deleteBook(book: Book): ResourceRemote<Boolean> {
        return try {
            val docRef = auth.uid?.let { book.id?.let { it1 -> db.collection("users").document(it).collection("books").document(it1) } }
            auth.uid?.let { Log.d("uid", it) }
            book.id?.let { Log.d("bookID", it) }
            docRef?.delete()?.await()
            ResourceRemote.Success(data = true)
        } catch (e: FirebaseFirestoreException) {
            ResourceRemote.Error(message = e.localizedMessage)
        } catch (e: FirebaseNetworkException) {
            ResourceRemote.Error(message = e.localizedMessage)
        }

    }
}