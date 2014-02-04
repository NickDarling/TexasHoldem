/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package texasholdem;

import java.awt.*;
/**
 *
 * @author Nick
 */
public class Card {
    private int value = 0;
    private int suit = 0;
    private Image img;
    private Image back;
    private boolean dealt = false;
    private boolean faceUp = false;

    // methods
    public int getValue () {
        return value;
    } // getValue

    public int getSuit () {
        return suit;
    } // getSuit

    public boolean isDealt () {
        return dealt;
    } // isDealt

    public void deal () {
        dealt = true;
    } // deal

    public boolean isFaceUp () {
        return faceUp;
    } // isFaceUp

    public void setFaceUp (boolean value) {
        faceUp = value;
    } // faceUp

    public void display (Graphics g, int x, int y, int w, int h) {
        if (faceUp) {
            g.drawImage(img, x, y, w, h, null);
        } else {
            g.drawImage(back, x, y, w, h, null);
        } // else
    } // display

    // construtor
    Card (int v, int s, Image i, Image cardback) {
        value = v;
        suit = s;
        img = i;
        back = cardback;
        dealt = false;
        faceUp = false;
    } // Card
} // Card
