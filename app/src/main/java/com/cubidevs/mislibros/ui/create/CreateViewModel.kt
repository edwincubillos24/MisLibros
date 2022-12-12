package com.cubidevs.mislibros.ui.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cubidevs.mislibros.data.BookRepository
import com.cubidevs.mislibros.data.ResourceRemote
import com.cubidevs.mislibros.model.Book
import kotlinx.coroutines.launch

class CreateViewModel : ViewModel() {

    private val bookRepository = BookRepository()

    private val _showMsg: MutableLiveData<String?> = MutableLiveData()
    val showMsg: LiveData<String?> = _showMsg

    private val _createBookSuccess: MutableLiveData<String?> = MutableLiveData()
    val createBookSuccess: LiveData<String?> = _createBookSuccess

    fun validateFields(name: String, author: String, genre: String, pages: Int) {
        if (name.isEmpty() || author.isEmpty() || genre.isEmpty() || pages.toString().isEmpty())
            _showMsg.postValue("Debe digitar todos los campos")
        else
            viewModelScope.launch {
                val book = Book(name = name, author = author, genre = genre, pages = pages)
                val result = bookRepository.createBook(book)
                result.let { resourceRemote ->
                    when (resourceRemote) {
                        is ResourceRemote.Success -> {
                            _createBookSuccess.postValue(result.data)
                            _showMsg.postValue("Libro Creado")
                        }
                        is ResourceRemote.Error -> {
                            val msg = result.message
                            _showMsg.postValue(msg)
                        }
                        else -> {
                            //don't use
                        }
                    }
                }
            }
    }
}