package hu.bsstudio.bssweb.member.qraphql.controller

import hu.bsstudio.bssweb.codegen.types.Author
import hu.bsstudio.bssweb.codegen.types.Book
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class MemberController {

    @QueryMapping
    fun books(): List<Book> = BOOKS

    @QueryMapping
    fun bookById(@Argument id: String): Book? {
        return BOOKS.find { it.id == id }
    }

    @SchemaMapping
    fun author(book: Book): Author? {
        return AUTHORS.find { it.id == book.authorId }
    }

    private companion object {
        private val BOOKS = listOf(
                Book("book-1", "Effective Java", 416, "author-1"),
                Book("book-2", "Hitchhiker's Guide to the Galaxy", 208, "author-2"),
                Book("book-3", "Down Under", 436, "author-3")
        )
        private val AUTHORS = listOf(
                Author("author-1", "Joshua", "Bloch"),
                Author("author-2", "Douglas", "Adams"),
                Author("author-3", "Bill", "Bryson")
        )
    }
}