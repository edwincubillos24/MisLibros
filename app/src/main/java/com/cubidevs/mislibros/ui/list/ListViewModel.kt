package com.cubidevs.mislibros.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cubidevs.mislibros.data.BookRepository
import com.cubidevs.mislibros.data.ResourceRemote
import com.cubidevs.mislibros.model.Book
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch

class ListViewModel : ViewModel() {

    private val bookRepository = BookRepository()

    private var bookList: ArrayList<Book> = ArrayList()

    private val _showMsg: MutableLiveData<String?> = MutableLiveData()
    val showMsg: LiveData<String?> = _showMsg

    private val _booksList: MutableLiveData<ArrayList<Book>> = MutableLiveData()
    val booksList: LiveData<ArrayList<Book>> = _booksList

    fun loadBooks() {
        bookList.clear()
        viewModelScope.launch {
            val result = bookRepository.searchBook()
            result.let { resourceRemote ->
                when (resourceRemote){
                    is ResourceRemote.Success -> {
                        result.data?.documents?.forEach{ document->
                            val book = document.toObject<Book>()
                            book?.let { bookList.add(book) }
                        }
                        _booksList.postValue(bookList)
                    }
                    is ResourceRemote.Error -> {
                        val msg = result.message
                        _showMsg.postValue(msg)
                    }
                    else -> {
                        //don´t use
                    }
                }
            }
        }
    }
}