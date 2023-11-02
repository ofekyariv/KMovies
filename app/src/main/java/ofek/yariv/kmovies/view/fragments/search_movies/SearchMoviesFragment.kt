package ofek.yariv.kmovies.view.fragments.search_movies

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.launch
import ofek.yariv.kmovies.R
import ofek.yariv.kmovies.databinding.FragmentSearchMoviesBinding
import ofek.yariv.kmovies.model.data.Resource
import ofek.yariv.kmovies.utils.Constants
import ofek.yariv.kmovies.utils.Constants.TAG
import ofek.yariv.kmovies.utils.ReportConstants
import ofek.yariv.kmovies.utils.managers.SpeechToTextManager
import ofek.yariv.kmovies.view.activities.main.listeners.ScrollListener
import ofek.yariv.kmovies.view.adapters.SearchMoviesAdapter
import ofek.yariv.kmovies.view.fragments.BaseFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SearchMoviesFragment : BaseFragment(R.layout.fragment_search_movies) {
    private val searchMoviesFragmentViewModel: SearchMoviesFragmentViewModel by viewModel()
    private lateinit var binding: FragmentSearchMoviesBinding

    override fun getFragmentName() = ReportConstants.SEARCH_MOVIES
    override fun getFragmentItemId() = R.id.searchMoviesFragment

    private lateinit var searchMoviesAdapter: SearchMoviesAdapter
    private val speechToTextManager: SpeechToTextManager by inject { parametersOf(requireActivity()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchMoviesBinding.bind(view)
        initiateLayout()
    }

    private fun initiateLayout() {
        (activity as? ScrollListener)?.onScrolledDown()
        setupRecyclerView()
        observeChanges()
        setupOnClickListeners()
    }

    private fun observeChanges() {
        binding.acSearch.addTextChangedListener { editable ->
            if (editable.toString().isNotEmpty()) {
                binding.btnMicSearch.visibility = View.GONE
                binding.btnClearSearch.visibility = View.VISIBLE
                searchMoviesFragmentViewModel.searchMovies(query = editable.toString())
                Log.d(TAG, "observeChanges: ${editable.toString()}")
            } else {
                binding.btnMicSearch.visibility = View.VISIBLE
                binding.btnClearSearch.visibility = View.GONE
            }
        }
        lifecycleScope.launch {
            searchMoviesFragmentViewModel.searchMoviesResult.collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        binding.pbSearchMovies.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.pbSearchMovies.visibility = View.GONE
                        result.data?.let { searchMoviesAdapter.submitList(it) }
                        Log.d(TAG, "observeChanges: ${result.data}")
                    }

                    is Resource.Failure -> {
                        binding.pbSearchMovies.visibility = View.GONE
                    }

                    is Resource.None -> {
                        binding.pbSearchMovies.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        searchMoviesAdapter = SearchMoviesAdapter(
            onMovieClickListener = { movie ->
                val bundle = Bundle().apply {
                    putParcelable(Constants.MOVIE, movie)
                }
                findNavController().navigate(
                    R.id.action_searchMoviesFragment_to_movieDetailsFragment,
                    bundle
                )
            }
        )
        binding.rvSearchMovies.apply {
            adapter = searchMoviesAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun setupOnClickListeners() {
        binding.btnClearSearch.setOnClickListener {
            binding.acSearch.text.clear()
        }
        binding.btnMicSearch.setOnClickListener {
            lifecycleScope.launch {
                speechToTextManager.startSpeechRecognition().also { result ->
                    speechToTextManager.unregisterLauncher()
                    binding.acSearch.setText(result)
                }
            }
        }
    }
}