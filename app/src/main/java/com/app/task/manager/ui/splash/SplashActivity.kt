package com.app.task.manager.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import com.app.task.manager.base.BaseActivity
import com.app.task.manager.databinding.ActivitySplashBinding
import com.app.task.manager.firebase.FireStoreHandler
import com.app.task.manager.ui.main.MainActivity
import com.app.task.manager.ui.sign.LoginActivity
import com.app.task.manager.utils.ex.openActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                val currentUserID = FireStoreHandler().getCurrentUserId()
                if (currentUserID.isNotEmpty()) {
                    openActivity(MainActivity::class.java, true)
                } else {
                    openActivity(LoginActivity::class.java, true)
                }
            },
            2500
        )
    }

    override fun inflateViewBinding(layoutInflater: LayoutInflater): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

}