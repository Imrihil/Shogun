/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shogun;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Imrihil
 */
public class ActionBid extends Action {

    public ActionBid() throws IOException {
        super("Bid", new Picture("/img/small/card/action/action_back.png"), new Picture("/img/large/card/action/action_back.png"), new Picture("/img/large/card/action/doing/accept.png"), new Picture("/img/large/card/action/doing/acceptNoAvailable.png"), new Picture("/img/large/card/action/doing/decline.png"), new Picture("/img/large/card/action/doing/choose_highlighted.png"), new Picture("/img/large/card/action/doing/action0.png"), 405, 282);
    }

    @Override
    public void doAction(GameStep3Game gameStep, MyJFrame window, List<Daimyo> daimyos) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
