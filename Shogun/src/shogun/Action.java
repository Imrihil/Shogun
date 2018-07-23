/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shogun;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Imrihil
 */
public abstract class Action extends Card {

    private static Picture cardBackS;
    private static Picture cardBackL;
    private static Picture accept;
    private static Picture acceptNo;
    private static Picture decline;
    private static Picture higlight;

    /**
     * @return the accept
     */
    public static Image getAccept() {
        return accept.getImg();
    }

    /**
     * @return the acceptNo
     */
    public static Image getAcceptNo() {
        return acceptNo.getImg();
    }

    /**
     * @return the decline
     */
    public static Image getDecline() {
        return decline.getImg();
    }

    /**
     * @return the higlight
     */
    public static Image getHiglight() {
        return higlight.getImg();
    }
    private final Picture info;

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

    private int x;

    private int y;

    private boolean possible;

    private int choose;

    public Action(String name, Picture cardS, Picture cardL, Picture acc, Picture accNo, Picture dec, Picture highlight, Picture info, int x, int y) throws IOException {
        super(name, cardS, cardL);
        Action.cardBackS = new Picture("/img/small/card/action/action_back.png");
        Action.cardBackL = new Picture("/img/large/card/action/action_back.png");
        Action.accept = acc;
        Action.acceptNo = accNo;
        Action.decline = dec;
        Action.higlight = highlight;
        this.info = info;
        this.x = x;
        this.y = y;
        this.possible = false;
        this.choose = 0;
    }

    public abstract void doAction(GameStep3Game gameStep, MyJFrame window, List<Daimyo> daimyos);

    public abstract void mouseClicked(MouseEvent e);

    public abstract void mouseMoved(MouseEvent e);

    public void mouseWheelMoved(MouseWheelEvent e) {
    }

    public void armyUp() {
    }

    public void armyDown() {
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return the info
     */
    public Image getInfo() {
        return info.getImg();
    }

    /**
     * @return the possible
     */
    public boolean isPossible() {
        return possible;
    }

    /**
     * @param possible the possible to set
     */
    public void setPossible(boolean possible) {
        this.possible = possible;
    }

    /**
     * @return the choose
     */
    public int getChoose() {
        return choose;
    }

    /**
     * @param choose the choose to set
     */
    public void setChoose(int choose) {
        this.choose = choose;
    }
}
