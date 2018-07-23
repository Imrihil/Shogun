/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shogun;

import java.awt.Image;

/**
 *
 * @author Imrihil
 */
public class Special extends Card {
    
    private final Picture cardhS;
    private final Picture cardhL;
    
    private final int attackBonus;
    
    private final int defenceBonus;
    
    private final int goldBonus;
    
    private final int riceBonus;
    
    private final int recruitment5;
    
    private int initiative;

    public Special(String name, Picture cardS, Picture cardL, Picture cardhS, Picture cardhL, int attackBonus, int defenceBonus, int goldBonus, int riceBonus, int recruitment5) {
        super(name, cardS, cardL);
        this.cardhS = cardhS;
        this.cardhL = cardhL;
        this.attackBonus = attackBonus;
        this.defenceBonus = defenceBonus;
        this.goldBonus = goldBonus;
        this.riceBonus = riceBonus;
        this.recruitment5 = recruitment5;
        this.initiative = 0;
    }

    /**
     * @return the cardhS
     */
    public Image getCardhS() {
        return cardhS.getImg();
    }

    /**
     * @return the cardhL
     */
    public Image getCardhL() {
        return cardhL.getImg();
    }

    /**
     * @return the attackBonus
     */
    public int getAttackBonus() {
        return attackBonus;
    }

    /**
     * @return the defenceBonus
     */
    public int getDefenceBonus() {
        return defenceBonus;
    }

    /**
     * @return the goldBonus
     */
    public int getGoldBonus() {
        return goldBonus;
    }

    /**
     * @return the riceBonus
     */
    public int getRiceBonus() {
        return riceBonus;
    }

    /**
     * @return the recruitment5
     */
    public int getRecruitment5() {
        return recruitment5;
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
}
