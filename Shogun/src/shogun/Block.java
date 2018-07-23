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
public class Block implements Comparable<Block> {

    private final Daimyo owner;
    
    private int delay;
    
    public Block(Daimyo owner) {
        this.owner = owner;
        this.delay = 0;
    }

    /**
     * @return the owner
     */
    public Daimyo getOwner() {
        return owner;
    }

    /**
     * @return the delay
     */
    public int getDelay() {
        return delay;
    }

    /**
     * @param delay the delay to set
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public int compareTo(Block o) {
        return Integer.compare(this.getDelay(), o.getDelay());
    }
}
