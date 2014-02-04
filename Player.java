/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package texasholdem;

import javax.swing.*;
/**
 *
 * @author Nick
 */
public class Player {
    private int playerMoney = 1000;
    private int playerBet = 0;              // the players last bet
    private int totalPot = 0;
    private String playerName = "";
    public boolean computer = false;       // true if player is a computer
    private boolean all = false;
    public boolean folded = false;
    public boolean tookTurn = false;

    public void getName (int pNum) {
        pNum++;
        if (computer) {
            playerName = "Computer " + pNum;
        } else {
            do {
                playerName = JOptionPane.showInputDialog("Enter player " + pNum + "'s name: ");
            } while (playerName.length () > 10);
        } // else
    } // getName

    public void getBet (int min) {
        boolean fail;
        int thisBet = 0;

        do {
            fail = false;
            try {
                if (computer) {
                    // computer bet
                    thisBet = min;
                } else {
                    thisBet = Integer.parseInt(JOptionPane.showInputDialog("How much do you want to bet? ($" + min + "...$" + playerMoney + ")"));
                    
                    if (thisBet < min || thisBet > playerMoney) {
                        JOptionPane.showMessageDialog(null, "Must be between $" + min + "...$" + playerMoney);
                    } // if
                } // else
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Must be between $" + min + "...$" + playerMoney);
                fail = true;
            } // catch
        } while (fail || thisBet < min || thisBet > playerMoney);
        playerMoney -= thisBet;
        totalPot += thisBet;
        playerBet += thisBet;
    } // getBet

    public int Pot () {
        return totalPot;
    } // Pot

    public String Name () {
        return playerName;
    } // Name

    public int Money () {
        return playerMoney;
    } // Money

    public int Bet () {
        return playerBet;
    } // Bet

    public void setBet (int bet) {
        playerBet = bet;
    } // setBet

    public void setMoney (int money) {
        playerMoney = money;
    } // setBet

    public boolean Computer () {
        return computer;
    } // Computer

    public void setComputer () {
        computer = true;
    } // computer

} // Player