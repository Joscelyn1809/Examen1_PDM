package com.example.examen_blackjack

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        content = {
            ScoresView()
        }
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

@Composable
fun ScoresView() {
    val context = LocalContext.current
    val tinydb = TinyDB(context)
    val gameScores = tinydb.getListString("gameScores")

    gameScores.reverse()

    LazyColumn(
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
            .fillMaxSize()
            .padding(15.dp)
    ) {
        items(gameScores) { score ->
            val textAux = score.split(';')
            Spacer(modifier = Modifier.height(10.dp))
            Row() {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                        .clip(CircleShape)
                        .background(Color.DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    val fechaSplit = textAux[3].split("T")
                    val fecha = fechaSplit[0]
                    val hora = fechaSplit[1].split(":")
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box() {
                                    Text(
                                        text = "Ganador: ",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                }
                                Box(modifier = Modifier.padding(5.dp)) {
                                    Text(
                                        text = textAux[0],
                                        color = Color.White,
                                        fontSize = 35.sp,
                                        fontFamily = FontFamily.Cursive,
                                        textAlign = TextAlign.Center
                                    )
                                }
                                Row(modifier = Modifier.padding(20.dp)) {
                                    Text(
                                        text = fecha,
                                        color = Color.White,
                                        fontSize = 10.sp
                                    )
                                    Spacer(modifier = Modifier.width(40.dp))
                                    Text(
                                        text = hora[0] + ":" + hora[1],
                                        color = Color.White,
                                        fontSize = 10.sp
                                    )
                                }

                            }
                            Spacer(modifier = Modifier.width(20.dp))
                            Column {
                                Text(
                                    text = "Jugador ",
                                    color = Color.White,
                                    fontSize = 17.sp,
                                    fontFamily = FontFamily.Cursive
                                )
                                LazyRow(contentPadding = PaddingValues(horizontal = 10.dp)) {
                                    val cartas = textAux[1].split(",")
                                    items(cartas.size - 1) { index ->
                                        CardItemScores(card = cartas[index])
                                    }
                                }
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "Crupier",
                                    color = Color.White,
                                    fontSize = 17.sp,
                                    fontFamily = FontFamily.Cursive
                                )
                                LazyRow(contentPadding = PaddingValues(horizontal = 10.dp)) {
                                    val cartas = textAux[2].split(",")
                                    items(cartas.size - 1) { index ->
                                        CardItemScores(card = cartas[index])
                                    }
                                }
                            }

                        }
                    }
                }
            }

        }
    }
}

@Composable
fun CardItemScores(card: String) {
    var imageResource by remember { mutableStateOf(R.drawable.dra) }
    var palo = card.split("-")[0]
    var value = card.split("-")[1]

    if (palo.compareTo(Palo.HEARTS.toString()) == 0) {
        imageResource = when (value) {
            "2" -> R.drawable.hr2
            "3" -> R.drawable.hr3
            "4" -> R.drawable.hr4
            "5" -> R.drawable.hr5
            "6" -> R.drawable.hr6
            "7" -> R.drawable.hr7
            "8" -> R.drawable.hr8
            "9" -> R.drawable.hr9
            "10" -> R.drawable.hr10
            "J" -> R.drawable.hrj
            "Q" -> R.drawable.hrq
            "K" -> R.drawable.hrk
            else -> R.drawable.hra
        }
    } else if (palo.compareTo(Palo.DIAMONDS.toString()) == 0) {
        imageResource = when (value) {
            "2" -> R.drawable.dr2
            "3" -> R.drawable.dr3
            "4" -> R.drawable.dr4
            "5" -> R.drawable.dr5
            "6" -> R.drawable.dr6
            "7" -> R.drawable.dr7
            "8" -> R.drawable.dr8
            "9" -> R.drawable.dr9
            "10" -> R.drawable.dr10
            "J" -> R.drawable.drj
            "Q" -> R.drawable.drq
            "K" -> R.drawable.dra
            else -> R.drawable.dra
        }
    } else if (palo.compareTo(Palo.SPADES.toString()) == 0) {
        imageResource = when (value) {
            "2" -> R.drawable.eb2
            "3" -> R.drawable.eb3
            "4" -> R.drawable.eb4
            "5" -> R.drawable.eb5
            "6" -> R.drawable.eb6
            "7" -> R.drawable.eb7
            "8" -> R.drawable.eb8
            "9" -> R.drawable.eb9
            "10" -> R.drawable.eb10
            "J" -> R.drawable.ebj
            "Q" -> R.drawable.ebq
            "K" -> R.drawable.ebk
            else -> R.drawable.eba
        }
    } else if (palo.compareTo(Palo.CLUBS.toString()) == 0) {
        imageResource = when (value) {
            "2" -> R.drawable.tb2
            "3" -> R.drawable.tb3
            "4" -> R.drawable.tb4
            "5" -> R.drawable.tb5
            "6" -> R.drawable.tb6
            "7" -> R.drawable.tb7
            "8" -> R.drawable.tb8
            "9" -> R.drawable.tb9
            "10" -> R.drawable.tb10
            "J" -> R.drawable.tbj
            "Q" -> R.drawable.tbq
            "K" -> R.drawable.tbk
            else -> R.drawable.tba
        }
    }

    Card(
        modifier = Modifier
            .width(38.dp)
            .height(60.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 8.dp,
        backgroundColor = Color.White
    ) {
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        )
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val darkMode = remember {
        mutableStateOf(false)
    }
    Examen_BlackJackTheme {
        ScoresView(darkMode)
    }
}