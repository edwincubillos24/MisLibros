package com.cubidevs.mislibros.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cubidevs.mislibros.R
import com.cubidevs.mislibros.databinding.FragmentCreateBinding

class CreateFragment : Fragment() {

    private lateinit var createViewModel: CreateViewModel
    private lateinit var createBinding: FragmentCreateBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        createBinding = FragmentCreateBinding.inflate(inflater, container, false)
        createViewModel = ViewModelProvider(this)[CreateViewModel::class.java]
        val view = createBinding.root

        val items = listOf("Aventura", "Romantica", "Suspenso", "Terror")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        createBinding.genreAutoCompleteTextView.setAdapter(adapter)

        createViewModel.showMsg.observe(viewLifecycleOwner) { msg -> showMsg(msg) }

        createViewModel.createBookSuccess.observe(viewLifecycleOwner) {

        }

        with(createBinding) {
            createButton.setOnClickListener {
                createViewModel.validateFields(
                    nameEditText.text.toString(),
                    authorEditText.text.toString(),
                    genreTextInputLayout.editText?.text.toString(),
                    pagesNumberEditText.text.toString().toInt()
                )
            }
        }

        return view
    }

    private fun showMsg(msg: String?) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_LONG).show()
    }

}
