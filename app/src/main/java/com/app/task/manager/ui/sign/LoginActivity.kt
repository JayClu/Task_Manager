package com.app.task.manager.ui.sign

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.task.manager.R
import com.app.task.manager.ui.theme.Color1
import com.app.task.manager.ui.theme.Color2
import com.app.task.manager.ui.theme.Color3
import com.app.task.manager.ui.theme.Color4
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

    @Preview(showBackground = true)
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
                    fontFamily = FontFamily(Font(R.font.manrope_bold_700, FontWeight.Bold, FontStyle.Normal)),
                    fontSize = 40.sp,
                    color = Color(0xFF0C90F1),
                    modifier = Modifier.padding(top = 40.dp),
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
                    fontFamily = FontFamily(Font(R.font.manrope_medium_500, FontWeight.Medium, FontStyle.Normal)),
                    fontSize = 25.sp,
                    color = Color(0xFF212121),
                    modifier = Modifier.padding(top = 25.dp)
                )

                Text(
                    text = "Collaborate and Plan Together Across Multiple Devices with Project Manager",
                    fontFamily = FontFamily(Font(R.font.manrope_medium_500, FontWeight.Medium, FontStyle.Normal)),
                    fontSize = 16.sp,
                    color = Color(0xFF757575),
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 15.dp, bottom = 40.dp)
                        .fillMaxWidth()
                )

                Box(
                    modifier = with (Modifier){
                        padding(bottom = 15.dp)
                        fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color1,
                                        Color2
                                    )
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable {
                                startActivity(
                                    Intent(this@LoginActivity, SignInActivity::class.java)
                                )
                            }
                    }
                ) {
                    Text(
                        text = "Sign in",
                        fontFamily = FontFamily(Font(R.font.manrope_medium_500, FontWeight.Medium, FontStyle.Normal)),
                        fontSize = 18.sp,
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(top = 12.dp, bottom = 12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                Box(
                    modifier = with (Modifier){
                        fillMaxWidth()
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .clickable {
                                startActivity(
                                    Intent(this@LoginActivity, SignUpActivity::class.java)
                                )
                            }
                            .border(1.dp, Color4, RoundedCornerShape(10.dp))
                    }
                ) {
                    Text(
                        text = "Sign up",
                        fontFamily = FontFamily(Font(R.font.manrope_medium_500, FontWeight.Medium, FontStyle.Normal)),
                        fontSize = 18.sp,
                        color = Color3,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(top = 12.dp, bottom = 12.dp)
                    )
                }
            }
        }
    }
}