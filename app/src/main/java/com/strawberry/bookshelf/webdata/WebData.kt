package com.strawberry.bookshelf.webdata

import com.android.volley.VolleyError
import com.strawberry.bookshelf.database.BookEntity

interface WebData {
    fun getVolleyResponse(bookEntity: BookEntity)
    fun getVolleyError(error: VolleyError)
}
