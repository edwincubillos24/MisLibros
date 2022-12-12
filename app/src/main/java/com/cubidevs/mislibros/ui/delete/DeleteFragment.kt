package com.cubidevs.mislibros.ui.delete

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cubidevs.mislibros.R
import com.cubidevs.mislibros.databinding.FragmentDeleteBinding

class DeleteFragment : Fragment() {

    private lateinit var deleteBinding: FragmentDeleteBinding
    private lateinit var deleteViewModel: DeleteViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?
    ): View {
        deleteBinding = FragmentDeleteBinding.inflate(inflater, container, false)
        deleteViewModel = ViewModelProvider(this)[DeleteViewModel::class.java]

        deleteViewModel.showMsg.observe(viewLifecycleOwner){ msg->
            showMsg(msg)
        }

        deleteViewModel.bookData.observe(viewLifecycleOwner) { bookData ->
            deleteBinding.bookDataTextView.text = bookData
        }

        deleteViewModel.foundBook.observe(viewLifecycleOwner){ foundBook ->
            deleteBinding.deleteButton.visibility = if (foundBook) View.VISIBLE else View.GONE
        }

        deleteViewModel.bookDeleted.observe(viewLifecycleOwner) { deletedBook ->
            deleteBinding.deleteButton.visibility = if (deletedBook == true) View.GONE else View.VISIBLE
            deleteBinding.bookEditText.setText("")
        }

        with(deleteBinding){
            searchButton.setOnClickListener {
                deleteViewModel.validateFields(bookEditText.text.toString())
            }

            deleteButton.setOnClickListener {
                deleteViewModel.deleteBook()
            }
        }

        val root = deleteBinding.root
        return root
    }

    private fun showMsg(msg: String?) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_LONG).show()
    }


}