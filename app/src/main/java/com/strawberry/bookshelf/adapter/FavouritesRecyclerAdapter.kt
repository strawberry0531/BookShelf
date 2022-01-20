package com.strawberry.bookshelf.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout

import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

import com.strawberry.bookshelf.R
import com.strawberry.bookshelf.activity.DescriptionActivity
import com.strawberry.bookshelf.database.BookEntity


class FavouritesRecyclerAdapter(val context: Context) :
   ListAdapter<BookEntity, FavouritesRecyclerAdapter.FavouriteViewHolder>(FavouriteDiffUtil()) {

    class FavouriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textBookNameFav: TextView = view.findViewById(R.id.bookNameFav)
        val textAuthorFav: TextView = view.findViewById(R.id.bookAuthorFav)
        val textCostFav: TextView = view.findViewById(R.id.bookPriceFav)
        val textRatingFav: TextView = view.findViewById(R.id.bookRatingFav)
        val imgBookcoverFav: ImageView = view.findViewById(R.id.bookCoverFav)
        val layoutFileContentFav: LinearLayout = view.findViewById(R.id.recyclerRowFavLinearLayout)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_favourites_row, parent, false)
        return FavouriteViewHolder(view)
    }


    override fun onBindViewHolder(
        holder: FavouriteViewHolder,
        position: Int
    ) {
        val book = getItem(position)
        holder.textBookNameFav.text = book.bookName
        holder.textAuthorFav.text = book.bookAuthor
        holder.textCostFav.text = book.bookPrice
        holder.textRatingFav.text = book.bookRating
        //holder.imgBookcover.setImageResource(book.bookImage)
        Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover)
            .into(holder.imgBookcoverFav)
        holder.layoutFileContentFav.setOnClickListener {
            val intent = Intent(context, DescriptionActivity::class.java)
            intent.putExtra("book_id", book.book_id.toString())
            intent.putExtra("activity", "fav")
            context.startActivity(intent)
        }
    }

}
 class FavouriteDiffUtil:DiffUtil.ItemCallback<BookEntity>(){
     override fun areItemsTheSame(oldItem: BookEntity, newItem: BookEntity): Boolean {
         return oldItem.book_id==newItem.book_id
     }

     override fun areContentsTheSame(oldItem: BookEntity, newItem: BookEntity): Boolean {
         return oldItem==newItem
     }

 }