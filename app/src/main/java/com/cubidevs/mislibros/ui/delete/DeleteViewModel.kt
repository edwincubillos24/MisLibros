package com.cubidevs.mislibros.ui.delete

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cubidevs.mislibros.data.BookRepository
import com.cubidevs.mislibros.data.ResourceRemote
import com.cubidevs.mislibros.model.Book
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch

class DeleteViewModel : ViewModel() {

    private val _showMsg: MutableLiveData<String?> = MutableLiveData()
    val showMsg: LiveData<String?> = _showMsg

    private val _bookData: MutableLiveData<String> = MutableLiveData()
    val bookData: LiveData<String> = _bookData

    private val _foundBook: MutableLiveData<Boolean> = MutableLiveData()
    val foundBook: LiveData<Boolean> = _foundBook

    private val _bookDeleted: MutableLiveData<Boolean?> = MutableLiveData()
    val bookDeleted: LiveData<Boolean?> = _bookDeleted

    private val bookRepository = BookRepository()
    private lateinit var foundBookToDelete : Book

    fun validateFields(bookName: String) {
        if(bookName.isEmpty())
            _showMsg.value = "Debe digitar el nombre"
        else {
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
                                    _foundBook.postValue(true)
                                    foundBookToDelete = book
                                }
                            }
                            if (!existBook){
                                _bookData.postValue("Libro no encontrado")
                                _foundBook.postValue(false)
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

    fun deleteBook() {
        viewModelScope.launch{
            val result = bookRepository.deleteBook(foundBookToDelete)
            result.let { resourceRemote ->
                when(resourceRemote) {
                    is ResourceRemote.Success -> {
                        _bookDeleted.postValue(result.data)
                    }
                    is ResourceRemote.Error -> {
                        val msg = result.message
                        _showMsg.postValue(msg)
                    }
                    else -> {
                    //don't use }
                    }
                }

            }

        }

    }
}