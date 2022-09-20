package com.example.marvelapp.presentation.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.marvelapp.databinding.FragmentFavoritesBinding
import com.example.marvelapp.framework.imageloader.ImageLoader
import com.example.marvelapp.presentation.common.getGenericAdapterOf
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    companion object {
        private const val FLIPPER_CHILD_CHARACTERS = 0
        private const val FLIPPER_CHILD_EMPTY = 1
    }

    private var _binding: FragmentFavoritesBinding? = null
    private val binding: FragmentFavoritesBinding get() = _binding!!

    @Inject
    lateinit var imageLoader: ImageLoader

    private val viewModel: FavoritesViewModel by viewModels()

    private val favoriteAdapter by lazy {
        getGenericAdapterOf {
            FavoritesViewHolder.create(it, imageLoader)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentFavoritesBinding.inflate(
        inflater,
        container,
        false
    ).apply {
        _binding = this
    }.root

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()

        viewModel.state.observe(viewLifecycleOwner) { uiState ->
            binding.flipperFavorites.displayedChild = when (uiState) {
                FavoritesViewModel.UIState.ShowEmpty -> {
                    favoriteAdapter.submitList(emptyList())
                    FLIPPER_CHILD_EMPTY
                }

                is FavoritesViewModel.UIState.ShowFavorites -> {
                    favoriteAdapter.submitList(uiState.favorites)
                    FLIPPER_CHILD_CHARACTERS
                }
            }
        }

        viewModel.getAll()
    }

    private fun initAdapter() {
        binding.recyclerFavorites.run {
            setHasFixedSize(true)
            this.adapter = favoriteAdapter
        }
    }
}