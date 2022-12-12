package com.cubidevs.mislibros.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cubidevs.mislibros.data.ResourceRemote
import com.cubidevs.mislibros.data.UserRepository
import com.cubidevs.mislibros.model.User
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {

    private val userRepository = UserRepository()

    private lateinit var user: User

    private val _errorMsg: MutableLiveData<String?> = MutableLiveData()
    val errorMsg: LiveData<String?> = _errorMsg

    private val _registerSuccess: MutableLiveData<String?> = MutableLiveData()
    val registerSucces: LiveData<String?> = _registerSuccess

    fun validateFields(email: String, password: String, repPassword: String, name: String, phone: String) {
        if (email.isEmpty() || password.isEmpty() || repPassword.isEmpty() || name.isEmpty() || phone.isEmpty())
            _errorMsg.value = "Debe digitar todos los campos"
        else
            if (password != repPassword)
                _errorMsg.value = "Las contraseñas deben de ser iguales"
            else
                if (password.length < 6)
                    _errorMsg.value = "La contraseña debe tener mínimo 6 caracteres"
                else
                    viewModelScope.launch {
                        val result = userRepository.registerUser(email, password)
                        result.let { resourceRemote ->
                            when (resourceRemote) {
                                is ResourceRemote.Success -> {
                                    user = User(result.data, name, email, phone)
                                    createUser(user)
                                }
                                is ResourceRemote.Error -> {
                                    var msg = result.message
                                    when (result.message) {
                                        "The email address is already in use by another account." -> msg = "Ya existe una cuenta con ese correo electrónico"
                                        "The email address is badly formatted." -> msg = "El email esta mal escrito"
                                        "A network error (such as timeout, interrupted connection or unreachable host) has occurred." -> msg = "Revise su conexión de Internet"
                                    }
                                    _errorMsg.postValue(msg)
                                }
                                else -> {
                                    //don´t use
                                }
                            }
                        }
                    }
    }

    private fun createUser(user: User) {
        viewModelScope.launch {
            val result = userRepository.createUser(user)
            result.let { resourceRemote ->
                sequenceOf(
                    when (resourceRemote) {
                        is ResourceRemote.Success -> {
                            _registerSuccess.postValue(result.data)
                            _errorMsg.postValue("Registro Exitoso")
                        }
                        is ResourceRemote.Error -> {
                            val msg = result.message
                            _errorMsg.postValue(msg)
                        }
                        else -> {
                            // dont´t use
                        }
                    }
                )
            }
        }
    }
}