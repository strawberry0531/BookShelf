package com.strawberry.bookshelf.database

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.android.volley.VolleyError
import com.strawberry.bookshelf.webdata.WebData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookViewModel(application: Application) : AndroidViewModel(application) {
    private val bookDao = BookDatabase.getDatabase(application).bookDao()
    var bookDescData = MutableLiveData<BookEntity>()
    private var bookRepository = BookRepository(bookDao)

    private val bookID = MutableLiveData<String>()

    fun getBookDesc(bookID: String?, context: Context) {
        bookRepository = BookRepository(bookDao,object :WebData{
            override fun getVolleyResponse(bookEntity: BookEntity) {
                bookDescData.value = bookEntity
            }

            override fun getVolleyError(error: VolleyError) {
                Toast.makeText(
                    context,
                    "Some volley error",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
        if (bookID != null) {
            bookRepository.getBookDesc(bookID,context)
        }
    }

        fun setBookId(value: String) {
        this.bookID.value = value
    }

    val bookData = bookRepository.getAllBooks()

    val bookById = Transformations.switchMap(bookID) {
        bookRepository.getBookById(it)
    }
    val checkFav = Transformations.map(bookById) {
        it!=null
    }

    fun favouritesOperations() {
        viewModelScope.launch(Dispatchers.IO) {
            val fav = checkFav.value
            if(checkFav.value == true) bookDescData.value?.let { bookRepository.deleteBook(it) }
            else bookDescData.value?.let { bookRepository.insertBook(it) }
        }

    }

}