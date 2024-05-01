package wig.utils

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText
import android.widget.LinearLayout
import wig.models.entities.Borrower
import wig.models.entities.Ownership

class Alerts {

    fun removeConfirmation(name: String, context: Context, callback: (Boolean) -> Unit) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Confirm Remove")
        alertDialogBuilder.setMessage("Are you sure you want to remove $name from queue?")

        alertDialogBuilder.setPositiveButton("REMOVE") { dialog, _ ->
            dialog.dismiss()
            callback(true)
        }

        alertDialogBuilder.setNegativeButton("CANCEL") { dialog, _ ->
            dialog.dismiss()
            callback(false)
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun addConfirmation(name: String, context: Context, callback: (Boolean) -> Unit) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Confirm Add")
        alertDialogBuilder.setMessage("Are you sure you want to add $name to queue?")

        alertDialogBuilder.setPositiveButton("Add") { dialog, _ ->
            dialog.dismiss()
            callback(true)
        }

        alertDialogBuilder.setNegativeButton("CANCEL") { dialog, _ ->
            dialog.dismiss()
            callback(false)
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun returnAllConfirmation(context: Context, callback: (Boolean) -> Unit) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Confirm Return All")
        alertDialogBuilder.setMessage("Are you sure you want to return all Checked Out items to their original locations??")

        alertDialogBuilder.setPositiveButton("RETURN") { dialog, _ ->
            dialog.dismiss()
            callback(true)
        }

        alertDialogBuilder.setNegativeButton("CANCEL") { dialog, _ ->
            dialog.dismiss()
            callback(false)
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun returnSingleConfirmation(ownership: Ownership, context: Context, callback: (Boolean) -> Unit) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Confirm Return All")
        alertDialogBuilder.setMessage("Are you sure you want to return ${ownership.customItemName} to it's original locations?")

        alertDialogBuilder.setPositiveButton("RETURN") { dialog, _ ->
            dialog.dismiss()
            callback(true)
        }

        alertDialogBuilder.setNegativeButton("CANCEL") { dialog, _ ->
            dialog.dismiss()
            callback(false)
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun returnBorrowerConfirmation(borrower: Borrower, context: Context, callback: (Boolean) -> Unit) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Confirm Return Borrower")
        alertDialogBuilder.setMessage("Are you sure you want to return all of ${borrower.borrowerName}'s borrowed items to their original locations?")

        alertDialogBuilder.setPositiveButton("RETURN") { dialog, _ ->
            dialog.dismiss()
            callback(true)
        }

        alertDialogBuilder.setNegativeButton("CANCEL") { dialog, _ ->
            dialog.dismiss()
            callback(false)
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun showNewBorrowerDialog(context: Context, onPositiveButtonClick: (String) -> Unit) {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("New Borrower")

        val inputEditText = EditText(context)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        inputEditText.layoutParams = layoutParams
        alertDialog.setView(inputEditText)

        alertDialog.setPositiveButton("Create") { _, _ ->
            val borrowerName = inputEditText.text.toString()
            onPositiveButtonClick.invoke(borrowerName)
        }

        alertDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        alertDialog.show()
    }
}