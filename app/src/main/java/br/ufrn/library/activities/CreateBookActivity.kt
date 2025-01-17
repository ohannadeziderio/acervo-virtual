package br.ufrn.library.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.ufrn.library.R
import br.ufrn.library.domain.Book
import br.ufrn.library.persistence.BookPersistence

class CreateBookActivity : AppCompatActivity() {

    private lateinit var db: BookPersistence

    private lateinit var editTextTitulo: EditText
    private lateinit var editTextAutor: EditText
    private lateinit var editTextISBN: EditText
    private lateinit var editTextEditora: EditText
    private lateinit var editTextDescricao: EditText
    private lateinit var editTextURL: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        db = BookPersistence(this)

        editTextTitulo = findViewById(R.id.editTextTitulo)
        editTextAutor = findViewById(R.id.editTextAutor)
        editTextISBN = findViewById(R.id.editTextISBN)
        editTextEditora = findViewById(R.id.editTextEditora)
        editTextDescricao = findViewById(R.id.editTextDescricao)
        editTextURL = findViewById(R.id.editTextURL)

        val buttonCadastrar = findViewById<Button>(R.id.buttonCadastrar)
        buttonCadastrar.setOnClickListener {
            if (isFormValid()) {
                val book = Book(
                    title = editTextTitulo.text.toString(),
                    author = editTextAutor.text.toString(),
                    isbn = editTextISBN.text.toString(),
                    publisher = editTextEditora.text.toString(),
                    description = editTextDescricao.text.toString(),
                    url = editTextURL.text.toString()
                )

                db.salvar(book)
                Toast.makeText(this, "Livro cadastrado com sucesso!", Toast.LENGTH_LONG).show()
                finish()
            }
        }

        val buttonBack = findViewById<Button>(R.id.buttonVoltar)
        buttonBack.setOnClickListener {
            startActivity(Intent(this, ListBookActivity::class.java))
        }
    }

    private fun isFormValid(): Boolean {
        var isValid = true

        val title = editTextTitulo.text.toString().trim()
        val author = editTextAutor.text.toString().trim()
        val isbn = editTextISBN.text.toString().trim()
        val publisher = editTextEditora.text.toString().trim()

        if (title.isBlank()) {
            editTextTitulo.error = "Campo obrigatório"
            isValid = false
        }

        if (author.isBlank()) {
            editTextAutor.error = "Campo obrigatório"
            isValid = false
        }

        if (isbn.isBlank()) {
            editTextISBN.error = "Campo obrigatório"
            isValid = false
        }

        if (publisher.isBlank()) {
            editTextEditora.error = "Campo obrigatório"
            isValid = false
        }

        return isValid
    }
}
