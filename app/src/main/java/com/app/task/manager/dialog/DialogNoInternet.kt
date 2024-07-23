package com.app.task.manager.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AlertDialog
import com.app.task.manager.R
import com.app.task.manager.databinding.LayoutDialogNoInternetBinding
import com.app.task.manager.utils.ex.setOnTouchScale

class DialogNoInternet(private val context: Context) {

    private val binding by lazy {
        LayoutDialogNoInternetBinding.inflate(LayoutInflater.from(context))
    }

    private val dialog: AlertDialog by lazy {
        AlertDialog.Builder(context, R.style.dialog_transparent_width).setView(binding.root)
            .create()
    }

    init {
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
    }

    fun isShowing(): Boolean {
        return dialog.isShowing
    }

    fun hide() {
        dialog.dismiss()
    }

    fun show(action: () -> Unit) {

        dialog.setCancelable(false)

        binding.txtConnect.setOnTouchScale{
            action()
        }

        if (!dialog.isShowing)
            dialog.show()
    }
}