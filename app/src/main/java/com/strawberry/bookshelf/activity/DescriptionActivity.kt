package com.strawberry.bookshelf.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.strawberry.bookshelf.R
import com.strawberry.bookshelf.database.BookViewModel
import com.strawberry.bookshelf.databinding.ActivityDescriptionBinding
import com.strawberry.bookshelf.util.ConnectionManager

class DescriptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDescriptionBinding
    private val bookViewModel: BookViewModel by viewModels()
    private var bookId: String? = "100"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_description)

        binding.viewModel = bookViewModel
        binding.lifecycleOwner = this
        setSupportActionBar(binding.descToolbar)
        supportActionBar?.title = "Book Details"
        if (intent != null)
            bookId = intent.getStringExtra("book_id")
        else {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some unexpected error in desc launching",
                Toast.LENGTH_SHORT
            )
                .show()
        }
        if (bookId == "100") {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some unexpected error in desc launching",
                Toast.LENGTH_SHORT
            )
                .show()
        }
        bookViewModel.setBookId(bookId.toString())

        if (ConnectionManager().checkConnectivity(this@DescriptionActivity)) {
            bookViewModel.getBookDesc(bookId, this)
        } else {
            val dialog = AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection not found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(this@DescriptionActivity)
            }
            dialog.create()
            dialog.show()
        }
    }
}


