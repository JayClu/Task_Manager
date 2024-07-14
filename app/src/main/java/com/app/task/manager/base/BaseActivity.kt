package com.app.task.manager.base

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.app.task.manager.R
import com.app.task.manager.dialog.DialogNoInternet
import com.edge.light.ledscreen.edgescreen.utils.ex.isNetworkAdsConnected
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: VB

    private lateinit var mProgressDialog: Dialog

    companion object {
        const val ACTION_NETWORK_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflateViewBinding(layoutInflater)
        setContentView(binding.root)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(networkReceiver, IntentFilter(ACTION_NETWORK_CHANGE))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkReceiver)
    }

    private val dialogNoInternet by lazy {
        DialogNoInternet(this)
    }

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ACTION_NETWORK_CHANGE) {
                Handler(Looper.getMainLooper()).postDelayed({
                    if (isNetworkAdsConnected()) {
                        dialogNoInternet.hide()
                    } else {
                        if (isShowDialogInternet()) {
                            dialogNoInternet.show {
                                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                            }
                        }
                    }
                }, 1000L)
            }
        }
    }

    open fun onBack() {
        finish()
    }

    private var isClickBack = false
    fun doubleBackToExit() {
        if (isClickBack) {
            finish()
            return
        }
        isClickBack = true
        Toast.makeText(this, "Please press back once again to exit", Toast.LENGTH_LONG).show()
        Handler(Looper.getMainLooper()).postDelayed({
            isClickBack = false
        }, 2000)
    }

    open fun isShowDialogInternet(): Boolean {
        return true
    }

    abstract fun inflateViewBinding(layoutInflater: LayoutInflater): VB


    fun showProgressDialog() {
        mProgressDialog = Dialog(this)
        mProgressDialog.setContentView(R.layout.progress_dialog)
        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

    fun getCurrentUserID(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    fun showErrorSnackBar(message: String) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        snackBar.view.setBackgroundColor(
            ContextCompat.getColor(
                this,
                R.color.snack_bar_error_color
            )
        )
        snackBar.show()
    }

}