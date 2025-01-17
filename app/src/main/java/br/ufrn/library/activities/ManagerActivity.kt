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
import br.ufrn.library.persistence.BookPersistence

class ManagerActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_manager)

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

        val buttonVoltar = findViewById<Button>(R.id.buttonVoltar)
        buttonVoltar.setOnClickListener {
            startActivity(Intent(this, ListBookActivity::class.java))
        }

        val apagarButton = findViewById<Button>(R.id.buttonApagar)
        apagarButton.setOnClickListener { handleDelete() }

        val atualizarButton = findViewById<Button>(R.id.buttonAtualizar)
        atualizarButton.setOnClickListener {
            val intent = Intent(this, UpdateBookActivity::class.java).apply {
                putExtra("BOOK_ID", bookId)
                putExtra("BOOK_TITLE", editTextTitulo.text.toString())
                putExtra("BOOK_AUTHOR", editTextAutor.text.toString())
                putExtra("BOOK_ISBN", editTextISBN.text.toString())
                putExtra("BOOK_PUBLISHER", editTextEditora.text.toString())
                putExtra("BOOK_DESCRIPTION", editTextDescricao.text.toString())
                putExtra("BOOK_URL", editTextURL.text.toString())
            }
            startActivity(intent)
        }

    }

    private fun handleDelete() {
        val isDeleted = runCatching { db.deletar(bookId!!) }
            .onFailure {
                Toast.makeText(this, "Ops, um erro inesperado aconteceu!", Toast.LENGTH_SHORT).show()
            }
            .getOrDefault(false)

        if (isDeleted) {
            Toast.makeText(this, "Apagando livro...", Toast.LENGTH_SHORT).show()

            Handler(Looper.getMainLooper()).postDelayed({
                Toast.makeText(this, "Livro apagado com sucesso!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, ListBookActivity::class.java))
                finish()
            }, 2000)
        } else {
            Toast.makeText(this, "Ops, um erro inesperado aconteceu!", Toast.LENGTH_SHORT).show()
        }
    }
}
