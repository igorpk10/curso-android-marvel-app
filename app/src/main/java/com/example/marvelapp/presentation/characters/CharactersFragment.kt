package com.example.marvelapp.presentation.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.MenuItem
import androidx.appcompat.widget.SearchView

import androidx.fragment.app.Fragment
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.marvelapp.R
import com.example.marvelapp.databinding.FragmentCharactersBinding
import com.example.marvelapp.framework.imageloader.ImageLoader
import com.example.marvelapp.presentation.characters.adapter.CharactersAdapter
import com.example.marvelapp.presentation.characters.adapter.CharactersLoadingMoreStateAdapter
import com.example.marvelapp.presentation.characters.adapter.CharactersRefreshStateAdapter
import com.example.marvelapp.presentation.details.DetailViewArgs
import com.example.marvelapp.presentation.sort.SortFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
@Suppress("TooManyFunctions")
class CharactersFragment :
    Fragment(),
    SearchView.OnQueryTextListener,
    MenuItem.OnActionExpandListener {

    companion object {
        private const val FLIPPER_CHILD_LOADING = 0
        private const val FLIPPER_CHILD_CHARACTERS = 1
        private const val FLIPPER_CHILD_ERROR = 2
    }

    private lateinit var binding: FragmentCharactersBinding
    private lateinit var searchView: SearchView

    private val characterAdapter: CharactersAdapter by lazy {
        CharactersAdapter(imageLoader) { character, view ->
            val extras = FragmentNavigatorExtras(
                view to character.name
            )

            val directions = CharactersFragmentDirections
                .actionCharactersFragmentToDetailFragment(
                    character.name,
                    DetailViewArgs(
                        characterId = character.id,
                        name = character.name,
                        imageURL = character.imageUrl
                    )
                )

            findNavController().navigate(directions, extras)
        }
    }

    private val viewModel: CharactersViewModel by viewModels()

    @Inject
    lateinit var imageLoader: ImageLoader

    private val headerAdapter: CharactersRefreshStateAdapter by lazy {
        CharactersRefreshStateAdapter(
            characterAdapter::retry
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharactersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.characters_menu_items, menu)

        val searchItem = menu.findItem(R.id.menu_search)
        searchView = searchItem.actionView as SearchView

        searchItem.setOnActionExpandListener(this)

        if(viewModel.currentSearchQuery.isNotEmpty()){
            searchItem.expandActionView()
            searchView.setQuery(viewModel.currentSearchQuery,false)
        }

        searchView.apply {
            isSubmitButtonEnabled = true
            setOnQueryTextListener(this@CharactersFragment)
        }
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        return query?.let {
            viewModel.currentSearchQuery = it
            viewModel.searchCharacters()
            true
        } ?: false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_sort -> {
                findNavController().navigate(R.id.action_charactersFragment_to_sortFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCharactersAdapter()
        observeInitialLoadingState()
        observeSortingData()

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is CharactersViewModel.UIState.SearchResult -> {
                    characterAdapter.submitData(viewLifecycleOwner.lifecycle, it.data)
                }
            }
        }

        viewModel.searchCharacters()
    }

    private fun initCharactersAdapter() {
        postponeEnterTransition()
        with(binding.recyclerCharacters) {
            setHasFixedSize(true)
            adapter = characterAdapter.withLoadStateHeaderAndFooter(
                header = headerAdapter,
                footer = CharactersLoadingMoreStateAdapter {
                    characterAdapter.retry()
                }
            )

            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        }
    }

    private fun observeInitialLoadingState() {
        lifecycleScope.launch {
            characterAdapter.loadStateFlow.collectLatest { loadState ->

                headerAdapter.loadState = loadState.mediator?.refresh?.takeIf {
                    it is LoadState.Error && characterAdapter.itemCount > 0
                } ?: loadState.prepend

                binding.flipperCharacters.displayedChild = when {

                    //Caso de loading
                    loadState.mediator?.refresh is LoadState.Loading -> {
                        setShimmerVisibility(true)
                        FLIPPER_CHILD_LOADING
                    }


                    //Caso local
                    loadState.mediator?.refresh is LoadState.Error
                            && characterAdapter.itemCount == 0 -> {
                        setShimmerVisibility(false)
                        binding.includeViewCharactersErrorState.buttonRetry.setOnClickListener {
                            characterAdapter.refresh()
                        }
                        FLIPPER_CHILD_ERROR
                    }


                    //Caso de sucesso
                    loadState.source.refresh is LoadState.NotLoading
                            || loadState.mediator?.refresh is LoadState.NotLoading -> {
                        setShimmerVisibility(false)
                        FLIPPER_CHILD_CHARACTERS
                    }

                    else -> {
                        setShimmerVisibility(false)
                        FLIPPER_CHILD_CHARACTERS
                    }
                }
            }
        }
    }

    private fun setShimmerVisibility(visibility: Boolean) {
        binding.includeViewCharactersLoadingState.shimmerCharacters.run {
            isVisible = visibility
            if (visibility) {
                startShimmer()
            } else stopShimmer()
        }
    }

    /**
     * Wheneaver works with fragment + dialogFragment
     * the documentation says to use backstackentry and not currentbackstack
     * fuckit
     */
    private fun observeSortingData() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.charactersFragment)
        val observer = LifecycleEventObserver { _, event ->
            val isSortingApplied =
                navBackStackEntry.savedStateHandle.contains(SortFragment.SORTING_APPLIED_BASK_STACK_KEY)
            if (event == Lifecycle.Event.ON_RESUME && isSortingApplied) {
                viewModel.applySort()
                navBackStackEntry.savedStateHandle.remove<Boolean>(SortFragment.SORTING_APPLIED_BASK_STACK_KEY)
            }
        }

        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })

        navBackStackEntry.lifecycle.addObserver(observer)
    }

    override fun onMenuItemActionExpand(item: MenuItem): Boolean {
        return true
    }

    override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
        viewModel.closeSearch()
        viewModel.searchCharacters()
        return true
    }
}