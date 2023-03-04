package com.example.task.helperClass

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.ViewGroup
import com.example.task.databinding.RowProgressBarLayoutBinding

class Utility(private val activity: Activity) {

    private var dialogProgressBar: Dialog? = null

    fun checkInternetConnection(): Boolean {
        val connectivity =
            activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivity.allNetworkInfo
        for (i in info.indices) if (info[i].state == NetworkInfo.State.CONNECTED) {
            return true
        }
        return false
    }

    fun progressDialogShow() {
        val viewDialogBinding = RowProgressBarLayoutBinding.inflate(activity.layoutInflater)
        dialogProgressBar = Dialog(activity)
        dialogProgressBar!!.setContentView(viewDialogBinding.root)
        dialogProgressBar!!.setCanceledOnTouchOutside(false)
        dialogProgressBar!!.setCancelable(false)
        dialogProgressBar!!.window!!.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )

        dialogProgressBar!!.window!!.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialogProgressBar!!.show()
    }

    fun progressDialogHide() {
        if (dialogProgressBar != null) {
            if (dialogProgressBar!!.isShowing) {
                dialogProgressBar!!.dismiss()
            }
        }
    }
}