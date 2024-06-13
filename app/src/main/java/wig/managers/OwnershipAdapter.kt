package wig.managers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import wig.R
import wig.models.entities.Ownership

class OwnershipAdapter(private val ownershipList: MutableList<Ownership>) :
    RecyclerView.Adapter<OwnershipAdapter.OwnershipViewHolder>() {

    inner class OwnershipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.txtOwnershipName)
        val locationTextView: TextView = itemView.findViewById(R.id.txtLocationName)
        val quantityTextView: TextView = itemView.findViewById(R.id.txtQuantity)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val ownership = ownershipList[position]
                    // TODO Handle item click here

                }
            }

            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val ownership = ownershipList[position]
                    // TODO Handle item long click here

                    return@setOnLongClickListener true
                }
                return@setOnLongClickListener false
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
        ownershipList.add(ownership)
        notifyItemInserted(ownershipList.size - 1)
    }

    fun removeOwnership(position: Int) {
        ownershipList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateOwnership(position: Int, ownership: Ownership) {
        ownershipList[position] = ownership
        notifyItemChanged(position)
    }
}
