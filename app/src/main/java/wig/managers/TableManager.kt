package wig.managers

import android.graphics.Color
import android.widget.LinearLayout
import android.widget.TableRow

class TableManager {

    private val tableRowMap = mutableMapOf<String, TableRow>()

    // resetRowColors makes sure all colors of rows stay consistent
    fun resetRowColors(tableLayout: LinearLayout) {
        for (i in 0 until tableLayout.childCount) {
            val row = tableLayout.getChildAt(i) as? TableRow
            row?.let { setColorForRow(it, i) }
        }
    }

    // setColorForRow rotates and sets the colors for each created row
    fun setColorForRow(row: TableRow, position: Int) {
        val backgroundColor = if (position % 2 == 0) Color.BLACK else Color.DKGRAY
        row.setBackgroundColor(backgroundColor)
    }

}