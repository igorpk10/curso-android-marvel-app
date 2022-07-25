package com.example.marvelapp.presentation.details

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.marvelapp.databinding.FragmentDetailBinding
import com.example.marvelapp.framework.imageloader.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment() {

    companion object {
        private const val FLIPPER_LOADING = 0
        private const val FLIPPER_DETAIL = 1
        private const val FLIPPER_ERROR = 2
        private const val FLIPPER_EMPTY = 3
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

        binding.includeErrorView.buttonRetry.setOnClickListener {
            viewModel.getCharacterCategorys(detailViewArg.characterId)
        }

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            binding.flipperDetail.displayedChild = when (uiState) {
                is DetailViewModel.UiState.Success -> {
                    binding.recyclerParentDetail.run {
                        setHasFixedSize(true)
                        adapter = DetailParentAdapter(uiState.detailParentList, imageLoader)
                    }
                    FLIPPER_DETAIL
                }
                DetailViewModel.UiState.Loading -> FLIPPER_LOADING
                DetailViewModel.UiState.Empty -> FLIPPER_EMPTY
                DetailViewModel.UiState.Error -> FLIPPER_ERROR

            }
        }

        viewModel.getCharacterCategorys(detailViewArg.characterId)
    }

    // Define a animação da transição como "move"
    private fun setSharedElementTransitionOnEnter() {
        TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move).apply {
                sharedElementEnterTransition = this
            }
    }
}