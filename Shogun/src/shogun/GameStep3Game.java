/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shogun;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;
import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Imrihil
 */
public class GameStep3Game extends GameStep {

    protected static boolean showResources;

    protected static boolean showArmies;

    protected static int turnNr;

    protected static int actionNr;

    protected static int phase;

    protected static volatile Daimyo showing;

    protected static volatile Daimyo activeDaimyo;

    protected static volatile Province provinceHiglighted;

    protected static volatile Chest chestHiglighted;

    protected static volatile Daimyo daimyoHiglighted;

    protected static volatile Special specialHiglighted;

    protected static List<Province> choosingProvonces;

    protected static List<Picture> armyS;
    protected static List<Picture> armyL;

    protected static Picture armyShadeS;
    protected static Picture armyShadeL;

    private static List<Daimyo> daimyos;

    private static int nRevolts;

    private static int rebeliantsBonus;

    private static int step;

    private static List<Event> events;

    private static Picture resourcesS;
    private static Picture resourcesL;

    private static Picture buildingShadeS;
    private static Picture buildingShadeL;

    private static Picture castleS;
    private static Picture castleL;

    private static Picture templeS;
    private static Picture templeL;

    private static Picture theatreS;
    private static Picture theatreL;

    private static Picture revoltS;
    private static Picture revoltL;

    private static Picture revoltShadeS;
    private static Picture revoltShadeL;

    private static Picture gold1S;
    private static Picture gold1L;
    private static Picture gold5S;
    private static Picture gold5L;

    private static Picture katanasS;
    private static Picture katanasL;

    private static Picture pointShadeS;
    private static Picture pointShadeL;

    private static Picture riceShadeS;
    private static Picture riceShadeL;

    private static Picture winterRevoltS;
    private static Picture winterRevoltL;

    private static Picture specialInfoS;
    private static Picture specialInfoL;

    private static boolean readyToBattle;

    private static boolean endGame;

    public static void mouseClicked(MouseEvent e) {
        if (provinceHiglighted != null) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (phase == 3) {
                    if (provinceHiglighted.getOwner() != null && provinceHiglighted.getOwner().equals(showing)) {
                        if (provinceHiglighted.getAction() == null || !provinceHiglighted.getAction().getName().equals("Drag")) {
                            if (chestHiglighted == null) {
                                provinceHiglighted.setAction(Shogun.actionDrag);
                            }
                        } else {
                            boolean dropped = false;
                            int stX, stY, siX, siY;
                            for (Action action : Shogun.actions) {
                                stX = action.getX() / (Shogun.size % 2 + 1);
                                stY = action.getY() / (Shogun.size % 2 + 1);
                                siX = 100 / (Shogun.size % 2 + 1);
                                siY = 149 / (Shogun.size % 2 + 1);
                                if (stX + Shogun.shiftX <= e.getX() && e.getX() <= stX + siX + Shogun.shiftX && stY + Shogun.shiftY <= e.getY() && e.getY() <= stY + siY + Shogun.shiftY) {
                                    if (showing.getAction(action.getName()) == null) {
                                        provinceHiglighted.setAction(action);
                                    } else {
                                        if (showing.getActionProvince(action.getName()) != null) {
                                            showing.getActionProvince(action.getName()).setAction(Shogun.actionDrag);
                                            provinceHiglighted.setAction(action);
                                            provinceHiglighted = null;
                                            provinceHiglighted = showing.getActionProvince(Shogun.actionDrag.getName());
                                        } else if (showing.getActionChest(action.getName()) != null) {
                                            showing.getActionChest(action.getName()).setAction(Shogun.actionDrag);
                                            provinceHiglighted.setAction(action);
                                            provinceHiglighted = null;
                                            chestHiglighted = showing.getActionChest(Shogun.actionDrag.getName());
                                        }
                                    }
                                    dropped = true;
                                }
                            }
                            stX = Shogun.actionBid.getX() / (Shogun.size % 2 + 1);
                            stY = Shogun.actionBid.getY() / (Shogun.size % 2 + 1);
                            siX = 100 / (Shogun.size % 2 + 1);
                            siY = 149 / (Shogun.size % 2 + 1);
                            if (stX + Shogun.shiftX <= e.getX() && e.getX() <= stX + siX + Shogun.shiftX && stY + Shogun.shiftY <= e.getY() && e.getY() <= stY + siY + Shogun.shiftY) {
                                if (showing.getAction(Shogun.actionBid.getName()) == null) {
                                    provinceHiglighted.setAction(Shogun.actionBid);
                                } else {
                                    if (showing.getActionProvince(Shogun.actionBid.getName()) != null) {
                                        showing.getActionProvince(Shogun.actionBid.getName()).setAction(Shogun.actionDrag);
                                        provinceHiglighted.setAction(Shogun.actionBid);
                                        provinceHiglighted = null;
                                        provinceHiglighted = showing.getActionProvince(Shogun.actionDrag.getName());
                                    } else if (showing.getActionChest(Shogun.actionBid.getName()) != null) {
                                        showing.getActionChest(Shogun.actionBid.getName()).setAction(Shogun.actionDrag);
                                        provinceHiglighted.setAction(Shogun.actionBid);
                                        provinceHiglighted = null;
                                        chestHiglighted = showing.getActionChest(Shogun.actionDrag.getName());
                                    }
                                }
                                dropped = true;
                            }
                            if (!dropped) {
                                provinceHiglighted.setAction(null);
                                provinceHiglighted = null;
                            }
                        }
                    }
                } else if (phase == 8) {
                    readyToBattle = true;
                }
                /*int rand = new Random().nextInt(4);
                 switch (rand) {
                 case 0:
                 provinceHiglighted.addBuildings(Building.CASTLE);
                 break;
                 case 1:
                 provinceHiglighted.addBuildings(Building.TEMPLE);
                 break;
                 case 2:
                 provinceHiglighted.addBuildings(Building.THEATRE);
                 break;
                 default:
                 provinceHiglighted.setRevolt(provinceHiglighted.getRevolt() + 1);
                 break;
                 }*/
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                /*provinceHiglighted.destroy();
                 int maxArmies = 20;
                 if (provinceHiglighted.getOwner() != null) {
                 maxArmies = provinceHiglighted.getArmies() + provinceHiglighted.getOwner().getArmies();
                 }
                 provinceHiglighted.setArmies(min(provinceHiglighted.getArmies() + 1, maxArmies));*/
            }
        } else if (chestHiglighted != null) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (phase == 3) {
                    if (chestHiglighted.getAction() == null || !chestHiglighted.getAction().getName().equals("Drag")) {
                        if (provinceHiglighted == null) {
                            chestHiglighted.setAction(Shogun.actionDrag);
                        }
                    } else {
                        boolean dropped = false;
                        int stX, stY, siX, siY;
                        for (Action action : Shogun.actions) {
                            stX = action.getX() / (Shogun.size % 2 + 1);
                            stY = action.getY() / (Shogun.size % 2 + 1);
                            siX = 100 / (Shogun.size % 2 + 1);
                            siY = 149 / (Shogun.size % 2 + 1);
                            if (stX + Shogun.shiftX <= e.getX() && e.getX() <= stX + siX + Shogun.shiftX && stY + Shogun.shiftY <= e.getY() && e.getY() <= stY + siY + Shogun.shiftY) {
                                if (showing.getAction(action.getName()) == null) {
                                    chestHiglighted.setAction(action);
                                } else {
                                    if (showing.getActionProvince(action.getName()) != null) {
                                        showing.getActionProvince(action.getName()).setAction(Shogun.actionDrag);
                                        chestHiglighted.setAction(action);
                                        chestHiglighted = null;
                                        provinceHiglighted = showing.getActionProvince(Shogun.actionDrag.getName());
                                    } else if (showing.getActionChest(action.getName()) != null) {
                                        showing.getActionChest(action.getName()).setAction(Shogun.actionDrag);
                                        chestHiglighted.setAction(action);
                                        chestHiglighted = null;
                                        chestHiglighted = showing.getActionChest(Shogun.actionDrag.getName());
                                    }
                                }
                                dropped = true;
                            }
                        }
                        stX = Shogun.actionBid.getX() / (Shogun.size % 2 + 1);
                        stY = Shogun.actionBid.getY() / (Shogun.size % 2 + 1);
                        siX = 100 / (Shogun.size % 2 + 1);
                        siY = 149 / (Shogun.size % 2 + 1);
                        if (stX + Shogun.shiftX <= e.getX() && e.getX() <= stX + siX + Shogun.shiftX && stY + Shogun.shiftY <= e.getY() && e.getY() <= stY + siY + Shogun.shiftY) {
                            if (showing.getAction(Shogun.actionBid.getName()) == null) {
                                chestHiglighted.setAction(Shogun.actionBid);
                            } else {
                                if (showing.getActionProvince(Shogun.actionBid.getName()) != null) {
                                    showing.getActionProvince(Shogun.actionBid.getName()).setAction(Shogun.actionDrag);
                                    chestHiglighted.setAction(Shogun.actionBid);
                                    chestHiglighted = null;
                                    provinceHiglighted = showing.getActionProvince(Shogun.actionDrag.getName());
                                } else if (showing.getActionChest(Shogun.actionBid.getName()) != null) {
                                    showing.getActionChest(Shogun.actionBid.getName()).setAction(Shogun.actionDrag);
                                    chestHiglighted.setAction(Shogun.actionBid);
                                    chestHiglighted = null;
                                    chestHiglighted = showing.getActionChest(Shogun.actionDrag.getName());
                                }
                            }
                            dropped = true;
                        }
                        if (!dropped) {
                            chestHiglighted.setAction(null);
                            chestHiglighted = null;
                        }
                    }
                }
            }
        }
        if (specialHiglighted != null) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (phase == 5) {
                    activeDaimyo.setSpecial(specialHiglighted);
                    activeDaimyo.setReady(true);
                    activeDaimyo.setInitiative(specialHiglighted.getInitiative());
                    specialHiglighted.turn();
                    specialHiglighted = null;
                }
            }
        }
        if (daimyoHiglighted != null) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                showing = daimyoHiglighted;
                if (phase == 3) {
                    activeDaimyo = showing;
                }
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                if (phase == 3) {
                    if (daimyoHiglighted == showing && daimyoHiglighted.getStatus() == 0) {
                        daimyoHiglighted.setReady(!daimyoHiglighted.isReady());
                        for (String region : Shogun.world.keySet()) {
                            for (String province : Shogun.world.get(region).getProvinces().keySet()) {
                                if (Shogun.world.get(region).getProvinces().get(province).getOwner() != null && Shogun.world.get(region).getProvinces().get(province).getOwner().equals(showing)) {
                                    Shogun.world.get(region).getProvinces().get(province).turn();
                                }
                            }
                        }
                        for (Chest chestCard : showing.getChestCards()) {
                            chestCard.turn();
                        }
                    }
                }
            }
        }
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (phase == 0 && showing == activeDaimyo) {
                if (step == 0) {
                    if (provinceHiglighted != null) {
                        provinceHiglighted.setOwner(activeDaimyo);
                        choosingProvonces.remove(provinceHiglighted);
                        ++step;
                        //activeDaimyo.setReady(true);
                    } else {
                        if (Shogun.resolutionX - 440 + 240 <= e.getX() && e.getX() <= Shogun.resolutionX - 440 + 240 + 100 && 100 <= e.getY() && e.getY() <= 100 + 149) {
                            choosingProvonces.get(2).setOwner(activeDaimyo);
                            provinceHiglighted = choosingProvonces.get(2);
                            choosingProvonces.remove(2);
                            ++step;
                            //activeDaimyo.setReady(true);
                        }
                    }
                } else {
                    for (Province province : activeDaimyo.getStartingProvinces()) {
                        if (province.getArmies() > 0) {
                            int stX, stY, siX, siY;
                            stX = province.getStartX() / (Shogun.size % 2 + 1);
                            stY = province.getStartY() / (Shogun.size % 2 + 1);
                            siX = province.getSizeX() / (Shogun.size % 2 + 1);
                            siY = province.getSizeY() / (Shogun.size % 2 + 1);
                            if (stX + Shogun.shiftX <= e.getX() && e.getX() <= stX + siX + Shogun.shiftX && stY + Shogun.shiftY <= e.getY() && e.getY() <= stY + siY + Shogun.shiftY) {
                                provinceHiglighted.setArmies(province.getArmies());
                                provinceHiglighted = null;
                                province.setArmies(0);
                                activeDaimyo.setReady(true);
                                step = 0;
                            }
                        }
                    }
                }
            } else if (phase == 3) {
                /*int stX, stY, siX, siY;
                 stX = 386 / (Shogun.size % 2 + 1);
                 stY = 82 / (Shogun.size % 2 + 1);
                 siX = 707 / (Shogun.size % 2 + 1);
                 siY = 500 / (Shogun.size % 2 + 1);
                 if (stX + Shogun.shiftX <= e.getX() && e.getX() <= stX + siX + Shogun.shiftX && stY + Shogun.shiftY <= e.getY() && e.getY() <= stY + siY + Shogun.shiftY) {
                 for (String region : Shogun.world.keySet()) {
                 for (String province : Shogun.world.get(region).getProvinces().keySet()) {
                 if (Shogun.world.get(region).getProvinces().get(province).getOwner() != null && Shogun.world.get(region).getProvinces().get(province).getOwner().equals(showing)) {
                 Shogun.world.get(region).getProvinces().get(province).turn();
                 }
                 }
                 }
                 for (Chest chestCard : showing.getChestCards()) {
                 chestCard.turn();
                 }
                 }*/
            } else if (phase == 6) {
                Shogun.actions.get(actionNr).mouseClicked(e);
            } else if (phase == 10) {
                phase++;
            } else if (phase == 11) {
                phase++;
            }
        } else {
            if (phase == 3) {
                int stX, stY, siX, siY;
                stX = 386 / (Shogun.size % 2 + 1);
                stY = 82 / (Shogun.size % 2 + 1);
                siX = 707 / (Shogun.size % 2 + 1);
                siY = 500 / (Shogun.size % 2 + 1);
                if (stX + Shogun.shiftX <= e.getX() && e.getX() <= stX + siX + Shogun.shiftX && stY + Shogun.shiftY <= e.getY() && e.getY() <= stY + siY + Shogun.shiftY) {
                    showing.actionRandomise();
                }
            }
        }
    }

    public static void mouseMoved(MouseEvent e) {
        Shogun.actionDrag.setX((e.getX() - Shogun.shiftX) * (Shogun.size % 2 + 1));
        Shogun.actionDrag.setY((e.getY() - Shogun.shiftY) * (Shogun.size % 2 + 1));
        Shogun.provinceDrag.setArmiesX((e.getX() - Shogun.shiftX) * (Shogun.size % 2 + 1));
        Shogun.provinceDrag.setArmiesY((e.getY() - Shogun.shiftY) * (Shogun.size % 2 + 1));
        if ((provinceHiglighted == null || provinceHiglighted.getAction() == null || !provinceHiglighted.getAction().getName().equals("Drag")) && (step != 1 || phase != 0)) {
            provinceHiglighted = null;
        }
        if (chestHiglighted == null || chestHiglighted.getAction() == null || !chestHiglighted.getAction().getName().equals("Drag")) {
            chestHiglighted = null;
        }
        daimyoHiglighted = null;
        specialHiglighted = null;
        int showCards = 0, stX, stY, siX, siY;
        if (phase == 0) {
            if (step == 0) {
                if (provinceHiglighted == null) {
                    for (int i = 0; i < 2; i++) {
                        stX = Shogun.resolutionX - 440 + i * 120;
                        stY = 100;
                        siX = 100;
                        siY = 149;
                        if (stX <= e.getX() && e.getX() <= stX + siX && stY <= e.getY() && e.getY() <= stY + siY) {
                            provinceHiglighted = choosingProvonces.get(i);
                        }
                    }
                }
            }
        } else if (phase == 3) {
            if (provinceHiglighted == null && chestHiglighted == null) {
                for (String region : Shogun.world.keySet()) {
                    for (String province : Shogun.world.get(region).getProvinces().keySet()) {
                        if (Shogun.world.get(region).getProvinces().get(province).isInGame()) {
                            stX = Shogun.world.get(region).getProvinces().get(province).getStartX() / (Shogun.size % 2 + 1);
                            stY = Shogun.world.get(region).getProvinces().get(province).getStartY() / (Shogun.size % 2 + 1);
                            siX = Shogun.world.get(region).getProvinces().get(province).getSizeX() / (Shogun.size % 2 + 1);
                            siY = Shogun.world.get(region).getProvinces().get(province).getSizeY() / (Shogun.size % 2 + 1);
                            if (stX + Shogun.shiftX <= e.getX() && e.getX() <= stX + siX + Shogun.shiftX && stY + Shogun.shiftY <= e.getY() && e.getY() <= stY + siY + Shogun.shiftY) {
                                provinceHiglighted = Shogun.world.get(region).getProvinces().get(province);
                            }
                        }
                        if (Shogun.world.get(region).getProvinces().get(province).getOwner() != null) {
                            if (Shogun.world.get(region).getProvinces().get(province).getOwner().equals(showing)) {
                                if (Shogun.world.get(region).getProvinces().get(province).isTurnOn()) {
                                    if (Shogun.world.get(region).getProvinces().get(province).getAction() == null) {
                                        stX = (1101 + 104 * (showCards % 9)) / (Shogun.size % 2 + 1);
                                        stY = (113 + 153 * (showCards / 9)) / (Shogun.size % 2 + 1);
                                        showCards++;
                                    } else {
                                        stX = Shogun.world.get(region).getProvinces().get(province).getAction().getX() / (Shogun.size % 2 + 1);
                                        stY = Shogun.world.get(region).getProvinces().get(province).getAction().getY() / (Shogun.size % 2 + 1);
                                    }
                                    siX = 100 / (Shogun.size % 2 + 1);
                                    siY = 149 / (Shogun.size % 2 + 1);
                                    if (stX + Shogun.shiftX <= e.getX() && e.getX() <= stX + siX + Shogun.shiftX && stY + Shogun.shiftY <= e.getY() && e.getY() <= stY + siY + Shogun.shiftY) {
                                        provinceHiglighted = Shogun.world.get(region).getProvinces().get(province);
                                    }
                                }
                            }
                        }
                    }
                }
                /*if (showCards % 9 > 4) {
                 showCards += 9 - showCards % 9;
                 }*/
                if (showing != null && chestHiglighted == null && provinceHiglighted == null) {
                    for (int i = 0; i < showing.getChestCards().size(); i++) {
                        if (showing.getChestCards().get(i).isTurnOn()) {
                            if (showing.getChestCards().get(i).getAction() == null) {
                                stX = (1101 + 104 * (showCards % 9)) / (Shogun.size % 2 + 1);
                                stY = (113 + 153 * (showCards / 9)) / (Shogun.size % 2 + 1);
                                showCards++;
                            } else {
                                stX = showing.getChestCards().get(i).getAction().getX() / (Shogun.size % 2 + 1);
                                stY = showing.getChestCards().get(i).getAction().getY() / (Shogun.size % 2 + 1);
                            }
                            siX = 100 / (Shogun.size % 2 + 1);
                            siY = 149 / (Shogun.size % 2 + 1);
                            if (stX + Shogun.shiftX <= e.getX() && e.getX() <= stX + siX + Shogun.shiftX && stY + Shogun.shiftY <= e.getY() && e.getY() <= stY + siY + Shogun.shiftY) {
                                chestHiglighted = showing.getChestCards().get(i);
                            }
                        }
                    }
                }
            }
        } else if (phase == 5) {
            for (int i = 0; i < Shogun.specials.size(); i++) {
                if (Shogun.specials.get(i).isTurnOn()) {
                    stX = (125 + 179 * i) / (Shogun.size % 2 + 1);
                    stY = 645 / (Shogun.size % 2 + 1);
                    siX = 149 / (Shogun.size % 2 + 1);
                    siY = 100 / (Shogun.size % 2 + 1);
                    if (stX + Shogun.shiftX <= e.getX() && e.getX() <= stX + siX + Shogun.shiftX && stY + Shogun.shiftY <= e.getY() && e.getY() <= stY + siY + Shogun.shiftY) {
                        specialHiglighted = Shogun.specials.get(i);
                    }
                }
            }
        } else if (phase == 6) {
            Shogun.actions.get(actionNr).mouseMoved(e);
        } else if (phase == 8) {
            if (provinceHiglighted == null) {
                for (String region : Shogun.world.keySet()) {
                    for (String province : Shogun.world.get(region).getProvinces().keySet()) {
                        if (Shogun.world.get(region).getProvinces().get(province).isInGame()) {
                            if (Shogun.world.get(region).getProvinces().get(province).isTurnOn()) {
                                stX = Shogun.world.get(region).getProvinces().get(province).getStartX() / (Shogun.size % 2 + 1);
                                stY = Shogun.world.get(region).getProvinces().get(province).getStartY() / (Shogun.size % 2 + 1);
                                siX = Shogun.world.get(region).getProvinces().get(province).getSizeX() / (Shogun.size % 2 + 1);
                                siY = Shogun.world.get(region).getProvinces().get(province).getSizeY() / (Shogun.size % 2 + 1);
                                if (stX + Shogun.shiftX <= e.getX() && e.getX() <= stX + siX + Shogun.shiftX && stY + Shogun.shiftY <= e.getY() && e.getY() <= stY + siY + Shogun.shiftY) {
                                    provinceHiglighted = Shogun.world.get(region).getProvinces().get(province);
                                }
                            }
                        }
                        if (Shogun.world.get(region).getProvinces().get(province).getOwner() != null) {
                            if (Shogun.world.get(region).getProvinces().get(province).getOwner().equals(showing)) {
                                if (Shogun.world.get(region).getProvinces().get(province).isTurnOn()) {
                                    stX = (1101 + 104 * (showCards % 9)) / (Shogun.size % 2 + 1);
                                    stY = (113 + 153 * (showCards / 9)) / (Shogun.size % 2 + 1);
                                    siX = 100 / (Shogun.size % 2 + 1);
                                    siY = 149 / (Shogun.size % 2 + 1);
                                    if (stX + Shogun.shiftX <= e.getX() && e.getX() <= stX + siX + Shogun.shiftX && stY + Shogun.shiftY <= e.getY() && e.getY() <= stY + siY + Shogun.shiftY) {
                                        provinceHiglighted = Shogun.world.get(region).getProvinces().get(province);
                                    }
                                }
                                showCards++;
                            }
                        }
                    }
                }
            }
        }
        int i = 0;
        for (Daimyo daimyo : Shogun.daimyos) {
            if (daimyo.isPlaying() && daimyo.getInitiative() > 0) {
                stX = (125 + 179 * (daimyo.getInitiative() - 1)) / (Shogun.size % 2 + 1);
                stY = 645 / (Shogun.size % 2 + 1);
                siX = 149 / (Shogun.size % 2 + 1);
                siY = 100 / (Shogun.size % 2 + 1);
                if (stX + Shogun.shiftX <= e.getX() && e.getX() <= stX + siX + Shogun.shiftX && stY + Shogun.shiftY <= e.getY() && e.getY() <= stY + siY + Shogun.shiftY) {
                    daimyoHiglighted = daimyo;
                    break;
                }
            }
            if (daimyo.isPlaying()) {
                stX = 21 / (Shogun.size % 2 + 1);
                stY = (64 + i * 104) / (Shogun.size % 2 + 1);
                siX = 149 / (Shogun.size % 2 + 1);
                siY = 100 / (Shogun.size % 2 + 1);
                if (stX + Shogun.shiftX <= e.getX() && e.getX() <= stX + siX + Shogun.shiftX && stY + Shogun.shiftY <= e.getY() && e.getY() <= stY + siY + Shogun.shiftY) {
                    daimyoHiglighted = daimyo;
                    break;
                }
                i++;
            }
        }
    }

    public static void mouseDoubleClicked(MouseEvent e) {
        if (phase == 3) {
            int stX, stY, siX, siY;
            stX = 400 / (Shogun.size % 2 + 1);
            stY = 94 / (Shogun.size % 2 + 1);
            siX = 116 / (Shogun.size % 2 + 1);
            siY = 177 / (Shogun.size % 2 + 1);
            if (stX + Shogun.shiftX <= e.getX() && e.getX() <= stX + siX + Shogun.shiftX && stY + Shogun.shiftY <= e.getY() && e.getY() <= stY + siY + Shogun.shiftY) {
                showing.actionTotalRandomise();
            }
            if (daimyoHiglighted != null) {
                if (daimyoHiglighted.getStatus() == 0) {
                    daimyoHiglighted.setReady(!daimyoHiglighted.isReady());
                }
            }
        }
    }

    public static void mouseWheelMoved(MouseWheelEvent e, MyJFrame jFrame) {
        if (Shogun.provinceDrag.getOwner() != null) {
            if (Shogun.actions.get(actionNr) == Shogun.getAction("MoveA")) {
                Shogun.getAction("MoveA").mouseWheelMoved(e);
            } else if (Shogun.actions.get(actionNr) == Shogun.getAction("MoveB")) {
                Shogun.getAction("MoveB").mouseWheelMoved(e);
            } else if (Shogun.actions.get(actionNr) == Shogun.getAction("Recruitment1")) {
                Shogun.getAction("Recruitment1").mouseWheelMoved(e);
            }
        } else {
            if (e.getWheelRotation() > 0 && Shogun.size == 2) {
                Shogun.size = 1;
                Shogun.shiftX += jFrame.getMx();
                Shogun.shiftY += jFrame.getMy();
            } else if (e.getWheelRotation() < 0 && Shogun.size == 1) {
                Shogun.size = 2;
                Shogun.shiftX -= jFrame.getMx();
                Shogun.shiftY -= jFrame.getMy();
            }
        }
    }

    public static void KeyTyped(KeyEvent e, int x, int y) {
        if (e.getKeyChar() == 's' || e.getKeyChar() == 'S') {
            if (Shogun.size == 2) {
                Shogun.size = 1;
                Shogun.shiftX += x;
                Shogun.shiftY += y;
            } else if (Shogun.size == 1) {
                Shogun.size = 2;
                Shogun.shiftX -= x;
                Shogun.shiftY -= y;
            }
        }
        if (e.getKeyChar() == 'r' || e.getKeyChar() == 'R') {
            showResources = !showResources;
        }
        if (e.getKeyChar() == 'a' || e.getKeyChar() == 'A') {
            showArmies = !showArmies;
        }
        if (e.getKeyChar() == 'q' || e.getKeyChar() == 'Q') {
            showing = activeDaimyo;
        }
        if (phase == 3) {
            if (e.getKeyChar() == '1') {
                setDaimyoAction("Castle");
            } else if (e.getKeyChar() == '2') {
                setDaimyoAction("Temple");
            } else if (e.getKeyChar() == '3') {
                setDaimyoAction("Theatre");
            } else if (e.getKeyChar() == '4') {
                setDaimyoAction("Rice");
            } else if (e.getKeyChar() == '5') {
                setDaimyoAction("Gold");
            } else if (e.getKeyChar() == '6') {
                setDaimyoAction("Recruitment5");
            } else if (e.getKeyChar() == '7') {
                setDaimyoAction("Recruitment3");
            } else if (e.getKeyChar() == '8') {
                setDaimyoAction("Recruitment1");
            } else if (e.getKeyChar() == '9') {
                setDaimyoAction("MoveA");
            } else if (e.getKeyChar() == '0') {
                setDaimyoAction("MoveB");
            } else if (e.getKeyChar() == '-') {
                setDaimyoAction("Bid");
            }
        } else if (phase == 6) {
            //System.out.printf("%d: %d, %d\n", (int) e.getKeyChar(), (int) e.getKeyCode(), (int) KeyEvent.VK_UP);
            if (e.getKeyChar() == 'z') {
                if (Shogun.provinceDrag.getOwner() != null) {
                    if (Shogun.actions.get(actionNr) == Shogun.getAction("MoveA")) {
                        Shogun.getAction("MoveA").armyUp();
                    } else if (Shogun.actions.get(actionNr) == Shogun.getAction("MoveB")) {
                        Shogun.getAction("MoveB").armyUp();
                    } else if (Shogun.actions.get(actionNr) == Shogun.getAction("Recruitment1")) {
                        Shogun.getAction("Recruitment1").armyUp();
                    }
                }
            } else if (e.getKeyChar() == 'x') {
                if (Shogun.provinceDrag.getOwner() != null) {
                    if (Shogun.actions.get(actionNr) == Shogun.getAction("MoveA")) {
                        Shogun.getAction("MoveA").armyDown();
                    } else if (Shogun.actions.get(actionNr) == Shogun.getAction("MoveB")) {
                        Shogun.getAction("MoveB").armyDown();
                    } else if (Shogun.actions.get(actionNr) == Shogun.getAction("Recruitment1")) {
                        Shogun.getAction("Recruitment1").armyDown();
                    }
                }
            }
        } else if (phase == 20) {
            if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                endGame = false;
            }
        }
    }

    private static void setDaimyoAction(String name) {
        if (showing.getAction(name) == null) {
            if (provinceHiglighted != null) {
                setDaimyoActionProvince(name);
            } else if (chestHiglighted != null) {
                setDaimyoActionChest(name);
            }
        }
    }

    private static void setDaimyoActionProvince(String name) {
        if (showing.getAction(name) == null) {
            provinceHiglighted.setAction(Shogun.getAction(name));
            provinceHiglighted = null;
        }
    }

    private static void setDaimyoActionChest(String name) {
        if (showing.getAction(name) == null) {
            chestHiglighted.setAction(Shogun.getAction(name));
            chestHiglighted = null;
        }
    }

    public GameStep3Game(String boardType) throws IOException {
        super(new Picture("/img/small/background/step3" + boardType + ".png"), new Picture("/img/large/background/step3" + boardType + ".png"));
        resourcesS = new Picture("/img/small/background/resources" + boardType + ".png");
        resourcesL = new Picture("/img/large/background/resources" + boardType + ".png");
        showResources = false;
        showArmies = true;
        provinceHiglighted = null;
        chestHiglighted = null;
        daimyoHiglighted = null;
        buildingShadeS = new Picture("/img/small/counter/building/building_shade.png");
        buildingShadeL = new Picture("/img/large/counter/building/building_shade.png");
        castleS = new Picture("/img/small/counter/building/castle.png");
        castleL = new Picture("/img/large/counter/building/castle.png");
        templeS = new Picture("/img/small/counter/building/temple.png");
        templeL = new Picture("/img/large/counter/building/temple.png");
        theatreS = new Picture("/img/small/counter/building/theatre.png");
        theatreL = new Picture("/img/large/counter/building/theatre.png");
        revoltS = new Picture("/img/small/counter/building/revolt.png");
        revoltL = new Picture("/img/large/counter/building/revolt.png");
        revoltShadeS = new Picture("/img/small/counter/building/revolt_shade.png");
        revoltShadeL = new Picture("/img/large/counter/building/revolt_shade.png");
        armyS = new ArrayList<>();
        armyL = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            armyS.add(new Picture("/img/small/player/army/army" + Integer.toString(i) + ".png"));
            armyL.add(new Picture("/img/large/player/army/army" + Integer.toString(i) + ".png"));
        }
        armyShadeS = new Picture("/img/small/player/army/army_shade.png");
        armyShadeL = new Picture("/img/large/player/army/army_shade.png");
        pointShadeS = new Picture("/img/small/player/bigcounter/big_shade.png");
        pointShadeL = new Picture("/img/large/player/bigcounter/big_shade.png");
        riceShadeS = new Picture("/img/small/player/smallcounter/small_shade.png");
        riceShadeL = new Picture("/img/large/player/smallcounter/small_shade.png");
        for (Daimyo daimyo : Shogun.daimyos) {
            if (daimyo.isPlaying()) {
                showing = daimyo;
                break;
            }
        }
        activeDaimyo = showing;
        gold1S = new Picture("/img/small/counter/gold/chest1.png");
        gold1L = new Picture("/img/large/counter/gold/chest1.png");
        gold5S = new Picture("/img/small/counter/gold/chest5.png");
        gold5L = new Picture("/img/large/counter/gold/chest5.png");
        katanasS = new Picture("/img/small/katanas.png");
        katanasL = new Picture("/img/large/katanas.png");
        winterRevoltS = new Picture("/img/small/revolt.png");
        winterRevoltL = new Picture("/img/large/revolt.png");
        specialInfoS = new Picture("/img/small/special.png");
        specialInfoL = new Picture("/img/large/special.png");
        daimyos = new ArrayList<>();
        events = new ArrayList<>();
        choosingProvonces = new ArrayList<>();
        step = 0;
    }

    @Override
    public void work(MyJFrame window) {
        Tower.setGameStep(this);
        Tower.setWindow(window);
        prepareBoard(window); //0
        for (int i = 0; i < 2; i++) {
            prepareYear(); //1
            for (int j = 0; j < 3; j++) {
                prepareTurn(); //2
                actionPlanning(window); //3
                prepareEvent(); //4
                playersInitiative(window); //5
                actions(window); //6
                endTurn(); //7
            }
            prepareEvent(); //4
            winterTurn(window); //8
            endTurn(); //7
        }
        endGame(window); // 20
    }

    @Override
    public void paintBackground() {
        super.paintBackground();
        if (phase == 0) {
            super.paintObject(showing.getTrayFrontS(), showing.getTrayFrontL(), 381, 77);
            for (Province startingProvince : showing.getStartingProvinces()) {
                paintArmies(startingProvince.getArmies(), startingProvince.getOwner().getId(), startingProvince.getArmiesX(), startingProvince.getArmiesY());
            }
        } else {
            super.paintObject(showing.getTrayBackS(), showing.getTrayBackL(), 381, 77);
        }
        paintDaimyosHorizontal(125, 645);
        if (provinceHiglighted != null) {
            super.paintObject(provinceHiglighted.getHighlightS(), provinceHiglighted.getHighlightL(), provinceHiglighted.getX(), provinceHiglighted.getY());
            //Podświetlanie sąsiadów
            /*if (phase == 6 && (Shogun.actions.get(actionNr).getName().equals("MoveA") || Shogun.actions.get(actionNr).getName().equals("MoveB"))) {
             Map<String, Province> neighbours = null;
             if (provinceHiglighted.getNeighbour() != null) {
             neighbours = provinceHiglighted.getNeighbour();
             }
             if (neighbours != null) {
             for (String neighbour : neighbours.keySet()) {
             if (neighbours.get(neighbour) != null) {
             if (neighbours.get(neighbour).isInGame()) {
             super.paintObject(neighbours.get(neighbour).getHighlightS(), neighbours.get(neighbour).getHighlightL(), neighbours.get(neighbour).getX(), neighbours.get(neighbour).getY());
             }
             }
             }
             }
             }*/
        }
        for (String region : Shogun.world.keySet()) {
            for (String province : Shogun.world.get(region).getProvinces().keySet()) {
                for (Place place : Shogun.world.get(region).getProvinces().get(province).getBuildings()) {
                    if (place.getType() != Building.EMPTY) {
                        super.paintObjectCentered(buildingShadeS.getImg(), buildingShadeL.getImg(), place.getX(), place.getY());
                    }
                    if (place.getType() == Building.CASTLE) {
                        super.paintObjectCentered(castleS.getImg(), castleL.getImg(), place.getX(), place.getY());
                    }
                    if (place.getType() == Building.TEMPLE) {
                        super.paintObjectCentered(templeS.getImg(), templeL.getImg(), place.getX(), place.getY());
                    }
                    if (place.getType() == Building.THEATRE) {
                        super.paintObjectCentered(theatreS.getImg(), theatreL.getImg(), place.getX(), place.getY());
                    }
                    paintRevolts(Shogun.world.get(region).getProvinces().get(province));
                }
            }
        }
        if (showArmies) {
            for (String region : Shogun.world.keySet()) {
                for (String province : Shogun.world.get(region).getProvinces().keySet()) {
                    paintProvinceArmies(Shogun.world.get(region).getProvinces().get(province));
                }
            }
            paintProvinceArmies(Shogun.provinceDrag);
        }
        if (showResources) {
            super.paintObject(resourcesS.getImg(), resourcesL.getImg(), 0, 0);
        }
        paintArmies(showing.getArmies(), showing.getId(), 310, 345);
        paintGold(showing.getGold(), 350, 140);
        paintSpecials(125, 645);
        paintDaimyosVertical(21, 64);
        paintPoints();
        paintRise();
        paintCards();
    }

    private void paintProvinceArmies(Province province) {
        if (province.getArmies() == 0) {
            return;
        }
        int armynr = 5;
        if (province.getOwner() != null) {
            armynr = province.getOwner().getId();
        }
        paintArmies(province.getArmies(), armynr, province.getArmiesX(), province.getArmiesY());
    }

    private void paintArmies(int n, int armynr, int bx, int by) {
        if (n == 0) {
            return;
        }
        int cube = 1;
        while (cube * cube * cube < n) {
            cube++;
        }
        int paintedArmies = 0, x, y;
        for (int i = 0; i < cube; i++) {
            for (int j = 0; j < cube; j++) {
                for (int k = 0; k < cube; k++) {
                    x = bx + (1 - k + j) * armyL.get(armynr).getImg().getWidth(null) / 2 - cube * armyL.get(armynr).getImg().getWidth(null) / 4;
                    y = by + (1 + k + j) * (armyL.get(armynr).getImg().getHeight(null) - 8) / 2 - cube * (armyL.get(armynr).getImg().getHeight(null) - 8) / 4 - i * armyL.get(armynr).getImg().getHeight(null) / 2;
                    super.paintObjectCentered(armyShadeS.getImg(), armyShadeL.getImg(), x, y);
                    super.paintObjectCentered(armyS.get(armynr).getImg(), armyL.get(armynr).getImg(), x, y);
                    paintedArmies++;
                    if (paintedArmies == n) {
                        return;
                    }
                }
            }
        }
    }

    void paintGold(int n, int bx, int by) {
        int i = 0, x, y;
        while (n > 0) {
            if (i % 5 < 3) {
                x = bx + (i % 5) * 45 - 80;
                y = by + (i / 5) * 34;
            } else {
                x = bx + 22 + ((i + 2) % 5) * 45 - 80;
                y = by + 17 + (i / 5) * 34;
            }
            if (n >= 5) {
                super.paintObjectCentered(gold5S.getImg(), gold5L.getImg(), x, y);
                n -= 5;
            } else {
                super.paintObjectCentered(gold1S.getImg(), gold1L.getImg(), x, y);
                n -= 1;
            }
            i++;
        }
    }

    private void paintRevolts(Province province) {
        int revoltShiftY = (1 - province.getRevolt()) * 2;
        if (province.getRevolt() > 0) {
            super.paintObjectCentered(revoltShadeS.getImg(), revoltShadeL.getImg(), province.getRevoltX(), province.getRevoltY() + revoltShiftY);
            super.paintObjectCentered(revoltS.getImg(), revoltL.getImg(), province.getRevoltX(), province.getRevoltY() + revoltShiftY);
        }
        for (int i = 1; i < province.getRevolt(); i++) {
            super.paintObjectCentered(revoltShadeS.getImg(), revoltShadeL.getImg(), province.getRevoltX() + (2 * (i % 2) - 1) * 4, province.getRevoltY() + revoltShiftY + i * 2);
            super.paintObjectCentered(revoltS.getImg(), revoltL.getImg(), province.getRevoltX() + (2 * (i % 2) - 1) * 4, province.getRevoltY() + revoltShiftY + i * 2);
        }
    }

    private void paintCards() {
        int showCards = 0;
        for (int i = 0; i < Shogun.actions.size(); i++) {
            super.paintObject(super.getCardVerticalShadeS(), super.getCardVerticalShadeL(), 430 + 121 * i - 5, 1358 - 5);
            if (Shogun.actions.get(i).isTurnOn()) {
                super.paintObject(Shogun.actions.get(i).getCardS(), Shogun.actions.get(i).getCardL(), 430 + 121 * i, 1358);
            } else {
                super.paintObject(Action.getCardBackS(), Action.getCardBackL(), 430 + 121 * i, 1358);
            }
            if (actionNr == i) {
                super.paintObject(super.getCardVerticalHighlightS(), super.getCardVerticalHighlightL(), 430 + 121 * i, 1358);
            }
        }
        for (int i = 0; i < events.size(); i++) {
            super.paintObject(super.getCardVerticalShadeS(), super.getCardVerticalShadeL(), 16 + 103 * i - 5 + (4 - events.size()) * 51, 1358 - 5);
            super.paintObject(events.get(i).getCardS(), events.get(i).getCardL(), 16 + 103 * i + (4 - events.size()) * 51, 1358);
        }
        if (phase > 3 && phase < 10) {
            super.paintObject(super.getCardVerticalShadeS(), super.getCardVerticalShadeL(), 172 - 5, 1128 - 5);
            super.paintObject(Shogun.allEvents.get(turnNr).getCardS(), Shogun.allEvents.get(turnNr).getCardL(), 172, 1128);
        }
        if (showing.getSpecial() != null) {
            super.paintObject(super.getCardVerticalShadeS(), super.getCardVerticalShadeL(), 268 - 5, 417 - 5);
            super.paintObject(showing.getSpecial().getCardS(), showing.getSpecial().getCardL(), 268, 417);
        }
        for (String region : Shogun.world.keySet()) {
            for (String province : Shogun.world.get(region).getProvinces().keySet()) {
                if (Shogun.world.get(region).getProvinces().get(province).getOwner() != null) {
                    if (Shogun.world.get(region).getProvinces().get(province).getOwner().equals(showing)) {
                        if (Shogun.world.get(region).getProvinces().get(province).getAction() == null) {
                            super.paintObject(super.getCardVerticalShadeS(), super.getCardVerticalShadeL(), 1101 + 104 * (showCards % 9) - 5, 113 + 153 * (showCards / 9) - 5);
                            if (Shogun.world.get(region).getProvinces().get(province).isTurnOn()) {
                                super.paintObject(Shogun.world.get(region).getProvinces().get(province).getCardS(), Shogun.world.get(region).getProvinces().get(province).getCardL(), 1101 + 104 * (showCards % 9), 113 + 153 * (showCards / 9));
                                if (Shogun.world.get(region).getProvinces().get(province).equals(provinceHiglighted)) {
                                    super.paintObject(super.getCardVerticalHighlightS(), super.getCardVerticalHighlightL(), 1101 + 104 * (showCards % 9), 113 + 153 * (showCards / 9));
                                }
                            } else {
                                super.paintObject(Province.getCardBackS(), Province.getCardBackL(), 1101 + 104 * (showCards % 9), 113 + 153 * (showCards / 9));
                            }
                            showCards++;
                        } else {
                            if (!Shogun.world.get(region).getProvinces().get(province).getAction().getName().equals("Drag")) {
                                super.paintObject(super.getCardVerticalShadeS(), super.getCardVerticalShadeL(), Shogun.world.get(region).getProvinces().get(province).getAction().getX() - 5, Shogun.world.get(region).getProvinces().get(province).getAction().getY() - 5);
                                if (Shogun.world.get(region).getProvinces().get(province).isTurnOn()) {
                                    super.paintObject(Shogun.world.get(region).getProvinces().get(province).getCardS(), Shogun.world.get(region).getProvinces().get(province).getCardL(), Shogun.world.get(region).getProvinces().get(province).getAction().getX(), Shogun.world.get(region).getProvinces().get(province).getAction().getY());
                                    if (Shogun.world.get(region).getProvinces().get(province).equals(provinceHiglighted)) {
                                        super.paintObject(super.getCardVerticalHighlightS(), super.getCardVerticalHighlightL(), Shogun.world.get(region).getProvinces().get(province).getAction().getX(), Shogun.world.get(region).getProvinces().get(province).getAction().getY());
                                    }
                                } else {
                                    super.paintObject(Province.getCardBackS(), Province.getCardBackL(), Shogun.world.get(region).getProvinces().get(province).getAction().getX(), Shogun.world.get(region).getProvinces().get(province).getAction().getY());
                                }
                            }
                        }
                    }
                }
            }
        }
        /*if (showCards % 9 > 4) {
         showCards += 9 - showCards % 9;
         }*/
        for (int i = 0; i < showing.getChestCards().size(); i++) {
            if (showing.getChestCards().get(i).getAction() == null) {
                super.paintObject(super.getCardVerticalShadeS(), super.getCardVerticalShadeL(), 1101 + 104 * (showCards % 9) - 5, 113 + 153 * (showCards / 9) - 5);
                if (showing.getChestCards().get(i).isTurnOn()) {
                    super.paintObject(showing.getChestCards().get(i).getCardS(), showing.getChestCards().get(i).getCardL(), 1101 + 104 * (showCards % 9), 113 + 153 * (showCards / 9));
                    if (showing.getChestCards().get(i).equals(chestHiglighted)) {
                        super.paintObject(super.getCardVerticalHighlightS(), super.getCardVerticalHighlightL(), 1101 + 104 * (showCards % 9), 113 + 153 * (showCards / 9));
                    }
                } else {
                    super.paintObject(Chest.getCardBackS(), Chest.getCardBackL(), 1101 + 104 * (showCards % 9), 113 + 153 * (showCards / 9));
                }
                showCards++;
            } else {
                if (!showing.getChestCards().get(i).getAction().getName().equals("Drag")) {
                    super.paintObject(super.getCardVerticalShadeS(), super.getCardVerticalShadeL(), showing.getChestCards().get(i).getAction().getX() - 5, showing.getChestCards().get(i).getAction().getY() - 5);
                    if (showing.getChestCards().get(i).isTurnOn()) {
                        super.paintObject(showing.getChestCards().get(i).getCardS(), showing.getChestCards().get(i).getCardL(), showing.getChestCards().get(i).getAction().getX(), showing.getChestCards().get(i).getAction().getY());
                        if (showing.getChestCards().get(i).equals(chestHiglighted)) {
                            super.paintObject(super.getCardVerticalHighlightS(), super.getCardVerticalHighlightL(), showing.getChestCards().get(i).getAction().getX(), showing.getChestCards().get(i).getAction().getY());
                        }
                    } else {
                        super.paintObject(Chest.getCardBackS(), Chest.getCardBackL(), showing.getChestCards().get(i).getAction().getX(), showing.getChestCards().get(i).getAction().getY());
                    }
                }
            }
        }
        if (provinceHiglighted != null && provinceHiglighted.getAction() != null && provinceHiglighted.getAction().getName().equals("Drag")) {
            super.paintObject(super.getCardVerticalShadeS(), super.getCardVerticalShadeL(), provinceHiglighted.getAction().getX() - 5, provinceHiglighted.getAction().getY() - 5);
            super.paintObject(provinceHiglighted.getCardS(), provinceHiglighted.getCardL(), provinceHiglighted.getAction().getX(), provinceHiglighted.getAction().getY());
        }
        if (chestHiglighted != null && chestHiglighted.getAction() != null && chestHiglighted.getAction().getName().equals("Drag")) {
            super.paintObject(super.getCardVerticalShadeS(), super.getCardVerticalShadeL(), chestHiglighted.getAction().getX() - 5, chestHiglighted.getAction().getY() - 5);
            super.paintObject(chestHiglighted.getCardS(), chestHiglighted.getCardL(), chestHiglighted.getAction().getX(), chestHiglighted.getAction().getY());
        }
        if (activeDaimyo != null) {
            super.paintObject(super.getCardHorizontalShadeS(), super.getCardHorizontalShadeL(), 1862 - 5, 1380 - 5);
            super.paintObject(activeDaimyo.getEmblemS(), activeDaimyo.getEmblemL(), 1862, 1380);
        }
    }

    private void paintDaimyosHorizontal(int bx, int by) {
        for (Daimyo daimyo : Shogun.daimyos) {
            if (daimyo.isPlaying()) {
                if (daimyo.getInitiative() > 0) {
                    super.paintObject(super.getCardHorizontalShadeS(), super.getCardHorizontalShadeL(), bx + 179 * (daimyo.getInitiative() - 1) - 5, by - 5);
                    super.paintObject(daimyo.getDaimyoS(), daimyo.getDaimyoL(), bx + 179 * (daimyo.getInitiative() - 1), by);
                    if (daimyo.equals(daimyoHiglighted)) {
                        super.paintObject(super.getCardHorizontalHighlightS(), super.getCardHorizontalHighlightL(), bx + 179 * (daimyo.getInitiative() - 1), by);
                    }
                }
            }
        }
    }

    private void paintDaimyosVertical(int bx, int by) {
        int i = 0;
        for (Daimyo daimyo : Shogun.daimyos) {
            if (daimyo.isPlaying()) {
                super.paintObject(super.getCardHorizontalShadeS(), super.getCardHorizontalShadeL(), bx - 5, by + i * 104 - 5);
                super.paintObject(daimyo.getDaimyoS(), daimyo.getDaimyoL(), bx, by + i * 104);
                if (daimyo.equals(daimyoHiglighted)) {
                    super.paintObject(super.getCardHorizontalHighlightS(), super.getCardHorizontalHighlightL(), bx, by + i * 104);
                }
                if (daimyo.isReady()) {
                    super.paintObject(katanasS.getImg(), katanasL.getImg(), bx + 160, by + 15 + i * 104);
                }
                i++;
            }
        }
    }

    private void paintSpecials(int bx, int by) {
        for (int i = 0; i < Shogun.specials.size(); i++) {
            if (Shogun.specials.get(i).isTurnOn()) {
                super.paintObject(super.getCardHorizontalShadeS(), super.getCardHorizontalShadeL(), bx + 179 * i - 5, by - 5);
                super.paintObject(Shogun.specials.get(i).getCardhS(), Shogun.specials.get(i).getCardhL(), bx + 179 * i, by);
                if (Shogun.specials.get(i).equals(specialHiglighted)) {
                    super.paintObject(super.getCardHorizontalHighlightS(), super.getCardHorizontalHighlightL(), bx + 179 * i, by);
                }
            }
        }
    }

    private void paintPoints() {
        Map<Integer, List<Daimyo>> points = new HashMap<>();
        int pts;
        for (Daimyo daimyo : daimyos) {
            pts = min(daimyo.getPoints(), 80);
            if (points.get(pts) == null) {
                points.put(pts, new ArrayList<Daimyo>());
            }
            points.get(pts).add(daimyo);
        }
        int bX, bY;
        for (Integer i : points.keySet()) {
            if (i == 0) {
                bX = 30;
                bY = 1291;
            } else if (i <= 15) {
                bX = 30;
                bY = 1217 - 40 * (i - 1);
            } else if (i <= 64) {
                bX = 75 + 40 * (i - 16) - (i - 16) / 4;
                bY = 613;
            } else if (i <= 79) {
                bX = 2018;
                bY = 657 + 40 * (i - 65);
            } else {
                bX = 2018;
                bY = 1291;
            }
            if (points.get(i).size() == 1) {
                paintObjectCentered(pointShadeS.getImg(), pointShadeL.getImg(), bX, bY);
                paintObjectCentered(points.get(i).get(0).getPointCounterS(), points.get(i).get(0).getPointCounterL(), bX, bY);
            } else if (points.get(i).size() == 2) {
                paintObjectCentered(pointShadeS.getImg(), pointShadeL.getImg(), bX - 13, bY);
                paintObjectCentered(pointShadeS.getImg(), pointShadeL.getImg(), bX + 15, bY);
                paintObjectCentered(points.get(i).get(0).getPointCounterS(), points.get(i).get(0).getPointCounterL(), bX - 13, bY);
                paintObjectCentered(points.get(i).get(1).getPointCounterS(), points.get(i).get(1).getPointCounterL(), bX + 15, bY);
            } else if (points.get(i).size() == 3) {
                paintObjectCentered(pointShadeS.getImg(), pointShadeL.getImg(), bX, bY - 15);
                paintObjectCentered(points.get(i).get(0).getPointCounterS(), points.get(i).get(0).getPointCounterL(), bX, bY - 15);
                paintObjectCentered(pointShadeS.getImg(), pointShadeL.getImg(), bX - 13, bY);
                paintObjectCentered(pointShadeS.getImg(), pointShadeL.getImg(), bX + 15, bY);
                paintObjectCentered(points.get(i).get(1).getPointCounterS(), points.get(i).get(1).getPointCounterL(), bX - 13, bY);
                paintObjectCentered(points.get(i).get(2).getPointCounterS(), points.get(i).get(2).getPointCounterL(), bX + 15, bY);
            } else if (points.get(i).size() == 4) {
                paintObjectCentered(pointShadeS.getImg(), pointShadeL.getImg(), bX, bY - 15);
                paintObjectCentered(points.get(i).get(0).getPointCounterS(), points.get(i).get(0).getPointCounterL(), bX, bY - 15);
                paintObjectCentered(pointShadeS.getImg(), pointShadeL.getImg(), bX - 13, bY);
                paintObjectCentered(pointShadeS.getImg(), pointShadeL.getImg(), bX + 15, bY);
                paintObjectCentered(points.get(i).get(1).getPointCounterS(), points.get(i).get(1).getPointCounterL(), bX - 13, bY);
                paintObjectCentered(points.get(i).get(2).getPointCounterS(), points.get(i).get(2).getPointCounterL(), bX + 15, bY);
                paintObjectCentered(pointShadeS.getImg(), pointShadeL.getImg(), bX, bY + 15);
                paintObjectCentered(points.get(i).get(3).getPointCounterS(), points.get(i).get(3).getPointCounterL(), bX, bY + 15);
            } else if (points.get(i).size() == 5) {
                paintObjectCentered(pointShadeS.getImg(), pointShadeL.getImg(), bX, bY - 15);
                paintObjectCentered(points.get(i).get(0).getPointCounterS(), points.get(i).get(0).getPointCounterL(), bX, bY - 15);
                paintObjectCentered(pointShadeS.getImg(), pointShadeL.getImg(), bX - 13, bY);
                paintObjectCentered(pointShadeS.getImg(), pointShadeL.getImg(), bX + 15, bY);
                paintObjectCentered(points.get(i).get(1).getPointCounterS(), points.get(i).get(1).getPointCounterL(), bX - 13, bY);
                paintObjectCentered(points.get(i).get(2).getPointCounterS(), points.get(i).get(2).getPointCounterL(), bX + 15, bY);
                paintObjectCentered(pointShadeS.getImg(), pointShadeL.getImg(), bX, bY + 15);
                paintObjectCentered(points.get(i).get(3).getPointCounterS(), points.get(i).get(3).getPointCounterL(), bX, bY + 15);
                paintObjectCentered(pointShadeS.getImg(), pointShadeL.getImg(), bX + 5, bY - 10);
                paintObjectCentered(points.get(i).get(4).getPointCounterS(), points.get(i).get(4).getPointCounterL(), bX + 5, bY - 10);
            }
        }
    }

    private void paintRise() {
        Map<Integer, List<Daimyo>> rice = new HashMap<>();
        int riceUnits;
        for (Daimyo daimyo : daimyos) {
            riceUnits = min(daimyo.getRice(), 18);
            if (rice.get(riceUnits) == null) {
                rice.put(riceUnits, new ArrayList<Daimyo>());
            }
            rice.get(riceUnits).add(daimyo);
        }
        int bX, bY;
        for (Integer i : rice.keySet()) {
            bX = 89;
            bY = 1256 - 35 * (i - 1);
            if (rice.get(i).size() == 1) {
                paintObjectCentered(riceShadeS.getImg(), riceShadeL.getImg(), bX, bY);
                paintObjectCentered(rice.get(i).get(0).getRiceCounterS(), rice.get(i).get(0).getRiceCounterL(), bX, bY);
            } else if (rice.get(i).size() == 2) {
                paintObjectCentered(riceShadeS.getImg(), riceShadeL.getImg(), bX - 13, bY);
                paintObjectCentered(riceShadeS.getImg(), riceShadeL.getImg(), bX + 15, bY);
                paintObjectCentered(rice.get(i).get(0).getRiceCounterS(), rice.get(i).get(0).getRiceCounterL(), bX - 13, bY);
                paintObjectCentered(rice.get(i).get(1).getRiceCounterS(), rice.get(i).get(1).getRiceCounterL(), bX + 15, bY);
            } else if (rice.get(i).size() == 3) {
                paintObjectCentered(riceShadeS.getImg(), riceShadeL.getImg(), bX, bY - 12);
                paintObjectCentered(rice.get(i).get(0).getRiceCounterS(), rice.get(i).get(0).getRiceCounterL(), bX, bY - 12);
                paintObjectCentered(riceShadeS.getImg(), riceShadeL.getImg(), bX - 13, bY);
                paintObjectCentered(riceShadeS.getImg(), riceShadeL.getImg(), bX + 15, bY);
                paintObjectCentered(rice.get(i).get(1).getRiceCounterS(), rice.get(i).get(1).getRiceCounterL(), bX - 13, bY);
                paintObjectCentered(rice.get(i).get(2).getRiceCounterS(), rice.get(i).get(2).getRiceCounterL(), bX + 15, bY);
            } else if (rice.get(i).size() == 4) {
                paintObjectCentered(riceShadeS.getImg(), riceShadeL.getImg(), bX, bY - 12);
                paintObjectCentered(rice.get(i).get(0).getRiceCounterS(), rice.get(i).get(0).getRiceCounterL(), bX, bY - 12);
                paintObjectCentered(riceShadeS.getImg(), riceShadeL.getImg(), bX - 13, bY);
                paintObjectCentered(riceShadeS.getImg(), riceShadeL.getImg(), bX + 15, bY);
                paintObjectCentered(rice.get(i).get(1).getRiceCounterS(), rice.get(i).get(1).getRiceCounterL(), bX - 13, bY);
                paintObjectCentered(rice.get(i).get(2).getRiceCounterS(), rice.get(i).get(2).getRiceCounterL(), bX + 15, bY);
                paintObjectCentered(riceShadeS.getImg(), riceShadeL.getImg(), bX - 8, bY - 8);
                paintObjectCentered(rice.get(i).get(3).getRiceCounterS(), rice.get(i).get(3).getRiceCounterL(), bX - 8, bY - 8);
            } else if (rice.get(i).size() == 5) {
                paintObjectCentered(riceShadeS.getImg(), riceShadeL.getImg(), bX, bY - 12);
                paintObjectCentered(rice.get(i).get(0).getRiceCounterS(), rice.get(i).get(0).getRiceCounterL(), bX, bY - 12);
                paintObjectCentered(riceShadeS.getImg(), riceShadeL.getImg(), bX - 13, bY);
                paintObjectCentered(riceShadeS.getImg(), riceShadeL.getImg(), bX + 15, bY);
                paintObjectCentered(rice.get(i).get(1).getRiceCounterS(), rice.get(i).get(1).getRiceCounterL(), bX - 13, bY);
                paintObjectCentered(rice.get(i).get(2).getRiceCounterS(), rice.get(i).get(2).getRiceCounterL(), bX + 15, bY);
                paintObjectCentered(riceShadeS.getImg(), riceShadeL.getImg(), bX - 8, bY - 8);
                paintObjectCentered(rice.get(i).get(3).getRiceCounterS(), rice.get(i).get(3).getRiceCounterL(), bX - 8, bY - 8);
                paintObjectCentered(riceShadeS.getImg(), riceShadeL.getImg(), bX + 22, bY - 8);
                paintObjectCentered(rice.get(i).get(4).getRiceCounterS(), rice.get(i).get(4).getRiceCounterL(), bX + 22, bY - 8);
            }
        }
    }

    private void prepareBoard(MyJFrame window) {
        phase = 0;
        turnNr = 0;
        daimyos = new ArrayList<>();
        events = new ArrayList<>();
        choosingProvonces = new ArrayList<>();
        Collections.shuffle(Shogun.allEvents);
        for (Daimyo daimyo : daimyos) {
            daimyo.setPoints(0);
        }
        int nPlayers = Shogun.nPlayers();
        if (nPlayers == 3) {
            Shogun.world.get("brown").getProvinces().get("Mutsu").setInGame(false);
            Shogun.world.get("brown").getProvinces().get("Echigo").setInGame(false);
            Shogun.world.get("yellow").getProvinces().get("Awa-Boso").setInGame(false);
            Shogun.world.get("yellow").getProvinces().get("Kazusa").setInGame(false);
            Shogun.world.get("purple").getProvinces().get("Sanuki").setInGame(false);
            Shogun.world.get("purple").getProvinces().get("Tosa").setInGame(false);
            Shogun.world.get("green").getProvinces().get("Izumo").setInGame(false);
            Shogun.world.get("green").getProvinces().get("Iwami").setInGame(false);
        }
        if (nPlayers == 3) {
            for (Daimyo daimyo : Shogun.daimyos) {
                if (daimyo.isPlaying()) {
                    daimyos.add(daimyo);
                    daimyo.setGold(18);
                    daimyo.setReady(false);
                    daimyo.initializeStartingProvinces(nPlayers);
                }
            }
        } else if (nPlayers == 4) {
            for (Daimyo daimyo : Shogun.daimyos) {
                if (daimyo.isPlaying()) {
                    daimyos.add(daimyo);
                    daimyo.setGold(15);
                    daimyo.setReady(false);
                    daimyo.initializeStartingProvinces(nPlayers);
                }
            }
        } else if (nPlayers == 5) {
            for (Daimyo daimyo : Shogun.daimyos) {
                if (daimyo.isPlaying()) {
                    daimyos.add(daimyo);
                    daimyo.setGold(12);
                    daimyo.setReady(false);
                    daimyo.initializeStartingProvinces(nPlayers);
                }
            }
        }
        Collections.rotate(daimyos, new Random().nextInt(nPlayers));
        if (Shogun.choosingStartingProvinces) {
            List<Integer> provinceNr = new ArrayList<>();
            int provinceToChoose, daimyoTurn = 0;
            for (int i = 0; i < 45; i++) {
                provinceNr.add(i);
            }
            Collections.shuffle(provinceNr);
            if (nPlayers == 3) {
                provinceToChoose = 9 * 3;
            } else if (nPlayers == 4) {
                provinceToChoose = 8 * 4;
            } else {
                provinceToChoose = 7 * 5;
            }
            for (int i = 0; i < provinceNr.size(); i++) {
                if (provinceFromNr(provinceNr.get(i)) != null) {
                    if (provinceFromNr(provinceNr.get(i)).isInGame()) {
                        System.out.println(provinceFromNr(provinceNr.get(i)).getName());
                    } else {
                        provinceNr.remove(i);
                        --i;
                    }
                }
            }
            for (int i = 0; i < provinceNr.size(); i++) {
                choosingProvonces.add(provinceFromNr(provinceNr.get(i)));
            }
            Map<Daimyo, Province> lastProvince0 = new HashMap<>();
            Map<Daimyo, Province> lastProvince1 = new HashMap<>();
            for (int i = 0; i < provinceToChoose; i++) {
                activeDaimyo = daimyos.get(daimyoTurn);
                showing = activeDaimyo;
                step = 0;
                if (lastProvince0.containsKey(activeDaimyo) && lastProvince0.get(activeDaimyo).equals(choosingProvonces.get(0)) && lastProvince1.containsKey(activeDaimyo) && lastProvince1.get(activeDaimyo).equals(choosingProvonces.get(1))) {
                    choosingProvonces.add(choosingProvonces.get(0));
                    choosingProvonces.add(choosingProvonces.get(1));
                    choosingProvonces.remove(1);
                    choosingProvonces.remove(0);
                }
                lastProvince0.put(activeDaimyo, choosingProvonces.get(0));
                lastProvince1.put(activeDaimyo, choosingProvonces.get(1));
                while (!daimyos.get(daimyoTurn).isReady()) {
                    if (activeDaimyo.getStatus() > 0) {
                        activeDaimyo.getAI().chooseProvince(activeDaimyo);
                    }
                    this.paintBackground();
                    if (step == 0) {
                        for (int j = 0; j < 2; j++) {
                            super.getGraphics().drawImage(super.getCardVerticalShadeL(), Shogun.resolutionX - 440 + j * 120 - 5, 100 - 5, null);
                            super.getGraphics().drawImage(choosingProvonces.get(j).getCardL(), Shogun.resolutionX - 440 + j * 120, 100, null);
                            if (provinceHiglighted != null && provinceHiglighted.equals(choosingProvonces.get(j))) {
                                super.getGraphics().drawImage(super.getCardVerticalHighlightL(), Shogun.resolutionX - 440 + j * 120, 100, null);
                            }
                        }
                        super.getGraphics().drawImage(super.getCardVerticalShadeL(), Shogun.resolutionX - 440 + 240 - 5, 100 - 5, null);
                        super.getGraphics().drawImage(Province.getCardBackL(), Shogun.resolutionX - 440 + 240, 100, null);
                    }
                    super.showOnScreen(window);
                    Shogun.sem.release();
                    try {
                        Thread.sleep(Shogun.refreshPeriod);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Shogun.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Shogun.sem.acquireUninterruptibly();
                }
                activeDaimyo.setReady(false);
                daimyoTurn = (daimyoTurn + 1) % nPlayers;
            }
        } else {
            if (nPlayers == 3) {
                nPlayers = 0;
                for (Daimyo daimyo : daimyos) {
                    daimyo.zeroStartingProvinces();
                    ++nPlayers;
                    if (nPlayers == 1) {
                        giveProvince("Suruga", daimyo, 5);

                        giveProvince("Mino", daimyo, 4);
                        giveProvince("Tamba", daimyo, 4);

                        giveProvince("Musashi", daimyo, 3);
                        giveProvince("Harima", daimyo, 3);

                        giveProvince("Izu", daimyo, 2);
                        giveProvince("Owari", daimyo, 2);
                        giveProvince("Sagami", daimyo, 2);
                        giveProvince("Tajima", daimyo, 2);
                    } else if (nPlayers == 2) {
                        giveProvince("Yamato", daimyo, 5);

                        giveProvince("Echizen", daimyo, 4);
                        giveProvince("Shimotsuke", daimyo, 4);

                        giveProvince("Shimosa", daimyo, 3);
                        giveProvince("Ise", daimyo, 3);

                        giveProvince("Hitachi", daimyo, 2);
                        giveProvince("Awa-Shikoku", daimyo, 2);
                        giveProvince("Kaga", daimyo, 2);
                        giveProvince("Kii", daimyo, 2);
                    } else if (nPlayers == 3) {
                        giveProvince("Bizen", daimyo, 5);

                        giveProvince("Omi", daimyo, 4);
                        giveProvince("Hida", daimyo, 4);

                        giveProvince("Etchu", daimyo, 3);
                        giveProvince("Hoki", daimyo, 3);

                        giveProvince("Bitchu", daimyo, 2);
                        giveProvince("Bingo", daimyo, 2);
                        giveProvince("Settsu", daimyo, 2);
                        giveProvince("Shinano", daimyo, 2);
                    }
                }
            } else if (nPlayers == 4) {
                nPlayers = 0;
                for (Daimyo daimyo : daimyos) {
                    daimyo.zeroStartingProvinces();
                    ++nPlayers;
                    if (nPlayers == 1) {
                        giveProvince("Yamato", daimyo, 5);

                        giveProvince("Awa-Shikoku", daimyo, 4);
                        giveProvince("Kaga", daimyo, 4);

                        giveProvince("Omi", daimyo, 3);
                        giveProvince("Tamba", daimyo, 3);

                        giveProvince("Kii", daimyo, 2);
                        giveProvince("Settsu", daimyo, 2);
                        giveProvince("Noto", daimyo, 2);
                    } else if (nPlayers == 2) {
                        giveProvince("Kozuke", daimyo, 5);

                        giveProvince("Hida", daimyo, 4);
                        giveProvince("Ise", daimyo, 4);

                        giveProvince("Echizen", daimyo, 3);
                        giveProvince("Shinano", daimyo, 3);

                        giveProvince("Etchu", daimyo, 2);
                        giveProvince("Shimotsuke", daimyo, 2);
                        giveProvince("Shima", daimyo, 2);
                    } else if (nPlayers == 3) {
                        giveProvince("Mimasaka", daimyo, 5);

                        giveProvince("Wakasa", daimyo, 4);
                        giveProvince("Awa-Boso", daimyo, 4);

                        giveProvince("Harima", daimyo, 3);
                        giveProvince("Bitchu", daimyo, 3);

                        giveProvince("Hoki", daimyo, 2);
                        giveProvince("Tajima", daimyo, 2);
                        giveProvince("Kazusa", daimyo, 2);
                    } else if (nPlayers == 4) {
                        giveProvince("Kai", daimyo, 5);

                        giveProvince("Musashi", daimyo, 4);
                        giveProvince("Mino", daimyo, 4);

                        giveProvince("Mikawa", daimyo, 3);
                        giveProvince("Bingo", daimyo, 3);

                        giveProvince("Aki", daimyo, 2);
                        giveProvince("Totomi", daimyo, 2);
                        giveProvince("Sagami", daimyo, 2);
                    }
                }
            } else if (nPlayers == 5) {
                nPlayers = 0;
                for (Daimyo daimyo : Shogun.daimyos) {
                    daimyo.zeroStartingProvinces();
                    ++nPlayers;
                    if (nPlayers == 1) {
                        giveProvince("Sagami", daimyo, 5);

                        giveProvince("Mimasaka", daimyo, 4);
                        giveProvince("Harima", daimyo, 4);

                        giveProvince("Kazusa", daimyo, 3);
                        giveProvince("Izu", daimyo, 3);

                        giveProvince("Awa-Boso", daimyo, 2);
                        giveProvince("Bizen", daimyo, 2);
                    } else if (nPlayers == 2) {
                        giveProvince("Shimotsuke", daimyo, 5);

                        giveProvince("Echizen", daimyo, 4);
                        giveProvince("Tamba", daimyo, 4);

                        giveProvince("Shimosa", daimyo, 3);
                        giveProvince("Kozuke", daimyo, 3);

                        giveProvince("Hitachi", daimyo, 2);
                        giveProvince("Wakasa", daimyo, 2);
                    } else if (nPlayers == 3) {
                        giveProvince("Mino", daimyo, 5);

                        giveProvince("Hida", daimyo, 4);
                        giveProvince("Iyo", daimyo, 4);

                        giveProvince("Owari", daimyo, 3);
                        giveProvince("Totomi", daimyo, 3);

                        giveProvince("Mikawa", daimyo, 2);
                        giveProvince("Tosa", daimyo, 2);
                    } else if (nPlayers == 4) {
                        giveProvince("Hoki", daimyo, 5);

                        giveProvince("Shinano", daimyo, 4);
                        giveProvince("Bingo", daimyo, 4);

                        giveProvince("Echigo", daimyo, 3);
                        giveProvince("Aki", daimyo, 3);

                        giveProvince("Izumo", daimyo, 2);
                        giveProvince("Etchu", daimyo, 2);
                    } else if (nPlayers == 5) {
                        giveProvince("Yamato", daimyo, 5);

                        giveProvince("Kaga", daimyo, 4);
                        giveProvince("Kii", daimyo, 4);

                        giveProvince("Shima", daimyo, 3);
                        giveProvince("Omi", daimyo, 3);

                        giveProvince("Ise", daimyo, 2);
                        giveProvince("Noto", daimyo, 2);
                    }
                }
            }
        }
        Shogun.tower.battle(null, 0, null);
        ++phase;
    }

    private Province provinceFromNr(int n) {
        int i = 0;
        for (String region : Shogun.world.keySet()) {
            for (String province : Shogun.world.get(region).getProvinces().keySet()) {
                if (i == n) {
                    return Shogun.world.get(region).getProvinces().get(province);
                }
                ++i;
            }
        }
        return null;
    }

    public void giveProvince(String name, Daimyo daimyo, int nArmies) {
        Shogun.getProvince(name).setOwner(daimyo);
        Shogun.getProvince(name).setArmies(nArmies);
    }

    private void prepareYear() {
        phase = 1;
        events = new ArrayList<>();
        for (Daimyo daimyo : daimyos) {
            daimyo.setRice(0);
        }
        for (String region : Shogun.world.keySet()) {
            for (String province : Shogun.world.get(region).getProvinces().keySet()) {
                Shogun.world.get(region).getProvinces().get(province).setRevolt(0);
            }
        }
        for (int i = 0; i < 4; i++) {
            events.add(Shogun.allEvents.get(turnNr + i));
        }
        Collections.shuffle(events);
        ++phase;
    }

    private void prepareTurn() {
        phase = 2;
        actionNr = 0;
        showing = daimyos.get(0);
        activeDaimyo = daimyos.get(0);
        for (Daimyo daimyo : daimyos) {
            daimyo.setInitiative(0);
            daimyo.setReady(false);
            daimyo.setSpecial(null);
            Collections.sort(daimyo.getChestCards());
            for (Chest chest : daimyo.getChestCards()) {
                if (chest.getAction() != null) {
                    chest.setAction(null);
                }
                if (!chest.isTurnOn()) {
                    chest.turn();
                }
            }
        }
        Collections.shuffle(Shogun.actions);
        for (Action action : Shogun.actions) {
            if (action.isTurnOn()) {
                action.turn();
            }
        }
        for (Action action : Shogun.actions) {
            if (!action.isTurnOn()) {
                action.turn();
            }
        }
        for (int i = 0; i < Shogun.actions.size(); i++) {
            if (i > 4) {
                Shogun.actions.get(i).turn();
            }
        }
        Collections.shuffle(Shogun.specials);
        for (int i = 0; i < 5; i++) {
            if (!Shogun.specials.get(i).isTurnOn()) {
                Shogun.specials.get(i).turn();
            }
            Shogun.specials.get(i).setInitiative(i + 1);
        }
        for (String region : Shogun.world.keySet()) {
            for (String province : Shogun.world.get(region).getProvinces().keySet()) {
                if (Shogun.world.get(region).getProvinces().get(province).getAction() != null) {
                    Shogun.world.get(region).getProvinces().get(province).setAction(null);
                }
                if (!Shogun.world.get(region).getProvinces().get(province).isTurnOn()) {
                    Shogun.world.get(region).getProvinces().get(province).turn();
                }
            }
        }

        ++phase;
    }

    private void actionPlanning(MyJFrame window) {
        phase = 3;
        for (Daimyo daimyo : daimyos) {
            if (daimyo.getStatus() > 0) {
                daimyo.getAI().planActions(daimyo);
            }
        }
        while (phase == 3) {
            this.paintBackground();
            super.showOnScreen(window);
            Shogun.sem.release();
            try {
                Thread.sleep(Shogun.refreshPeriod);
            } catch (InterruptedException ex) {
                Logger.getLogger(Shogun.class.getName()).log(Level.SEVERE, null, ex);
            }
            Shogun.sem.acquireUninterruptibly();
            boolean end = true;
            for (Daimyo daimyo : daimyos) {
                if (!daimyo.isReady()) {
                    end = false;
                    break;
                }
            }
            if (end) {
                ++phase;
            }
        }
        for (Daimyo daimyo : daimyos) {
            daimyo.setReady(false);
        }
    }

    private void prepareEvent() {
        phase = 4;
        events.remove(Shogun.allEvents.get(turnNr));
        ++phase;
    }

    private void playersInitiative(MyJFrame window) {
        phase = 5;
        int daimyoTurn = 0;
        Collections.sort(daimyos);
        Collections.reverse(daimyos);
        for (Daimyo daimyo : daimyos) {
            if (daimyo.getAction(Shogun.actionBid.getName()) != null) {
                for (Chest chest : daimyo.getChestCards()) {
                    if (chest.getAction() != null && chest.getAction().getName().equals(Shogun.actionBid.getName())) {
                        daimyo.setGold(max(daimyo.getGold() - chest.getGold(), 0));
                    }
                }
                if (!daimyo.getAction(Shogun.actionBid.getName()).isTurnOn()) {
                    daimyo.getAction(Shogun.actionBid.getName()).turn();
                }
            }
        }
        showing = daimyos.get(daimyoTurn);
        activeDaimyo = daimyos.get(daimyoTurn);
        while (phase == 5) {
            if (activeDaimyo.getStatus() > 0) {
                activeDaimyo.getAI().chooseSpecialCard(activeDaimyo);
            }
            this.paintBackground();
            super.paintObject(specialInfoS.getImg(), specialInfoL.getImg(), 628, 109);
            super.showOnScreen(window);
            Shogun.sem.release();
            try {
                Thread.sleep(Shogun.refreshPeriod);
            } catch (InterruptedException ex) {
                Logger.getLogger(Shogun.class.getName()).log(Level.SEVERE, null, ex);
            }
            Shogun.sem.acquireUninterruptibly();
            if (activeDaimyo.isReady()) {
                daimyoTurn++;
                if (daimyoTurn == daimyos.size()) {
                    ++phase;
                } else {
                    showing = daimyos.get(daimyoTurn);
                    activeDaimyo = daimyos.get(daimyoTurn);
                }
            }
        }
        for (Daimyo daimyo : daimyos) {
            daimyo.setReady(false);
        }
        Collections.sort(daimyos);
    }

    private void actions(MyJFrame window) {
        phase = 6;
        for (actionNr = 0; actionNr < Shogun.actions.size(); actionNr++) {
            Shogun.actions.get(actionNr).doAction(this, window, daimyos);
            if (actionNr < Shogun.actions.size() - 5) {
                Shogun.actions.get(actionNr + 5).turn();
            }
        }
        for (Daimyo daimyo : daimyos) {
            for (Province province : daimyo.myProvinces()) {
                province.setAction(null);
                if (province.isTurnOn()) {
                    province.turn();
                }
            }
            for (Chest chest : daimyo.getChestCards()) {
                chest.setAction(null);
                if (chest.isTurnOn()) {
                    chest.turn();
                }
            }
        }
        ++phase;
    }

    private void endTurn() {
        phase = 7;
        turnNr++;
        phase = 0;
    }

    private void winterTurn(MyJFrame window) {
        phase = 8;
        for (Daimyo daimyo : daimyos) {
            daimyo.setSpecial(null);
        }
        checkRice(window);
        addPoints();
        phase = 0;
    }

    private void checkRice(MyJFrame window) {
        for (String region : Shogun.world.keySet()) {
            for (String province : Shogun.world.get(region).getProvinces().keySet()) {
                if (Shogun.world.get(region).getProvinces().get(province).isInGame() && Shogun.world.get(region).getProvinces().get(province).isTurnOn()) {
                    Shogun.world.get(region).getProvinces().get(province).turn();
                    Shogun.world.get(region).getProvinces().get(province).setAction(null);
                }
            }
        }
        int riceShortage;
        List<Province> provinces;
        for (Daimyo daimyo : daimyos) {
            daimyo.setRice(max(daimyo.getRice() - Shogun.allEvents.get(turnNr).getRiceRequest(), 0));
        }
        for (Daimyo daimyo : daimyos) {
            showing = daimyo;
            activeDaimyo = daimyo;
            provinces = daimyo.myProvinces();
            riceShortage = max(0, provinces.size() - daimyo.getRice());
            System.out.printf("Daimyo %d brakuje %d ryżu!\n", daimyo.getId(), riceShortage);
            if (riceShortage == 0) {
                nRevolts = 0;
                rebeliantsBonus = 0;
                daimyo.setReady(true);
            } else if (riceShortage == 1) {
                nRevolts = 1;
                rebeliantsBonus = 1;
            } else if (riceShortage == 2) {
                nRevolts = 1;
                rebeliantsBonus = 2;
            } else if (riceShortage <= 4) {
                nRevolts = 2;
                rebeliantsBonus = 2;
            } else if (riceShortage <= 6) {
                nRevolts = 2;
                rebeliantsBonus = 3;
            } else {
                nRevolts = 3;
                rebeliantsBonus = 3;
            }
            Collections.shuffle(provinces);
            for (int i = 0; i < nRevolts && i < provinces.size(); i++) {
                System.out.printf("  Odwróciłem prowincję %s.\n", provinces.get(i).getName());
                provinces.get(i).turn();
            }
            readyToBattle = false;
            while (!activeDaimyo.isReady()) {
                this.paintBackground();
                super.paintObject(winterRevoltS.getImg(), winterRevoltL.getImg(), 628, 109);
                super.showOnScreen(window);
                Shogun.sem.release();
                try {
                    Thread.sleep(Shogun.refreshPeriod);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Shogun.class.getName()).log(Level.SEVERE, null, ex);
                }
                Shogun.sem.acquireUninterruptibly();
                if (activeDaimyo.getStatus() > 0) {
                    activeDaimyo.getAI().chooseRebelion(activeDaimyo);
                    readyToBattle = true;
                }
                if (readyToBattle) {
                    provinceHiglighted.turn();
                    Shogun.tower.battle(null, provinceHiglighted.getRevolt() + rebeliantsBonus, provinceHiglighted);
                    nRevolts--;
                    if (nRevolts == 0) {
                        activeDaimyo.setReady(true);
                    }
                    readyToBattle = false;
                    provinceHiglighted = null;
                }
            }
        }
        for (Daimyo daimyo : daimyos) {
            daimyo.setReady(false);
        }
        for (String region : Shogun.world.keySet()) {
            for (String province : Shogun.world.get(region).getProvinces().keySet()) {
                if (Shogun.world.get(region).getProvinces().get(province).isInGame() && !Shogun.world.get(region).getProvinces().get(province).isTurnOn()) {
                    Shogun.world.get(region).getProvinces().get(province).turn();
                }
            }
        }
    }

    private void addPoints() {
        Map<Daimyo, Integer> castles = new HashMap<>();
        Map<Daimyo, Integer> temples = new HashMap<>();
        Map<Daimyo, Integer> theatres = new HashMap<>();
        int bestCastles, bestTemples, bestTheatres;
        for (String region : Shogun.world.keySet()) {
            for (Daimyo daimyo : daimyos) {
                castles.put(daimyo, 0);
                temples.put(daimyo, 0);
                theatres.put(daimyo, 0);
            }
            for (String province : Shogun.world.get(region).getProvinces().keySet()) {
                if (Shogun.world.get(region).getProvinces().get(province).isInGame() && Shogun.world.get(region).getProvinces().get(province).getOwner() != null) {
                    Shogun.world.get(region).getProvinces().get(province).getOwner().setPoints(Shogun.world.get(region).getProvinces().get(province).getOwner().getPoints() + 1);
                    System.out.printf("Daimyo %d otrzymał 1 punkt za prowincję %s. Ma teraz: %d punktów.\n", Shogun.world.get(region).getProvinces().get(province).getOwner().getId(), Shogun.world.get(region).getProvinces().get(province).getName(), Shogun.world.get(region).getProvinces().get(province).getOwner().getPoints());
                    for (Place place : Shogun.world.get(region).getProvinces().get(province).getBuildings()) {
                        if (place.getType().equals(Building.CASTLE)) {
                            Shogun.world.get(region).getProvinces().get(province).getOwner().setPoints(Shogun.world.get(region).getProvinces().get(province).getOwner().getPoints() + 1);
                            System.out.printf("  Daimyo %d otrzymał 1 punkt za zamek. Ma teraz: %d punktów.\n", Shogun.world.get(region).getProvinces().get(province).getOwner().getId(), Shogun.world.get(region).getProvinces().get(province).getOwner().getPoints());
                            castles.put(Shogun.world.get(region).getProvinces().get(province).getOwner(), castles.get(Shogun.world.get(region).getProvinces().get(province).getOwner()) + 1);
                        } else if (place.getType().equals(Building.TEMPLE)) {
                            Shogun.world.get(region).getProvinces().get(province).getOwner().setPoints(Shogun.world.get(region).getProvinces().get(province).getOwner().getPoints() + 1);
                            System.out.printf("  Daimyo %d otrzymał 1 punkt za świątynię. Ma teraz: %d punktów.\n", Shogun.world.get(region).getProvinces().get(province).getOwner().getId(), Shogun.world.get(region).getProvinces().get(province).getOwner().getPoints());
                            temples.put(Shogun.world.get(region).getProvinces().get(province).getOwner(), temples.get(Shogun.world.get(region).getProvinces().get(province).getOwner()) + 1);
                        } else if (place.getType().equals(Building.THEATRE)) {
                            Shogun.world.get(region).getProvinces().get(province).getOwner().setPoints(Shogun.world.get(region).getProvinces().get(province).getOwner().getPoints() + 1);
                            System.out.printf("  Daimyo %d otrzymał 1 punkt za teatr. Ma teraz: %d punktów.\n", Shogun.world.get(region).getProvinces().get(province).getOwner().getId(), Shogun.world.get(region).getProvinces().get(province).getOwner().getPoints());
                            theatres.put(Shogun.world.get(region).getProvinces().get(province).getOwner(), theatres.get(Shogun.world.get(region).getProvinces().get(province).getOwner()) + 1);
                        }
                    }
                }
            }
            for (Daimyo daimyo : daimyos) {
                bestCastles = 0;
                bestTemples = 0;
                bestTheatres = 0;
                for (Daimyo daimyoCmp : daimyos) {
                    if (!daimyoCmp.equals(daimyo)) {
                        bestCastles = max(bestCastles, castles.get(daimyoCmp));
                        bestTemples = max(bestTemples, temples.get(daimyoCmp));
                        bestTheatres = max(bestTheatres, theatres.get(daimyoCmp));
                    }
                }
                if (castles.get(daimyo) > bestCastles && castles.get(daimyo) > 0) {
                    daimyo.setPoints(daimyo.getPoints() + 3);
                    System.out.printf("    Daimyo %d otrzymał 3 punkty za zamki w regionie %s. Ma teraz: %d punktów.\n", daimyo.getId(), region, daimyo.getPoints());
                } else if (castles.get(daimyo) == bestCastles && castles.get(daimyo) > 0) {
                    daimyo.setPoints(daimyo.getPoints() + 2);
                    System.out.printf("    Daimyo %d otrzymał 2 punkty za zamki w regionie %s. Ma teraz: %d punktów.\n", daimyo.getId(), region, daimyo.getPoints());
                }
                if (temples.get(daimyo) > bestTemples && temples.get(daimyo) > 0) {
                    daimyo.setPoints(daimyo.getPoints() + 2);
                    System.out.printf("    Daimyo %d otrzymał 2 punkty za świątynie w regionie %s. Ma teraz: %d punktów.\n", daimyo.getId(), region, daimyo.getPoints());
                } else if (temples.get(daimyo) == bestTemples && temples.get(daimyo) > 0) {
                    daimyo.setPoints(daimyo.getPoints() + 1);
                    System.out.printf("    Daimyo %d otrzymał 1 punkt za świątynie w regionie %s. Ma teraz: %d punktów.\n", daimyo.getId(), region, daimyo.getPoints());
                }
                if (theatres.get(daimyo) > bestTheatres && theatres.get(daimyo) > 0) {
                    daimyo.setPoints(daimyo.getPoints() + 1);
                    System.out.printf("    Daimyo %d otrzymał 1 punkt za teatry w regionie %s. Ma teraz: %d punktów.\n", daimyo.getId(), region, daimyo.getPoints());
                }
            }
        }
    }

    private void endGame(MyJFrame window) {
        phase = 20;
        endGame = true;
        while (endGame) {
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
}
