package wig.managers

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import wig.R
import wig.api.API
import wig.models.entities.Ownership

class OwnershipAdapter(private val ownershipList: MutableList<Ownership>,
                       private val lifecycleOwner: LifecycleOwner,
                       private val api: API) :
    RecyclerView.Adapter<OwnershipAdapter.OwnershipViewHolder>() {

    inner class OwnershipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.txtOwnershipName)
        val locationTextView: TextView = itemView.findViewById(R.id.txtLocationName)
        val quantityTextView: TextView = itemView.findViewById(R.id.txtQuantity)
        val plusButton: Button = itemView.findViewById(R.id.btnPlus)
        val minusButton: Button = itemView.findViewById(R.id.btnMinus)

        init {
            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val ownership = ownershipList[position]
                    // TODO Handle item long click here

                    return@setOnLongClickListener true
                }
                return@setOnLongClickListener false
            }

            plusButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val ownership = ownershipList[position]
                    lifecycleOwner.lifecycleScope.launch {
                        val response = api.ownershipQuantity("increment", 1, ownership.ownershipUID)
                        if (response.success) {
                            ownership.itemQuantity += 1
                            notifyItemChanged(position)
                        }
                    }
                }
            }

            minusButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val ownership = ownershipList[position]
                    if (ownership.itemQuantity > 0) {
                        lifecycleOwner.lifecycleScope.launch {
                            val response = api.ownershipQuantity("decrement", 1, ownership.ownershipUID)
                            if (response.success) {
                                ownership.itemQuantity -= 1
                                notifyItemChanged(position)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OwnershipViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.scanner_ownership_list_item, parent, false)
        return OwnershipViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: OwnershipViewHolder, position: Int) {
        val ownership = ownershipList[position]
        holder.nameTextView.text = ownership.customItemName.substring(0, 30)
        holder.locationTextView.text = ownership.location.locationName
        holder.quantityTextView.text = ownership.itemQuantity.toString()
    }

    override fun getItemCount(): Int {
        return ownershipList.size
    }

    fun addOwnership(ownership: Ownership) {
        var exists = false

        for (item in ownershipList) {
            if (item.ownershipUID == ownership.ownershipUID) {
                exists = true
            }
        }

        if (!exists) {
            ownershipList.add(ownership)
            notifyItemInserted(ownershipList.size - 1)
        }
    }

    fun removeOwnership(position: Int) {
        ownershipList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateOwnership(position: Int, ownership: Ownership) {
        ownershipList[position] = ownership
        notifyItemChanged(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearOwnerships() {
        ownershipList.clear()
        notifyDataSetChanged()
    }
}
