/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shogun;

import java.awt.Graphics;
import java.awt.Image;

/**
 *
 * @author Imrihil
 */
public class GameStep1WelcomeButton {

    private final Picture image;
    private final int x;
    private final int y;
    private final int stX;
    private final int stY;
    private final int siX;
    private final int siY;

    public GameStep1WelcomeButton(Picture img, int x, int y, int stX, int stY, int siX, int siY) {
        this.image = img;
        this.x = x;
        this.y = y;
        this.stX = stX;
        this.stY = stY;
        this.siX = siX;
        this.siY = siY;
    }

    /**
     * @return the image
     */
    public Image getImage() {
        return image.getImg();
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @return the stX
     */
    public int getStX() {
        return stX;
    }

    /**
     * @return the stY
     */
    public int getStY() {
        return stY;
    }

    /**
     * @return the siX
     */
    public int getSiX() {
        return siX;
    }

    /**
     * @return the siY
     */
    public int getSiY() {
        return siY;
    }

    public void paintComponent(Graphics graphic) {
        graphic.drawImage(image.getImg(), x, y, null);
    }
}
