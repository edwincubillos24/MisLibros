package com.cubidevs.mislibros.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.cubidevs.mislibros.databinding.FragmentLogInBinding

class LogInFragment : Fragment() {

    private lateinit var logInBinding: FragmentLogInBinding
    private lateinit var logInViewModel: LogInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        logInBinding = FragmentLogInBinding.inflate(inflater, container, false)
        logInViewModel = ViewModelProvider(this)[LogInViewModel::class.java]

        logInViewModel.errorMsg.observe(viewLifecycleOwner){ msg->
            showErrorMessage(msg)
        }

        logInViewModel.loginSuccess.observe(viewLifecycleOwner){
            goToList()
        }

        with(logInBinding) {
            logInButton.setOnClickListener {
                logInViewModel.validateFields(emalEditText.text.toString(), passwordEditText.text.toString())
            }

            signUpTextView.setOnClickListener {
                findNavController().navigate(LogInFragmentDirections.actionNavigationLoginToNavigationSignup())
            }
        }

        return logInBinding.root
    }

    private fun showErrorMessage(msg: String?) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_LONG ).show()
    }

    fun goToList(){
        findNavController().navigate(LogInFragmentDirections.actionNavigationLoginToNavigationList())
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.hide()
    }

}