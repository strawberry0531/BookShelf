package com.strawberry.bookshelf.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.strawberry.bookshelf.activity.DescriptionActivity
import com.strawberry.bookshelf.model.Book
import com.strawberry.bookshelf.R

class DashboardRecyclerAdapter(val context: Context, private val itemList: ArrayList<Book>) :
    RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {
    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textBookName: TextView = view.findViewById(R.id.txtName)
        val textAuthor: TextView = view.findViewById(R.id.txtAuthor)
        val textCost: TextView = view.findViewById(R.id.txtCost)
        val textRating: TextView = view.findViewById(R.id.txtRating)
        val imgBookcover: ImageView = view.findViewById(R.id.imgBookCover)
        val imgFavourite:ImageView = view.findViewById(R.id.imgFavourite)
        val layoutFileContent: RelativeLayout = view.findViewById(R.id.recycler_item_row_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_dashboard_row, parent, false)
        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val book = itemList[position]
        holder.textBookName.text = book.bookName
        holder.textAuthor.text = book.bookAuthor
        holder.textCost.text = book.bookPrice
        holder.textRating.text = book.bookRating
        //holder.imgBookcover.setImageResource(book.bookImage)
        Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.imgBookcover)
        if(!book.bookFav){
            holder.imgFavourite.visibility = View.GONE
        }
        holder.layoutFileContent.setOnClickListener {
            val intent = Intent(context,DescriptionActivity::class.java)
            intent.putExtra("book_id",book.bookId)
            intent.putExtra("activity","dash")
            context.startActivity(intent)
        }
    }
}