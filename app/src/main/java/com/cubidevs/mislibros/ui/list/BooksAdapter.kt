package com.cubidevs.mislibros.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cubidevs.mislibros.R
import com.cubidevs.mislibros.databinding.CardViewBookItemBinding
import com.cubidevs.mislibros.model.Book
import com.squareup.picasso.Picasso

class BooksAdapter(
    private val bookList: ArrayList<Book>,
    private val onItemClicked: (Book) -> Unit,
    private val nameClicked: (Book) -> Unit,
    private val authorClicked: (String?) -> Unit
) :  RecyclerView.Adapter<BooksAdapter.BookViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_book_item, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
        holder.bind(book,nameClicked, authorClicked)
        holder.itemView.setOnClickListener{ onItemClicked(bookList[position])}
    }

    override fun getItemCount(): Int = bookList.size

    fun appendItems(newList: ArrayList<Book>){
        bookList.clear()
        bookList.addAll(newList)
        notifyDataSetChanged()
    }

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val binding = CardViewBookItemBinding.bind(itemView)

        fun bind(book: Book, nameClicked: (Book) -> Unit, authorClicked: (String?) -> Unit){
            with(binding){
                nameTextView.text = book.name
                authorTextView.text = book.author
                if (book.urlPhoto != null)
                    Picasso.get().load(book.urlPhoto).into(pictureImageView)

                nameTextView.setOnClickListener{
                    nameClicked(book)
                }

                authorTextView.setOnClickListener{
                    authorClicked(book.author)
                }
            }
        }
    }
}