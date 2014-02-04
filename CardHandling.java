/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package texasholdem;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
/**
 *
 * @author Nick
 */

public class CardHandling {
    Toolkit tk;
    int rand;
    
    // [0] is the 2 of clubs
    //  :
    // [12] is the Ace of clubs
    public Card [] deck;
    private int players;

    // flop cards
    public int [] fCards;           // store indices into deck, of cards in the flop

    // player's cards
    public int [][] pCards;         // store indices into deck, of the players cards

    // player's useable cards
    public int [][] pValues;        // card values from 0-12
                                    // 0 = 2 card
                                    // :
                                    // 12 = Ace card
    public int [][] pSuits;         // card suites from 0-3
                                    // 0 = clubs
                                    // 1 = diamonds
                                    // 2 = hearts
                                    // 3 = spades

    public int [] strength;         // strength of player's hand
                                    // 0 = high card
                                    // :
                                    // 9 = royal flush
    public int [] value;            // goes along with strength to indicate the high card


    CardHandling (int numPlayers) {
        tk = Toolkit.getDefaultToolkit ();
        rand = (int)(Math.random () * 52);
        deck = new Card [52];

        players = numPlayers;
        strength = new int [players];
        value = new int [players];
        setUpDeck ();
        dealCards ();
    } // CardHandling

    public void setUpDeck () {
        // construct 52 cards
        Image cardback = tk.getImage("Images/cardback.png");
        for (int card = 0; card < 52; card++) {
            String img = "Images/img" + (card + 1) + ".png";
            deck [card] = new Card (card % 13, card / 13, tk.getImage(img), cardback);
        } // for

        // flop cards
        fCards = new int [5];

        // player's cards
        pCards = new int [players] [2];

        pValues = new int [players] [7];
        pSuits = new int [players] [7];
    } // setUpDeck

    public void dealCards () {
        // deal player's cards
        for (int player = 0; player < players; player++) {
            for (int card = 0; card < 2; card++) {
                int rand = dealCard ();
                pCards [player] [card] = rand;
            } // for
        } // for

        // deal 5 cards for the flop
        for (int card = 0; card < 5; card++) {
            int rand = dealCard ();
            fCards [card] = rand;
        } // for
    } // dealCards

    public int getRandNum () {
        int rand = (int)(Math.random () * 52);
        return rand;
    } // getRandNum

    public int dealCard () {
        // assumes we dont use all 52 cards
        while (true) {
            // randomly select cards until you find one that is not dealt
            int card = getRandNum ();
            if (!deck [card].isDealt ()) {
                deck [card].deal ();
                return card;
            } // if
        } // while
    } // dealCard

// --------------------------------------------------------------------------------------

    // makes arrays to use for checking hand values
    public void groupCards(int player) {
        int card = 0;

        for (int i = 0; i < 2; i++) {
            pValues [player] [card] = deck [pCards [player] [i]].getValue ();
            pSuits [player] [card] = deck [pCards [player] [i]].getSuit ();
            card++;
        } // for

        for (int flop = 0; flop < 5; flop++) {
            pValues [player] [card] = deck [fCards [flop]].getValue ();
            pSuits [player] [card] = deck [fCards [flop]].getSuit ();
            card++;
        } // for
    } // groupCards

    public int [] getSortedCards (int player) {
        int card = 0;
        int [] temp = new int [7];

        for (int i = 0; i < 2; i++) {
            temp [card] = deck [pCards [player] [i]].getValue ();
            card++;
        } // for

        for (int flop = 0; flop < 5; flop++) {
            temp [card] = deck [fCards [flop]].getValue ();
            card++;
        } // for

        Arrays.sort(temp);
        return temp;
    } // getHighCard

    public int getHighCard (int player) {
        int [] temp = getSortedCards (player);
        int highCard = temp [6];
//        JOptionPane.showMessageDialog(null, player + " your high card is " + highCard);
        return highCard;
    } // getHighCard

    // checks for a pair and returns the value of the highest
    public int isPair (int player) {
        for (int value = 12; value >= 0; value--) {
            int counter = 0;
            for (int card = 0; card < 7; card++) {
                if (value == pValues [player] [card]) {
                    if (++counter == 2) {
                        return value;
                    } // if
                } // if
            } // for
        } // for
        return -1;
    } // isPairs

    // checks for two pairs and returns the value of the highest pair
    public int isTwoPair (int player) {
        for (int value = 12; value >= 0; value--) {
            int counter = 0;
            for (int card = 0; card < 7; card++) {
                if (value == pValues [player] [card]) {
                    if (++counter == 2) {

                        // have one pair checking for 2nd pair
                        for (int value2 = value - 1; value2 >= 0; value2--) {
                            int counter2 = 0;
                            for (int card2 = 0; card2 < 7; card2++) {
                                if (value2 == pValues [player] [card2]) {
                                    if (++counter2 == 2) {
                                        return value;
                                    } // if
                                } // if
                            } // for
                        } // for

                    } // if
                } // if
            } // for
        } // for
        return -1;
    } // isTwoPair

    // checks for 3 of a kind and returns the value of the highest
    public int isThreeOfAKind (int player) {
        for (int value = 12; value >= 0; value--) {
            int counter = 0;
            for (int card = 0; card < 7; card++) {
                if (value == pValues [player] [card]) {
                    if (++counter == 3) {
                        return value;
                    } // if
                } // if
            } // for
        } // for
        return -1;
    } // isThreeOfAKind

    public int isStraight (int player) {
        int [] temp = getSortedCards (player);
        int counter = 0;
        for (int card = 6; card > 0; card--) {
            if ((temp [card] - 1) == temp [card - 1]) {
                if (++counter == 4) {
                    return temp [card + 3];
                } // if
            } else {
                counter = 0;
            } // if
        } // for
        return -1;
    } // isStraight

    // checks for flush
    public int isFlush (int player) {
        int card = 0;
        int [] temp = new int [7];

        for (int i = 0; i < 2; i++) {
            temp [card] = deck [pCards [player] [i]].getSuit ();
            card++;
        } // for

        for (int flop = 0; flop < 5; flop++) {
            temp [card] = deck [fCards [flop]].getSuit ();
            card++;
        } // for

        Arrays.sort(temp);

        for (int suit = 3; suit >= 0; suit--) {
            int counter = 0;
            for (int card2 = 0; card2 < 7; card2++) {
                if (suit == temp [card2]) {
                    if (++counter == 5) {

                        // return value of highest card in the flush
                        int high = -1;
                        for (int card3 = 0; card3 < 7; card3++) {
                            if ((pValues [player] [card3] > high) && (pSuits [player] [card3] == suit)) {
                                high = pValues [player] [card3];
                            } // if
                        } // for
                        return high;

                    } // if
                } // if
            } // for
        } // for
        return -1;

    } // isFlush

    // checks for a pair that isn't the value passed
    public boolean isFullHousePair (int player, int threeOfAKind) {
        for (int value = 12; value >= 0; value--) {
            if (threeOfAKind != value) {
                int counter = 0;
                for (int card = 0; card < 7; card++) {
                    if (value == pValues [player] [card]) {
                        if (++counter == 2) {
                            return true;
                        } // if
                    } // if
                } // for
            } // if
        } // for
        return false;
    } // isPairs

    // checks for full house return value of 3 of a kind
    public int isFullHouse(int player) {
        int isThree = isThreeOfAKind (player);
        if (isThree != -1) {
            if (isFullHousePair (player, isThree)) {
                return isThree;
            } // if
        } // if
        return -1;
    } // isFullHouse

    // checks for 4 of a kind and returns the value of the highest
    public int isFourOfAKind (int player) {
        for (int value = 12; value >= 0; value--) {
            int counter = 0;
            for (int card = 0; card < 7; card++) {
                if (value == pValues [player] [card]) {
                    if (++counter == 4) {
                        return value;
                    } // if
                } // if
            } // for
        } // for
        return -1;
    } // isFourOfAKind

    // returns the highest value of the straight flush
    public int isStraightFlush(int player) {
        int card = 0;
        int [] tempS = new int [7];
        int [] tempV = new int [7];
        
        // creates copy of card values and suits
        for (int i = 0; i < 2; i++) {
            tempS [card] = deck [pCards [player] [i]].getSuit ();
            tempV [card] = deck [pCards [player] [i]].getValue ();
            card++;
        } // for

        for (int flop = 0; flop < 5; flop++) {
            tempS [card] = deck [fCards [flop]].getSuit ();
            tempV [card] = deck [fCards [flop]].getValue ();
            card++;
        } // for
         
        // sorts card values and suits
        int swap = -1;
        for (int i = 6; i > 0; i--) {       
            for (int c = 6; c > 0; c--) {
                if ((tempV [c] - 1) < tempV [c - 1]) {
                    swap = tempV [c];
                    tempV [c] = tempV [c - 1];
                    tempV [c - 1] = swap;

                    swap = tempS [c];
                    tempS [c] = tempS [c - 1];
                    tempS [c - 1] = swap;
                } // if
            } // for
        } // for
        
        // look for a straight with all cards the same suit
        int counter = 0;
        for (int card2 = 6; card2 > 0; card2--) {
            if (((tempV [card2] - 1) == tempV [card2 - 1]) && (tempS [card2] == tempS [card2 - 1])) {
                if (++counter == 4) {
                    return tempV [card2 + 3];
                } // if
            } else {
                counter = 0;
            } // if
        } // for
        return -1;
    } // isStraightFlush

    // returns the suit of the royal flush
    public int isRoyalFlush (int player) {
        if (isStraightFlush (player) == 12) {
            return 12;
        } else {
            return -1;
        } // else
    } // isRoyalFlush

    // prints the hand value
    public void getHandValue(int player, boolean folded) {
        if (folded) {
            strength [player] = -1;
            return;
        } // if

        value [player] = isRoyalFlush(player);
        if (value [player] != -1) {
            strength [player] = 9;
            return;
        } // if
        value [player] = isStraightFlush(player);
        if (value [player] != -1) {
            strength [player] = 8;
            return;
        } // if
        value [player] = isFourOfAKind(player);
        if (value [player] != -1) {
            strength [player] = 7;
            return;
        } // if
        value [player] = isFullHouse(player);
        if (value [player] != -1) {
            strength [player] = 6;
            return;
        } // if
        value [player] = isFlush(player);
        if (value [player] != -1) {
            strength [player] = 5;
            return;
        } // if
        value [player] = isStraight(player);
        if (value [player] != -1) {
            strength [player] = 4;
            return;
        } // if
        value [player] = isThreeOfAKind(player);
        if (value [player] != -1) {
            strength [player] = 3;
            return;
        } // if
        value [player] = isTwoPair(player);
        if (value [player] != -1) {
            strength [player] = 2;
            return;
        } // if
        value [player] = isPair(player);
        if (value [player] != -1) {
            strength [player] = 1;
            return;
        } // if
        value [player] = getHighCard(player);
        strength [player] = 0;
    } // getHandValue

    public String getHandRank (int player) {
        switch (strength[player]) {
            case 9: return "Royal Flush";
            case 8: return "Straight Flush";
            case 7: return "Four of a Kind";
            case 6: return "Full House";
            case 5: return "Flush";
            case 4: return "Straight";
            case 3: return "Three of a Kind";
            case 2: return "Two Pair";
            case 1: return "Pair";
            case 0: return "High card";
        } // switch
        return "ERROR";
    } // getHandRank

    public int getWinner () {
        int highStrength = -1;
        int highValue = -1;
        int highPlayer = -1;

        for (int player = 0; player < players; player ++) {
            if ((highStrength < strength [player]) ||
               ((highStrength == strength [player]) && (highValue < value [player]))) {
                highStrength = strength [player];
                highValue = value [player];
                highPlayer = player;
            } // if
        } // for
        return highPlayer;
    } // getWinner

    public int countWinners () {
        int highStrength = -1;
        int highValue = -1;
        int highPlayer = -1;
        int winners = 0;

        for (int player = 0; player < players; player ++) {
            if ((highStrength < strength [player]) ||
               ((highStrength == strength [player]) && (highValue < value [player]))) {
                highStrength = strength [player];
                highValue = value [player];
                highPlayer = player;
            } // if
        } // for

        for (int player = 0; player < players; player ++) {
            if ((highStrength == strength [player]) && (highValue == value [player])) {
                winners ++;
            } // if
        } // for

        return winners;
    } // countWinners

} // CardHandling


