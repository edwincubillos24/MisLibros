package com.cubidevs.mislibros.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cubidevs.mislibros.data.BookRepository
import com.cubidevs.mislibros.data.ResourceRemote
import com.cubidevs.mislibros.model.Book
import com.cubidevs.mislibros.model.User
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val bookRepository = BookRepository()

    private val _showMsg: MutableLiveData<String?> = MutableLiveData()
    val showMsg: LiveData<String?> = _showMsg

    private val _bookData: MutableLiveData<String> = MutableLiveData()
    val bookData: LiveData<String> = _bookData

    fun validateData(bookName: String) {
        if (bookName.isEmpty())
            _showMsg.value = "Debe digitar el nombre del libro"
        else{
            viewModelScope.launch {
                val result = bookRepository.searchBook()
                result.let { resourceRemote ->
                    when (resourceRemote){
                        is ResourceRemote.Success -> {
                            var existBook = false
                            result.data?.documents?.forEach { document ->
                                val book = document.toObject<Book>()
                                if (bookName == book?.name) {
                                    _bookData.postValue("Nombre: " + book.name + "\nAutor: " + book.author + "\nGénero: " + book.genre)
                                    existBook = true
                                }
                            }
                            if (!existBook){
                                _bookData.postValue("Libro no encontrado")
                            }
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

}