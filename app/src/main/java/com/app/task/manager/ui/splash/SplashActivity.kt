package com.app.task.manager.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.app.task.manager.databinding.ActivitySplashBinding
import com.app.task.manager.firebase.FireStoreHandler
import com.app.task.manager.ui.main.MainActivity
import com.app.task.manager.ui.sign.LoginActivity
import com.app.task.manager.utils.ex.openActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler().postDelayed(
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

}