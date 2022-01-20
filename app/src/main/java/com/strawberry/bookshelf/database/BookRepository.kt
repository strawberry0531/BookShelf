package com.strawberry.bookshelf.database

import android.content.Context
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.strawberry.bookshelf.webdata.VolleyRequest
import com.strawberry.bookshelf.webdata.WebData
import org.json.JSONException
import org.json.JSONObject


class BookRepository(private val bookDao: BookDao,private val webData: WebData? = null) {

    suspend fun insertBook(bookEntity: BookEntity) = bookDao.insert(bookEntity)

    suspend fun deleteBook(bookEntity: BookEntity) = bookDao.delete(bookEntity)

    fun getAllBooks() = bookDao.getAllBooks()

    fun getBookById(bookID: String) = bookDao.getBookByID(bookID)

    fun getBookDesc(bookID: String, context: Context) {
        val url = "http://13.235.250.119/v1/book/get_book/"
        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookID)
        val jsonRequest = object : JsonObjectRequest(
            Method.POST, url, jsonParams,
            Response.Listener {
                try {
                    val success = it.getBoolean("success")
                   if (success){
                       val bookJsonObject = it.getJSONObject("book_data")
                       val bookEntity = BookEntity(
                           bookJsonObject.getString("book_id").toInt(),
                           bookJsonObject.getString("name"),
                           bookJsonObject.getString("author"),
                           bookJsonObject.getString("price"),
                           bookJsonObject.getString("rating"),
                           bookJsonObject.getString("description"),
                           bookJsonObject.getString("image")
                       )
                       webData?.getVolleyResponse(bookEntity)
                   }
                }catch (e:JSONException){

                }

            },
            Response.ErrorListener {
                webData?.getVolleyError(it)
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                headers["token"] = "74302f68224d72"
                return headers
            }
        }
        VolleyRequest.getInstance(context.applicationContext).addToRequestQueue(jsonRequest)
    }
}