package com.app.task.manager.ui.sign

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.task.manager.R
import com.app.task.manager.ui.theme.TaskManagerAppTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.app.task.manager.firebase.FireStoreHandler
import com.app.task.manager.models.User

class SignUpActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskManagerAppTheme {
                MainScreen()
            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun MainScreen() {

        var name by remember {
            mutableStateOf("")
        }

        //val backgroundPainter = painterResource(id = R.drawable.ic_background)
        //val buttonBackground = painterResource(id = R.drawable.shape_button_rounded)

        Surface(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    TextField(
                        value = name,
                        onValueChange = {
                            name = it
                        },
                        label = { Text("Name") }
                    )

                    Button(
                        onClick = {
                            name = "ạhfjkdshgfkhjgsd"
                            FireStoreHandler().register(this@SignUpActivity) {
                                Toast.makeText(this@SignUpActivity, "Okla", Toast.LENGTH_LONG).show()
                            }
                        },
                    ) {
                        Text(
                            text = "Sign in",
                            fontFamily = FontFamily.Serif,
                            fontSize = 18.sp,
                            color = Color.White,
                            modifier = Modifier
                                .padding(top = 12.dp, bottom = 12.dp)
                        )
                    }
                }
            }
        }
    }

}