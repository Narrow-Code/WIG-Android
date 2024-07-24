package wig.managers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import wig.R
import wig.models.entities.Ownership
import wig.models.responses.InventoryDTO

class InventoryExpandableListAdapter(
    private val context: Context,
    private val inventory: InventoryDTO
) : BaseExpandableListAdapter() {

    override fun getGroup(groupPosition: Int): InventoryDTO {
        return if (groupPosition == 0){
            inventory
        } else {
            return inventory.locations?.get(groupPosition - 1) ?: inventory
        }
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Ownership {
        return if (groupPosition == 0) {
            inventory.ownerships?.get(childPosition) ?: throw IllegalStateException("Ownership not found")
        } else {
            inventory.locations?.get(groupPosition - 1)?.ownerships?.get(childPosition)
                ?: throw IllegalStateException("Ownership not found")
        }
    }

    override fun getGroupCount(): Int {
        return (inventory.locations?.size?.plus(1))?: 1
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return if (groupPosition == 0) {
            inventory.ownerships?.size ?: 0
        } else {
            inventory.locations?.get(groupPosition - 1)?.ownerships?.size ?: 0
        }
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.inventory_location_list_group, parent, false)
        val txtLocationName = view.findViewById<TextView>(R.id.txtBorrowerName)

        if (groupPosition == 0) {
            txtLocationName.text = context.getString(R.string.unpacked_items)
        } else {
            val location = getGroup(groupPosition)
            txtLocationName.text = location.parent.locationName
        }

        return view
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.inventory_ownership_list_item, parent, false)
        val txtItemName = view.findViewById<TextView>(R.id.txtOwnershipDescription)

        val ownership = getChild(groupPosition, childPosition)
        txtItemName.text = ownership.customItemName

        return view
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    fun removeChild(groupPosition: Int, childPosition: Int) {
        getGroup(groupPosition).ownerships?.removeAt(childPosition)
        notifyDataSetChanged()
    }
}
