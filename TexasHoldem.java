/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package texasholdem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Image.*;
/**
 *
 * @author Nick
 */

public class TexasHoldem extends JPanel {
    static JPanel panel;                   // main drawing panel
    static JFrame frame;                   // window frame which contains the panel
    static final int WINDOW_WIDTH = 800;   // width of display window
    static final int WINDOW_HEIGHT = 600;  // height of display window

    static gameStages gameStage = gameStages.WELCOME_SCREEN;                         // game stages
    public enum gameStages { WELCOME_SCREEN, SPLASH, MENU, INSTRUCTIONS, PLAY }
    static playStages playStage = playStages.START;                                  // play stages
    public enum playStages { START, BET1, BET2, BET3, BET4, END }

    static CardHandling cards;  // CardHandling class
    static Player [] players;   // Player class

    // card image size
    static final int CARDW = 80;                // image files are 200 x 250
    static final int CARDH = 100;

    static boolean blink = true;

    static int totalPot = 0;
    static int currentBet = 0;
    static int minRaise = 0;
    static int turn = 0;                        // the # of player whose turn it is
    static int numHumans = 0;                   // # of humans
    static int numComputers = 0;                // # of AI
    static int numPlayers = 0;                  // total # of humans plus computers
    static boolean menu = false;                // true when waiting for a key during the menu
    static boolean bet = false;                 // true when waiting for a key during betting
    static boolean fold = false;                // true when someone folds
    static boolean allIn = false;
    static int winnerCount = -1;
    static int winningPlayer = -1;              // string that prints the winning hand
    static int winningValue = -1;
    static String winningValue2 = "";           // if winning value is above 10
    static String winningHand = "";
    static String playOutput = "";

    public static void main (String args[]) {
        // Create Image Object
        Toolkit tk = Toolkit.getDefaultToolkit();

        panel = new TexasHoldem();
        panel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));  // set size of application window
        frame = new JFrame ("TexasHoldem");                                  // set title of window
        frame.add (panel);

        // add a key input listener (defined below) to our canvas so we can respond to key pressed
        frame.addKeyListener(new KeyInputHandler());

        // request the focus so key events come to the frame
        frame.requestFocus();
        frame.pack();
        frame.setVisible(true);

        // pauses splash screen
        while (gameStage != gameStages.SPLASH) {
            try {
                Thread.sleep(10);
            } catch (Exception e) {
            } // catch
        } // while

        // pauses splash screen
        for (int i = 0; (i < 1000) && (gameStage == gameStages.SPLASH); i++) {
            try {
                Thread.sleep(25);
            } catch (Exception e) {
            } // catch
            panel.repaint();
        } // for

        while (true) {
            blink = !blink;
            try {
                Thread.sleep(900);
            } catch (Exception e) {
            } // catch
            panel.repaint();
        } // while
    } // main

    /* paintComponent gets called whenever panel.repaint() is
     * called or when frame.pack()/frame.show() is called. It paints
     * to the screen.  Since we want to paint different things
     * depending on what stage of the game we are in, a variable
     * "gamestage" will keep track of this.
     */
    public void paintComponent(Graphics g) {
        switch (gameStage) {
        case WELCOME_SCREEN:
            super.paintComponent(g);
            g.setColor(Color.black);
            g.fillRect (0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
            gameStage = gameStages.SPLASH;
            break;

        case SPLASH:
            // draws "Texas Holdem" in random places and random colours
            int red = ((int)(Math.random() * 255));
            int green = ((int)(Math.random() * 255));
            int blue = ((int)(Math.random() * 255));
            g.setColor(new Color(red,green,blue));
            int x = ((int)(Math.random() * 800));
            int y = ((int)(Math.random() * 600));
            g.setFont(new Font("SansSerif", Font.BOLD, 32));
            g.drawString("Texas Hold'em",x,y);
            g.setColor(Color.black);
            g.fillRect (0, 560, 800, 50);
            g.setColor(Color.white);
            g.setFont(new Font("SansSerif", Font.BOLD, 20));
            g.drawString("Press any key to continue", 280, 585);
            break;
      
       case MENU:
            super.paintComponent(g);
            g.setColor(Color.black);
            g.fillRect (0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
            g.setColor(Color.white);
            g.setFont(new Font("SansSerif", Font.BOLD, 36));   // set font
            g.drawString("Texas Hold'em",280,270);  // display
            g.setFont(new Font("SansSerif", Font.BOLD, 16));   // set font
            g.drawString("Please make one of the following choices:",280,300);  // display
            g.drawString("1) Instructions",280,330);
            g.drawString("2) Single Player Game",280,360);
            g.drawString("3) Multiplayer Game",280,390);
            g.drawString("4) Exit",280,420);
            break;

       case INSTRUCTIONS:
            super.paintComponent(g);   // calls the paintComponent method of JPanel to display the background
            g.setColor(Color.black);
            g.fillRect (0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
            g.setColor(Color.white);
            g.setFont(new Font("SansSerif", Font.BOLD, 36));   // set font
            g.drawString("Instructions", 305, 50);
            g.setFont(new Font("SansSerif", Font.BOLD, 12));   // set font
            g.drawString("In this version of Texas Hold'em you use keys 0-4 to play.", 20, 80);
            g.drawString("The 0 key will let you flip your cards over.", 20, 100);
            g.drawString("The 1 key will let you bet.", 20, 120);
            g.drawString("The 2 key will let you check.", 20, 140);
            g.drawString("The 3 key will let you fold.", 20, 160);
            g.drawString("If you are a hardcore Texas Hold'em player you may notice some simplications in the betting", 20, 200);
            g.setFont(new Font("SansSerif", Font.BOLD, 20));
            g.drawString("Press any key to continue", 280, 585);
            break;

       case PLAY:
            super.paintComponent(g);   // calls the paintComponent method of JPanel to display the background
            g.setColor(Color.black);
            g.fillRect (0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
            g.setColor(Color.green);
            g.setFont(new Font("SansSerif", Font.BOLD, 16));   // set font

            // display money
            for (int i = 0; i < numPlayers; i++) {
                g.setColor(Color.green);
                g.drawString(players[i].Name() + ": $" + players[i].Money(), 10, (i * 20 + 20));  // display
                if (players[i].folded) {
                    g.setColor(Color.red);
                    g.drawString(players[i].Name() + ": $" + players[i].Money(), 10, (i * 20 + 20));  // display
                } // if
            } // for

            g.drawString("Pot Money: $" + totalPot, 320, 20);
            g.drawString("Current Bet: $" + currentBet, 320, 40);

            g.setColor(Color.white);
            // display flop
            int left = 60;
            int top = 250;
            g.drawString("FLOP", left, top - 15);
            for (int i = 0; i < 5; i++) {
                cards.deck [cards.fCards[i]].display(g, (i * (CARDW + 10) + left), top, CARDW, CARDH);
            } // for

            if (playStage == playStages.END) {
                for (int i = 0; i < numPlayers; i++) {
                    cards.deck [cards.pCards[i][0]].setFaceUp(true);
                    cards.deck [cards.pCards[i][1]].setFaceUp(true);
                } // for
            } // if

            // player cards
            left = 600;
            for (int i = 0; i < numPlayers; i++) {
                top = (i * (CARDH + 30)) + 75;
                g.drawString(players[i].Name() + "'s Cards:" , left, top - 5);  // display
                cards.deck [cards.pCards[i][0]].display(g, left, top, CARDW, CARDH);
                cards.deck [cards.pCards[i][1]].display(g, CARDW + 10 + left, top, CARDW, CARDH);
            } // for

            g.setFont(new Font("SansSerif", Font.BOLD, 25));   // set font
            if (blink) {
                g.setColor(Color.white);
            } else {
                g.setColor(Color.gray);
            } // else if
            g.drawString(players[turn].Name() + "'s turn" , 26, 580);

            switch (playStage) {
            case BET1:
            case BET2:
            case BET3:
            case BET4:
                g.setColor(Color.white);
                g.setFont(new Font("SansSerif", Font.BOLD, 16));   // set font
                left = 400;
                top = 500;
                g.drawString ("Choose:", left, top - 10);  // display
                g.drawString ("0) Flip", left, top + 10);
                if (players[turn].Money () >= (currentBet - players[turn].Bet ())) {
                    g.drawString ("1) Bet / Raise (Min >= $" + minRaise + ")" , left, top + 30);
                    g.drawString ("2) Check / Call ($" + (currentBet - players[turn].Bet ()) + ")", left, top + 50);
                } // if
                g.drawString ("3) Fold", left, top + 90);
                break;

            case END:
                g.setColor(Color.white);
                if (winnerCount == 1) {
                    g.drawString ("The winner is " + players[winningPlayer].Name (), 300, 450);
                } else {
                    g.drawString ("It's a " + winnerCount + "-way tie!" , 300, 450);
                } // else

                switch (cards.strength[winningPlayer]) {
                case 9: // "Royal Flush";
                    g.drawString ("with a " + winningHand, 300, 475);
                    break;
                case 8: // "Straight Flush";
                    g.drawString ("with a " + winningHand + ", high card is " + winningValue2, 300, 475);
                    break;
                case 7: // "Four of a Kind";
                    g.drawString ("with a " + winningHand + " of " + winningValue2 + "s" , 300, 475);
                    break;
                case 6: // "Full House";
                    g.drawString ("with a " + winningHand + ", with three " + winningValue2 + "s" , 300, 475);
                    break;
                case 5: // "Flush";
                    g.drawString ("with a " + winningHand + ", high card is " + winningValue2 , 300, 475);
                    break;
                case 4: // "Straight";
                    g.drawString ("with a " + winningHand + ", high card is " + winningValue2 , 300, 475);
                    break;
                case 3: // "Three of a Kind";
                    g.drawString ("with " + winningHand + " of " + winningValue2 + "s" , 300, 475);
                    break;
                case 2: // "Two Pair";
                    g.drawString ("with " + winningHand + ", high pair is " + winningValue2 + "s" , 300, 475);
                    break;
                case 1: // "Pair";
                    g.drawString ("with a " + winningHand + " of " + winningValue2 + "s" , 300, 475);
                    break;
                case 0: // "High card";
                    g.drawString ("with a " + winningHand + " of " + winningValue2 , 300, 475);
                    break;
                } // switch

                g.setFont(new Font("SansSerif", Font.BOLD, 20));
                g.drawString("Press any key to continue", 280, 585);
                break;
            } // switch playStage

            break;
        } // switch gameStage

    } // paintComponent

    /* A class to handle keyboard input from the user.
     * Implemented as a inner class because it is not
     * needed outside the EvenAndOdd class.
     */
    private static class KeyInputHandler extends KeyAdapter {
        public void keyTyped(KeyEvent e) {
            // quit if the user presses "escape"
            if (e.getKeyChar() == 27) {
                System.exit(0);
            } else if (playStage == playStages.END) {
                playGame ();
            } else if (menu == true) {
                // respond to menu selection
                switch (e.getKeyChar()) {
                    // Key "1" pressed
                    case 49:  showInstructions(); break;
                    
                    // Key "2" pressed
                    case 50:  numHumans = 1;
                              numComputers = 1;
                              numPlayers = numHumans + numComputers;
                              playGame();
                              break;

                    // Key "3" pressed
                    case 51:  do {
                                  // Only if you need more than 2 players
                                  //JOptionPane.showMessageDialog(panel, "You can have a maximum of 5 players");
                                  //numHumans = safeParseInt("How many HUMAN players?", 1, 5);
                                  numComputers = safeParseInt("How many COMPUTER players?", 0, 2);
                                  numHumans = 2;
                                  numPlayers = numHumans + numComputers;
                              } while (numPlayers > 7);
                              playGame();
                              break;

                    // Key "4" pressed
                    case 52:  System.exit(0);
                } // switch
            } else if (bet == true) {
                // respond to menu selection
                switch (e.getKeyChar()) {
                    case 48:  flip(); break;            // Key "0" pressed
                    case 49:  bet(); break;             // Key "1" pressed
                    case 50:  check(); break;           // Key "2" pressed
                    case 51:  fold(); break;            // Key "3" pressed
                } // switch
            } else {
                showMenu();
            } // else
        } // keyTyped
    } // KeyInputHandler class

    public static int safeParseInt (String prompt, int low, int high) {
        int value = 0;         // user input
        boolean fail = false;  // wrong value

        do {
            fail = false;
            try {
                value = Integer.parseInt(JOptionPane.showInputDialog(prompt));
                if (value < low || value > high) {
                    JOptionPane.showMessageDialog(null, "Must be between " + low + "..." + high);
                } // if
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Must be between " + low + "..." + high);
                fail = true;
            } // catch
        } while (fail || value < low || value > high);

        return value;
    } // safeParseInt
    
    static void showMenu() {
        // display this stage of the game
        gameStage = gameStages.MENU;
        menu = true;
        panel.repaint();
    } // startGame

    static void showInstructions() {
        gameStage = gameStages.INSTRUCTIONS;
        menu = false;
        panel.repaint();
    } // startGame

    static void flip() {
        cards.deck [cards.pCards[turn] [0]].setFaceUp (!cards.deck [cards.pCards[turn] [0]].isFaceUp ());
        cards.deck [cards.pCards[turn] [1]].setFaceUp (!cards.deck [cards.pCards[turn] [1]].isFaceUp ());

        panel.repaint();
    } // bet

    // prompt player to enter bet, then advance to next player
    // computer bets automatically
    static void bet() {
        int lastBet = players[turn].Bet ();
        players[turn].getBet(currentBet + minRaise - players[turn].Bet ());
        totalPot += players[turn].Bet () - lastBet;
        int lastCurrentBet = currentBet;
        currentBet = players[turn].Bet ();
        minRaise = currentBet - lastCurrentBet;

        players[turn].tookTurn = true;

        AdvanceAI ();
        panel.repaint();
    } // bet

    static void check() {
        players[turn].setMoney (players[turn].Money () - (currentBet - players[turn].Bet ()));
        totalPot += currentBet - players[turn].Bet ();
        players[turn].setBet (currentBet);

        players[turn].tookTurn = true;
      
        AdvanceAI ();
        panel.repaint();
    } // check

    static void fold() {
        players[turn].folded = true;

        int notFolded = 0;
        int winner = 0;
        for (int i = 0; i < numPlayers; i++) {
            if (players[i].folded == false) {
                notFolded++;
                winner = i;
            } // if
        } // for

        // If this is the last player who hasn't folded then they win
        if (notFolded == 1) {
            roundDone ();
            return;
        } // if

        AdvanceAI ();
        panel.repaint();
    } // fold

    /** Advance to next player who hasn't folded
        if you have past every player then if
        everyone's bet is the currentBet then 
        advance to the next playStage
        
        - Go to next player who hasn't folded and hasn't bet current bet
        IF past last player THEN
           IF all bets equal currentBet or folded THEN
              advance playStage and set current player to first non folded player and clear all players' bets
           ELSE
              advance to next player whose bet doesn't equal current bet
    **/
    static void Advance() {
        // Go to next player who hasn't folded or back to the first player
        boolean pastLastPlayer = false;
        do {
            if (++turn == numPlayers) {
                turn = 0;
                pastLastPlayer = true;
            } // if
            if (AllBetsEqual ()) {
                break;
            } // if
        } while (players[turn].folded || players[turn].tookTurn);

     //   if (pastLastPlayer) {
            if (AllBetsEqual ()) {
                    for (int i = 0; i < numPlayers; i++) {
                        players[i].setBet(0);
                        players[i].tookTurn = false;
                    } // for
                    currentBet = 0;
                    minRaise = 0;

                    // go to next playStage
                    switch (playStage) {
                    case BET1:
                        // flip over the first 3 cards in the flop
                        cards.deck [cards.fCards [0]].setFaceUp (true);
                        cards.deck [cards.fCards [1]].setFaceUp (true);
                        cards.deck [cards.fCards [2]].setFaceUp (true);
                        playStage = playStages.BET2;
                        break;
                    case BET2:
                        // flip over the next card in the flop
                        cards.deck [cards.fCards [3]].setFaceUp (true);
                        playStage = playStages.BET3;
                        break;
                    case BET3:
                        // flip over the last card in the flop
                        cards.deck [cards.fCards [4]].setFaceUp (true);
                        playStage = playStages.BET4;
                        break;
                    case BET4:
                        roundDone ();
                        break;
                    } // switch
            } else {
                while (players[turn].tookTurn || players[turn].folded ) {
                    if (++turn == numPlayers) {
                        turn = 0;
                    } // if
                } // while
            } // else
       // } // if
    } // Advance

    static void roundDone () {
        bet = false;
        findWinner ();
        splitPot ();
        totalPot = 0;     // reset the total pot for the next game
        playStage = playStages.END;
    } // roundDone

    static void findWinner () {
        for (int i = 0; i < numPlayers; i++) {
            cards.groupCards (i);
            cards.getHandValue (i, players[i].folded);
        } // for
        winningPlayer = cards.getWinner ();
        winnerCount = cards.countWinners ();
        winningHand = cards.getHandRank (winningPlayer);
        winningValue = cards.value [winningPlayer] + 2;
        winningValue2 = Integer.toString(winningValue); // makes it so if value is 2 the winningValue is 2
        if (winningValue > 10) {
            if (winningValue == 11) {
                winningValue2 = "Jack";
            } else if (winningValue == 12) {
                winningValue2 = "Queen";
            } else if (winningValue == 13) {
                winningValue2 = "King";
            } else if (winningValue == 14) {
                winningValue2 = "Ace";
            } // else if
        } // if
    } // findWinner

    static void splitPot () {
        // finding the winners
        int highStrength = -1;
        int highValue = -1;
        int highPlayer = -1;

        for (int p = 0; p < numPlayers; p ++) {
            if ((highStrength < cards.strength [p]) ||
               ((highStrength == cards.strength [p]) && (highValue < cards.value [p]))) {
                highStrength = cards.strength [p];
                highValue = cards.value [p];
                highPlayer = p;
            } // if
        } // for

        int share = totalPot / winnerCount;
        for (int p = 0; p < numPlayers; p ++) {
            if ((highStrength == cards.strength [p]) && (highValue == cards.value [p])) {
                players[p].setMoney (players[p].Money() + share);
            } // if
        } // for

    } // splitPot

    static void AdvanceAI () {
        do {
            Advance ();
            // if current player is a computer take turn for him
            if (players[turn].computer) {
                // take computers turn
                totalPot += currentBet - players[turn].Bet ();
                players[turn].getBet (currentBet - players[turn].Bet ());
                players[turn].tookTurn = true;
            } // if
        } while (players[turn].computer);

    } // AdvanceAI

    // return true if all player bets are equal to the current bet
    // and every player bet or checked or went all in
    static boolean AllBetsEqual () {
        boolean equalBet = true;

        for (int i = 0; i < numPlayers; i++) {
            if (players[i].folded == false) {
                if (players[i].Bet () != currentBet) {
                    equalBet = false;
                } // if
                if (!players[i].tookTurn) {
                    equalBet = false;
                } // if
            } // if
        } // for
        return equalBet;
    } // AllBetsEqual

    // called when they select # of players, starts the game
    static void playGame() {
        // only ask for names the first time
        if (players == null) {
            players = new Player [numPlayers];
            for (int i = 0; i < numPlayers; i++) {
                players [i] = new Player ();
                if (i >= numHumans) {
                    players [i].setComputer ();
                } // if
                players [i].getName (i);
            } // for
        } // if

        gameStage = gameStages.PLAY;
        menu = false;
        bet = true;
        turn = 0;
        currentBet = 0;
        minRaise = 0;
        playStage = playStages.BET1;
        
        cards = new CardHandling (numPlayers);

        int ante = 20;

        // take antes
        for (int i = 0; i < numPlayers; i++) {
            if (players[i].Money () < ante) {
                JOptionPane.showMessageDialog(panel, players[i].Name() + " is out of money. Game over.");
                System.exit(0);
            } // if
            players[i].setMoney (players[i].Money () - (ante - players[i].Bet ()));
            totalPot += ante - players[i].Bet ();
        } // for

        JOptionPane.showMessageDialog(panel, "$20 Ante has been taken");
        
        panel.repaint();

    } // playGame

} // TexasHoldem

