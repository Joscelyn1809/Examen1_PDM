package com.example.examen_blackjack

class Croupier {
    val hand = mutableListOf<Card>()

    fun recibirCarta(card: Card) {
        hand.add(card)
    }

    fun limpiarMano() {
        hand.clear()
    }

    fun getHandValue(): Int {
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
            if (puntaje > gamevars.numberCards.toInt() && numAces != 0) {
                puntaje -= 10
                numAces--
            }
        }
        return puntaje
    }

    fun shouldDrawCard(): Boolean {
        return getHandValue() < 17
    }

    fun mostrarMano(): String{
        var mano = ""
        for (carta in hand) {
            mano += "${carta.toString()},"
        }
        return mano
    }
}