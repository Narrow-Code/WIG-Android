package wig.managers

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import wig.R
import wig.models.entities.Ownership
import wig.utils.Alerts

class SearchOwnershipAdapter(private val ownershipList: MutableList<Ownership>,
                             private val context: Context,
                             private val ownershipAdapter: OwnershipAdapter
) :
    RecyclerView.Adapter<SearchOwnershipAdapter.OwnershipViewHolder>() {

    inner class OwnershipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.txtName)

        init {
            itemView.setOnClickListener {
                    ownershipViewClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OwnershipViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_item, parent, false)
        return OwnershipViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: OwnershipViewHolder, position: Int) {
        val ownership = ownershipList[position]
        if (ownership.customItemName.length >= 30){
            holder.nameTextView.text = ownership.customItemName.substring(0, 30)
        } else {
            holder.nameTextView.text = ownership.customItemName
        }
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

    private fun removeOwnership(position: Int) {
        ownershipList.removeAt(position)
        notifyItemRemoved(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearOwnerships() {
        ownershipList.clear()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun ownershipViewClick(position: Int) {
        val ownership = ownershipList[position]
        Alerts().addConfirmation(ownership.customItemName, context) {
                shouldAdd ->
            if (shouldAdd) {
                ownershipAdapter.addOwnership(ownership)
                removeOwnership(position)
            }
        }
    }

}
