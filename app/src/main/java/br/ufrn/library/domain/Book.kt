package br.ufrn.library.domain

data class Book(
    val id: Int? = null,
    val title: String,
    val author: String,
    val publisher: String,
    val isbn: String,
    val description: String? = null,
    val url: String? = null
)
