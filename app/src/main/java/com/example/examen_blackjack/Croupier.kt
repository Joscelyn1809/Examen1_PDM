package com.example.examen_blackjack

class Croupier {
    val hand = mutableListOf<Card>()

    fun recibirCarta(card: Card) {
        hand.add(card)
    }

    fun limpiarMano() {
        hand.clear()
    }

    fun getHandValue(): Int{
        var value = 0
        var aces = 0

        for (card in hand) {
            if (card.value == "A") {
                aces++ //contador de aces
            } else if (card.value.toIntOrNull() != null) {
                //Para el valor de todas las cartas con numero
                value += card.value.toInt()
            } else {
                value += 10 //Para J, Q, K
            }

            for (i in 1..aces) {
                if (value + 11 <= 21) {
                    value += 11
                } else {
                    value = +1
                }
            }

        }

        return value
    }

    fun shouldDrawCard(): Boolean {
        return getHandValue() < 17
    }
}