/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shogun;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import static java.lang.Math.max;
import static java.lang.Math.min;
import javax.swing.JFrame;

/**
 *
 * @author Imrihil
 */
public abstract class GameStep {

    private static Picture cardHorizontalShadeS;
    private static Picture cardVerticalShadeS;
    private static Picture cardHorizontalShadeL;
    private static Picture cardVerticalShadeL;

    private static Picture cardHorizontalHighlightS;
    private static Picture cardVerticalHighlightS;
    private static Picture cardHorizontalHighlightL;
    private static Picture cardVerticalHighlightL;

    private static BufferedImage buffer;

    private static Graphics graphics;

    private final Picture backgroundS;
    private final Picture backgroundL;

    public GameStep(Picture backgroundS, Picture backgroundL) throws IOException {
        this.backgroundS = backgroundS;
        this.backgroundL = backgroundL;
        buffer = new BufferedImage(2048, 1200, BufferedImage.TYPE_INT_RGB);
        graphics = buffer.createGraphics();
        graphics.setFont(new Font("Verdana", Font.BOLD, 12));
        cardHorizontalShadeS = new Picture("/img/small/card/horizontal_shade.png");
        cardVerticalShadeS = new Picture("/img/small/card/vertical_shade.png");
        cardHorizontalShadeL = new Picture("/img/large/card/horizontal_shade.png");
        cardVerticalShadeL = new Picture("/img/large/card/vertical_shade.png");
        cardHorizontalHighlightS = new Picture("/img/small/card/horizontal_highlight.png");
        cardVerticalHighlightS = new Picture("/img/small/card/vertical_highlight.png");
        cardHorizontalHighlightL = new Picture("/img/large/card/horizontal_highlight.png");
        cardVerticalHighlightL = new Picture("/img/large/card/vertical_highlight.png");
    }

    public void paintBackground() {
        getGraphics().clearRect(0, 0, buffer.getWidth(), buffer.getHeight());
        if (Shogun.size == 1) {
            Shogun.shiftX = max(Shogun.resolutionX - backgroundS.getImg().getWidth(null), min(Shogun.shiftX, 0));
            Shogun.shiftY = max(Shogun.resolutionY - backgroundS.getImg().getHeight(null), min(Shogun.shiftY, 0));
            getGraphics().drawImage(backgroundS.getImg(), Shogun.shiftX, Shogun.shiftY, null);
        } else {
            Shogun.shiftX = max(Shogun.resolutionX - backgroundL.getImg().getWidth(null), min(Shogun.shiftX, 0));
            Shogun.shiftY = max(Shogun.resolutionY - backgroundL.getImg().getHeight(null), min(Shogun.shiftY, 0));
            getGraphics().drawImage(backgroundL.getImg(), Shogun.shiftX, Shogun.shiftY, null);
        }
    }

    public void showOnScreen(JFrame window) {
        window.getGraphics().drawImage(buffer, 0, 0, null);
    }

    public void paintObject(Image imgS, Image imgL, int x, int y) {
        if (Shogun.size == 1) {
            graphics.drawImage(imgS, x / 2 + Shogun.shiftX, y / 2 + Shogun.shiftY, null);
        } else {
            graphics.drawImage(imgL, x + Shogun.shiftX, y + Shogun.shiftY, null);
        }
    }

    public void paintObjectCentered(Image imgS, Image imgL, int x, int y) {
        if (Shogun.size == 1) {
            graphics.drawImage(imgS, x / 2 - imgS.getWidth(null) / 2 + Shogun.shiftX, y / 2 - imgS.getHeight(null) / 2 + Shogun.shiftY, null);
        } else {
            graphics.drawImage(imgL, x - imgL.getWidth(null) / 2 + Shogun.shiftX, y - imgL.getHeight(null) / 2 + Shogun.shiftY, null);
        }
    }

    /**
     *
     * @param window
     */
    public abstract void work(MyJFrame window);

    /**
     * @return the graphics
     */
    public Graphics getGraphics() {
        return graphics;
    }

    /**
     * @return the cardHorizontalShadeS
     */
    public Image getCardHorizontalShadeS() {
        return cardHorizontalShadeS.getImg();
    }

    /**
     * @return the cardVerticalShadeS
     */
    public Image getCardVerticalShadeS() {
        return cardVerticalShadeS.getImg();
    }

    /**
     * @return the cardHorizontalShadeL
     */
    public Image getCardHorizontalShadeL() {
        return cardHorizontalShadeL.getImg();
    }

    /**
     * @return the cardVerticalShadeL
     */
    public Image getCardVerticalShadeL() {
        return cardVerticalShadeL.getImg();
    }

    /**
     * @return the cardHorizontalHighlightS
     */
    public Image getCardHorizontalHighlightS() {
        return cardHorizontalHighlightS.getImg();
    }

    /**
     * @return the cardVerticalHighlightS
     */
    public Image getCardVerticalHighlightS() {
        return cardVerticalHighlightS.getImg();
    }

    /**
     * @return the cardHorizontalHighlightL
     */
    public Image getCardHorizontalHighlightL() {
        return cardHorizontalHighlightL.getImg();
    }

    /**
     * @return the cardVerticalHighlightL
     */
    public Image getCardVerticalHighlightL() {
        return cardVerticalHighlightL.getImg();
    }
}
