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

class Game : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val darkMode = remember {
                mutableStateOf(false)
            }

            gamevars.name = intent.getStringExtra("name").toString()
            gamevars.numberCards = intent.getStringExtra("cards").toString()
            darkMode.value = intent.extras!!.getBoolean("darkMode")

            Examen_BlackJackTheme(darkTheme = darkMode.value) {
                GameView(darkMode.value)
            }
        }
    }
}

class GameVariables : ViewModel() {
    var name = "Jugador"
    var numberCards = "21"
    var puntuacion = "0"
    var onWinState = mutableStateOf(false)
}

val gamevars = GameVariables()
val player = Player("name")
val croupier = Croupier()
val deck = Deck()

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GameView(darkMode: Boolean) {
    val context = LocalContext.current
    Scaffold(
        topBar = { GameTopBar(darkMode) },

        content = {
            //BlackjackGame()
            var croupierHand by remember {
                mutableStateOf(mutableStateListOf<Card>())
            }
            var playerHand by remember {
                mutableStateOf(mutableStateListOf<Card>())
            }
            WelcomeMessage()
            StartButton(croupierHand, playerHand)
            CroupierView(croupierHand)
            BotonesView(playerHand)
            PlayerView(playerHand)
        }
    )

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
        croupierHand.value = croupier.hand.toMutableStateList()
        playerHand.value = player.hand.toMutableStateList()
    }
}

@Composable
fun WelcomeMessage() {
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
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun StartButton(croupierHand: SnapshotStateList<Card>, playerHand: SnapshotStateList<Card>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 20.dp)
            .size(300.dp)
            .clip(CircleShape),
        contentAlignment = Alignment.BottomCenter
    ) {
        Button(
            onClick = { drawCard(mutableStateOf(croupierHand), mutableStateOf(playerHand)) },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
        ) {
            Text(
                text = "Comenzar",
                color = Color.White
            )
        }
    }
}

@Composable
fun BotonesView(playerHand: SnapshotStateList<Card>) {
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
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.dark_red)),
                onClick = { tomarCarta(mutableStateOf(playerHand)) }
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
                    text = "${gamevars.puntuacion}",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
            }


            Button(modifier = Modifier
                .padding(start = 5.dp)
                .size(100.dp)
                .clip(CircleShape),
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.dark_red)),
                onClick = {
                    parar()
                }) {
                Text(
                    text = "Parar",
                    color = Color.White
                )
            }
        }

    }
}

fun tomarCarta(playerHand: MutableState<SnapshotStateList<Card>>) {
    if (player.getHandValue() > 21) {
        gamevars.onWinState.value = false
    } else {
        player.recibirCarta(deck.drawCard())
        playerHand.value = player.hand.toMutableStateList()
    }

}

fun parar() {
    gamevars.onWinState.value = player.getHandValue() <= 21
}

@Composable
fun CroupierView(croupierHand: SnapshotStateList<Card>) {
    Text(
        text = "Croupier",
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 70.dp),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 30.sp
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 130.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        cardRow(cards = croupierHand)
        Text(text = "${croupier.hand}")
    }
}

@Composable
fun PlayerView(playerHand: SnapshotStateList<Card>) {
    Text(text = "${gamevars.onWinState.value}")

    Text(
        text = "${gamevars.name}",
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 650.dp),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 30.sp
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 130.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        cardRow(cards = playerHand)
        Text(text = "${player.hand}")
    }
}

@Composable
fun cardRow(cards: SnapshotStateList<Card>) {
    var hand by remember {
        mutableStateOf(cards.toMutableStateList())
    }
    LazyRow(
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        items(hand!!.size) { card ->
            CardItem(card = hand!![card])
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
    } else if (card.palo.compareTo(Palo.HEARTS) == 0) {
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
    Examen_BlackJackTheme {
        //GameView(darkMode)
    }
}
