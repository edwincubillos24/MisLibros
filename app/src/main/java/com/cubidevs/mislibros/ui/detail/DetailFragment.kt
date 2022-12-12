package com.cubidevs.mislibros.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.cubidevs.mislibros.R
import com.cubidevs.mislibros.databinding.FragmentDetailBinding
import com.squareup.picasso.Picasso

class DetailFragment : Fragment() {

    private lateinit var detailBinding: FragmentDetailBinding
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        detailBinding = FragmentDetailBinding.inflate(inflater, container, false)

        val book = args.book

        with(detailBinding){
            nameTextView.text = book.name
            authorTextView.text = book.author
            pagesTextView.text = requireContext().getString(R.string.pages, book.pages.toString())
            if (book.urlPhoto != null)
                Picasso.get().load(book.urlPhoto).into(pictureImageView)
            genreTextView.text = book.genre
        }

        val root = detailBinding.root

        return root
    }
}