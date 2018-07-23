/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shogun;

import java.awt.event.MouseEvent;
import java.io.IOException;
import static java.lang.Math.min;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Imrihil
 */
public class Action6Recruitment3 extends Action {

    public Action6Recruitment3() throws IOException {
        super("Recruitment3", new Picture("/img/small/card/action/action6.png"), new Picture("/img/large/card/action/action6.png"), new Picture("/img/large/card/action/doing/accept.png"), new Picture("/img/large/card/action/doing/acceptNoAvailable.png"), new Picture("/img/large/card/action/doing/decline.png"), new Picture("/img/large/card/action/doing/choose_highlighted.png"), new Picture("/img/large/card/action/doing/action6.png"), 638, 290);
    }

    @Override
    public void doAction(GameStep3Game gameStep, MyJFrame window, List<Daimyo> daimyos) {
        for (Daimyo daimyo : daimyos) {
            if (daimyo.getAction(super.getName()) != null) {
                if (!daimyo.getAction(super.getName()).isTurnOn()) {
                    daimyo.getAction(super.getName()).turn();
                }
            }
        }
        int daimyoTurn = 0;
        GameStep3Game.showing = daimyos.get(daimyoTurn);
        GameStep3Game.activeDaimyo = daimyos.get(daimyoTurn);
        if (GameStep3Game.activeDaimyo.getActionProvince(super.getName()) != null && GameStep3Game.activeDaimyo.getGold() >= 2 && GameStep3Game.activeDaimyo.getArmies() > 0) {
            super.setPossible(true);
            GameStep3Game.provinceHiglighted = GameStep3Game.activeDaimyo.getActionProvince(super.getName());
        } else {
            super.setPossible(false);
        }
        while (true) {
            gameStep.paintBackground();
            gameStep.getGraphics().drawImage(super.getInfo(), (Shogun.resolutionX - super.getInfo().getWidth(null)) / 2, (Shogun.resolutionY - super.getInfo().getHeight(null)) / 2, null);
            if (super.isPossible()) {
                gameStep.getGraphics().drawImage(Action.getAccept(), (Shogun.resolutionX - super.getInfo().getWidth(null)) / 2 + super.getInfo().getWidth(null) / 3 - Action.getAccept().getWidth(null) / 2, (Shogun.resolutionY - super.getInfo().getHeight(null)) / 2 + super.getInfo().getHeight(null) / 3 * 2 - Action.getAccept().getHeight(null) / 3 * 2, null);
            } else {
                gameStep.getGraphics().drawImage(Action.getAcceptNo(), (Shogun.resolutionX - super.getInfo().getWidth(null)) / 2 + super.getInfo().getWidth(null) / 3 - Action.getAcceptNo().getWidth(null) / 2, (Shogun.resolutionY - super.getInfo().getHeight(null)) / 2 + super.getInfo().getHeight(null) / 3 * 2 - Action.getAcceptNo().getHeight(null) / 3 * 2, null);
            }
            gameStep.getGraphics().drawImage(Action.getDecline(), (Shogun.resolutionX - super.getInfo().getWidth(null)) / 2 + super.getInfo().getWidth(null) / 3 * 2 - Action.getDecline().getWidth(null) / 2, (Shogun.resolutionY - super.getInfo().getHeight(null)) / 2 + super.getInfo().getHeight(null) / 3 * 2 - Action.getDecline().getHeight(null) / 3 * 2, null);
            if (super.getChoose() == 1) {
                gameStep.getGraphics().drawImage(Action.getHiglight(), (Shogun.resolutionX - super.getInfo().getWidth(null)) / 2 + super.getInfo().getWidth(null) / 3 - Action.getHiglight().getWidth(null) / 2, (Shogun.resolutionY - super.getInfo().getHeight(null)) / 2 + super.getInfo().getHeight(null) / 3 * 2 - Action.getHiglight().getHeight(null) / 3 * 2, null);
            } else if (super.getChoose() == 2) {
                gameStep.getGraphics().drawImage(Action.getHiglight(), (Shogun.resolutionX - super.getInfo().getWidth(null)) / 2 + super.getInfo().getWidth(null) / 3 * 2 - Action.getHiglight().getWidth(null) / 2, (Shogun.resolutionY - super.getInfo().getHeight(null)) / 2 + super.getInfo().getHeight(null) / 3 * 2 - Action.getHiglight().getHeight(null) / 3 * 2, null);
            }
            gameStep.showOnScreen(window);
            Shogun.sem.release();
            try {
                Thread.sleep(Shogun.refreshPeriod);
            } catch (InterruptedException ex) {
                Logger.getLogger(Shogun.class.getName()).log(Level.SEVERE, null, ex);
            }
            Shogun.sem.acquireUninterruptibly();
            if (GameStep3Game.activeDaimyo.getStatus() > 0) {
                GameStep3Game.activeDaimyo.getAI().doAction6Recruitment3(GameStep3Game.activeDaimyo, super.isPossible(), this);
            }
            if (GameStep3Game.activeDaimyo.isReady()) {
                daimyoTurn++;
                if (daimyoTurn == daimyos.size()) {
                    break;
                } else {
                    GameStep3Game.showing = daimyos.get(daimyoTurn);
                    GameStep3Game.activeDaimyo = daimyos.get(daimyoTurn);
                }
                if (GameStep3Game.activeDaimyo.getActionProvince(super.getName()) != null && GameStep3Game.activeDaimyo.getGold() >= 2 && GameStep3Game.activeDaimyo.getArmies() > 0) {
                    super.setPossible(true);
                    GameStep3Game.provinceHiglighted = GameStep3Game.activeDaimyo.getActionProvince(super.getName());
                } else {
                    super.setPossible(false);
                }
            }
        }
        for (Daimyo daimyo : Shogun.daimyos) {
            if (daimyo.isPlaying()) {
                daimyo.setReady(false);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (super.getChoose() == 1) {
            GameStep3Game.activeDaimyo.setGold(GameStep3Game.activeDaimyo.getGold() - 2);
            GameStep3Game.activeDaimyo.getActionProvince(super.getName()).setArmies(GameStep3Game.activeDaimyo.getActionProvince(super.getName()).getArmies() + min(min(3, Shogun.allEvents.get(GameStep3Game.turnNr).getRecruitment3()), GameStep3Game.activeDaimyo.getArmies()));
            GameStep3Game.activeDaimyo.setReady(true);
            GameStep3Game.provinceHiglighted = null;
        } else if (super.getChoose() == 2) {
            GameStep3Game.activeDaimyo.setReady(true);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        GameStep3Game.provinceHiglighted = GameStep3Game.activeDaimyo.getActionProvince(super.getName());
        super.setChoose(0);
        if (GameStep3Game.activeDaimyo.getStatus() == 0) {
            int stX, stY, siX, siY;
            if (super.isPossible()) {
                stX = (Shogun.resolutionX - super.getInfo().getWidth(null)) / 2 + super.getInfo().getWidth(null) / 3 - Action.getAccept().getWidth(null) / 2;
                stY = (Shogun.resolutionY - super.getInfo().getHeight(null)) / 2 + super.getInfo().getHeight(null) / 3 * 2 - Action.getAccept().getHeight(null) / 3 * 2;
                siX = Action.getAccept().getWidth(null);
                siY = Action.getAccept().getHeight(null);
                if (stX <= e.getX() && e.getX() <= stX + siX && stY <= e.getY() && e.getY() <= stY + siY) {
                    super.setChoose(1);
                }
            }
            stX = (Shogun.resolutionX - super.getInfo().getWidth(null)) / 2 + super.getInfo().getWidth(null) / 3 * 2 - Action.getDecline().getWidth(null) / 2;
            stY = (Shogun.resolutionY - super.getInfo().getHeight(null)) / 2 + super.getInfo().getHeight(null) / 3 * 2 - Action.getDecline().getHeight(null) / 3 * 2;
            siX = Action.getDecline().getWidth(null);
            siY = Action.getDecline().getHeight(null);
            if (stX <= e.getX() && e.getX() <= stX + siX && stY <= e.getY() && e.getY() <= stY + siY) {
                super.setChoose(2);
            }
        }
    }
}
