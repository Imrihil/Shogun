/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shogun;

import java.awt.Image;
import java.io.IOException;
import static java.lang.Math.max;
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
public class Daimyo implements Comparable<Daimyo> {

    private boolean playing;

    private boolean ready;

    private final int id;

    private int initiative;

    private int rice;

    private int gold;

    private int points;

    private Special special;

    private final Picture daimyoS;

    private final Picture daimyoL;

    private final Picture emblemS;

    private final Picture emblemL;

    private final Picture trayFrontS;

    private final Picture trayFrontL;

    private final Picture trayBackS;

    private final Picture trayBackL;

    private final Picture pointCounterS;

    private final Picture pointCounterL;

    private final Picture riceCounterS;

    private final Picture riceCounterL;

    private final List<Chest> chestCards;

    private final List<Province> startingProvinces;

    /**
     * 0 - gracz, 1 - losowy, 2 - budowniczy, 3 - zrównoważony, 4 - ofensywny;
     */
    private int status;

    private ArtificialIntelligence AI;

    public Daimyo(int id, Picture dS, Picture dL, Picture eS, Picture eL, Picture trayFrontS, Picture trayFrontL, Picture trayBackS, Picture trayBackL, Picture pointCounterS, Picture pointCounterL, Picture riceCounterS, Picture riceCounterL) throws IOException {
        this.id = id;
        this.playing = true;
        this.ready = false;
        this.daimyoS = dS;
        this.daimyoL = dL;
        this.emblemS = eS;
        this.emblemL = eL;
        this.trayFrontS = trayFrontS;
        this.trayFrontL = trayFrontL;
        this.trayBackS = trayBackS;
        this.trayBackL = trayBackL;
        this.initiative = 0;
        this.rice = 0;
        this.points = 0;
        this.pointCounterS = pointCounterS;
        this.pointCounterL = pointCounterL;
        this.riceCounterS = riceCounterS;
        this.riceCounterL = riceCounterL;
        this.chestCards = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            chestCards.add(new Chest(Integer.toString(i), new Picture("/img/small/card/chest/chest" + Integer.toString(i) + ".png"), new Picture("/img/large/card/chest/chest" + Integer.toString(i) + ".png"), i));
        }
        this.status = 0;
        this.AI = null;
        this.startingProvinces = new ArrayList<>();
    }

    /**
     * @return the playing
     */
    public boolean isPlaying() {
        return playing;
    }

    /**
     * @param playing the playing to set
     */
    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    /**
     * @return the daimyoS
     */
    public Image getDaimyoS() {
        return daimyoS.getImg();
    }

    /**
     * @return the daimyoL
     */
    public Image getDaimyoL() {
        return daimyoL.getImg();
    }

    /**
     * @return the emblemS
     */
    public Image getEmblemS() {
        return emblemS.getImg();
    }

    /**
     * @return the emblemL
     */
    public Image getEmblemL() {
        return emblemL.getImg();
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the initiative
     */
    public int getInitiative() {
        return initiative;
    }

    /**
     * @param initiative the initiative to set
     */
    public void setInitiative(int initiative) {
        this.initiative = initiative;
    }

    /**
     * @return the rice
     */
    public int getRice() {
        return rice;
    }

    /**
     * @param rice the rice to set
     */
    public void setRice(int rice) {
        this.rice = rice;
    }

    /**
     * @return the gold
     */
    public int getGold() {
        return gold;
    }

    /**
     * @param gold the gold to set
     */
    public void setGold(int gold) {
        this.gold = gold;
    }

    /**
     * @return the points
     */
    public int getPoints() {
        return points;
    }

    /**
     * @return the special
     */
    public Special getSpecial() {
        return special;
    }

    /**
     * @param special the special to set
     */
    public void setSpecial(Special special) {
        this.special = special;
    }

    /**
     * @param points the points to set
     */
    public void setPoints(int points) {
        this.points = points;
    }

    public int getArmies() {
        int result = 62;
        for (String region : Shogun.world.keySet()) {
            for (String province : Shogun.world.get(region).getProvinces().keySet()) {
                if (Shogun.world.get(region).getProvinces().get(province).getOwner() != null && Shogun.world.get(region).getProvinces().get(province).getOwner().equals(this)) {
                    result -= Shogun.world.get(region).getProvinces().get(province).getArmies();
                }
            }
        }
        for (Province province : startingProvinces) {
            result -= province.getArmies();
        }
        if (Shogun.provinceDrag.getOwner() != null && Shogun.provinceDrag.getOwner().equals(this)) {
            result -= Shogun.provinceDrag.getArmies();
        }
        result -= Shogun.tower.inTower(this);
        return result;
    }

    /**
     * @return the trayFrontS
     */
    public Image getTrayFrontS() {
        return trayFrontS.getImg();
    }

    /**
     * @return the trayFrontL
     */
    public Image getTrayFrontL() {
        return trayFrontL.getImg();
    }

    /**
     * @return the trayBackS
     */
    public Image getTrayBackS() {
        return trayBackS.getImg();
    }

    /**
     * @return the trayBackL
     */
    public Image getTrayBackL() {
        return trayBackL.getImg();
    }

    /**
     * @return the chestCards
     */
    public List<Chest> getChestCards() {
        return chestCards;
    }

    public Card getAction(String name) {
        if (getActionProvince(name) != null) {
            return getActionProvince(name);
        }
        if (getActionChest(name) != null) {
            return getActionChest(name);
        }
        return null;
    }

    public Province getActionProvince(String name) {
        for (String region : Shogun.world.keySet()) {
            for (String province : Shogun.world.get(region).getProvinces().keySet()) {
                if (Shogun.world.get(region).getProvinces().get(province).getOwner() != null && Shogun.world.get(region).getProvinces().get(province).getOwner().equals(this)) {
                    if (Shogun.world.get(region).getProvinces().get(province).getAction() != null && Shogun.world.get(region).getProvinces().get(province).getAction().getName().equals(name)) {
                        return Shogun.world.get(region).getProvinces().get(province);
                    }
                }
            }
        }
        return null;
    }

    public Chest getActionChest(String name) {
        for (Chest chest : chestCards) {
            if (chest.getAction() != null && chest.getAction().getName().equals(name)) {
                return chest;
            }
        }
        return null;
    }

    public void actionRandomise() {
        List<Action> actions = new ArrayList<>();
        for (Action action : Shogun.actions) {
            if (this.getAction(action.getName()) == null) {
                actions.add(action);
            }
        }
        List<Province> provinces = new ArrayList<>();
        for (Province province : myProvinces()) {
            if (province.getAction() == null) {
                provinces.add(province);
            }
        }
        List<Chest> chests = new ArrayList<>();
        for (Chest chest : chestCards) {
            if (chest.getAction() == null) {
                chests.add(chest);
            }
        }
        Collections.shuffle(provinces);
        Collections.shuffle(actions);
        Collections.shuffle(chests);
        int j = 0;
        for (int i = 0; i < actions.size(); i++) {
            if (i < provinces.size()) {
                provinces.get(i).setAction(actions.get(i));
            } else if (j < chests.size() - 1) {
                chests.get(j).setAction(actions.get(i));
                ++j;
            } else {
                break;
            }
        }
        if (this.getAction(Shogun.actionBid.getName()) == null) {
            chests.get(j).setAction(Shogun.actionBid);
        } else {
            for (Action action : actions) {
                if (this.getAction(action.getName()) == null) {
                    if (j < chests.size()) {
                        chests.get(j).setAction(action);
                    }
                    break;
                }
            }
        }
    }

    public void actionTotalRandomise() {
        List<Action> actions = new ArrayList<>();
        for (Action action : Shogun.actions) {
            actions.add(action);
        }
        List<Province> provinces = myProvinces();
        for (Province province : provinces) {
            province.setAction(null);
        }
        List<Chest> chests = chestCards;
        for (Chest chest : chests) {
            chest.setAction(null);
        }
        Collections.shuffle(provinces);
        Collections.shuffle(actions);
        Collections.shuffle(chests);
        int j = 0;
        for (int i = 0; i < actions.size(); i++) {
            if (i < provinces.size()) {
                provinces.get(i).setAction(actions.get(i));
            } else if (j < 4) {
                chests.get(j).setAction(actions.get(i));
                ++j;
            } else {
                break;
            }
        }
        if (this.getAction(Shogun.actionBid.getName()) == null) {
            chests.get(j).setAction(Shogun.actionBid);
        }
    }

    public int nProvinces() {
        int result = 0;
        for (String region : Shogun.world.keySet()) {
            for (String province : Shogun.world.get(region).getProvinces().keySet()) {
                if (Shogun.world.get(region).getProvinces().get(province).getOwner() != null && Shogun.world.get(region).getProvinces().get(province).getOwner().equals(this)) {
                    ++result;
                }
            }
        }
        return result;
    }

    public List<Province> myProvinces() {
        List<Province> result = new ArrayList<>();
        for (String region : Shogun.world.keySet()) {
            for (String province : Shogun.world.get(region).getProvinces().keySet()) {
                if (Shogun.world.get(region).getProvinces().get(province).getOwner() != null && Shogun.world.get(region).getProvinces().get(province).getOwner().equals(this)) {
                    result.add(Shogun.world.get(region).getProvinces().get(province));
                }
            }
        }
        return result;
    }

    /**
     * @return the ready
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * @param ready the ready to set
     */
    public void setReady(boolean ready) {
        this.ready = ready;
    }

    @Override
    public int compareTo(Daimyo o) {
        if (Integer.compare(this.getInitiative(), o.getInitiative()) != 0) {
            return Integer.compare(this.getInitiative(), o.getInitiative());
        } else {
            int offeredGoldThis = 0;
            int offeredGoldO = 0;
            for (Chest chest : this.getChestCards()) {
                if (chest.getAction() != null && chest.getAction().getName().equals(Shogun.actionBid.getName())) {
                    offeredGoldThis = 2 * (this.getGold() - max(this.getGold() - chest.getGold(), 0) + 1);
                }
            }
            if (offeredGoldThis == 0) {
                if (this.getAction(Shogun.actionBid.getName()) != null) {
                    offeredGoldThis = 3;
                } else {
                    offeredGoldThis = 2;
                }
            }
            for (Chest chest : o.getChestCards()) {
                if (chest.getAction() != null && chest.getAction().getName().equals(Shogun.actionBid.getName())) {
                    offeredGoldO = 2 * (o.getGold() - max(o.getGold() - chest.getGold(), 0) + 1);
                }
            }
            if (offeredGoldO == 0) {
                if (o.getAction(Shogun.actionBid.getName()) != null) {
                    offeredGoldO = 3;
                } else {
                    offeredGoldO = 2;
                }
            }
            if (offeredGoldThis != offeredGoldO) {
                return Integer.compare(offeredGoldThis, offeredGoldO);
            } else {
                return (new Random().nextInt(2) * 2) - 1;
            }
        }
    }

    /**
     * @return the pointCounterS
     */
    public Image getPointCounterS() {
        return pointCounterS.getImg();
    }

    /**
     * @return the pointCounterL
     */
    public Image getPointCounterL() {
        return pointCounterL.getImg();
    }

    /**
     * @return the riceCounterS
     */
    public Image getRiceCounterS() {
        return riceCounterS.getImg();
    }

    /**
     * @return the riceCounterL
     */
    public Image getRiceCounterL() {
        return riceCounterL.getImg();
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
        if (status > 0) {
            if (status == 1) {
                int generator = new Random().nextInt(3);
                switch (generator) {
                    case 0:
                        this.AI = new ArtificialIntelligenceBuilder();
                        break;
                    case 1:
                        this.AI = new ArtificialIntelligenceBalanced();
                        break;
                    case 2:
                        this.AI = new ArtificialIntelligenceOffensive();
                        break;
                    default:
                        this.AI = new ArtificialIntelligenceRandom();
                        break;
                }
            } else if (status == 2) {
                this.AI = new ArtificialIntelligenceBuilder();
            } else if (status == 3) {
                this.AI = new ArtificialIntelligenceBalanced();
            } else if (status == 4) {
                this.AI = new ArtificialIntelligenceOffensive();
            } else {
                this.AI = new ArtificialIntelligenceBalanced();
            }
        }
    }

    /**
     * @return the AI
     */
    public ArtificialIntelligence getAI() {
        return AI;
    }

    /**
     * @param AI the AI to set
     */
    public void setAI(ArtificialIntelligence AI) {
        this.AI = AI;
    }

    public void initializeStartingProvinces(int nPlayers) {
        try {
            getStartingProvinces().add(new Province("P5", 0, 0, null, 674, 372, 674, 372, 118, 57, 733, 399, 0, 0, null, null, null, null));
            getStartingProvinces().get(getStartingProvinces().size() - 1).setArmies(5);
            getStartingProvinces().get(getStartingProvinces().size() - 1).setOwner(this);
            getStartingProvinces().add(new Province("P4a", 0, 0, null, 863, 301, 863, 301, 104, 56, 914, 333, 0, 0, null, null, null, null));
            getStartingProvinces().get(getStartingProvinces().size() - 1).setArmies(4);
            getStartingProvinces().get(getStartingProvinces().size() - 1).setOwner(this);
            getStartingProvinces().add(new Province("P4b", 0, 0, null, 651, 289, 651, 289, 92, 48, 699, 319, 0, 0, null, null, null, null));
            getStartingProvinces().get(getStartingProvinces().size() - 1).setArmies(4);
            getStartingProvinces().get(getStartingProvinces().size() - 1).setOwner(this);
            getStartingProvinces().add(new Province("P3a", 0, 0, null, 606, 243, 606, 243, 65, 39, 638, 263, 0, 0, null, null, null, null));
            getStartingProvinces().get(getStartingProvinces().size() - 1).setArmies(3);
            getStartingProvinces().get(getStartingProvinces().size() - 1).setOwner(this);
            getStartingProvinces().add(new Province("P3b", 0, 0, null, 745, 236, 745, 236, 82, 48, 785, 261, 0, 0, null, null, null, null));
            getStartingProvinces().get(getStartingProvinces().size() - 1).setArmies(3);
            getStartingProvinces().get(getStartingProvinces().size() - 1).setOwner(this);
            getStartingProvinces().add(new Province("P2a", 0, 0, null, 881, 209, 881, 209, 91, 45, 923, 231, 0, 0, null, null, null, null));
            getStartingProvinces().get(getStartingProvinces().size() - 1).setArmies(2);
            getStartingProvinces().get(getStartingProvinces().size() - 1).setOwner(this);
            getStartingProvinces().add(new Province("P2b", 0, 0, null, 1005, 242, 1005, 242, 55, 36, 1032, 259, 0, 0, null, null, null, null));
            getStartingProvinces().get(getStartingProvinces().size() - 1).setArmies(2);
            getStartingProvinces().get(getStartingProvinces().size() - 1).setOwner(this);
        } catch (IOException ex) {
            Logger.getLogger(Daimyo.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (nPlayers <= 4) {
            try {
                getStartingProvinces().add(new Province("P2c", 0, 0, null, 1008, 379, 1008, 379, 71, 52, 1045, 405, 0, 0, null, null, null, null));
                getStartingProvinces().get(getStartingProvinces().size() - 1).setArmies(2);
                getStartingProvinces().get(getStartingProvinces().size() - 1).setOwner(this);
            } catch (IOException ex) {
                Logger.getLogger(Daimyo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (nPlayers <= 3) {
            try {
                getStartingProvinces().add(new Province("P2d", 0, 0, null, 1015, 292, 1015, 292, 67, 47, 1047, 313, 0, 0, null, null, null, null));
                getStartingProvinces().get(getStartingProvinces().size() - 1).setArmies(2);
                getStartingProvinces().get(getStartingProvinces().size() - 1).setOwner(this);
            } catch (IOException ex) {
                Logger.getLogger(Daimyo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void zeroStartingProvinces() {
        for (Province province : startingProvinces) {
            province.setArmies(0);
        }
    }

    /**
     * @return the startingProvinces
     */
    public List<Province> getStartingProvinces() {
        return startingProvinces;
    }
}
