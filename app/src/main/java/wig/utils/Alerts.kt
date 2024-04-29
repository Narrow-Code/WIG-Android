package wig.utils

import android.app.AlertDialog
import android.content.Context

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
}