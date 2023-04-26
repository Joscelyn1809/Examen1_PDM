package com.example.examen_blackjack

enum class Palo{
    HEARTS, DIAMONDS, CLUBS, SPADES
}

data class Card(val palo: Palo, val value: String)