import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List; // Ensure we are using java.util.List

// Enums for Suit and Rank
enum Suit {
    SPADE, CLUB, HEART, DIAMOND
}

enum Rank {
    A, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, J, Q, K
}

// Class representing a Card
class Card {
    private Suit suit;
    private Rank rank;

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

// Class managing the Deck of Cards
class Deck {
    private List<Card> cards = new ArrayList<>();

    public Deck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public List<Card> drawCards(int count) {
        List<Card> drawnCards = new ArrayList<>();
        for (int i = 0; i < count && !cards.isEmpty(); i++) {
            drawnCards.add(cards.remove(cards.size() - 1));
        }
        return drawnCards;
    }
}

// Custom Comparator for sorting Cards
class CardComparator implements Comparator<Card> {
    @Override
    public int compare(Card c1, Card c2) {
        // Color: Black (Spade, Club), Red (Heart, Diamond)
        int color1 = (c1.getSuit() == Suit.SPADE || c1.getSuit() == Suit.CLUB) ? 1 : 2;
        int color2 = (c2.getSuit() == Suit.SPADE || c2.getSuit() == Suit.CLUB) ? 1 : 2;

        if (color1 != color2) {
            return color1 - color2;
        }

        int suitComparison = c1.getSuit().compareTo(c2.getSuit());
        if (suitComparison != 0) {
            return suitComparison;
        }

        return c1.getRank().compareTo(c2.getRank());
    }
}

// Main class for the Card Game GUI
public class CardGame extends JFrame {
    private Deck deck = new Deck();
    private JTextArea displayArea;
    private JButton drawButton, sortButton;

    public CardGame() {
        setTitle("Card Game Simulator");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        drawButton = new JButton("Draw 20 Cards");
        sortButton = new JButton("Sort Cards");

        buttonPanel.add(drawButton);
        buttonPanel.add(sortButton);
        add(buttonPanel, BorderLayout.SOUTH);

        drawButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deck.shuffle();
                List<Card> drawnCards = deck.drawCards(20);
                displayCards(drawnCards);
            }
        });

        sortButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<Card> drawnCards = deck.drawCards(20);
                Collections.sort(drawnCards, new CardComparator());
                displayCards(drawnCards);
            }
        });
    }

    private void displayCards(List<Card> cards) {
        displayArea.setText("");
        for (Card card : cards) {
            displayArea.append(card + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CardGame().setVisible(true);
            }
        });
    }
}
