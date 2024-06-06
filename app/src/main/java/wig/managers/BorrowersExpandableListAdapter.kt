package wig.managers

import android.content.Context
import android.util.Log
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
    private val borrowersList: List<Borrowers>
) : BaseExpandableListAdapter() {

    override fun getGroup(groupPosition: Int): Any {
        return borrowersList[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
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
        val convertView = convertView ?: LayoutInflater.from(context).inflate(R.layout.borrower_list_group, parent, false)
        val txtBorrowerName = convertView.findViewById<TextView>(R.id.txtBorrowerName)
        val borrower = getGroup(groupPosition) as Borrowers
        txtBorrowerName.text = borrower.borrower.borrowerName
        return convertView
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val convertView = convertView ?: LayoutInflater.from(context).inflate(R.layout.ownership_list_item, parent, false)
        val txtOwnershipDescription = convertView.findViewById<TextView>(R.id.txtOwnershipDescription)
        val ownership = getChild(groupPosition, childPosition) as Ownership
        txtOwnershipDescription.text = ownership.customItemName
        return convertView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}
