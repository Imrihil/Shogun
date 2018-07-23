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
public abstract class ArtificialIntelligence {
    public abstract void planActions(Daimyo daimyo);
    
    public abstract void chooseProvince(Daimyo daimyo);
    
    public abstract void chooseSpecialCard(Daimyo daimyo);
    
    public abstract void doAction0Castle(Daimyo daimyo, boolean possible, Action action);
    public abstract void doAction1Temple(Daimyo daimyo, boolean possible, Action action);
    public abstract void doAction2Theatre(Daimyo daimyo, boolean possible, Action action);
    public abstract void doAction3Rice(Daimyo daimyo, boolean possible, Action action);
    public abstract void doAction4Gold(Daimyo daimyo, boolean possible, Action action);
    public abstract void doAction5Recruitment5(Daimyo daimyo, boolean possible, Action action);
    public abstract void doAction6Recruitment3(Daimyo daimyo, boolean possible, Action action);
    public abstract void doAction7Recruitment1(Daimyo daimyo, boolean possible, Action action);
    public abstract void doAction89Move(Daimyo daimyo, boolean possible, Action action);
    
    public abstract void chooseRebelion(Daimyo daimyo);
}
