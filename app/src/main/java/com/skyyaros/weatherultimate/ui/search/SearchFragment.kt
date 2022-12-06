package com.skyyaros.weatherultimate.ui.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.skyyaros.weatherultimate.App
import com.skyyaros.weatherultimate.R
import com.skyyaros.weatherultimate.databinding.SearchFragmentBinding
import com.skyyaros.weatherultimate.ui.AppBarCallbacks
import kotlinx.coroutines.flow.collect

class SearchFragment: Fragment() {
    private var _bind: SearchFragmentBinding? = null
    private val bind get() = _bind!!
    private var appBarCallbacks: AppBarCallbacks? = null
    private lateinit var adapter: ItemSearchCityAdapter
    private val viewModel: SearchViewModel by viewModels {
        object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SearchViewModel(requireActivity().application, App.component.forecaRepo()) as T
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appBarCallbacks = context as AppBarCallbacks
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _bind = SearchFragmentBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ItemSearchCityAdapter { favouriteCity ->
            viewModel.saveSearch(favouriteCity.name)
            viewModel.addCity(favouriteCity)
            App.deleteCities.remove(favouriteCity.id) //удаляем его из списка удаленных городов, ведь город снова добавлен
            val action = SearchFragmentDirections.actionSearchFragmentToFavouriteFragment()
            findNavController().navigate(action)
        }
        bind.recycler.adapter = adapter
        bind.editText.hint = viewModel.getSearch()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.stateCitiesFlow.collect { stateSearchCities ->
                when (stateSearchCities) {
                    is StateSearchCity.Begin -> {
                        bind.progressBar.visibility = View.GONE
                        bind.imageButton.isEnabled = true
                        bind.editText.isEnabled = true
                        bind.recycler.visibility = View.VISIBLE
                        adapter.submitList(emptyList())
                        bind.textView.visibility = View.VISIBLE
                        bind.textView.text = getString(R.string.begin_search)
                    }
                    is StateSearchCity.Loading -> {
                        bind.textView.visibility = View.GONE
                        bind.recycler.visibility = View.GONE
                        bind.imageButton.isEnabled = false
                        bind.editText.isEnabled = false
                        bind.progressBar.visibility = View.VISIBLE
                    }
                    is StateSearchCity.Success -> {
                        bind.progressBar.visibility = View.GONE
                        bind.imageButton.isEnabled = true
                        bind.editText.isEnabled = true
                        bind.recycler.visibility = View.VISIBLE
                        val listCities = stateSearchCities.listCities
                        adapter.submitList(listCities)
                        if (listCities.isEmpty()) {
                            bind.textView.visibility = View.VISIBLE
                            bind.textView.text = getString(R.string.no_cities_search)
                        } else {
                            bind.textView.visibility = View.GONE
                        }
                    }
                    is StateSearchCity.Error -> {
                        bind.progressBar.visibility = View.GONE
                        bind.imageButton.isEnabled = true
                        bind.editText.isEnabled = true
                        bind.recycler.visibility = View.VISIBLE
                        adapter.submitList(emptyList())
                        bind.textView.visibility = View.VISIBLE
                        bind.textView.text = stateSearchCities.message
                    }
                }
            }
        }
        bind.imageButton.setOnClickListener {
            val text = bind.editText.text.toString()
            if (text.isNotBlank())
                viewModel.getCities(text)
            else {
                val hint = bind.editText.hint.toString()
                if (hint.isNotBlank())
                    viewModel.getCities(hint)
            }
        }
        bind.editText.addTextChangedListener(
            onTextChanged = { str, _, _, _ ->
                if (str != null) {
                    viewModel.helpFlow.value = str.toString()
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        appBarCallbacks!!.setTitle(getString(R.string.fragment_search))
    }

    override fun onDestroyView() {
        _bind = null
        super.onDestroyView()
    }

    override fun onDetach() {
        appBarCallbacks = null
        super.onDetach()
    }
}