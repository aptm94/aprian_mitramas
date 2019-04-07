package com.apriantrimulyana.mitramas.util

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import com.apriantrimulyana.mitramas.R
import kotlinx.android.synthetic.main.dialog_loading.view.*

object OtherUtil {

    var alertDialog: AlertDialog? = null

    fun showAlertDialogLoading(context: Context, pesan: String) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val builder1: android.support.v7.app.AlertDialog.Builder
        builder1 = android.support.v7.app.AlertDialog.Builder(context)
        val view = inflater.inflate(R.layout.dialog_loading, null)
        val textView1: TextView? = view.textViewPesanDialog
        //val textView1 = view.findViewById(R.id.textViewPesanDialog) as TextView
        textView1!!.text = pesan
        builder1.setView(view)
        alertDialog = builder1.create()
        //alertDialog.getWindow().setBackgroundDrawableResource(0);
        alertDialog!!.setCancelable(false)
        alertDialog!!.setCanceledOnTouchOutside(false)
        alertDialog!!.show()
    }

    /**
     * Hide alert dialog merupakan function untuk menutup dialog loading.
     */
    fun hideAlertDialog(): Boolean {
        if (alertDialog != null && alertDialog!!.isShowing) {
            alertDialog!!.dismiss()
            return true
        }
        return false
    }
}
