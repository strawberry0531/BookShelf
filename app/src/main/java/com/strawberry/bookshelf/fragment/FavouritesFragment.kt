package com.strawberry.bookshelf.fragment


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.strawberry.bookshelf.R
import com.strawberry.bookshelf.adapter.FavouritesRecyclerAdapter
import com.strawberry.bookshelf.database.BookViewModel
import com.strawberry.bookshelf.databinding.FragmentFavouritesBinding

class FavouritesFragment : Fragment() {
    private lateinit var binding: FragmentFavouritesBinding
    private val bookViewModel: BookViewModel by activityViewModels()
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var layoutAdapter: FavouritesRecyclerAdapter
    private var shouldRefresh = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourites, container, false)

        layoutManager = GridLayoutManager(activity as Context, 2)
        layoutAdapter = FavouritesRecyclerAdapter(activity as Context)

        if (activity != null) {
            binding.relativeProgress.visibility = View.GONE
            binding.recyclerFavourites.adapter = layoutAdapter
            binding.recyclerFavourites.layoutManager = layoutManager
        }
        bookViewModel.bookData.observe(viewLifecycleOwner, {
            layoutAdapter.submitList(it)
        })
        return binding.root
    }


    override fun onStop() {
        super.onStop()
        shouldRefresh = true
    }

    override fun onResume() {
        super.onResume()
        if (shouldRefresh) {
            val fragment = FavouritesFragment()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_layout, fragment)
            transaction?.addToBackStack("Favourites")
            activity?.actionBar?.title = "Favourites"
            transaction?.commit()
        }
    }
}
