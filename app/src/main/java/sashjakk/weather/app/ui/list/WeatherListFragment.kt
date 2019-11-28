package sashjakk.weather.app.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_weather_list.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.viewmodel.ext.android.viewModel
import sashjakk.weather.app.R
import sashjakk.weather.app.extensions.onClick
import sashjakk.weather.app.tools.MarginItemDecoration

class WeatherListFragment : Fragment() {

    private val viewModel by viewModel<WeatherListViewModel>()

    private val adapter by lazy {
        WeatherListItemAdapter(onClick = { showDetails(it.city) })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_weather_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupWeatherList()
        setupSearch()

        handleLocationListUpdates()
    }

    private fun setupWeatherList() {
        val context = requireContext()

        weatherList.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = this@WeatherListFragment.adapter
            addItemDecoration(MarginItemDecoration(8))
        }
    }

    private fun setupSearch() {
        searchButton.onClick
            .onEach { showDetails(searchQuery.text.toString()) }
            .launchIn(lifecycleScope)
    }

    private fun handleLocationListUpdates() {
        viewModel.locations
            .observe(viewLifecycleOwner) {
                adapter.items = it
                adapter.notifyDataSetChanged()
            }
    }

    private fun showDetails(city: String) {
        val direction = WeatherListFragmentDirections
            .listToDetails(city)

        findNavController().navigate(direction)
    }
}
