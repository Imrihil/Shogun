/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shogun;

import java.awt.Image;
import java.io.IOException;

/**
 *
 * @author Imrihil
 */
public class Chest extends Card implements Comparable<Chest> {

    private static Picture cardBackS;
    private static Picture cardBackL;

    /**
     * @return the cardBackS
     */
    public static Image getCardBackS() {
        return cardBackS.getImg();
    }

    /**
     * @return the cardBackL
     */
    public static Image getCardBackL() {
        return cardBackL.getImg();
    }

    private Action action;

    private final int gold;

    public Chest(String name, Picture cardS, Picture cardL, int gold) throws IOException {
        super(name, cardS, cardL);
        Chest.cardBackS = new Picture("/img/small/card/province/province_back.png");
        Chest.cardBackL = new Picture("/img/large/card/province/province_back.png");
        action = null;
        this.gold = gold;
    }

    /**
     * @return the action
     */
    public Action getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(Action action) {
        this.action = action;
    }

    /**
     * @return the gold
     */
    public int getGold() {
        return gold;
    }

    @Override
    public int compareTo(Chest o) {
        return Integer.compare(this.getGold(), o.getGold());
    }
}
