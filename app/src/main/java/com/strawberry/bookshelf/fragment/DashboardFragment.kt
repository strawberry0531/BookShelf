package com.strawberry.bookshelf.fragment


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.strawberry.bookshelf.R
import com.strawberry.bookshelf.adapter.DashboardRecyclerAdapter
import com.strawberry.bookshelf.database.BookEntity
import com.strawberry.bookshelf.database.BookViewModel
import com.strawberry.bookshelf.databinding.FragmentDashboardBinding
import com.strawberry.bookshelf.model.Book
import com.strawberry.bookshelf.util.ConnectionManager
import com.strawberry.bookshelf.webdata.VolleyRequest
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

class DashboardFragment : Fragment() {

    lateinit var recyclerAdapter: DashboardRecyclerAdapter

    private val  bookViewModel: BookViewModel by activityViewModels()
    private lateinit var binding: FragmentDashboardBinding
    var bookInfoList = arrayListOf<Book>()
    private var shouldRefresh = false
    private var ratingComparator =
        Comparator<Book> { book1, book2 ->
            if (book1.bookRating.compareTo(book2.bookRating, true) == 0)
                book1.bookName.compareTo(book2.bookName, true)
            else
                book1.bookRating.compareTo(book2.bookRating, true)
        }
    private var bookComparator =
        Comparator<Book> { book1, book2 ->
            book1.bookName.compareTo(book2.bookName, true)
        }
    private var authorComparator =
        Comparator<Book> { book1, book2 ->
            book1.bookAuthor.compareTo(book2.bookAuthor, true)
        }
    private var priceComparator =
        Comparator<Book> { book1, book2 ->
            if (book1.bookPrice.compareTo(book2.bookPrice, true) == 0)
                book1.bookName.compareTo(book2.bookName, true)
            else
                book1.bookPrice.compareTo(book2.bookPrice, true)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        binding = FragmentDashboardBinding.bind(view)
        setHasOptionsMenu(true)

        binding.relativeProgress.visibility = View.VISIBLE
//        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v1/book/fetch_books/"
        if (ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(
                    Method.GET, url, null, Response.Listener {
                    try {
                        binding.relativeProgress.visibility = View.GONE
                        val success = it.getBoolean("success")
                        if (success) {
                            val data = it.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val bookJsonObject = data.getJSONObject(i)
                                val dbBookEntity = BookEntity(
                                    bookJsonObject.getString("book_id").toInt(),
                                    bookJsonObject.getString("name").toString(),
                                    bookJsonObject.getString("author").toString(),
                                    bookJsonObject.getString("rating").toString(),
                                    bookJsonObject.getString("price").toString(),
                                    "null",
                                    bookJsonObject.getString("image").toString()
                                )


                                val isFav = getFavourites(dbBookEntity)
                                val bookObject = Book(
                                    bookJsonObject.getString("book_id"),
                                    bookJsonObject.getString("name"),
                                    bookJsonObject.getString("author"),
                                    bookJsonObject.getString("rating"),
                                    bookJsonObject.getString("price"),
                                    bookJsonObject.getString("image"),
                                    isFav
                                )
                                bookInfoList.add(bookObject)

                                recyclerAdapter =
                                    DashboardRecyclerAdapter(
                                        activity as Context,
                                        bookInfoList

                                    )
                                binding.recyclerDashboard.adapter = recyclerAdapter
                            }
                        } else {
                            Toast.makeText(
                                activity as Context,
                                "Some error",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(
                            activity as Context,
                            "Some error in JSON",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                },
                    Response.ErrorListener {
                        Toast.makeText(
                            activity as Context,
                            "Volley Error Occurred",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "****" //will be given by beackend developer
                        return headers
                    }
                }
            VolleyRequest.getInstance((activity as Context).applicationContext).addToRequestQueue(jsonObjectRequest)
//            queue.add(jsonObjectRequest)
        } else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection not found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_sort, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort_rating_dash -> {
                Collections.sort(bookInfoList, ratingComparator)
                bookInfoList.reverse()
            }
            R.id.sort_author_dash -> {
                Collections.sort(bookInfoList, authorComparator)
                bookInfoList.reverse()
            }
            R.id.sort_book_dash ->{
                Collections.sort(bookInfoList, bookComparator)
                bookInfoList.reverse()
            }
            R.id.sort_cost_dash ->{
                Collections.sort(bookInfoList, priceComparator)
                bookInfoList.reverse()
            }
        }
        recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }

    fun getFavourites(bookEntity: BookEntity) :Boolean
        {
            var book:BookEntity? = null
            bookViewModel.setBookId(bookEntity.book_id.toString())
            bookViewModel.bookById.observe(viewLifecycleOwner,{
                book = it
            })
            return book != null
    }

    override fun onStop() {
        super.onStop()
        shouldRefresh = true
    }

    override fun onResume() {
        super.onResume()
        if (shouldRefresh) {
            val fragment = DashboardFragment()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_layout, fragment)
            transaction?.addToBackStack("Dashboard")
            activity?.actionBar?.title = "Dashboard"
            transaction?.commit()
        }
    }
}
