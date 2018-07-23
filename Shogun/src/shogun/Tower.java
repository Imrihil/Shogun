/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shogun;

import java.io.IOException;
import static java.lang.Math.min;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Imrihil
 */
public class Tower {

    /**
     * @param aGameStep the gameStep to set
     */
    public static void setGameStep(GameStep3Game aGameStep) {
        gameStep = aGameStep;
    }

    /**
     * @param aWindow the window to set
     */
    public static void setWindow(MyJFrame aWindow) {
        window = aWindow;
    }

    private List<Block> firstLvl;
    private List<Block> secondLvl;
    private List<Block> tray;

    private static Picture battleTower;

    private static GameStep3Game gameStep;

    private static MyJFrame window;

    Tower() throws IOException {
        battleTower = new Picture("/img/battleTower.png");
        firstLvl = new ArrayList<>();
        secondLvl = new ArrayList<>();
        tray = new ArrayList<>();
    }

    public void battle(Daimyo attacker, int strength, Province province) {
        List<Block> toThrow = new ArrayList<>();
        if (province == null) {
            for (Daimyo daimyo : Shogun.daimyos) {
                if (daimyo.isPlaying()) {
                    for (int i = 0; i < 7; i++) {
                        toThrow.add(new Block(daimyo));
                    }
                }
            }
            for (int i = 0; i < 10; i++) {
                toThrow.add(new Block(null));
            }
            pushToTower(toThrow, province);
        } else if (attacker == null) {
            for (int i = 0; i < strength; i++) {
                toThrow.add(new Block(attacker));
            }
            for (int i = 0; i < province.getArmies(); i++) {
                toThrow.add(new Block(province.getOwner()));
            }
            if (province.getOwner().getSpecial() != null && province.getOwner().getSpecial().getDefenceBonus() == 1) {
                toThrow.add(new Block(province.getOwner()));
            }
            if (Shogun.allEvents.get(GameStep3Game.turnNr).getCastleDefenderBonus() == 1) {
                for (Place building : province.getBuildings()) {
                    if (building.getType() == Building.CASTLE) {
                        toThrow.add(new Block(province.getOwner()));
                    }
                }
            }
            tray = pushToTower(toThrow, province);
            int nAtk = 0, nDef = 0;
            toThrow = new ArrayList<>();
            for (Block block : tray) {
                if (block.getOwner() == null) {
                    nAtk++;
                } else if (block.getOwner().equals(province.getOwner())) {
                    nDef++;
                } else {
                    toThrow.add(block);
                }
            }
            tray = toThrow;
            if (nAtk >= nDef) {
                province.destroy();
            } else {
                province.setArmies(nDef - nAtk);
                province.setRevolt(province.getRevolt() + 1);
            }
        } else {
            for (int i = 0; i < strength; i++) {
                toThrow.add(new Block(attacker));
            }
            if (attacker.getSpecial() != null && attacker.getSpecial().getAttackBonus() == 1) {
                toThrow.add(new Block(attacker));
            }
            if (province.getOwner() == null) {
                toThrow.add(new Block(null));
                if (Shogun.allEvents.get(GameStep3Game.turnNr).getRevolt() == 1) {
                    toThrow.add(new Block(null));
                }
                tray = pushToTower(toThrow, province);
                int nAtk = 0, nDef = 0;
                toThrow = new ArrayList<>();
                for (Block block : tray) {
                    if (block.getOwner() == null) {
                        nDef++;
                    } else if (block.getOwner().equals(attacker)) {
                        nAtk++;
                    } else {
                        toThrow.add(block);
                    }
                }
                tray = toThrow;
                if (nAtk > nDef) {
                    province.setOwner(attacker);
                    province.setArmies(nAtk - nDef);
                } else {
                    province.destroy();
                }
            } else {
                for (int i = 0; i < province.getArmies(); i++) {
                    toThrow.add(new Block(province.getOwner()));
                }
                if (province.getOwner().getSpecial() != null && province.getOwner().getSpecial().getDefenceBonus() == 1) {
                    toThrow.add(new Block(province.getOwner()));
                }
                if (Shogun.allEvents.get(GameStep3Game.turnNr).getCastleDefenderBonus() == 1) {
                    for (Place building : province.getBuildings()) {
                        if (building.getType() == Building.CASTLE) {
                            toThrow.add(new Block(province.getOwner()));
                        }
                    }
                }
                tray = pushToTower(toThrow, province);
                int nAtk = 0, nDef = 0, nSup = 0;
                toThrow = new ArrayList<>();
                if (province.getRevolt() == 0) {
                    for (Block block : tray) {
                        if (block.getOwner() == null) {
                            nSup++;
                        } else if (block.getOwner().equals(attacker)) {
                            nAtk++;
                        } else if (block.getOwner().equals(province.getOwner())) {
                            nDef++;
                        } else {
                            toThrow.add(block);
                        }
                    }
                } else {
                    for (Block block : tray) {
                        if (block.getOwner() != null && block.getOwner().equals(attacker)) {
                            nAtk++;
                        } else if (block.getOwner() != null && block.getOwner().equals(province.getOwner())) {
                            nDef++;
                        } else {
                            toThrow.add(block);
                        }
                    }
                }
                tray = toThrow;
                if (nAtk > nDef + nSup) {
                    province.setAction(null);
                    province.setOwner(attacker);
                    province.setArmies(nAtk - nDef - nSup);
                } else if (nAtk == nDef + nSup || nDef == 0) {
                    province.destroy();
                } else {
                    province.setArmies(min(nDef + nSup - nAtk, nDef));
                }
            }
        }
    }

    private List<Block> pushToTower(List<Block> in, Province province) {
        int startPhase = GameStep3Game.phase;
        for (Block block : tray) {
            in.add(block);
        }
        List<Block> result;
        List<Block> showResult = new ArrayList<>();
        int delay = 0;
        Random generator = new Random();
        Collections.shuffle(in);
        System.out.println("Wrzucono do wieży:");
        for (int i = 0; i < in.size(); i++) {
            if (in.get(i).getOwner() == null) {
                System.out.printf("5 ");
            } else {
                System.out.printf("%d ", in.get(i).getOwner().getId());
            }
            if (i % 10 == 9) {
                System.out.println("");
            }
        }
        System.out.println("");
        GameStep3Game.phase = 10;
        int x, y;
        while (GameStep3Game.phase == 10) {
            gameStep.paintBackground();
            if (startPhase > 3) {
                gameStep.paintObject(gameStep.getCardVerticalShadeS(), gameStep.getCardVerticalShadeL(), 172 - 5, 1128 - 5);
                gameStep.paintObject(Shogun.allEvents.get(GameStep3Game.turnNr).getCardS(), Shogun.allEvents.get(GameStep3Game.turnNr).getCardL(), 172, 1128);
            }
            if (province != null) {
                gameStep.paintObject(province.getHighlightS(), province.getHighlightL(), province.getX(), province.getY());
            }
            gameStep.getGraphics().drawImage(battleTower.getImg(), (Shogun.resolutionX - battleTower.getImg().getWidth(null)) / 2, (Shogun.resolutionY - battleTower.getImg().getHeight(null)) / 2, null);
            for (int i = 0; i < in.size(); i++) {
                if (i < 100) {
                    x = (Shogun.resolutionX - battleTower.getImg().getWidth(null)) / 2 + 259 + 20 * (i % 8) + ((i / 8) % 2) * 10;
                    y = (Shogun.resolutionY - battleTower.getImg().getHeight(null)) / 2 + 22 + 7 * (i / 8);
                } else {
                    x = (Shogun.resolutionX - battleTower.getImg().getWidth(null)) / 2 + 259 + 40 * ((i % 100) % 4) + (((i % 100) / 4) % 2) * 20 + 5;
                    y = (Shogun.resolutionY - battleTower.getImg().getHeight(null)) / 2 + 22 + 11 * ((i % 100) / 4) + 3;
                }
                gameStep.getGraphics().drawImage(GameStep3Game.armyShadeL.getImg(), x - 3, y - 4, null);
                if (in.get(i).getOwner() != null) {
                    gameStep.getGraphics().drawImage(GameStep3Game.armyL.get(in.get(i).getOwner().getId()).getImg(), x, y, null);
                } else {
                    gameStep.getGraphics().drawImage(GameStep3Game.armyL.get(5).getImg(), x, y, null);
                }
            }
            gameStep.showOnScreen(window);
            Shogun.sem.release();
            try {
                Thread.sleep(Shogun.refreshPeriod);
            } catch (InterruptedException ex) {
                Logger.getLogger(Shogun.class.getName()).log(Level.SEVERE, null, ex);
            }
            Shogun.sem.acquireUninterruptibly();
        }
        result = throughLvl(in, 1);
        result = throughLvl(result, 2);
        System.out.println("Wypadło z wieży:");
        for (int i = 0; i < result.size(); i++) {
            showResult.add(result.get(i));
            showResult.get(i).setDelay(generator.nextInt(result.size() * 3));
            if (result.get(i).getOwner() == null) {
                System.out.printf("5 ");
            } else {
                System.out.printf("%d ", result.get(i).getOwner().getId());
            }
            if (i % 10 == 9) {
                System.out.println("");
            }
        }
        System.out.println("");
        Collections.sort(showResult);
        while (GameStep3Game.phase == 11) {
            gameStep.paintBackground();
            if (startPhase > 3) {
                gameStep.paintObject(gameStep.getCardVerticalShadeS(), gameStep.getCardVerticalShadeL(), 172 - 5, 1128 - 5);
                gameStep.paintObject(Shogun.allEvents.get(GameStep3Game.turnNr).getCardS(), Shogun.allEvents.get(GameStep3Game.turnNr).getCardL(), 172, 1128);
            }
            if (province != null) {
                gameStep.paintObject(province.getHighlightS(), province.getHighlightL(), province.getX(), province.getY());
            }
            gameStep.getGraphics().drawImage(battleTower.getImg(), (Shogun.resolutionX - battleTower.getImg().getWidth(null)) / 2, (Shogun.resolutionY - battleTower.getImg().getHeight(null)) / 2, null);
            int showed = 0;
            for (Block block : showResult) {
                if (block.getDelay() <= delay) {
                    if (showed < 100) {
                        x = (Shogun.resolutionX - battleTower.getImg().getWidth(null)) / 2 + 84 + 20 * (showed % 8) + ((showed / 8) % 2) * 10;
                        y = (Shogun.resolutionY - battleTower.getImg().getHeight(null)) / 2 + 374 + 7 * (showed / 8);
                    } else {
                        x = (Shogun.resolutionX - battleTower.getImg().getWidth(null)) / 2 + 84 + 40 * ((showed % 100) % 4) + (((showed % 100) / 4) % 2) * 20 + 5;
                        y = (Shogun.resolutionY - battleTower.getImg().getHeight(null)) / 2 + 374 + 11 * ((showed % 100) / 4) + 3;
                    }
                    gameStep.getGraphics().drawImage(GameStep3Game.armyShadeL.getImg(), x - 3, y - 4, null);
                    if (block.getOwner() != null) {
                        gameStep.getGraphics().drawImage(GameStep3Game.armyL.get(block.getOwner().getId()).getImg(), x, y, null);
                    } else {
                        gameStep.getGraphics().drawImage(GameStep3Game.armyL.get(5).getImg(), x, y, null);
                    }
                    ++showed;
                }
            }
            gameStep.showOnScreen(window);
            Shogun.sem.release();
            try {
                Thread.sleep(Shogun.refreshPeriod);
            } catch (InterruptedException ex) {
                Logger.getLogger(Shogun.class.getName()).log(Level.SEVERE, null, ex);
            }
            Shogun.sem.acquireUninterruptibly();
            if (delay < 1000) {
                delay++;
            }
        }
        System.out.println("Na 1. poziomie zostało:");
        for (int i = 0; i < firstLvl.size(); i++) {
            if (firstLvl.get(i).getOwner() == null) {
                System.out.printf("5 ");
            } else {
                System.out.printf("%d ", firstLvl.get(i).getOwner().getId());
            }
            if (i % 10 == 9) {
                System.out.println("");
            }
        }
        System.out.println("");
        System.out.println("Na 2. poziomie zostało:");
        for (int i = 0; i < secondLvl.size(); i++) {
            if (secondLvl.get(i).getOwner() == null) {
                System.out.printf("5 ");
            } else {
                System.out.printf("%d ", secondLvl.get(i).getOwner().getId());
            }
            if (i % 10 == 9) {
                System.out.println("");
            }
        }
        System.out.println("");
        System.out.println("");
        GameStep3Game.phase = startPhase;
        return result;
    }

    private List<Block> throughLvl(List<Block> in, int lvlNr) {
        Random generator = new Random();
        List<Block> newLvl, result = new ArrayList<>();
        for (Block block : in) {
            newLvl = new ArrayList<>();
            if (lvlNr == 1) {
                for (Block blockOnFirstLvl : firstLvl) {
                    if (generator.nextInt(40) < 1) {
                        result.add(blockOnFirstLvl);
                    } else {
                        newLvl.add(blockOnFirstLvl);
                    }
                }
            } else {
                for (Block blockOnFirstLvl : secondLvl) {
                    if (generator.nextInt(40) < 1) {
                        result.add(blockOnFirstLvl);
                    } else {
                        newLvl.add(blockOnFirstLvl);
                    }
                }
            }
            if (generator.nextInt(20) < 17) {
                result.add(block);
            } else {
                newLvl.add(block);
            }
            if (lvlNr == 1) {
                firstLvl = newLvl;
            } else {
                secondLvl = newLvl;
            }
        }
        return result;
    }

    public int inTower(Daimyo daimyo) {
        int result = 0;
        for (Block block : firstLvl) {
            if (block.getOwner() != null) {
                if (block.getOwner().equals(daimyo)) {
                    result++;
                }
            }
        }
        for (Block block : secondLvl) {
            if (block.getOwner() != null) {
                if (block.getOwner().equals(daimyo)) {
                    result++;
                }
            }
        }
        for (Block block : tray) {
            if (block.getOwner() != null) {
                if (block.getOwner().equals(daimyo)) {
                    result++;
                }
            }
        }
        return result;
    }
}
