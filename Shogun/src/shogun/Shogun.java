/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shogun;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Imrihil
 */
public class Shogun {

    /**
     * On how step is the game: 1 - welcome, 2 - game initializing, 3 - playing.
     */
    protected static int step;

    protected static int shiftX;

    protected static int shiftY;

    protected static int size;

    protected static List<Daimyo> daimyos;

    protected static Map<String, Region> world;

    protected static Province provinceDrag;

    protected static List<Action> actions;

    protected static ActionBid actionBid;

    protected static ActionDrag actionDrag;

    protected static List<Event> allEvents;

    protected static List<Special> specials;

    protected static Tower tower;

    protected static int resolutionX = 1366;

    protected static int resolutionY = 700;

    protected static int refreshPeriod = 50;

    protected static Semaphore sem = new Semaphore(1);

    protected static boolean choosingStartingProvinces = true;

    protected static boolean bonuses = true;

    public static Action getAction(String name) {
        for (Action action : actions) {
            if (action.getName().equals(name)) {
                return action;
            }
        }
        if (actionBid.getName().equals(name)) {
            return actionBid;
        }
        if (actionDrag.getName().equals(name)) {
            return actionDrag;
        }
        return null;
    }

    public static int nPlayers() {
        int nPlayers = 0;
        for (Daimyo daimyo : Shogun.daimyos) {
            if (daimyo.isPlaying()) {
                nPlayers++;
            }
        }
        return nPlayers;
    }

    private static MyJFrame window;

    private static GameStep1Welcome gameStep1;

    private static GameStep2Options gameStep2;

    private static GameStep3Game gameStep3;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        window = new MyJFrame();
        while (true) {
            initialize();
            try {
                step = 0;
                size = 2;
                step = 1;
                if (step == 1) {
                    sem.acquireUninterruptibly();
                    gameStep1 = new GameStep1Welcome();
                    shiftX = 0;
                    shiftY = 0;
                    gameStep1.work(window);
                    sem.release();
                }
                if (step == 2) {
                    sem.acquireUninterruptibly();
                    gameStep2 = new GameStep2Options();
                    shiftX = 0;
                    shiftY = 0;
                    gameStep2.work(window);
                    sem.release();
                }
                if (step == 3) {
                    sem.acquireUninterruptibly();
                    gameStep3 = new GameStep3Game(boardType());
                    shiftX = 0;
                    shiftY = 0;
                    gameStep3.work(window);
                    sem.release();
                }
            } catch (IOException ex) {
                Logger.getLogger(Shogun.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void initialize() {
        try {
            Initializator.initializeDaimyo();
        } catch (IOException ex) {
            Logger.getLogger(Shogun.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Initializator.initializeProvinces();
        } catch (IOException ex) {
            Logger.getLogger(Shogun.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Initializator.initializeActions();
        } catch (IOException ex) {
            Logger.getLogger(Shogun.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Initializator.initializeEvents();
        } catch (IOException ex) {
            Logger.getLogger(Shogun.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Initializator.initializeSpecials();
        } catch (IOException ex) {
            Logger.getLogger(Shogun.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            tower = new Tower();
        } catch (IOException ex) {
            Logger.getLogger(Shogun.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Province getProvince(String name) {
        Province result = null;
        for (String reg : world.keySet()) {
            result = world.get(reg).getProvinces().get(name);
            if (result != null) {
                return result;
            }
        }
        return result;
    }

    private static String boardType() {
        if (nPlayers() == 3) {
            return "_3players";
        } else {
            return "";
        }
    }
}
