package br.ufrn.library

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.ufrn.library.domain.Book
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class BookAdapter(private var bookList: List<Book>) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
        holder.bind(book)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(position)
        }
    }

    override fun getItemCount(): Int = bookList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateBooks(books: List<Book>) {
        bookList = books
        notifyDataSetChanged()
    }

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val titleTextView: TextView = itemView.findViewById(R.id.bookTitle)
        private val authorTextView: TextView = itemView.findViewById(R.id.bookAuthor)
        private val coverImageView: ImageView = itemView.findViewById(R.id.bookCover)

        fun bind(book: Book) {
            titleTextView.text = book.title
            authorTextView.text = book.author

            val requestOptions = RequestOptions()
                .placeholder(R.drawable.loading_placeholder)
                .error(R.drawable.default_cover)

            Glide.with(itemView.context)
                .load(book.url)
                .apply(requestOptions)
                .into(coverImageView)
        }
    }
}