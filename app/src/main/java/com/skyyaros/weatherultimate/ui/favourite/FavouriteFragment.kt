package com.skyyaros.weatherultimate.ui.favourite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.skyyaros.weatherultimate.App
import com.skyyaros.weatherultimate.R
import com.skyyaros.weatherultimate.databinding.FavouriteFragmentBinding
import com.skyyaros.weatherultimate.entity.FavouriteCity
import com.skyyaros.weatherultimate.ui.AppBarCallbacks
import kotlinx.coroutines.flow.collect

class FavouriteFragment: Fragment() {
    private var _bind: FavouriteFragmentBinding? = null
    private val bind get() = _bind!!
    private var appBarCallbacks: AppBarCallbacks? = null
    private lateinit var adapter: ItemCityAdapter
    private val viewModel: FavouriteViewModel by viewModels {
        object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FavouriteViewModel(App.component.forecaRepo()) as T
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appBarCallbacks = context as AppBarCallbacks
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _bind = FavouriteFragmentBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ItemCityAdapter (
            onClick = { city ->
                val action = FavouriteFragmentDirections.actionFavouriteFragmentToMainFragment(city)
                findNavController().navigate(action)
            },
            onDeleteClick = { deleteCity ->
                viewModel.deleteFavouriteCity(deleteCity)
                App.deleteCities += deleteCity.id
            },
            getMode = {
                viewModel.isEdit
            }
        )
        bind.recycler.adapter = adapter
        if (viewModel.isEdit) {
            bind.buttonEdit.setImageResource(R.drawable.ic_edit_complete)
        } else {
            bind.buttonEdit.setImageResource(R.drawable.ic_mode_edit)
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.flowFavouriteCities.collect { favouriteCities ->
                adapter.submitList(favouriteCities.sortedBy { it.name })
            }
        }
        bind.buttonAdd.setOnClickListener {
            val action = FavouriteFragmentDirections.actionFavouriteFragmentToSearchFragment()
            findNavController().navigate(action)
        }
        bind.buttonEdit.setOnClickListener {
            if (viewModel.isEdit) {
                bind.buttonEdit.setImageResource(R.drawable.ic_mode_edit)
            } else {
                bind.buttonEdit.setImageResource(R.drawable.ic_edit_complete)
            }
            viewModel.isEdit = !viewModel.isEdit
            adapter.notifyDataSetChanged()
        }
        bind.myLocation.setOnClickListener {
            val action = FavouriteFragmentDirections.actionFavouriteFragmentToMainFragment(
                FavouriteCity(-1, "", "", "", 0.0, 0.0)
            )
            findNavController().navigate(action)
        }
    }

    override fun onResume() {
        super.onResume()
        appBarCallbacks!!.setTitle(getString(R.string.fragment_favourite))
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