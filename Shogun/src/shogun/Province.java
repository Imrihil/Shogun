/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shogun;

import java.awt.Image;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Imrihil
 */
public class Province extends Card implements Comparable<Province> {

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

    private boolean inGame;

    private Action action;

    private int armies;

    //położenie armii na mapie
    private int armiesX;
    private int armiesY;

    private int revolt;

    //położenie znaczników buntu na mapie
    private final int revoltX;
    private final int revoltY;

    private Daimyo owner;

    private final int gold;

    private final int rice;

    //położenie higlight na mapie
    private final int x;
    private final int y;

    //prostokąt zaznaczenia regionu na mapie
    private final int startX;
    private final int startY;
    private final int sizeX;
    private final int sizeY;

    private final Picture highlightS;
    private final Picture highlightL;

    private final List<Place> buildings;

    private Map<String, Province> neighbour;

    //Na potrzeby Sztucznej Inteligencji
    private double fit;
    private int danger;

    public Province(String name, int gold, int rice, List<Place> buildings, int x, int y, int startX, int startY, int sizeX, int sizeY, int armiesX, int armiesY, int revoltX, int revoltY, Picture highlightS, Picture highlightL, Picture cardS, Picture cardL) throws IOException {
        super(name, cardS, cardL);
        Province.cardBackS = new Picture("/img/small/card/province/province_back.png");
        Province.cardBackL = new Picture("/img/large/card/province/province_back.png");
        this.inGame = true;
        this.action = null;
        this.armies = 0;
        this.revolt = 0;
        this.owner = null;
        this.gold = gold;
        this.rice = rice;
        this.buildings = buildings;
        this.x = x;
        this.y = y;
        this.startX = startX;
        this.startY = startY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.armiesX = armiesX;
        this.armiesY = armiesY;
        this.revoltX = revoltX;
        this.revoltY = revoltY;
        this.highlightS = highlightS;
        this.highlightL = highlightL;
        this.fit = 0;
    }

    /**
     * @return the inGame
     */
    public boolean isInGame() {
        return inGame;
    }

    /**
     * @param inGame the inGame to set
     */
    public void setInGame(boolean inGame) {
        this.inGame = inGame;
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
     * @return the armies
     */
    public int getArmies() {
        return armies;
    }

    /**
     * @param armies the armies to set
     */
    public void setArmies(int armies) {
        this.armies = armies;
    }

    /**
     * @return the revolt
     */
    public int getRevolt() {
        return revolt;
    }

    /**
     * @param revolt the revolt to set
     */
    public void setRevolt(int revolt) {
        this.revolt = revolt;
    }

    /**
     * @return the owner
     */
    public Daimyo getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(Daimyo owner) {
        this.owner = owner;
    }

    /**
     * @return the gold
     */
    public int getGold() {
        return gold;
    }

    /**
     * @return the rice
     */
    public int getRice() {
        return rice;
    }

    /**
     * @return the buildings
     */
    public List<Place> getBuildings() {
        return buildings;
    }

    /**
     * @param type
     * @return
     */
    public boolean addBuildings(Building type) {
        //Jeśli już jest wybudowany budynek danego typu - nie można wybudować nowego.
        for (Place place : buildings) {
            if (place.getType() == type) {
                return false;
            }
        }
        //Jeśli istnieje wolne miejsce - wybuduj budynek.
        for (int i = 0; i < buildings.size(); i++) {
            if (buildings.get(i).getType() == Building.EMPTY) {
                buildings.set(i, new Place(type, buildings.get(i).getX(), buildings.get(i).getY()));
                return true;
            }
        }
        //Gdy nie było miejsca na nowy budynek.
        return false;
    }

    /**
     * Destroy every building in the province. When province is neutral.
     */
    public void destroy() {
        for (int i = 0; i < buildings.size(); i++) {
            buildings.set(i, new Place(Building.EMPTY, buildings.get(i).getX(), buildings.get(i).getY()));
        }
        revolt = 0;
        owner = null;
        action = null;
        armies = 0;
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
     * @return the highlight
     */
    public Image getHighlightS() {
        return highlightS.getImg();
    }

    /**
     * @return the highlight
     */
    public Image getHighlightL() {
        return highlightL.getImg();
    }

    /**
     * @return the neighbour
     */
    public Map<String, Province> getNeighbour() {
        return neighbour;
    }

    /**
     * @param neighbour the neighbour to set
     */
    public void setNeighbour(Map<String, Province> neighbour) {
        this.neighbour = neighbour;
    }

    /**
     * @return the armiesX
     */
    public int getArmiesX() {
        return armiesX;
    }

    /**
     * @return the armiesY
     */
    public int getArmiesY() {
        return armiesY;
    }

    /**
     * @return the revoltX
     */
    public int getRevoltX() {
        return revoltX;
    }

    /**
     * @return the revoltY
     */
    public int getRevoltY() {
        return revoltY;
    }

    /**
     * @return the startX
     */
    public int getStartX() {
        return startX;
    }

    /**
     * @return the startY
     */
    public int getStartY() {
        return startY;
    }

    /**
     * @return the sizeX
     */
    public int getSizeX() {
        return sizeX;
    }

    /**
     * @return the sizeY
     */
    public int getSizeY() {
        return sizeY;
    }

    /**
     * @param armiesX the armiesX to set
     */
    public void setArmiesX(int armiesX) {
        this.armiesX = armiesX;
    }

    /**
     * @param armiesY the armiesY to set
     */
    public void setArmiesY(int armiesY) {
        this.armiesY = armiesY;
    }

    /**
     * @return the fit
     */
    public double getFit() {
        return fit;
    }

    /**
     * @param fit the fit to set
     */
    public void setFit(double fit) {
        this.fit = fit;
    }

    @Override
    public int compareTo(Province o) {
        return Double.compare(o.getFit(), this.getFit());
    }

    /**
     * @return the danger
     */
    public int getDanger() {
        return danger;
    }

    /**
     */
    public void setDanger() {
        danger = -armies;
        for (String province : neighbour.keySet()) {
            if (neighbour.get(province).getOwner() != null && !neighbour.get(province).getOwner().equals(owner) && neighbour.get(province).getArmies() - armies > danger) {
                danger = neighbour.get(province).getArmies() - armies;
            }
        }
    }
}
