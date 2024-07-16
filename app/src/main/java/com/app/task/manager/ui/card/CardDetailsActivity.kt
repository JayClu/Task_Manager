package com.app.task.manager.ui.card

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.app.task.manager.R
import com.app.task.manager.base.BaseActivity
import com.app.task.manager.databinding.ActivityCardDetailsBinding
import com.app.task.manager.databinding.ActivityTaskListBinding

class CardDetailsActivity : BaseActivity<ActivityCardDetailsBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initData()
        initListener()
    }

    private fun initView() {

    }

    private fun initData() {

    }

    private fun initListener() {

    }

    override fun inflateViewBinding(layoutInflater: LayoutInflater): ActivityCardDetailsBinding {
        return ActivityCardDetailsBinding.inflate(layoutInflater)
    }
}