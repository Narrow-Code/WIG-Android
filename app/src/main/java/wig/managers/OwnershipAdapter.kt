package wig.managers

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableRow
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import wig.R
import wig.api.API
import wig.databinding.EditOwnershipBinding
import wig.models.entities.ItemViewModel
import wig.models.entities.Ownership
import wig.models.requests.OwnershipEditRequest
import wig.utils.Alerts

class OwnershipAdapter(private val ownershipList: MutableList<Ownership>,
                       private val lifecycleOwner: LifecycleOwner,
                       private val api: API,
                       private val context: Context
) :
    RecyclerView.Adapter<OwnershipAdapter.OwnershipViewHolder>() {

    inner class OwnershipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.txtOwnershipName)
        val locationTextView: TextView = itemView.findViewById(R.id.txtLocationName)
        val quantityTextView: TextView = itemView.findViewById(R.id.txtQuantity)
        private val plusButton: Button = itemView.findViewById(R.id.btnPlus)
        private val minusButton: Button = itemView.findViewById(R.id.btnMinus)

        init {
            itemView.setOnClickListener {
                    ownershipViewClick(adapterPosition)
            }

            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    ownershipViewLongClick(position)
                    return@setOnLongClickListener true
                }
                return@setOnLongClickListener false
            }

            plusButton.setOnClickListener {
                plusButtonClick(adapterPosition)
            }

            minusButton.setOnClickListener {
                minusButtonClick(adapterPosition)
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
        if (ownership.customItemName.length >= 30){
            holder.nameTextView.text = ownership.customItemName.substring(0, 30)

        } else {
            holder.nameTextView.text = ownership.customItemName
        }
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

    private fun removeOwnership(position: Int) {
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

    private fun plusButtonClick(position: Int) {
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

    private fun minusButtonClick(position: Int) {
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

    private fun ownershipViewLongClick(position: Int) {
        val ownership = ownershipList[position]
        Alerts().removeConfirmation(ownership.customItemName, context) {
                shouldDelete ->
            if (shouldDelete) removeOwnership(position)
        }
    }

    private fun ownershipViewClick(position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val ownership = ownershipList[position]
            val editOwnershipBinding: EditOwnershipBinding = EditOwnershipBinding.inflate(LayoutInflater.from(context))
            val popupDialog = Dialog(context)
            popupDialog.setContentView(editOwnershipBinding.root)
            // TODO add setOnDismissListener

            val viewModel = ItemViewModel()
            viewModel.name = ownership.customItemName
            viewModel.qr = ownership.itemQR
            viewModel.description = ownership.customItemDescription
            viewModel.tags = ownership.itemTags
            editOwnershipBinding.viewModel = viewModel

            popupDialog.window?.setLayout(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)

            editOwnershipBinding.cancelButton.setOnClickListener { popupDialog.dismiss() }
            editOwnershipBinding.saveButton.setOnClickListener {
                val editOwnershipRequest = OwnershipEditRequest(
                    ownership.ownershipUID,
                    editOwnershipBinding.name.text.toString(),
                    "",
                    editOwnershipBinding.Note.text.toString(),
                    editOwnershipBinding.tags.text.toString(),
                    editOwnershipBinding.qr.text.toString()
                )
                saveOwnershipButton(ownership ,editOwnershipRequest)
                popupDialog.dismiss()
            }
            popupDialog.show()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun saveOwnershipButton(ownership: Ownership, editOwnershipRequest: OwnershipEditRequest) {
        lifecycleOwner.lifecycleScope.launch {
            val response = api.ownershipEdit(editOwnershipRequest)
            if (response.success) {
                ownership.customItemName = editOwnershipRequest.customItemName
                notifyDataSetChanged()
            }
        }
    }

}
