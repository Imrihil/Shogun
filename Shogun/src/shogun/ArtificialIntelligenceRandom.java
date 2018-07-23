/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shogun;

import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Imrihil
 */
public class ArtificialIntelligenceRandom extends ArtificialIntelligence {

    @Override
    public void planActions(Daimyo daimyo) {
        List<Action> actions = new ArrayList<>();
        for (Action action : Shogun.actions) {
            actions.add(action);
        }
        List<Province> provinces = daimyo.myProvinces();
        for (Province province : provinces) {
            province.setAction(null);
        }
        List<Chest> chests = daimyo.getChestCards();
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
        if (daimyo.getAction(Shogun.actionBid.getName()) == null) {
            chests.get(j).setAction(Shogun.actionBid);
        }
        daimyo.setReady(true);
        for (String region : Shogun.world.keySet()) {
            for (String province : Shogun.world.get(region).getProvinces().keySet()) {
                if (Shogun.world.get(region).getProvinces().get(province).getOwner() != null && Shogun.world.get(region).getProvinces().get(province).getOwner().equals(daimyo)) {
                    Shogun.world.get(region).getProvinces().get(province).turn();
                }
            }
        }
        for (Chest chestCard : daimyo.getChestCards()) {
            chestCard.turn();
        }
    }
    
    @Override
    public void chooseProvince(Daimyo daimyo) {
        List<Province> provincesWithArmies = new ArrayList<>();
        int rand = new Random().nextInt(3);
        for (Province province : daimyo.getStartingProvinces()) {
            if (province.getArmies() > 0) {
                provincesWithArmies.add(province);
            }
        }
        Collections.shuffle(provincesWithArmies);
        GameStep3Game.choosingProvonces.get(rand).setOwner(daimyo);
        GameStep3Game.choosingProvonces.get(rand).setArmies(provincesWithArmies.get(0).getArmies());
        provincesWithArmies.get(0).setArmies(0);
        GameStep3Game.choosingProvonces.remove(rand);
        GameStep3Game.activeDaimyo.setReady(true);
    }

    @Override
    public void chooseSpecialCard(Daimyo daimyo) {
        List<Special> specials = new ArrayList<>();
        for (Special special : Shogun.specials) {
            if (special.isTurnOn()) {
                specials.add(special);
            }
        }
        Collections.shuffle(specials);
        daimyo.setSpecial(specials.get(0));
        daimyo.setReady(true);
        daimyo.setInitiative(specials.get(0).getInitiative());
        specials.get(0).turn();
    }

    @Override
    public void doAction0Castle(Daimyo daimyo, boolean possible, Action action) {
        if (possible) {
            daimyo.getActionProvince(action.getName()).addBuildings(Building.CASTLE);
            daimyo.setGold(daimyo.getGold() - 3);
            GameStep3Game.provinceHiglighted = null;
            action.setChoose(1);
        } else {
            action.setChoose(2);
        }
        daimyo.setReady(true);
    }

    @Override
    public void doAction1Temple(Daimyo daimyo, boolean possible, Action action) {
        if (possible) {
            daimyo.getActionProvince(action.getName()).addBuildings(Building.TEMPLE);
            daimyo.setGold(daimyo.getGold() - 2);
            GameStep3Game.provinceHiglighted = null;
            action.setChoose(1);
        } else {
            action.setChoose(2);
        }
        daimyo.setReady(true);
    }

    @Override
    public void doAction2Theatre(Daimyo daimyo, boolean possible, Action action) {
        if (possible) {
            daimyo.getActionProvince(action.getName()).addBuildings(Building.THEATRE);
            daimyo.setGold(daimyo.getGold() - 1);
            GameStep3Game.provinceHiglighted = null;
            action.setChoose(1);
        } else {
            action.setChoose(2);
        }
        daimyo.setReady(true);
    }

    @Override
    public void doAction3Rice(Daimyo daimyo, boolean possible, Action action) {
        if (possible) {
            daimyo.setRice(daimyo.getRice() + max(min(daimyo.getActionProvince(action.getName()).getRice(), Shogun.allEvents.get(GameStep3Game.turnNr).getMaxRice()), Shogun.allEvents.get(GameStep3Game.turnNr).getMinRice()) + daimyo.getSpecial().getRiceBonus());
            daimyo.getActionProvince(action.getName()).setRevolt(daimyo.getActionProvince(action.getName()).getRevolt() + 1);
            GameStep3Game.provinceHiglighted = null;
            action.setChoose(1);
        } else {
            action.setChoose(2);
        }
        daimyo.setReady(true);
    }

    @Override
    public void doAction4Gold(Daimyo daimyo, boolean possible, Action action) {
        if (possible) {
            daimyo.setGold(daimyo.getGold() + max(min(daimyo.getActionProvince(action.getName()).getGold(), Shogun.allEvents.get(GameStep3Game.turnNr).getMaxGold()), Shogun.allEvents.get(GameStep3Game.turnNr).getMinGold()) + daimyo.getSpecial().getGoldBonus());
            daimyo.getActionProvince(action.getName()).setRevolt(daimyo.getActionProvince(action.getName()).getRevolt() + 1);
            daimyo.setReady(true);
            GameStep3Game.provinceHiglighted = null;
            action.setChoose(1);
        } else {
            action.setChoose(2);
        }
        daimyo.setReady(true);
    }

    @Override
    public void doAction5Recruitment5(Daimyo daimyo, boolean possible, Action action) {
        if (possible) {
            daimyo.setGold(daimyo.getGold() - 3);
            daimyo.getActionProvince(action.getName()).setArmies(daimyo.getActionProvince(action.getName()).getArmies() + min(max(min(5, Shogun.allEvents.get(GameStep3Game.turnNr).getRecruitment5()), GameStep3Game.activeDaimyo.getSpecial().getRecruitment5()), GameStep3Game.activeDaimyo.getArmies()));
            daimyo.setReady(true);
            GameStep3Game.provinceHiglighted = null;
            action.setChoose(1);
        } else {
            action.setChoose(2);
        }
        daimyo.setReady(true);
    }

    @Override
    public void doAction6Recruitment3(Daimyo daimyo, boolean possible, Action action) {
        if (possible) {
            daimyo.setGold(daimyo.getGold() - 2);
            daimyo.getActionProvince(action.getName()).setArmies(daimyo.getActionProvince(action.getName()).getArmies() + min(min(3, Shogun.allEvents.get(GameStep3Game.turnNr).getRecruitment3()), daimyo.getArmies()));
            daimyo.setReady(true);
            GameStep3Game.provinceHiglighted = null;
            action.setChoose(1);
        } else {
            action.setChoose(2);
        }
        daimyo.setReady(true);
    }

    @Override
    public void doAction7Recruitment1(Daimyo daimyo, boolean possible, Action action) {
        if (possible) {
            daimyo.setGold(daimyo.getGold() - 1);
            daimyo.getActionProvince(action.getName()).setArmies(daimyo.getActionProvince(action.getName()).getArmies() + min(1, daimyo.getArmies()));
            action.setChoose(1);
            int movedArmies = daimyo.getActionProvince(action.getName()).getArmies() - 1;
            Province provinceTo = daimyo.getActionProvince(action.getName());
            for (String province : daimyo.getActionProvince(action.getName()).getNeighbour().keySet()) {
                if (daimyo.getActionProvince(action.getName()).getNeighbour().get(province).getOwner() != null && daimyo.getActionProvince(action.getName()).getNeighbour().get(province).getOwner().equals(daimyo) && daimyo.getActionProvince(action.getName()).getNeighbour().get(province).getArmies() > provinceTo.getArmies()) {
                    provinceTo = daimyo.getActionProvince(action.getName()).getNeighbour().get(province);
                }
            }
            daimyo.getActionProvince(action.getName()).setArmies(daimyo.getActionProvince(action.getName()).getArmies() - movedArmies);
            provinceTo.setArmies(provinceTo.getArmies() + movedArmies);
            GameStep3Game.activeDaimyo.setReady(true);
            GameStep3Game.provinceHiglighted = null;
        } else {
            action.setChoose(2);
        }
        daimyo.setReady(true);
    }

    @Override
    public void doAction89Move(Daimyo daimyo, boolean possible, Action action) {
        if (possible) {
            List<Province> toMove = new ArrayList<>();
            action.setChoose(1);
            int movedArmies = daimyo.getActionProvince(action.getName()).getArmies() - 1;
            Shogun.provinceDrag.setOwner(daimyo);
            Shogun.provinceDrag.setArmies(movedArmies);
            daimyo.getActionProvince(action.getName()).setArmies(daimyo.getActionProvince(action.getName()).getArmies() - movedArmies);
            for (String province : daimyo.getActionProvince(action.getName()).getNeighbour().keySet()) {
                if (daimyo.getActionProvince(action.getName()).getNeighbour().get(province).isInGame() && (Action8MoveA.canAttack(daimyo.getActionProvince(action.getName()).getNeighbour().get(province)) || (daimyo.getActionProvince(action.getName()).getOwner() != null && daimyo.getActionProvince(action.getName()).getOwner().equals(daimyo)))) {
                    toMove.add(daimyo.getActionProvince(action.getName()).getNeighbour().get(province));
                }
            }
            GameStep3Game.provinceHiglighted = daimyo.getActionProvince(action.getName());
            Collections.shuffle(toMove);
            if (movedArmies > 1) {
                for (Province province : toMove) {
                    if (province.getArmies() < movedArmies) {
                        GameStep3Game.provinceHiglighted = province;
                        break;
                    }
                }
            }
        } else {
            action.setChoose(2);
        }
        daimyo.setReady(true);
    }

    @Override
    public void chooseRebelion(Daimyo daimyo) {
        for (Province province : daimyo.myProvinces()) {
            if (province.isTurnOn()) {
                GameStep3Game.provinceHiglighted = province;
                return;
            }
        }
    }
}
