package com.example.examen_blackjack

class Deck {
    private val cards = mutableListOf<Card>()

    init {
        val palos = enumValues<Palo>()
        //crea las cartas del 2 al 10 de cada palo
        for (i in 0..3) {
            for (value in 2..10) {
                cards.add(Card(palos[i], value.toString()))
            }
            //añade las cartas especiales a cada palo
            cards.add(Card(palos[i], "J"))
            cards.add(Card(palos[i], "Q"))
            cards.add(Card(palos[i], "K"))
            cards.add(Card(palos[i], "A"))
        }
    }

    fun shuffle() {
        cards.shuffle()
        //cards.shuffle()
    }

    fun drawCard(): Card {
        //return if(cards.size == 0) {
        //} else {
        val dealtCard = cards.get(1)
        cards.remove(dealtCard)
        return dealtCard
        //}
    }
}