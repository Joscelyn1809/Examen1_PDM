package com.example.examen_blackjack

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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

class Home : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val darkMode = remember {
            mutableStateOf(false)
        }
            //darkMode.value = intent.extras!!.getBoolean("darkMode")
            //darkMode.value = intent.extras!!.getBoolean("darkMode")

            Examen_BlackJackTheme(darkTheme = darkMode.value) {
                HomeView(darkMode)
            }
        }
    }
}

class variables : ViewModel() {
    var name = mutableStateOf("Jugador")
    var selectedCardNumber = mutableStateOf("21")
    //var darkTheme = mutableStateOf<Boolean>(false)
}

val vars = variables()

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeView(darkMode: MutableState<Boolean>) {
    val context = LocalContext.current

    Scaffold(
        topBar = { TopBar(darkMode) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    NewActivity(context,darkMode)
                },
                contentColor = Color.White, // Color del contenido del botón
                backgroundColor = Color.DarkGray, // Color del fondo del botón
                modifier = Modifier
                    .width(200.dp)
                    .height(80.dp)
            ) {
                Text(
                    text = "Jugar",
                    fontSize = 35.sp,
                    fontWeight = FontWeight.SemiBold,
                    //modifier = Modifier.padding(15.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,

        content = {
            BlackJackHome()
        }
    )

}

@Composable
fun TopBar(darkMode: MutableState<Boolean>) {
    TopAppBar(
        title = {
            Text(
                text = "BlackJack",
                color = Color.White
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                darkMode.value = !darkMode.value
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_auto_awesome_24),
                    contentDescription = "colorMode",
                    tint = Color.White
                )
            }
        },
        backgroundColor = colorResource(id = R.color.dark_red),
        elevation = 4.dp
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BlackJackHome() {
    val context = LocalContext.current
    var show by rememberSaveable { mutableStateOf(false) }
    MyDialog(show, { show = false })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, bottom = 20.dp)
    )
    {
        Image(
            painter = painterResource(id = R.drawable.blackjack),
            contentDescription = "Logo",
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.TopCenter)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                val scoresActivity = Intent(context, Scores::class.java)
                context.startActivity(scoresActivity)
            },
            modifier = Modifier
                .width(170.dp)
                .height(60.dp)
                .align(Alignment.CenterHorizontally)
                .clip(CircleShape),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.dark_red)
            )

        ) {
            Text(
                text = "Scores",
                color = Color.White,
                fontSize = 25.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Button(
            onClick = { show = true },
            modifier = Modifier
                .width(170.dp)
                .height(70.dp)
                .padding(top = 10.dp)
                .align(Alignment.CenterHorizontally)
                .clip(CircleShape),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.dark_red)
            )

        ) {
            Text(
                text = "Instrucciones",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Box(
            modifier = Modifier
                .width(250.dp),
            //.padding(bottom = 120.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            DropDownMenu()
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .width(250.dp),
            //.padding(bottom = 120.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            NameField()
        }
    }
}

@Composable
fun MyDialog(
    show: Boolean,
    onDismiss: () -> Unit
) {
    if (show) {
        AlertDialog(onDismissRequest = { onDismiss() },
            confirmButton = {
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(
                        text = "Ok",
                        color = colorResource(id = R.color.dark_red)
                    )
                }
            },
            title = { Text(text = "Instrucciones", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = "Este juego consiste en enfrentarse de forma individual a la banca comparando su mano con la propia de cada " +
                            "jugador, intentando conseguir 21 puntos o el número más cercano posible sin pasarse.\n" +
                            "Para conseguir dicha puntuación se suman los valores de dos cartas que se reparten de inicio a cada jugador, con " +
                            "los de aquellas nuevas cartas que, opcionalmente, se podrán añadir en el turno de juego. Si las dos cartas iniciales " +
                            "suman 21, se denomina Blackjack, y es la mejor jugada. Cuando un jugador no suma 21 con sus dos cartas podrá " +
                            "pedir cartas para conseguir dicho número o uno cercano pero si el jugador se pasa de esos 21 puntos pierde, " +
                            "indistintamente de lo que haga la banca.\n" +
                            "La banca también juega pero tiene unas reglas muy definidas que se han de tener en cuenta. Si la suma de las " +
                            "cartas de la banca es 16 o menos, debe pedir carta y si suman 17 o más se debe plantar.",
                    textAlign = TextAlign.Justify
                )
            }
        )
    }
}

@Composable
fun NameField() {
    TextField(
        value = vars.name.value,
        onValueChange = { vars.name.value = it },
        label = { Text("Ingrese su nombre") },
        modifier = Modifier
            .padding(top = 20.dp)
    )
}

@Composable
fun DropDownMenu() {
    val options = listOf("25", "21", "19", "17")

    var expanded by remember { mutableStateOf(false) }

    //vars.selectedCardNumber.value = options.first()

    Column {
        Box(
            modifier = Modifier.padding(top = 10.dp)
        ) {
            // This is the button that will show the menu
            Button(
                onClick = { expanded = true },
                modifier = Modifier
                    .width(170.dp)
                    .height(70.dp)
                    .padding(top = 10.dp)
                    .clip(CircleShape),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = R.color.dark_red)
                )
            ) {
                Text(
                    text = "Cartas: ${vars.selectedCardNumber.value}",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }

            // This is the menu itself
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            vars.selectedCardNumber.value = option
                            expanded = false
                        }
                    ) {
                        Text(text = option)
                    }
                }
            }
        }
    }
}

fun NewActivity(context: Context, darkMode: MutableState<Boolean>) {
    val newActivity = Intent(context, Game::class.java)
    newActivity.putExtra("name", vars.name.value)
    newActivity.putExtra("cards", vars.selectedCardNumber.value)
    newActivity.putExtra("darkMode", darkMode.value)
    context.startActivity(newActivity)
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    Examen_BlackJackTheme {
        //HomeView(darkMode)
    }
}