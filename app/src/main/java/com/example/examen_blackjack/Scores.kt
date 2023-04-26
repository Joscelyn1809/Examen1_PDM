package com.example.examen_blackjack

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.examen_blackjack.ui.theme.Examen_BlackJackTheme

class Scores : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val darkMode = remember {
            mutableStateOf(false)
        }
            darkMode.value = intent.extras!!.getBoolean("darkMode")
            Examen_BlackJackTheme {

                Examen_BlackJackTheme(darkTheme = darkMode.value) {
                    ScoresView(darkMode)
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScoresView(darkMode: MutableState<Boolean>) {
    Scaffold(
        topBar = { ScoresTopBar(darkMode) },
        content = {}
    )
}

@Composable
fun ScoresTopBar(darkMode: MutableState<Boolean>) {
    val context = LocalContext.current
    TopAppBar(
        title = {
            Text(
                text = "Scores",
                color = Color.White
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                (context as Activity).finish()
            }) {
                Icon(
                    //Icons.Filled.ArrowBack,
                    painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        },
        backgroundColor = colorResource(id = R.color.dark_red),
        elevation = 4.dp
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Examen_BlackJackTheme {
        //ScoresView(darkMode)
    }
}