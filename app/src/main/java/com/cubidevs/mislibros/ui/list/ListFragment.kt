package com.cubidevs.mislibros.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cubidevs.mislibros.databinding.FragmentListBinding
import com.cubidevs.mislibros.model.Book

class ListFragment : Fragment() {

    private lateinit var listBinding: FragmentListBinding
    private lateinit var listViewModel: ListViewModel
    private lateinit var booksAdapter: BooksAdapter
    private var bookList: ArrayList<Book> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        listViewModel = ViewModelProvider(this)[ListViewModel::class.java]
        listBinding = FragmentListBinding.inflate(inflater, container, false)
        val view = listBinding.root

        listViewModel.loadBooks()

        listViewModel.showMsg.observe(viewLifecycleOwner) { msg ->
            showMsg(msg)
        }

        listViewModel.booksList.observe(viewLifecycleOwner) { bookList ->
            booksAdapter.appendItems(bookList)
        }

        booksAdapter = BooksAdapter(bookList,
            onItemClicked = { onBookItemClicked(it) },
            nameClicked = { book ->
                Toast.makeText(context, book.name, Toast.LENGTH_LONG).show()
            },
            authorClicked = { author ->
                Toast.makeText(context, author, Toast.LENGTH_LONG).show()
            }
        )

        listBinding.booksRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ListFragment.requireContext())
            adapter = booksAdapter
            setHasFixedSize(false)
        }

        listBinding.createFloatingActionButton.setOnClickListener {
            findNavController().navigate(ListFragmentDirections.actionNavigationListToNavigationCreate())
        }

        return view
    }

    private fun onBookItemClicked(book: Book) {
        findNavController().navigate(ListFragmentDirections.actionNavigationListToNavigationDetail(book))
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.show()
    }

    private fun showMsg(msg: String?) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_LONG).show()
    }
}