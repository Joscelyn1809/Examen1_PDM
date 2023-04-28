package com.example.examen_blackjack

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.examen_blackjack.ui.theme.Examen_BlackJackTheme
import java.time.LocalDateTime

class Game : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val darkMode = remember {
                mutableStateOf(false)
            }

            var nombreJugador by remember {
                mutableStateOf("")  
            }

            var numeroCartas by remember {
                mutableStateOf("")
            }

            gamevars = GameVariables()
            player = Player("name")
            croupier = Croupier()
            deck = Deck()

            gamevars.name = intent.getStringExtra("name").toString()
            gamevars.numberCards = intent.getStringExtra("cards").toString()
            darkMode.value = intent.extras!!.getBoolean("darkMode")

            nombreJugador = gamevars.name
            numeroCartas = gamevars.numberCards

            Examen_BlackJackTheme(darkTheme = darkMode.value) {
                GameView(darkMode.value, nombreJugador, numeroCartas)
            }
        }
    }
}

class GameVariables : ViewModel() {
    var name = "Jugador"
    var numberCards = "21"
    var puntuacion = mutableStateOf("0")
    var onWinState = mutableStateOf(-1)
    var onPlayingState = mutableStateOf(1)
}

var gamevars = GameVariables()
var player = Player("name")
var croupier = Croupier()
var deck = Deck()

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GameView(darkMode: Boolean, nombreJugador: String, numeroCartas: String) {
    val context = LocalContext.current
    Scaffold(
        topBar = { GameTopBar(darkMode) },

        content = {
            //BlackjackGame()
            var croupierHand = remember {
                mutableStateOf(mutableStateListOf<Card>())
            }
            var playerHand = remember {
                mutableStateOf(mutableStateListOf<Card>())
            }

            var isDown = remember {
                mutableStateOf(true)
            }

            WelcomeMessage(darkMode)
            CroupierView(croupierHand, isDown, darkMode)
            BotonesView(playerHand, croupierHand, isDown, darkMode, numeroCartas, nombreJugador)
            PlayerView(playerHand, darkMode)
            rules(numeroCartas)
        }
    )
}

fun rules(numeroCartas: String) {
    if (gamevars.onPlayingState.value == 1) {
        if (player.getHandValue(numeroCartas.toInt()) > numeroCartas.toInt()) {
            gamevars.onPlayingState.value = 0
        }
    }
}

@Composable
fun GameTopBar(darkMode: Boolean) {
    val context = LocalContext.current
    TopAppBar(
        title = {
            Text(
                text = "BlackJack",
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

fun drawCard(
    croupierHand: MutableState<SnapshotStateList<Card>>,
    playerHand: MutableState<SnapshotStateList<Card>>
) {
    deck.shuffle()
    repeat(2) {
        player.recibirCarta(deck.drawCard())
        croupier.recibirCarta(deck.drawCard())
    }
    croupierHand.value = croupier.hand.toMutableStateList()
    playerHand.value = player.hand.toMutableStateList()
}

@Composable
fun WelcomeMessage(darkMode: Boolean) {
    if (gamevars.name == "") {
        gamevars.name = "Jugador"
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Hola ${gamevars.name}! estas jugando con ${gamevars.numberCards}",
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = if (darkMode) {
                Color.White
            } else {
                Color.Black
            }
        )
    }
}

@Composable
fun BotonesView(
    playerHand: MutableState<SnapshotStateList<Card>>,
    croupierHand: MutableState<SnapshotStateList<Card>>,
    isDown: MutableState<Boolean>,
    darkMode: Boolean,
    numeroCartas: String,
    nombreJugador: String
) {
    var buttonState by remember {
        mutableStateOf(false)
    }
    var context = LocalContext.current
    val tinyDB = remember { TinyDB(context) }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Row(modifier = Modifier.align(Alignment.Center)) {
            Button(modifier = Modifier
                .padding(end = 5.dp)
                .size(100.dp)
                .clip(CircleShape),
                enabled = buttonState,
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.dark_red)),
                onClick = {
                    tomarCarta(playerHand, numeroCartas)
                    gamevars.puntuacion = mutableStateOf(player.getHandValue(numeroCartas.toInt()).toString())
                }
            ) {
                Text(
                    text = "Tomar",
                    color = Color.White
                )
            }

            Box(
                modifier = Modifier.size(100.dp),
                contentAlignment = Alignment.Center
            )
            {
                Text(
                    text = gamevars.puntuacion.value,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    color = if (darkMode) {
                        Color.White
                    } else {
                        Color.Black
                    }
                )
            }

            Button(modifier = Modifier
                .padding(start = 5.dp)
                .size(100.dp)
                .clip(CircleShape),
                enabled = buttonState,
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.dark_red)),
                onClick = {
                    parar(croupierHand, numeroCartas, nombreJugador)
                    val gameScores = tinyDB.getListString("gameScores")
                    val ganador = when (gamevars.onWinState.value) {
                        1 -> nombreJugador
                        0 -> "Croupier"
                        else -> "Empate"
                    }
                    val score = ganador + ";" + player.mostrarMano() +
                            ";" + croupier.mostrarMano() + ";" + LocalDateTime.now().toString()
                    gameScores.add(score)
                    tinyDB.putListString("gameScores", gameScores)

                    buttonState = !buttonState
                    isDown.value = false
                    gamevars.puntuacion = mutableStateOf(player.getHandValue(numeroCartas.toInt()).toString())
                }) {
                Text(
                    text = "Parar",
                    color = Color.White
                )
            }
        }
        Text(
            text = when (gamevars.onWinState.value) {
                1 -> "Has Ganado!"
                0 -> "Perdiste!"
                2 -> "Empate"
                else -> ""
            },
            textAlign = TextAlign.Center,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = if (darkMode) {
                Color.White
            } else {
                Color.Black
            },
            modifier = Modifier.padding(top = 40.dp)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 20.dp)
            .size(300.dp)
            .clip(CircleShape),
        contentAlignment = Alignment.BottomCenter
    ) {
        Button(
            onClick = {
                gamevars = GameVariables()
                player = Player(gamevars.name)
                croupier = Croupier()
                deck = Deck()

                drawCard(croupierHand, playerHand)
                buttonState = !buttonState
                isDown.value = true
                gamevars.puntuacion = mutableStateOf(player.getHandValue(numeroCartas.toInt()).toString())
            },
            enabled = !buttonState,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
        ) {
            Text(
                text = "Comenzar",
                color = Color.White
            )
        }
    }
}

fun tomarCarta(playerHand: MutableState<SnapshotStateList<Card>>, numeroCartas: String) {
    if (player.getHandValue(numeroCartas.toInt()) > numeroCartas.toInt()) {
        gamevars.onWinState.value = 0
    } else {
        player.recibirCarta(deck.drawCard())
        playerHand.value = player.hand.toMutableStateList()
    }
}

fun parar(
    croupierHand: MutableState<SnapshotStateList<Card>>,
    numCards: String,
    nombreJugador: String
) {
    var croupierPoints = croupier.getHandValue()
    var playerPoints = player.getHandValue(numCards.toInt())

    while (croupierPoints < playerPoints && croupierPoints < numCards.toInt() &&
        !(playerPoints > numCards.toInt())
    ) {
        croupier.recibirCarta(deck.drawCard())
        croupierPoints = croupier.getHandValue()
    }

    croupierHand.value = croupier.hand.toMutableStateList()

    if (playerPoints in (croupierPoints + 1)..numCards.toInt() || croupierPoints > numCards.toInt()) {
        gamevars.onWinState.value = 1 //Gano jugador
    } else if (playerPoints < croupierPoints || playerPoints > numCards.toInt()) {
        gamevars.onWinState.value = 0 //Perdio jugador
    } else {
        gamevars.onWinState.value = 2 //Empate
    }
}

@Composable
fun CroupierView(
    croupierHand: MutableState<SnapshotStateList<Card>>,
    isDown: MutableState<Boolean>,
    darkMode: Boolean
) {
    Text(
        text = "Croupier",
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 70.dp),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 30.sp,
        color = if (darkMode) {
            Color.White
        } else {
            Color.Black
        }
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 130.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        CardRow(cards = croupierHand, isDown.value)
    }
}

@Composable
fun PlayerView(playerHand: MutableState<SnapshotStateList<Card>>, darkMode: Boolean) {
    Text(
        text = gamevars.name,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 640.dp),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 30.sp,
        color = if (darkMode) {
            Color.White
        } else {
            Color.Black
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 130.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        CardRow(cards = playerHand, false)
    }
}

@Composable
fun CardRow(cards: MutableState<SnapshotStateList<Card>>, b: Boolean) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        items(cards.value.size) { card ->
            if (b && card == 1) {
                val card = Card(Palo.BACK, "0")
                CardItem(card)
            } else {
                CardItem(card = cards.value[card])
            }
        }
    }
}

@Composable
fun CardItem(card: Card) {
    var imageResource by remember { mutableStateOf(R.drawable.dra) }

    if (card.palo.compareTo(Palo.HEARTS) == 0) {
        imageResource = when (card.value) {
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
    } else if (card.palo.compareTo(Palo.DIAMONDS) == 0) {
        imageResource = when (card.value) {
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
    } else if (card.palo.compareTo(Palo.SPADES) == 0) {
        imageResource = when (card.value) {
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
    } else if (card.palo.compareTo(Palo.CLUBS) == 0) {
        imageResource = when (card.value) {
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
    } else if (card.palo.compareTo(Palo.BACK) == 0) {
        imageResource = R.drawable.back
    }

    Card(
        modifier = Modifier
            .width(110.dp)
            .height(170.dp),
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
fun GamePreview() {
    val darkMode = remember {
        mutableStateOf(false)
    }
    var nombreJugador = "jugador"
    var numeroCartas = "21"
    Examen_BlackJackTheme {
        GameView(darkMode.value, nombreJugador, numeroCartas)
    }
}
