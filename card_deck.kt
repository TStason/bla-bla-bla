import java.util.*
import kotlin.math.max
import kotlin.math.min

fun main(){
    val deck = Deck(DeckType.STANDARD_WITH_JOKERS)
    //val deck = Deck(DeckType.SHORT)
    deck.shuffle()
    do {
        val card = deck.getTopCard()
        card?.let {
            println(card)
        }
    } while (card != null)
}

class Deck(private val deckType: DeckType){
    private val heartCards = HeartsCard()
    private val tilesCards = TilesCard()
    private val cloverCards = CloversCard()
    private val pikeCards = PikeCard()
    private val cards = Stack<Card>()
    init{
        initializeDeck()
    }
    private fun initializeDeck(){
        when (deckType){
            DeckType.THOUSAND,
            DeckType.SMALL,
            DeckType.SHORT,
            DeckType.STANDARD -> {
                val cardsInType = deckType.size / 4
                heartCards.initializeCardList(cardsInType)
                tilesCards.initializeCardList(cardsInType)
                cloverCards.initializeCardList(cardsInType)
                pikeCards.initializeCardList(cardsInType)
            }
            DeckType.STANDARD_WITH_JOKERS -> {
                val cardsInType = (deckType.size - 2) / 4
                heartCards.initializeCardList(cardsInType)
                tilesCards.initializeCardList(cardsInType + 1)
                cloverCards.initializeCardList(cardsInType + 1)
                pikeCards.initializeCardList(cardsInType)
            }
        }
    }
    fun shuffle(){
        println("deck size ${deckType.size}")
        for(i in 0 until deckType.size){
            cards.push(getRandomCard())
        }
    }
    fun getTopCard(): Card?{
        if(cards.empty())
            return null
        return cards.pop()
    }
    private fun getRandomCard(): Card{
        when(CardType.values().random()){
            CardType.Heart -> {
                return heartCards.getRandomCard() ?: getRandomCard()
            }
            CardType.Tile -> {
                return tilesCards.getRandomCard() ?: getRandomCard()
            }
            CardType.Clover -> {
                return cloverCards.getRandomCard() ?: getRandomCard()
            }
            CardType.Pike -> {
                return pikeCards.getRandomCard() ?: getRandomCard()
            }
        }
    }
}

class Card(val value: Int, val type: CardType){
    override fun toString(): String {
        return "${getTypeString(type.code)}${getCardRank(value)}"
    }
}

enum class CardType(val code: Int){
    Heart(0),
    Tile(1),
    Clover(2),
    Pike(3)
}
private fun getTypeString(code: Int): String {
    when (code){
        0 -> return "♡"
        1 -> return "♢"
        2 -> return "♣"
        3 -> return "♠"
        else -> return "Unknown"
    }
}
private fun getCardRank(value: Int): String{
    when(value){
        in 2..10 -> return value.toString()
        11 -> return "Valet"
        12 -> return "Dame"
        13 -> return "King"
        14 -> return "Ace"
        15 -> return "Joker"
        else -> return "Unknown value"
    }
}
enum class DeckType(val size: Int){
    THOUSAND(24),
    SMALL(32),
    SHORT(36),
    STANDARD(52),
    STANDARD_WITH_JOKERS(54)
}

abstract class SomeCardFactory{
    abstract val cardType: CardType
    private val cards = mutableListOf<Card>()

    fun initializeCardList(cardCount: Int){
        val min = max(2, 14 - cardCount + 1)
        val max = min + cardCount - 1
        for(i in min..max){
            cards.add(Card(i, cardType))
        }
    }

    fun getRandomCard(): Card?{
        if (cards.isNullOrEmpty())
            return null
        val index = cards.getRandomIndex()
        val res = cards[index]
        cards.removeAt(index)
        return res
    }

    private fun <T> List<T>.getRandomIndex():Int {
        return (0 until this.size).random()
    }
}

class HeartsCard:SomeCardFactory(){
    override val cardType: CardType = CardType.Heart
}

class TilesCard:SomeCardFactory(){
    override val cardType: CardType = CardType.Tile
}

class CloversCard:SomeCardFactory(){
    override val cardType: CardType = CardType.Clover
}

class PikeCard:SomeCardFactory(){
    override val cardType: CardType = CardType.Pike
}