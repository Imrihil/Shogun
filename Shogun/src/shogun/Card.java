/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shogun;

import java.awt.Image;

/**
 *
 * @author Imrihil
 */
public abstract class Card {

    private final String name;

    private final Picture cardS;

    private final Picture cardL;

    private boolean turnOn;

    public Card(String name, Picture cardS, Picture cardL) {
        this.name = name;
        this.cardS = cardS;
        this.cardL = cardL;
        this.turnOn = true;
    }

    /**
     * @return the turnOn
     */
    public boolean isTurnOn() {
        return turnOn;
    }

    /**
     */
    public void turn() {
        this.turnOn = !this.turnOn;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the cardS
     */
    public Image getCardS() {
        return cardS.getImg();
    }

    /**
     * @return the cardL
     */
    public Image getCardL() {
        return cardL.getImg();
    }
}
