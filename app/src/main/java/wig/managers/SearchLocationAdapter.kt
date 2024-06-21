package wig.managers

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import wig.R
import wig.models.entities.Location
import wig.utils.Alerts

class SearchLocationAdapter(private val locationList: MutableList<Location>,
                            private val context: Context,
                            private val locationAdapter: LocationAdapter
) :
    RecyclerView.Adapter<SearchLocationAdapter.LocationViewHolder>() {

    inner class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.txtName)

        init {
            itemView.setOnClickListener {
                    locationViewClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_item, parent, false)
        return LocationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = locationList[position]
        if (location.locationName.length >= 30){
            holder.nameTextView.text = location.locationName.substring(0, 30)
        } else {
            holder.nameTextView.text = location.locationName
        }
    }

    override fun getItemCount(): Int {
        return locationList.size
    }

    fun addLocation(location: Location) {
        var exists = false

        for (item in locationList) {
            if (item.locationUID == location.locationUID) {
                exists = true
            }
        }

        if (!exists) {
            locationList.add(location)
            notifyItemInserted(locationList.size - 1)
        }
    }

    private fun removeLocation(position: Int) {
        locationList.removeAt(position)
        notifyItemRemoved(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearLocations() {
        locationList.clear()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun locationViewClick(position: Int) {
        val location = locationList[position]
        Alerts().addConfirmation(location.locationName, context) {
                shouldAdd ->
            if (shouldAdd) {
                locationAdapter.addLocation(location)
                removeLocation(position)
            }
        }
    }

}
