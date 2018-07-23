/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shogun;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Imrihil
 */
public class GameStep2Options extends GameStep {

    private static final int cardWspX = 150;

    private static final int cardWspY = 40;

    private static int highlightXL;
    private static int highlightYL;
    private static int highlightNum;
    private static boolean playHighlighted;

    private static Picture daimyoBackS;
    private static Picture daimyoBackL;

    private static List<Picture> daimyoInfo;
    private static List<Picture> status;
    private static Picture play;
    private static Picture highlight;
    private static Picture choosingOn;
    private static Picture choosingOff;
    private static Picture bonusesOn;
    private static Picture bonusesOff;

    static void mouseClicked(MouseEvent e) {
        int activePlayer = 0;
        for (int i = 0; i < 5; i++) {
            if (Shogun.daimyos.get(i).isPlaying()) {
                activePlayer++;
            }
        }
        for (int i = 0; i < 5; i++) {
            if (cardWspX <= e.getX() && e.getX() <= cardWspX + 150 && cardWspY + 110 * i <= e.getY() && e.getY() <= cardWspY + 110 * i + 100) {
                if (!Shogun.daimyos.get(i).isPlaying() || activePlayer > 3) {
                    Shogun.daimyos.get(i).setPlaying(!Shogun.daimyos.get(i).isPlaying());
                }
            }
            if (cardWspX - 115 <= e.getX() && e.getX() <= cardWspX - 115 + 150 && cardWspY + 5 + 110 * i <= e.getY() && e.getY() <= cardWspY + 5 + 110 * i + 100) {
                if (Shogun.daimyos.get(i).isPlaying()) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        Shogun.daimyos.get(i).setStatus((Shogun.daimyos.get(i).getStatus() + 1) % 5);
                    } else {
                        Shogun.daimyos.get(i).setStatus((Shogun.daimyos.get(i).getStatus() + 4) % 5);
                    }
                }
            }
        }
        if (playHighlighted) {
            Shogun.step = Shogun.step % 3 + 1;
        }
        if (350 + (Shogun.resolutionX - 300 - play.getImg().getWidth(null)) / 2 <= e.getX() && e.getX() <= 350 + (Shogun.resolutionX - 300 - play.getImg().getWidth(null)) / 2 + 90 && (Shogun.resolutionY - play.getImg().getHeight(null)) / 2 - 110 <= e.getY() && e.getY() <= (Shogun.resolutionY - play.getImg().getHeight(null)) / 2 - 110 + 90) {
            Shogun.choosingStartingProvinces = !Shogun.choosingStartingProvinces;
        }
        if (450 + (Shogun.resolutionX - 300 - play.getImg().getWidth(null)) / 2 <= e.getX() && e.getX() <= 450 + (Shogun.resolutionX - 300 - play.getImg().getWidth(null)) / 2 + 90 && (Shogun.resolutionY - play.getImg().getHeight(null)) / 2 - 110 <= e.getY() && e.getY() <= (Shogun.resolutionY - play.getImg().getHeight(null)) / 2 - 110 + 90) {
            Shogun.bonuses = !Shogun.bonuses;
        }
    }

    static void mouseMoved(MouseEvent e) {
        boolean over = false;
        for (int i = 0; i < 5; i++) {
            if (cardWspX <= e.getX() && e.getX() <= cardWspX + 150 && cardWspY + 110 * i <= e.getY() && e.getY() <= cardWspY + 110 * i + 100) {
                highlightXL = 150;
                highlightYL = 40 + 110 * i;
                highlightNum = i;
                over = true;
            }
        }
        if (!over) {
            highlightXL = 0;
            highlightYL = 0;
            highlightNum = 5;
        }
        playHighlighted = 300 + (Shogun.resolutionX - 300 - play.getImg().getWidth(null)) / 2 + 12 <= e.getX() && e.getX() <= 300 + (Shogun.resolutionX - 300 - play.getImg().getWidth(null)) / 2 + 12 + 275 && (Shogun.resolutionY - play.getImg().getHeight(null)) / 2 + 52 <= e.getY() && e.getY() <= (Shogun.resolutionY - play.getImg().getHeight(null)) / 2 + 52 + 92;
    }

    public GameStep2Options() throws IOException {
        super(new Picture("/img/small/background/step2.png"), new Picture("/img/large/background/step2.png"));
        daimyoBackS = new Picture("/img/small/card/daimyo/daimyo_back.png");
        daimyoBackL = new Picture("/img/large/card/daimyo/daimyo_back.png");
        highlightXL = 0;
        highlightYL = 0;
        highlightNum = 5;
        daimyoInfo = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            daimyoInfo.add(new Picture("/img/start/daimyo" + Integer.toString(i) + ".png"));
        }
        status = new ArrayList<>();
        status.add(new Picture("/img/start/player.png"));
        status.add(new Picture("/img/start/random.png"));
        status.add(new Picture("/img/start/builder.png"));
        status.add(new Picture("/img/start/balanced.png"));
        status.add(new Picture("/img/start/offensive.png"));
        play = new Picture("/img/welcome/play.png");
        highlight = new Picture("/img/welcome/highlight.png");
        playHighlighted = false;
        choosingOn = new Picture("/img/start/choosingStartProvincesYes.png");
        choosingOff = new Picture("/img/start/choosingStartProvincesNo.png");
        bonusesOn = new Picture("/img/start/bonusesYes.png");
        bonusesOff = new Picture("/img/start/bonusesNo.png");
    }

    @Override
    public void work(MyJFrame window) {
        while (Shogun.step == 2) {
            this.paintBackground();
            super.showOnScreen(window);
            Shogun.sem.release();
            try {
                Thread.sleep(Shogun.refreshPeriod);
            } catch (InterruptedException ex) {
                Logger.getLogger(Shogun.class.getName()).log(Level.SEVERE, null, ex);
            }
            Shogun.sem.acquireUninterruptibly();
        }
    }

    @Override
    public void paintBackground() {
        super.paintBackground();
        super.getGraphics().drawImage(play.getImg(), 300 + (Shogun.resolutionX - 300 - play.getImg().getWidth(null)) / 2, (Shogun.resolutionY - play.getImg().getHeight(null)) / 2, null);
        if (playHighlighted) {
            super.getGraphics().drawImage(highlight.getImg(), 300 + (Shogun.resolutionX - 300 - play.getImg().getWidth(null)) / 2, (Shogun.resolutionY - play.getImg().getHeight(null)) / 2, null);
        }
        if (Shogun.choosingStartingProvinces) {
            super.getGraphics().drawImage(choosingOn.getImg(), 350 + (Shogun.resolutionX - 300 - play.getImg().getWidth(null)) / 2, (Shogun.resolutionY - play.getImg().getHeight(null)) / 2 - 110, null);
        } else {
            super.getGraphics().drawImage(choosingOff.getImg(), 350 + (Shogun.resolutionX - 300 - play.getImg().getWidth(null)) / 2, (Shogun.resolutionY - play.getImg().getHeight(null)) / 2 - 110, null);
        }
        if (Shogun.bonuses) {
            super.getGraphics().drawImage(bonusesOn.getImg(), 450 + (Shogun.resolutionX - 300 - play.getImg().getWidth(null)) / 2, (Shogun.resolutionY - play.getImg().getHeight(null)) / 2 - 110, null);
        } else {
            super.getGraphics().drawImage(bonusesOff.getImg(), 450 + (Shogun.resolutionX - 300 - play.getImg().getWidth(null)) / 2, (Shogun.resolutionY - play.getImg().getHeight(null)) / 2 - 110, null);
        }
        for (int i = 0; i < 5; i++) {
            super.getGraphics().drawImage(super.getCardHorizontalShadeL(), 150 - 5, 40 + 110 * i - 5, null);
            if (Shogun.daimyos.get(i).isPlaying()) {
                super.getGraphics().drawImage(Shogun.daimyos.get(i).getDaimyoL(), 150, 40 + 110 * i, null);
                super.getGraphics().drawImage(status.get(Shogun.daimyos.get(i).getStatus()).getImg(), 80 - status.get(Shogun.daimyos.get(i).getStatus()).getImg().getWidth(null) / 2, 45 + 110 * i, null);
            } else {
                super.getGraphics().drawImage(daimyoBackL.getImg(), 150, 40 + 110 * i, null);
            }
        }
        if (highlightXL != 0 || highlightYL != 0) {
            super.getGraphics().drawImage(super.getCardHorizontalHighlightL(), highlightXL, highlightYL, null);
            if (Shogun.daimyos.get(highlightNum).isPlaying()) {
                super.getGraphics().drawImage(daimyoInfo.get(highlightNum).getImg(), 350, 40, null);
            }
        }
    }
}
