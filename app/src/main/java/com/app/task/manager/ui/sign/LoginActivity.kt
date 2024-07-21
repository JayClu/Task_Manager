package com.app.task.manager.ui.sign

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.task.manager.R
import com.app.task.manager.ui.theme.TaskManagerAppTheme

class LoginActivity : ComponentActivity() {
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

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Image(
                painter = painterResource(id = R.drawable.intro_background),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                //verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Task Manager",
                    fontFamily = FontFamily.Serif,
                    fontSize = 40.sp,
                    color = Color(0xFF0C90F1),
                    modifier = Modifier.padding(top = 40.dp),
                    //style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold)
                )


                Image(
                    painter = painterResource(id = R.drawable.ic_task_image),
                    contentDescription = null,
                    modifier = Modifier
                        .size(250.dp)
                        .padding(top = 30.dp),
                    contentScale = ContentScale.Fit
                )


                Text(
                    text = "Let's Get Started",
                    fontFamily = FontFamily.Serif,
                    fontSize = 25.sp,
                    color = Color(0xFF212121),
                    modifier = Modifier.padding(top = 25.dp)
                )

                // Description
                Text(
                    text = "Collaborate and Plan Together Across Multiple Devices with Project Manager",
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp,
                    color = Color(0xFF757575),
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .fillMaxWidth()
                )

                Button(
                    onClick = {
                        startActivity(
                            Intent(this@LoginActivity, SignInActivity::class.java)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    //colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF00ACC1)), // Replace with @drawable/shape_button_rounded if using a drawable resource
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Text(
                        text = "Sign in",
                        fontFamily = FontFamily.Serif, // Replace with your fontFamily
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }

                // Sign Up Button
                Button(
                    onClick = {
                        startActivity(
                            Intent(this@LoginActivity, SignUpActivity::class.java)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp),
                    //colors = ButtonDefaults.buttonColors(backgroundColor = Color.White), // Replace with @drawable/white_border_shape_button_rounded if using a drawable resource
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Text(
                        text = "Sign up",
                        fontFamily = FontFamily.Serif, // Replace with your fontFamily
                        fontSize = 18.sp,
                        color = Color(0xFF00ACC1) // Replace with @color/colorAccent
                    )
                }
            }
        }
    }
}