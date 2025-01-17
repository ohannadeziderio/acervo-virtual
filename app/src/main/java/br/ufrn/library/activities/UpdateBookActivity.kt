package br.ufrn.library.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.ufrn.library.R
import br.ufrn.library.domain.Book
import br.ufrn.library.persistence.BookPersistence

class UpdateBookActivity : AppCompatActivity() {

    private lateinit var db: BookPersistence
    private var bookId: Int? = null
    private lateinit var editTextTitulo: EditText
    private lateinit var editTextAutor: EditText
    private lateinit var editTextISBN: EditText
    private lateinit var editTextEditora: EditText
    private lateinit var editTextDescricao: EditText
    private lateinit var editTextURL: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atualizar)

        db = BookPersistence(this)

        editTextTitulo = findViewById(R.id.editTextTitulo)
        editTextAutor = findViewById(R.id.editTextAutor)
        editTextISBN = findViewById(R.id.editTextISBN)
        editTextEditora = findViewById(R.id.editTextEditora)
        editTextDescricao = findViewById(R.id.editTextDescricao)
        editTextURL = findViewById(R.id.editTextURL)

        intent?.let {
            bookId = it.getIntExtra("BOOK_ID", -1).takeIf { id -> id != -1 }
            editTextTitulo.setText(it.getStringExtra("BOOK_TITLE"))
            editTextAutor.setText(it.getStringExtra("BOOK_AUTHOR"))
            editTextISBN.setText(it.getStringExtra("BOOK_ISBN"))
            editTextEditora.setText(it.getStringExtra("BOOK_PUBLISHER"))
            editTextDescricao.setText(it.getStringExtra("BOOK_DESCRIPTION"))
            editTextURL.setText(it.getStringExtra("BOOK_URL"))
        }

        val buttonAtualizar = findViewById<Button>(R.id.buttonAtualizar)
        buttonAtualizar.setOnClickListener {
            val book = Book(
                id = bookId,
                title = editTextTitulo.text.toString(),
                author = editTextAutor.text.toString(),
                isbn = editTextISBN.text.toString(),
                publisher = editTextEditora.text.toString(),
                description = editTextDescricao.text.toString(),
                url = editTextURL.text.toString()
            )

            db.atualizar(book)

            Toast.makeText(this, "Atualizando livro...", Toast.LENGTH_SHORT).show()

            Handler(Looper.getMainLooper()).postDelayed({
                Toast.makeText(this, "Livro atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, ListBookActivity::class.java))
                finish()
            }, 2000)
        }

        val buttonVoltar = findViewById<Button>(R.id.buttonVoltar)
        buttonVoltar.setOnClickListener {
            startActivity(Intent(this, ListBookActivity::class.java))
        }
    }
}
