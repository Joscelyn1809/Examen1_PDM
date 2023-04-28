package com.example.examen_blackjack

enum class Palo {
    HEARTS, DIAMONDS, CLUBS, SPADES, BACK
}

class Card(val palo: Palo, val value: String) {
    override fun toString(): String {
        return "$palo-$value"
    }
}

