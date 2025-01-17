package br.ufrn.library.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.ufrn.library.R
import br.ufrn.library.persistence.BookPersistence
import java.io.File

class LoginActivity : AppCompatActivity() {

    private lateinit var db: BookPersistence
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = BookPersistence(this)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)

        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)

        findViewById<Button>(R.id.buttonLogin).setOnClickListener { authenticate(sharedPreferences) }
    }

    private fun authenticate(sharedPreferences: SharedPreferences) {
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()

        when {
            email.isEmpty() -> editTextEmail.error = "Campo obrigatório"
            password.isEmpty() -> editTextPassword.error = "Campo obrigatório"
            email == "admin" && password == "admin" -> {
                val editor = sharedPreferences.edit()
                editor.putBoolean("is_logged_in", true)
                editor.apply()
                startActivity(Intent(this, ListBookActivity::class.java))
                finish()
            }
            else -> {
                Toast.makeText(this, "Não foi possível entrar!", Toast.LENGTH_LONG).show()
            }
        }
    }
}