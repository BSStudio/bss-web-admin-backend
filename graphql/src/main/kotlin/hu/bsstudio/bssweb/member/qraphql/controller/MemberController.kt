package hu.bsstudio.bssweb.member.qraphql.controller

import hu.bsstudio.bssweb.codegen.types.Author
import hu.bsstudio.bssweb.codegen.types.Book
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class MemberController {

    @QueryMapping
    fun books(): List<Book> = BOOKS

    @QueryMapping
    fun authors(): List<Author> = AUTHORS

    @QueryMapping
    fun bookById(@Argument id: String): Book? {
        return BOOKS.find { it.id == id }
    }

    @QueryMapping
    fun authorById(@Argument id: String): Author? {
        return AUTHORS.find { it.id == id }
    }

    private companion object {
        private val AUTHOR_1 = Author("author-1", "Joshua", "Bloch")
        private val AUTHOR_2 = Author("author-2", "Douglas", "Adams")
        private val AUTHOR_3 = Author("author-3", "Bill", "Bryson")
        private val AUTHORS = listOf(AUTHOR_1, AUTHOR_2, AUTHOR_3)
        private val BOOKS = listOf(
            Book("book-1", "Effective Java", 416, AUTHOR_1),
            Book("book-2", "Hitchhiker's Guide to the Galaxy", 208, AUTHOR_2),
            Book("book-3", "Down Under", 436, AUTHOR_3)
        )
    }
}