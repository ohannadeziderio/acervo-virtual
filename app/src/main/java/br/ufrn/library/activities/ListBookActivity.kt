package br.ufrn.library.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.ufrn.library.BookAdapter
import br.ufrn.library.R
import br.ufrn.library.domain.Book
import br.ufrn.library.persistence.BookPersistence
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListBookActivity : AppCompatActivity() {

    private lateinit var db: BookPersistence

    private lateinit var recyclerView: RecyclerView
    private lateinit var bookAdapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        recyclerView = findViewById(R.id.bookRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        db = BookPersistence(this)

        val books = db.obterTodos()
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, books.map { it.title })

//        val bookList = listOf(
//            Book(1, "Dom Casmurro", "Machado de Assis", "Romance", "CYGTV", "", ""),
//            Book(2, "O Alienista", "Machado de Assis", "Conto", "CYGT2", "", ""),
//            Book(3, "O Cortiço", "Aluísio Azevedo", "Romance", "CYGT3", "", ""),
//            Book(4, "O Guarani", "José de Alencar", "Romance", "CYGT5", "", ""),
//        )

        bookAdapter = BookAdapter(books)
        recyclerView.adapter = bookAdapter

        adapter.notifyDataSetChanged()

        bookAdapter.setOnItemClickListener { position ->
            val book = books[position]
            val intent = Intent(this, ManagerActivity::class.java).apply {
                putExtra("BOOK_ID", book.id)
                putExtra("BOOK_TITLE", book.title)
                putExtra("BOOK_AUTHOR", book.author)
                putExtra("BOOK_ISBN", book.isbn)
                putExtra("BOOK_PUBLISHER", book.publisher)
                putExtra("BOOK_DESCRIPTION", book.description)
                putExtra("BOOK_URL", book.url)
            }
            startActivity(intent)
        }


        val buttonCreate = findViewById<FloatingActionButton>(R.id.floatingActionButton2)
        buttonCreate.setOnClickListener {
            startActivity(Intent(this, CreateBookActivity::class.java))
        }

        val logoutButton = findViewById<ImageButton>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("is_logged_in", false)
            editor.apply()

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        val books = db.obterTodos()
        bookAdapter.updateBooks(books)
    }
}
