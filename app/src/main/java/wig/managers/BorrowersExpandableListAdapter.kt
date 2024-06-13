package wig.managers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import wig.R
import wig.models.entities.Ownership
import wig.models.responses.Borrowers

class BorrowersExpandableListAdapter(
    private val context: Context,
    private val borrowersList: MutableList<Borrowers>
) : BaseExpandableListAdapter() {

    override fun getGroup(groupPosition: Int): Borrowers {
        return borrowersList[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Ownership {
        return borrowersList[groupPosition].ownerships[childPosition]
    }

    override fun getGroupCount(): Int {
        return borrowersList.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return borrowersList[groupPosition].ownerships.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.inventory_location_list_group, parent, false)
        val txtBorrowerName = view.findViewById<TextView>(R.id.txtBorrowerName)
        val borrower = getGroup(groupPosition)
        txtBorrowerName.text = borrower.borrower.borrowerName
        return view
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.inventory_ownership_list_item, parent, false)
        val txtOwnershipDescription = view.findViewById<TextView>(R.id.txtOwnershipDescription)
        val ownership = getChild(groupPosition, childPosition)
        txtOwnershipDescription.text = ownership.customItemName
        return view
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    fun removeGroup(groupPosition: Int) {
        borrowersList.removeAt(groupPosition)
        notifyDataSetChanged()
    }

    fun removeChild(groupPosition: Int, childPosition: Int) {
        getGroup(groupPosition).ownerships.removeAt(childPosition)
        notifyDataSetChanged()
    }
}
