package com.strawberry.bookshelf.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
@Dao
interface BookDao {
    @Insert
    suspend fun insert(bookEntity: BookEntity)

    @Delete
    suspend fun delete(bookEntity: BookEntity)

    @Query("SELECT * from books")
    fun getAllBooks(): LiveData<List<BookEntity>>

    @Query("select * from books where book_id = :bookID")
    fun getBookByID(bookID: String): LiveData<BookEntity>

    @Query("select exists(select 1 from books where book_id=:bookID )")
    fun checkBookExists(bookID: Int):LiveData<Boolean>
}