package sashjakk.weather.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import kotlinx.android.synthetic.main.fragment_weather_list.*
import kotlinx.android.synthetic.main.weather_list_item.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import sashjakk.weather.app.GlideApp
import sashjakk.weather.app.R
import sashjakk.weather.app.db.DatabaseClient
import sashjakk.weather.app.db.WeatherEntity
import sashjakk.weather.app.tools.MarginItemDecoration

class WeatherListViewModel(
    private val database: DatabaseClient<WeatherEntity>
) : ViewModel() {

    val locations = liveData {
        val items = database.getAll()
        emit(items)
    }
}

class WeatherListItemHolder(
    private val view: View
) : RecyclerView.ViewHolder(view) {

    fun bind(item: WeatherEntity) {
        with(view) {
            thumbCity.text = item.city

            GlideApp.with(view)
                .load(item.iconUrl)
                .into(thumbIcon)
        }
    }
}

class WeatherListItemAdapter(
    var items: List<WeatherEntity> = mutableListOf(),
    private val onClick: (WeatherEntity) -> Unit = {}
) : Adapter<WeatherListItemHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherListItemHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.weather_list_item, parent, false)

        return WeatherListItemHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: WeatherListItemHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener { onClick(items[position]) }
    }
}

class WeatherListFragment : Fragment() {

    private val clicker: (WeatherEntity) -> Unit = {
        val action = WeatherListFragmentDirections
            .listToDetails(it.city)

        findNavController().navigate(action)
    }

    private val adapter = WeatherListItemAdapter(onClick = clicker)

    private val viewModel by viewModel<WeatherListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_weather_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupWeatherList()

        viewModel.locations
            .observe(viewLifecycleOwner) {
                adapter.items = it
                adapter.notifyDataSetChanged()
            }

        searchButton.setOnClickListener {
            val cityQuery = searchQuery.text.toString()

            val direction = WeatherListFragmentDirections
                .listToDetails(cityQuery)

            findNavController().navigate(direction)
        }
    }

    private fun setupWeatherList() {
        val context = requireContext()

        weatherList.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = this@WeatherListFragment.adapter
            addItemDecoration(MarginItemDecoration(8))
        }
    }
}
