package com.app.task.manager.ui.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.app.task.manager.R
import com.app.task.manager.base.BaseActivity
import com.app.task.manager.databinding.ActivityMyProfileBinding
import com.app.task.manager.databinding.ActivityTaskListBinding

class TaskListActivity : BaseActivity<ActivityTaskListBinding>() {
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

    override fun inflateViewBinding(layoutInflater: LayoutInflater): ActivityTaskListBinding {
        return ActivityTaskListBinding.inflate(layoutInflater)
    }
}