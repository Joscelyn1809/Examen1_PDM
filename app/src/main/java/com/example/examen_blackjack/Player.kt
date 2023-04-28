package com.example.examen_blackjack

class Player(private val name: String) {
    val hand = mutableListOf<Card>()
    val namePLayer = name

    fun recibirCarta(card: Card) {
        hand.add(card)
    }

    fun getHandValue(numCards: Int): Int {
        var puntaje = 0
        var numAces = 0
        for (carta: Card in hand!!) {
            if (carta.value == "A") {
                numAces += 1
                puntaje += 11
            } else if (carta.value.toIntOrNull() != null) {
                puntaje += carta.value.toInt()
            } else {
                puntaje += 10
            }
        }
        for (i in 0..numAces) {
            if (puntaje > numCards && numAces != 0) {
                puntaje -= 10
                numAces--
            }
        }
        return puntaje
    }

    fun mostrarMano():String {
        var mano = ""
        for (carta in hand) {
            mano += "${carta.toString()},"
        }
        return mano
    }
}