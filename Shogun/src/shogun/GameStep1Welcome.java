/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shogun;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Imrihil
 */
public class GameStep1Welcome extends GameStep {

    static void mouseMoved(MouseEvent e) {
        highlighted = null;
        if (play.getStX() <= e.getX() && e.getX() <= play.getStX() + play.getSiX() && play.getStY() <= e.getY() && e.getY() <= play.getStY() + play.getSiY()) {
            highlighted = play;
        } else if (settings.getStX() <= e.getX() && e.getX() <= settings.getStX() + settings.getSiX() && settings.getStY() <= e.getY() && e.getY() <= play.getStY() + settings.getSiY()) {
            highlighted = settings;
        } else if (instruction.getStX() <= e.getX() && e.getX() <= instruction.getStX() + instruction.getSiX() && instruction.getStY() <= e.getY() && e.getY() <= play.getStY() + instruction.getSiY()) {
            highlighted = instruction;
        }
    }

    static void mouseClicked(MouseEvent e) {
        if (highlighted != null) {
            if (highlighted.equals(play)) {
                Shogun.step++;
            } else {

            }
        }
    }

    private static Picture logoS;
    private static Picture logoL;

    private static GameStep1WelcomeButton play;
    private static GameStep1WelcomeButton settings;
    private static GameStep1WelcomeButton instruction;
    private static Picture highlight;
    private static GameStep1WelcomeButton highlighted;

    private int choose;

    public GameStep1Welcome() throws IOException {
        super(new Picture("/img/small/background/step1.png"), new Picture("/img/large/background/step1.png"));
        logoS = new Picture("/img/small/logo.png");
        logoL = new Picture("/img/large/logo.png");
        play = new GameStep1WelcomeButton(new Picture("/img/welcome/play.png"), (Shogun.resolutionX - 3 * 300 * 11 / 10) / 2, Shogun.resolutionY - 5 * 198 / 3, (Shogun.resolutionX - 3 * 300 * 11 / 10) / 2 + 12, Shogun.resolutionY - 5 * 198 / 3 + 52, 275, 92);
        settings = new GameStep1WelcomeButton(new Picture("/img/welcome/settings.png"), (Shogun.resolutionX - 300 * 11 / 10) / 2, Shogun.resolutionY - 5 * 198 / 3, (Shogun.resolutionX - 300 * 11 / 10) / 2 + 12, Shogun.resolutionY - 5 * 198 / 3 + 52, 275, 92);
        instruction = new GameStep1WelcomeButton(new Picture("/img/welcome/instruction.png"), (Shogun.resolutionX + 300 * 11 / 10) / 2, Shogun.resolutionY - 5 * 198 / 3, (Shogun.resolutionX + 300 * 11 / 10) / 2 + 12, Shogun.resolutionY - 5 * 198 / 3 + 52, 275, 92);
        highlight = new Picture("/img/welcome/highlight.png");
        highlighted = null;
    }

    @Override
    public void work(MyJFrame window) {
        while (Shogun.step == 1) {
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
        if (Shogun.size == 1) {
            super.getGraphics().drawImage(logoS.getImg(), (Shogun.resolutionX - logoS.getImg().getWidth(null)) / 2, 50, null);
        } else {
            super.getGraphics().drawImage(logoL.getImg(), (Shogun.resolutionX - logoL.getImg().getWidth(null)) / 2, 50, null);
        }
        play.paintComponent(super.getGraphics());
        settings.paintComponent(super.getGraphics());
        instruction.paintComponent(super.getGraphics());
        if (highlighted != null) {
            super.getGraphics().drawImage(highlight.getImg(), highlighted.getX(), highlighted.getY(), null);
        }
    }
}
