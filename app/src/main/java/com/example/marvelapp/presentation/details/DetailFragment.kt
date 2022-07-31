package com.example.marvelapp.presentation.details

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.marvelapp.databinding.FragmentDetailBinding
import com.example.marvelapp.framework.imageloader.ImageLoader
import com.example.marvelapp.presentation.details.actionstate.DetailFragmentUIActionState
import com.example.marvelapp.presentation.details.actionstate.DetailFragmentFavoriteUIActionState
import com.example.marvelapp.presentation.extensions.showShortToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment() {

    companion object {
        private const val FLIPPER_LOADING = 0
        private const val FLIPPER_DETAIL = 1
        private const val FLIPPER_ERROR = 2
        private const val FLIPPER_EMPTY = 3

        private const val FLIPPER_FAVORITE_CHILD_POSITION_IMAGE = 0
        private const val FLIPPER_FAVORITE_CHILD_POSITION_LOADING = 1
    }

    private var _binding: FragmentDetailBinding? = null
    private val binding: FragmentDetailBinding get() = _binding!!

    private val viewModel: DetailViewModel by viewModels()

    private val args by navArgs<DetailFragmentArgs>()

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentDetailBinding.inflate(
        inflater, container, false
    ).apply {
        _binding = this
    }.root

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val detailViewArg = args.detailViewArgs
        binding.imageCharacter.run {
            transitionName = detailViewArg.name
            imageLoader.load(
                imageView = this,
                imageURL = detailViewArg.imageURL
            )

        }

        setSharedElementTransitionOnEnter()

        loadCategoriesAndObserveUIState(detailViewArg)
        setAndObserveFavoriteUIState(detailViewArg)
    }

    private fun loadCategoriesAndObserveUIState(detailViewArg: DetailViewArgs) {
        viewModel.categories.load(detailViewArg.characterId)

        binding.includeErrorView.buttonRetry.setOnClickListener {
            viewModel.categories.load(detailViewArg.characterId)
        }

        viewModel.categories.state.observe(viewLifecycleOwner) { uiState ->
            binding.flipperDetail.displayedChild = when (uiState) {
                is DetailFragmentUIActionState.UiState.Success -> {
                    binding.recyclerParentDetail.run {
                        setHasFixedSize(true)
                        adapter = DetailParentAdapter(uiState.detailParentList, imageLoader)
                    }
                    FLIPPER_DETAIL
                }
                DetailFragmentUIActionState.UiState.Error -> FLIPPER_ERROR
                DetailFragmentUIActionState.UiState.Loading -> FLIPPER_LOADING
                DetailFragmentUIActionState.UiState.Empty -> FLIPPER_EMPTY
            }
        }
    }

    private fun setAndObserveFavoriteUIState(detailViewArg: DetailViewArgs) {
        binding.imageFavoriteIcon.setOnClickListener {
            viewModel.favorite.update(detailViewArg)
        }

        viewModel.favorite.state.observe(viewLifecycleOwner) { favoriteUIState ->
            binding.flipperFavorite.displayedChild = when (favoriteUIState) {
                DetailFragmentFavoriteUIActionState.UIState.Loading -> FLIPPER_FAVORITE_CHILD_POSITION_LOADING
                is DetailFragmentFavoriteUIActionState.UIState.Success -> {
                    binding.imageFavoriteIcon.setImageResource(favoriteUIState.icon)
                    FLIPPER_FAVORITE_CHILD_POSITION_IMAGE
                }
                is DetailFragmentFavoriteUIActionState.UIState.Error -> {
                    showShortToast(favoriteUIState.messageResId)
                    FLIPPER_FAVORITE_CHILD_POSITION_IMAGE
                }
            }
        }
    }

    // Define a animação da transição como "move"
    private fun setSharedElementTransitionOnEnter() {
        TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move).apply {
                sharedElementEnterTransition = this
            }
    }
}