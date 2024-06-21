package wig.managers

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import wig.R
import wig.api.API
import wig.databinding.EditLocationBinding
import wig.models.entities.ItemViewModel
import wig.models.entities.Location
import wig.models.requests.LocationEditRequest
import wig.utils.Alerts

class LocationAdapter(private val locationList: MutableList<Location>,
                      private val lifecycleOwner: LifecycleOwner,
                      private val api: API,
                      private val context: Context
) :
    RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    inner class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.txtOwnershipName)
        val locationTextView: TextView = itemView.findViewById(R.id.txtLocationName)

        init {
            itemView.setOnClickListener {
                    locationViewClick(adapterPosition)
            }

            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    locationViewLongClick(position)
                    return@setOnLongClickListener true
                }
                return@setOnLongClickListener false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.scanner_location_list_item, parent, false)
        return LocationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = locationList[position]
        if (location.locationName.length >= 30){
            holder.nameTextView.text = location.locationName.substring(0, 30)
        } else {
            holder.nameTextView.text = location.locationName
        }
        if (location.location != null) {
            holder.locationTextView.text = location.location!!.locationName
        } else {
            holder.locationTextView.text = context.getString(R.string.default_location)
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

    private fun locationViewLongClick(position: Int) {
        val location = locationList[position]
        Alerts().removeConfirmation(location.locationName, context) {
                shouldDelete ->
            if (shouldDelete) removeLocation(position)
        }
    }

    private fun locationViewClick(position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val location = locationList[position]
            val editLocationBinding: EditLocationBinding = EditLocationBinding.inflate(LayoutInflater.from(context))
            val popupDialog = Dialog(context)
            popupDialog.setContentView(editLocationBinding.root)
            // TODO add setOnDismissListener

            val viewModel = ItemViewModel()
            viewModel.name = location.locationName
            viewModel.qr = location.locationQR
            viewModel.description = location.locationDescription
            viewModel.tags = location.locationTags
            editLocationBinding.viewModel = viewModel

            popupDialog.window?.setLayout(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)

            editLocationBinding.cancelButton.setOnClickListener { popupDialog.dismiss() }
            editLocationBinding.saveButton.setOnClickListener {
                val editLocationRequest = LocationEditRequest(
                    location.locationUID,
                    editLocationBinding.name.text.toString(),
                    editLocationBinding.Note.text.toString(),
                    editLocationBinding.tags.text.toString(),
                    editLocationBinding.qr.text.toString()
                )
                saveOwnershipButton(location ,editLocationRequest)
                popupDialog.dismiss()
            }
            popupDialog.show()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun saveOwnershipButton(location: Location, editLocationRequest: LocationEditRequest) {
        lifecycleOwner.lifecycleScope.launch {
            val response = api.locationEdit(editLocationRequest)
            if (response.success) {
                location.locationName = editLocationRequest.locationName
                notifyDataSetChanged()
            }
        }
    }

}
