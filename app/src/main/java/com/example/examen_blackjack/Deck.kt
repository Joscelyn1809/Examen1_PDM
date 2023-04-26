package com.example.examen_blackjack

class Deck {
    private val cards = mutableListOf<Card>()

    init {
        val palos = enumValues<Palo>()
        //crea las cartas del 2 al 10 de cada palo
        for (palo in palos) {
            for (value in 2..10) {
                cards.add(Card(palo, value.toString()))
            }
            //añade las cartas especiales a cada palo
            cards.add(Card(palo, "J"))
            cards.add(Card(palo, "Q"))
            cards.add(Card(palo, "K"))
            cards.add(Card(palo, "A"))
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

    fun deal(numberOfCards: Int): List<Card> {
        //si el numero de cartas a dar es mayor que las
        // restantes retorna una lista vacia
        return if (numberOfCards > cards.size) {
            emptyList()
        } else {
            //hace una lista de las cartas repartidas
            // y las elimina de la baraja
            val dealtCards = cards.take(numberOfCards)
            cards.removeAll(dealtCards)
            return dealtCards
        }
    }

    fun remainingCard(): Int {
        return cards.size
    }

    fun reset() {
        cards.clear()
        val palos = enumValues<Palo>()
        //crea las cartas del 2 al 10 de cada palo
        for (palo in palos) {
            for (value in 2..10) {
                cards.add(Card(palo, value.toString()))
            }
            //añade las cartas especiales a cada palo
            cards.add(Card(palo, "J"))
            cards.add(Card(palo, "Q"))
            cards.add(Card(palo, "K"))
            cards.add(Card(palo, "A"))
        }
    }
}