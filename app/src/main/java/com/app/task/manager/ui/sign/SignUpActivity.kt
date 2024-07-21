package com.app.task.manager.ui.sign

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.app.task.manager.ui.theme.TaskManagerAppTheme

class SignUpActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskManagerAppTheme {
                MainScreen()
            }
        }
    }



    @Composable
    fun MainScreen() {

    }
}