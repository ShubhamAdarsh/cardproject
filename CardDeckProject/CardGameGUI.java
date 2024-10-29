import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

// Enum for Suit
enum Suit {
    SPADE, CLUB, HEART, DIAMOND
}

// Enum for Rank
enum Rank {
    A, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, J, Q, K
}

// Card Class
class Card {
    private final Suit suit;
    private final Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}

// Deck Class
class Deck {
    private final ArrayList<Card> cards;
    private final ArrayList<Card> drawnCards;

    public Deck() {
        cards = new ArrayList<>();
        drawnCards = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    // Shuffle using Fisher-Yates
    public void shuffle() {
        Random rand = new Random();
        for (int i = cards.size() - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            Collections.swap(cards, i, j);
        }
    }

    // Draw multiple cards
    public ArrayList<Card> drawMultipleCards(int number) {
        resetDrawnCards();
        ArrayList<Card> cardsDrawn = new ArrayList<>();
        for (int i = 0; i < number && !cards.isEmpty(); i++) {
            Card card = cards.remove(0);
            cardsDrawn.add(card);
            drawnCards.add(card);
        }
        return cardsDrawn;
    }

    public ArrayList<Card> getDrawnCards() {
        return new ArrayList<>(drawnCards);
    }

    public void resetDrawnCards() {
        cards.addAll(drawnCards);
        drawnCards.clear();
    }

    public ArrayList<Card> getCards() {
        return new ArrayList<>(cards);
    }
}

// Comparator for Sorting Cards
class CardComparator implements Comparator<Card> {
    @Override
    public int compare(Card c1, Card c2) {
        // 1. Compare color (Red before Black)
        int colorComparison = getColorValue(c1) - getColorValue(c2);
        if (colorComparison != 0) return colorComparison;

        // 2. Compare Suit within color
        int suitComparison = c1.getSuit().compareTo(c2.getSuit());
        if (suitComparison != 0) return suitComparison;

        // 3. Compare Rank
        return c1.getRank().compareTo(c2.getRank());
    }

    private int getColorValue(Card card) {
        return (card.getSuit() == Suit.HEART || card.getSuit() == Suit.DIAMOND) ? 0 : 1;
    }
}

// Main GUI Class
public class CardGameGUI extends JFrame {
    private final Deck deck;
    private final JTextArea cardDisplay;

    public CardGameGUI() {
        deck = new Deck();
        deck.shuffle();

        setTitle("Card Deck Simulator");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        cardDisplay = new JTextArea();
        cardDisplay.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(cardDisplay);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        JButton draw20Button = new JButton("Draw 20 Cards");
        JButton sortButton = new JButton("Sort Cards");
        JButton shuffleButton = new JButton("Shuffle Deck");

        buttonPanel.add(draw20Button);
        buttonPanel.add(sortButton);
        buttonPanel.add(shuffleButton);

        draw20Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                draw20Cards();
            }
        });

        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortCards();
            }
        });

        shuffleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shuffleDeck();
            }
        });
    }

    private void draw20Cards() {
        ArrayList<Card> drawnCards = deck.drawMultipleCards(20);
        cardDisplay.setText("Drew 20 Cards:\n");
        for (Card card : drawnCards) {
            cardDisplay.append(card + "\n");
        }
    }

    private void sortCards() {
        ArrayList<Card> drawnCards = deck.getDrawnCards();
        drawnCards.sort(new CardComparator());
        cardDisplay.setText("Sorted Cards:\n");
        for (Card card : drawnCards) {
            cardDisplay.append(card + "\n");
        }
    }

    private void shuffleDeck() {
        deck.resetDrawnCards();
        deck.shuffle();
        cardDisplay.setText("Shuffled Deck:\n");
        for (Card card : deck.getCards()) {
            cardDisplay.append(card + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CardGameGUI gui = new CardGameGUI();
            gui.setVisible(true);
        });
    }
}

