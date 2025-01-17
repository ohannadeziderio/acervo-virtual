package br.ufrn.library.persistence

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import br.ufrn.library.domain.Book

class BookPersistence(context: Context) : SQLiteOpenHelper(context, "books.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE books (
                _id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                author TEXT NOT NULL,
                publisher TEXT NOT NULL,
                isbn TEXT NOT NULL,
                description TEXT NOT NULL,
                url TEXT NOT NULL
            )
            """.trimIndent()
        )

        val initialBooks = listOf(
            ContentValues().apply {
                put("title", "Dom Casmurro")
                put("author", "Machado de Assis")
                put("publisher", "Editora Garnier")
                put("isbn", "9788535902778")
                put("description", "Um dos romances mais importantes da literatura brasileira, narrado pelo enigmático Bentinho.")
                put("url", "https://m.media-amazon.com/images/I/61Z2bMhGicL._SL1360_.jpg")
            },
            ContentValues().apply {
                put("title", "Grande Sertão: Veredas")
                put("author", "João Guimarães Rosa")
                put("publisher", "José Olympio")
                put("isbn", "9788520931725")
                put("description", "Uma das maiores obras da literatura brasileira, explorando o sertão mineiro e seus conflitos.")
                put("url", "https://m.media-amazon.com/images/I/81NtboFZziL.jpg")
            },
            ContentValues().apply {
                put("title", "O Quinze")
                put("author", "Rachel de Queiroz")
                put("publisher", "José Olympio")
                put("isbn", "9788503013136")
                put("description", "Um retrato da seca no Nordeste e suas consequências para as pessoas da região.")
                put("url", "https://m.media-amazon.com/images/I/912IDpDnZVL._SL1500_.jpg")
            },
            ContentValues().apply {
                put("title", "Capitães da Areia")
                put("author", "Jorge Amado")
                put("publisher", "Companhia das Letras")
                put("isbn", "9788520932128")
                put("description", "A história de crianças abandonadas nas ruas de Salvador, lutando pela sobrevivência.")
                put("url", "https://m.media-amazon.com/images/I/81t7altQZxL._SL1500_.jpg")
            },
            ContentValues().apply {
                put("title", "Morte e Vida Severina")
                put("author", "João Cabral de Melo Neto")
                put("publisher", "Nova Fronteira")
                put("isbn", "9788520922129")
                put("description", "Um poema narrativo que aborda a vida difícil no sertão nordestino.")
                put("url", "https://m.media-amazon.com/images/I/71AYPfbv2BL._SL1500_.jpg")
            }
        )

        initialBooks.forEach { values ->
            db.insert("books", null, values)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS books")
        onCreate(db)
    }

    fun salvar(book: Book) {
        require(book.title.isNotBlank()) { "Título não pode ser vazio." }
        require(book.author.isNotBlank()) { "Autor não pode ser negativa." }
        require(book.isbn.isNotBlank()) { "Autor não pode ser negativa." }
        require(book.publisher.isNotBlank()) { "Autor não pode ser negativa." }

        writableDatabase.use { db ->
            db.insertOrThrow(
                "books", null, ContentValues().apply {
                    put("title", book.title)
                    put("isbn", book.isbn)
                    put("description", book.description)
                    put("author", book.author)
                    put("publisher", book.publisher)
                    put("url", book.url)
                }
            )
        }
    }

    fun deletar(id: Int): Boolean {
        require(id > 0) { "ISBN não pode ser vazio." }

        return writableDatabase.use { db ->
            db.delete("books", "_id = ?", arrayOf(id.toString())) > 0
        }
    }

    fun atualizar(book: Book): Boolean {
        val values = ContentValues().apply {
            book.title.takeIf { it.isNotBlank() }?.let { put("title", it) }
            book.author.takeIf { it.isNotBlank() }?.let { put("author", it) }
            book.isbn.takeIf { it.isNotBlank() }?.let { put("isbn", it) }
            book.publisher.takeIf { it.isNotBlank() }?.let { put("publisher", it) }
            book.description.takeIf { it!!.isNotBlank() }?.let { put("description", it) }
            book.url.takeIf { it!!.isNotBlank() }?.let { put("url", it) }
        }

        require(values.size() > 0) { "Nenhum dado válido foi fornecido para atualizar." }

        return writableDatabase.use { db ->
            db.update("books", values, "_id = ?", arrayOf(book.id.toString())) > 0
        }
    }

    fun obterTodos(): List<Book> = readableDatabase.use { db ->
        db.query(
            "books",
            arrayOf("_id", "title", "author", "publisher", "isbn", "description", "url"),
            null, null, null, null, null
        ).use { cursor ->
            generateSequence { if (cursor.moveToNext()) cursor else null }
                .map { cursor.toBook() }
                .toList()
        }
    }

    private fun Cursor.toBook(): Book = Book(
        id = getInt(getColumnIndexOrThrow("_id")),
        title = getString(getColumnIndexOrThrow("title")),
        author = getString(getColumnIndexOrThrow("author")),
        publisher = getString(getColumnIndexOrThrow("publisher")),
        isbn = getString(getColumnIndexOrThrow("isbn")),
        description = getString(getColumnIndexOrThrow("description")),
        url = getString(getColumnIndexOrThrow("url"))
    )
}