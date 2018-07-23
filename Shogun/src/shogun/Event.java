/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shogun;

import java.awt.Image;
import java.io.IOException;

/**
 *
 * @author Imrihil
 */
public class Event extends Card {

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

    private final int riceRequest;

    private final int revolt;

    private final int neutralArmyBonus;

    private final int castleDefenderBonus;

    private final boolean templeProtection;

    private final int maxGold;

    private final int minGold;

    private final int maxRice;

    private final int minRice;

    private final int Recruitment5;

    private final int Recruitment3;

    public Event(String name, Picture cardS, Picture cardL, int riceRequest, int revolt, int neutralArmyBonus, int castleDefenderBonus, boolean templeProtection, int maxGold, int minGold, int maxRice, int minRice, int Recrutation5, int Recrutation3) throws IOException {
        super(name, cardS, cardL);
        Event.cardBackS = new Picture("/img/small/card/event/event_back.png");
        Event.cardBackL = new Picture("/img/large/card/event/event_back.png");
        this.riceRequest = riceRequest;
        this.revolt = revolt;
        this.neutralArmyBonus = neutralArmyBonus;
        this.castleDefenderBonus = castleDefenderBonus;
        this.templeProtection = templeProtection;
        this.maxGold = maxGold;
        this.minGold = minGold;
        this.maxRice = maxRice;
        this.minRice = minRice;
        this.Recruitment5 = Recrutation5;
        this.Recruitment3 = Recrutation3;
    }

    /**
     * @return the riceRequest
     */
    public int getRiceRequest() {
        return riceRequest;
    }

    /**
     * @return the revolt
     */
    public int getRevolt() {
        return revolt;
    }

    /**
     * @return the neutralArmyBonus
     */
    public int getNeutralArmyBonus() {
        return neutralArmyBonus;
    }

    /**
     * @return the castleDefenderBonus
     */
    public int getCastleDefenderBonus() {
        return castleDefenderBonus;
    }

    /**
     * @return the templeProtection
     */
    public boolean isTempleProtection() {
        return templeProtection;
    }

    /**
     * @return the maxGold
     */
    public int getMaxGold() {
        return maxGold;
    }

    /**
     * @return the minGold
     */
    public int getMinGold() {
        return minGold;
    }

    /**
     * @return the maxRice
     */
    public int getMaxRice() {
        return maxRice;
    }

    /**
     * @return the minRice
     */
    public int getMinRice() {
        return minRice;
    }

    /**
     * @return the Recrutation5
     */
    public int getRecruitment5() {
        return Recruitment5;
    }

    /**
     * @return the Recrutation3
     */
    public int getRecruitment3() {
        return Recruitment3;
    }
}
