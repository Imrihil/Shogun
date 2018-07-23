/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shogun;

/**
 *
 * @author Imrihil
 */
class Place {

    private Building type;

    private final int x;

    private final int y;

    public Place(Building type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    /**
     * @return the type
     */
    public Building getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Building type) {
        this.type = type;
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
}
