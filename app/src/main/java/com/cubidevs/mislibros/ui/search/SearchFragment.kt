package com.cubidevs.mislibros.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cubidevs.mislibros.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private lateinit var searchBinding: FragmentSearchBinding
    private lateinit var searchViewModel: SearchViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        searchBinding = FragmentSearchBinding.inflate(inflater, container, false)
        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        val root: View = searchBinding.root

        searchViewModel.showMsg.observe(viewLifecycleOwner){msg ->
            showMsg(msg)
        }

        searchViewModel.bookData.observe(viewLifecycleOwner) { bookData ->
            searchBinding.resultTextView.text = bookData
        }

        with(searchBinding) {
            searchButton.setOnClickListener {
                searchViewModel.validateData(bookEditText.text.toString())
            }
        }

        return root
    }

    private fun showMsg(msg: String?) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_LONG).show()
    }
}