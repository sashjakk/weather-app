package sashjakk.weather.app.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.weather_list_item.view.*
import sashjakk.weather.app.GlideApp
import sashjakk.weather.app.R
import sashjakk.weather.app.db.WeatherEntity

class WeatherListItemVH(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer


class WeatherListItemAdapter(
    var items: List<WeatherEntity> = mutableListOf(),
    private val onClick: (WeatherEntity) -> Unit = {}
) : RecyclerView.Adapter<WeatherListItemVH>() {

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherListItemVH {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.weather_list_item, parent, false)

        return WeatherListItemVH(view)
    }

    override fun onBindViewHolder(holder: WeatherListItemVH, position: Int) {
        val item = items[position]

        with(holder.containerView) {
            thumbCity.text = item.city

            GlideApp.with(this)
                .load(item.iconUrl)
                .into(thumbIcon)

            setOnClickListener { onClick(item) }
        }
    }
}

