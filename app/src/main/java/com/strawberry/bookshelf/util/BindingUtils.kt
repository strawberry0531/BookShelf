package com.strawberry.bookshelf.util


import android.widget.Button
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.strawberry.bookshelf.R

@BindingAdapter("favFlag")
fun Button.setColorAndText(flag: Boolean) {
    if (flag) {
        text = context.getString(R.string.remove_favourites)
        setBackgroundColor(context.getColor(R.color.teal_200))
    } else {
        text = context.getString(R.string.add_to_favourites)
        setBackgroundColor(context.getColor(R.color.purple_200))
    }

}

@BindingAdapter("url")
fun setImage(view: ImageView, imageUrl: String?) {
    if(imageUrl!=null)  Picasso.get().load(imageUrl).into(view)
}
