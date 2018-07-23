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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author Imrihil
 */
public class ArtificialIntelligenceBalanced extends ArtificialIntelligence {

    private List<Province> provinces;

    private List<Province> provincesToGold;

    private List<Action> actions;

    private List<Chest> chests;

    private Daimyo daimyo;

    private int riceShortage;

    private int turnToWinter;

    @Override
    public void planActions(Daimyo daimyo) {
        System.out.println("");
        System.out.println(daimyo.getId());
        init(daimyo);
        setBidAction();
        if (riceShortage > 0) {
            setRiceAction();
        }
        if (daimyo.getGold() < 20) {
            findGoldProvinces();
        }
        setRecruitment35Action("Recruitment5");
        setRecruitment35Action("Recruitment3");
        setRecruitment1Action();
        setMoveAction("MoveA");
        setMoveAction("MoveB");
        setRestActions();
        setGoldAction();
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
        List<Province> provincesToChoose = new ArrayList<>();
        for (Province province : GameStep3Game.choosingProvonces) {
            provincesToChoose.add(province);
            province.setFit(3 * province.getRice() + 2 * province.getGold());
            if (province.getRice() >= 4) {
                province.setFit(province.getFit() + 1);
            }
            if (province.getRice() >= 5) {
                province.setFit(province.getFit() + 1);
            }
            if (province.getGold() >= 7) {
                province.setFit(province.getFit() + 1);
            }
            int nRegions = 0;
            for (int i = 0; i < daimyo.myProvinces().size(); i++) {
                for (int j = i + 1; j < daimyo.myProvinces().size(); j++) {
                    //System.out.println("Odległość z " + daimyo.myProvinces().get(i).getName() + " do " + daimyo.myProvinces().get(j).getName() + " to " + distance(daimyo.myProvinces().get(i), daimyo.myProvinces().get(j)));
                    if (distance(daimyo.myProvinces().get(i), daimyo.myProvinces().get(j)) > 2) {
                        ++nRegions;
                    }
                }
            }
            int distancePenalty = Integer.MAX_VALUE;
            for (Province province0 : daimyo.myProvinces()) {
                if (distance(province, province0) < distancePenalty) {
                    distancePenalty = distance(province, province0);
                }
                if (distance(province, province0) == 1) {
                    province.setFit(province.getFit() + 1);
                }
            }
            if (nRegions > 1) {
                province.setFit(province.getFit() - 2 * distancePenalty);
            } else {
                province.setFit(province.getFit() - distancePenalty);
            }
        }
        Collections.sort(provincesToChoose);
        for (Province province : daimyo.getStartingProvinces()) {
            if (province.getArmies() > 0) {
                provincesWithArmies.add(province);
            }
        }
        Collections.shuffle(provincesWithArmies);
        provincesToChoose.get(0).setOwner(daimyo);
        provincesToChoose.get(0).setArmies(provincesWithArmies.get(0).getArmies());
        provincesWithArmies.get(0).setArmies(0);
        GameStep3Game.choosingProvonces.remove(provincesToChoose.get(0));
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
        if (possible && riceShortage > 0) {
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
            List<Province> toMove = new ArrayList<>();
            action.setChoose(1);
            int movedArmies = 0;

            daimyo.getActionProvince(action.getName()).setFit(provinceValue(daimyo.getActionProvince(action.getName())));
            System.out.println("Ruch:");
            System.out.printf("  %s: %4.2f\n", daimyo.getActionProvince(action.getName()).getName(), daimyo.getActionProvince(action.getName()).getFit());
            if (isBack(daimyo.getActionProvince(action.getName()))) {
                daimyo.getActionProvince(action.getName()).setFit(daimyo.getActionProvince(action.getName()).getFit() - 50);
            }
            for (String province : daimyo.getActionProvince(action.getName()).getNeighbour().keySet()) {
                if (daimyo.getActionProvince(action.getName()).getNeighbour().get(province).isInGame() && daimyo.getActionProvince(action.getName()).getNeighbour().get(province).getOwner() != null && daimyo.getActionProvince(action.getName()).getNeighbour().get(province).getOwner().equals(daimyo)) {
                    if (!isBack(daimyo.getActionProvince(action.getName()).getNeighbour().get(province))) {
                        toMove.add(daimyo.getActionProvince(action.getName()).getNeighbour().get(province));
                        daimyo.getActionProvince(action.getName()).getNeighbour().get(province).setFit(provinceValue(daimyo.getActionProvince(action.getName()).getNeighbour().get(province)));
                        System.out.printf("  %s: %4.2f\n", daimyo.getActionProvince(action.getName()).getNeighbour().get(province).getName(), daimyo.getActionProvince(action.getName()).getNeighbour().get(province).getFit());
                    }
                }
            }
            Province provinceTo = chooseProvince(toMove);
            if (provinceTo == null) {
                provinceTo = daimyo.getActionProvince(action.getName());
            } else {
                if (isBack(daimyo.getActionProvince(action.getName()))) {
                    movedArmies = daimyo.getActionProvince(action.getName()).getArmies() - 1;
                } else {
                    if (provinceTo.getFit() > daimyo.getActionProvince(action.getName()).getFit()) {
                        movedArmies = 0;
                        while (!isSafe(provinceTo) && daimyo.getActionProvince(action.getName()).getArmies() - movedArmies > 1) {
                            ++movedArmies;
                            daimyo.getActionProvince(action.getName()).setArmies(daimyo.getActionProvince(action.getName()).getArmies() - 1);
                        }
                    } else {
                        movedArmies = 0;
                        while (isSafe(daimyo.getActionProvince(action.getName())) && daimyo.getActionProvince(action.getName()).getArmies() - movedArmies > 1) {
                            ++movedArmies;
                            daimyo.getActionProvince(action.getName()).setArmies(daimyo.getActionProvince(action.getName()).getArmies() - 1);
                        }
                    }
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
            int movedArmies;
            daimyo.getActionProvince(action.getName()).setFit(provinceValue(daimyo.getActionProvince(action.getName())));
            System.out.println("Ruch:");
            System.out.printf("  %s: %4.2f\n", daimyo.getActionProvince(action.getName()).getName(), daimyo.getActionProvince(action.getName()).getFit());
            if (isBack(daimyo.getActionProvince(action.getName()))) {
                daimyo.getActionProvince(action.getName()).setFit(daimyo.getActionProvince(action.getName()).getFit() - 50);
            }
            for (String province : daimyo.getActionProvince(action.getName()).getNeighbour().keySet()) {
                if (daimyo.getActionProvince(action.getName()).getNeighbour().get(province).isInGame() && (Action8MoveA.canAttack(daimyo.getActionProvince(action.getName()).getNeighbour().get(province)) || (daimyo.getActionProvince(action.getName()).getOwner() != null && daimyo.getActionProvince(action.getName()).getOwner().equals(daimyo)))) {
                    if (daimyo.getActionProvince(action.getName()).getNeighbour().get(province).getOwner() == null) {
                        if (daimyo.getActionProvince(action.getName()).getArmies() >= 4) {
                            toMove.add(daimyo.getActionProvince(action.getName()).getNeighbour().get(province));
                            daimyo.getActionProvince(action.getName()).getNeighbour().get(province).setFit(provinceValue(daimyo.getActionProvince(action.getName()).getNeighbour().get(province)));
                            System.out.printf("  %s: %4.2f\n", daimyo.getActionProvince(action.getName()).getNeighbour().get(province).getName(), daimyo.getActionProvince(action.getName()).getNeighbour().get(province).getFit());
                        }
                    } else if (daimyo.getActionProvince(action.getName()).getNeighbour().get(province).getOwner().equals(daimyo)) {
                        if (!isSafe(daimyo.getActionProvince(action.getName()).getNeighbour().get(province))) {
                            toMove.add(daimyo.getActionProvince(action.getName()).getNeighbour().get(province));
                            daimyo.getActionProvince(action.getName()).getNeighbour().get(province).setFit(provinceValue(daimyo.getActionProvince(action.getName()).getNeighbour().get(province)));
                            System.out.printf("  %s: %4.2f\n", daimyo.getActionProvince(action.getName()).getNeighbour().get(province).getName(), daimyo.getActionProvince(action.getName()).getNeighbour().get(province).getFit());
                        }
                    } else if (daimyo.getActionProvince(action.getName()).getNeighbour().get(province).getArmies() < daimyo.getActionProvince(action.getName()).getArmies() + 1 + 3 * daimyo.getSpecial().getAttackBonus()) {
                        toMove.add(daimyo.getActionProvince(action.getName()).getNeighbour().get(province));
                        daimyo.getActionProvince(action.getName()).getNeighbour().get(province).setFit(provinceValue(daimyo.getActionProvince(action.getName()).getNeighbour().get(province)));
                        System.out.printf("  %s: %4.2f\n", daimyo.getActionProvince(action.getName()).getNeighbour().get(province).getName(), daimyo.getActionProvince(action.getName()).getNeighbour().get(province).getFit());
                    }
                }
            }
            GameStep3Game.provinceHiglighted = chooseProvince(toMove);
            if (GameStep3Game.provinceHiglighted == null) {
                GameStep3Game.provinceHiglighted = daimyo.getActionProvince(action.getName());
            }
            Shogun.provinceDrag.setOwner(daimyo);
            if (GameStep3Game.provinceHiglighted.getFit() > daimyo.getActionProvince(action.getName()).getFit()) {
                if (GameStep3Game.provinceHiglighted.getOwner() == null || !GameStep3Game.provinceHiglighted.getOwner().equals(daimyo)) {
                    movedArmies = daimyo.getActionProvince(action.getName()).getArmies() - 1;
                    daimyo.getActionProvince(action.getName()).setArmies(daimyo.getActionProvince(action.getName()).getArmies() - movedArmies);
                } else {
                    movedArmies = 0;
                    while (!isSafe(GameStep3Game.provinceHiglighted) && daimyo.getActionProvince(action.getName()).getArmies() - movedArmies > 1) {
                        ++movedArmies;
                        daimyo.getActionProvince(action.getName()).setArmies(daimyo.getActionProvince(action.getName()).getArmies() - 1);
                    }
                }
            } else {
                movedArmies = 0;
                while (isSafe(daimyo.getActionProvince(action.getName())) && daimyo.getActionProvince(action.getName()).getArmies() - movedArmies > 1) {
                    ++movedArmies;
                    daimyo.getActionProvince(action.getName()).setArmies(daimyo.getActionProvince(action.getName()).getArmies() - 1);
                }
            }
            Shogun.provinceDrag.setArmies(movedArmies);
            if (Shogun.provinceDrag.getArmies() == 0) {
                GameStep3Game.provinceHiglighted = daimyo.getActionProvince(action.getName());
            }
        } else {
            action.setChoose(2);
        }
        daimyo.setReady(true);
    }

    private double provinceValue(Province province) {
        double fit = 0;
        for (Place place : province.getBuildings()) {
            if (place.getType().equals(Building.CASTLE)) {
                fit += 20;
            } else if (place.getType().equals(Building.TEMPLE)) {
                fit += 15;
            } else if (place.getType().equals(Building.THEATRE)) {
                fit += 10;
            } else {
                fit += 1;
            }
        }
        if (province.getRevolt() == 0 && GameStep3Game.turnNr < 6) {
            fit += 12 * max(province.getRice() - 3, 0);
            fit += 8 * max(province.getGold() - 4, 0);
        } else if (GameStep3Game.turnNr < 3) {
            fit += 6 * max(province.getRice() - 3, 0);
            fit += 4 * max(province.getGold() - 4, 0);
        } else {
            fit += 3 * max(province.getRice() - 3, 0);
            fit += 2 * max(province.getGold() - 4, 0);
        }
        if (province.getOwner() != null) {
            if (province.getOwner().equals(daimyo)) {
                if (province.getDanger() > 0) {
                    fit += 5 * province.getDanger();
                } else if (isSafe(province)) {
                    fit -= 50;
                }
            } else {
                fit += 20 - province.getArmies();
            }
        }
        return fit;
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

    private int moveActionNr() {
        return min(actionNr("MoveA"), actionNr("MoveB"));
    }

    private int actionNr(String actionName) {
        for (int i = 0; i < Shogun.actions.size(); i++) {
            if (Shogun.actions.get(i).getName().equals(actionName)) {
                return i;
            }
        }
        return 10;
    }

    private void init(Daimyo daimyo) {
        turnToWinter = 3 - GameStep3Game.turnNr % 4;
        int winterTurn = GameStep3Game.turnNr + turnToWinter;
        this.daimyo = daimyo;
        riceShortage = daimyo.myProvinces().size() + Shogun.allEvents.get(winterTurn).getRiceRequest();
        actions = new ArrayList<>();
        provincesToGold = new ArrayList<>();
        for (Action action : Shogun.actions) {
            actions.add(action);
        }
        provinces = daimyo.myProvinces();
        for (Province province : provinces) {
            province.setAction(null);
            province.setDanger();
        }
        chests = new ArrayList<>();
        for (Chest chest : daimyo.getChestCards()) {
            chest.setAction(null);
            chests.add(chest);
        }
    }

    private boolean setBidAction() {
        for (Chest chest : chests) {
            if (chest.getGold() == 0) {
                chest.setAction(Shogun.getAction("Bid"));
                chests.remove(chest);
                return true;
            }
        }
        return false;
    }

    private boolean setRiceAction() {
        for (Province province : provinces) {
            int revoltPenalty = 0;
            if (province.getRevolt() > 0) {
                if (province.getRevolt() + 1 >= province.getArmies()) {
                    revoltPenalty = 2 * (province.getRevolt() + 4 - province.getArmies()) - 1;
                } else if (province.getRevolt() + 4 >= province.getArmies()) {
                    revoltPenalty = province.getRevolt() + 6 - province.getArmies() - 1;
                } else {
                    revoltPenalty = 1;
                }
            }
            if (actionNr("Rice") < moveActionNr()) {
                province.setFit(2 * min(max(min(province.getRice(), Shogun.allEvents.get(GameStep3Game.turnNr).getMaxRice()), Shogun.allEvents.get(GameStep3Game.turnNr).getMinRice()), riceShortage) - revoltPenalty);
            } else {
                if (province.getDanger() > 0) {
                    province.setFit(2 * min(max(min(province.getRice(), Shogun.allEvents.get(GameStep3Game.turnNr).getMaxRice()), Shogun.allEvents.get(GameStep3Game.turnNr).getMinRice()), riceShortage) - revoltPenalty - 2 * province.getDanger());
                } else {
                    province.setFit(2 * min(max(min(province.getRice(), Shogun.allEvents.get(GameStep3Game.turnNr).getMaxRice()), Shogun.allEvents.get(GameStep3Game.turnNr).getMinRice()), riceShortage) - revoltPenalty);
                }
            }
            if (province.getRice() > riceShortage || province.getRice() > Shogun.allEvents.get(GameStep3Game.turnNr).getMaxRice()) {
                province.setFit(province.getFit() - 2 * (province.getRice() - min(riceShortage, Shogun.allEvents.get(GameStep3Game.turnNr).getMaxRice())));
            }
        }
        if (!provinces.isEmpty()) {
            System.out.println("Rice:");
            Collections.sort(provinces);
            for (Province province : provinces) {
                System.out.printf("  %s: %4.2fl, Rice: %d, Gold: %d,\n", province.getName(), province.getFit(), province.getRice(), province.getGold());
            }
            for (Province province : provinces) {
                if (provincesToGold.size() > 1 || !provincesToGold.contains(province)) {
                    province.setAction(Shogun.getAction("Rice"));
                    actions.remove(Shogun.getAction("Rice"));
                    provinces.remove(province);
                    if (provincesToGold.contains(province)) {
                        provincesToGold.remove(province);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private void findGoldProvinces() {
        int riceProvinces = 0;
        for (Province province : provinces) {
            if (province.getRice() >= 4 && province.getRevolt() == 0) {
                ++riceProvinces;
            }
        }
        for (Province province : provinces) {
            int revoltPenalty = 0;
            if (province.getRevolt() > 0) {
                if (province.getRevolt() + 1 >= province.getArmies()) {
                    revoltPenalty = 2 * (province.getRevolt() + 4 - province.getArmies()) - 1;
                } else if (province.getRevolt() + 4 >= province.getArmies()) {
                    revoltPenalty = province.getRevolt() + 6 - province.getArmies() - 1;
                } else {
                    revoltPenalty = 1;
                }
            }
            if (actionNr("Gold") < moveActionNr()) {
                province.setFit(2 * min(min(province.getGold(), Shogun.allEvents.get(GameStep3Game.turnNr).getMaxGold()), riceShortage) - revoltPenalty);
            } else {
                if (province.getDanger() > 0) {
                    province.setFit(2 * min(min(province.getGold(), Shogun.allEvents.get(GameStep3Game.turnNr).getMaxGold()), riceShortage) - revoltPenalty - 2 * province.getDanger());
                } else {
                    province.setFit(2 * min(min(province.getGold(), Shogun.allEvents.get(GameStep3Game.turnNr).getMaxGold()), riceShortage) - revoltPenalty);
                }
            }
            if (province.getGold() > Shogun.allEvents.get(GameStep3Game.turnNr).getMaxGold()) {
                province.setFit(province.getFit() - 2 * (province.getGold() - Shogun.allEvents.get(GameStep3Game.turnNr).getMaxGold()));
            }
            if (riceProvinces <= turnToWinter) {
                province.setFit(province.getFit() - 3 * min(province.getRice() - 3, 0));
            }
        }
        Collections.sort(provinces);
        System.out.println("Gold:");
        for (Province province : provinces) {
            System.out.printf("  %s: %4.2fl, Rice: %d, Gold: %d,\n", province.getName(), province.getFit(), province.getRice(), province.getGold());
        }
        double lastFit = 0;
        if (provinces.size() >= 3) {
            lastFit = provinces.get(2).getFit() - 0.1;
        } else if (provinces.size() >= 2) {
            lastFit = provinces.get(1).getFit() - 0.1;
        } else if (provinces.size() >= 1) {
            lastFit = provinces.get(0).getFit() - 0.1;
        }
        for (Province province : provinces) {
            if (province.getGold() >= min(5, Shogun.allEvents.get(GameStep3Game.turnNr).getMaxGold()) && province.getFit() >= lastFit) {
                provincesToGold.add(province);
            }
        }
        if (provincesToGold.isEmpty()) {
            for (Province province : provinces) {
                if (province.getGold() >= min(5, Shogun.allEvents.get(GameStep3Game.turnNr).getMaxGold())) {
                    provincesToGold.add(province);
                }
            }
        }
        if (provincesToGold.isEmpty()) {
            for (Province province : provinces) {
                if (province.getGold() >= min(4, Shogun.allEvents.get(GameStep3Game.turnNr).getMaxGold())) {
                    provincesToGold.add(province);
                }
            }
        }
        if (provincesToGold.isEmpty() && daimyo.getGold() < 7) {
            for (Province province : provinces) {
                if (province.getGold() >= min(3, Shogun.allEvents.get(GameStep3Game.turnNr).getMaxGold())) {
                    provincesToGold.add(province);
                }
            }
        }
        for (Province province : provincesToGold) {
            System.out.printf("    To gold: %s\n", province.getName());
        }
    }

    private boolean setGoldAction() {
        if (!provincesToGold.isEmpty()) {
            for (Province province : provinces) {
                if (provincesToGold.contains(province)) {
                    province.setAction(Shogun.getAction("Gold"));
                    actions.remove(Shogun.getAction("Gold"));
                    provinces.remove(province);
                    provincesToGold = new ArrayList<>();
                    return true;
                }
            }
        }
        return false;
    }

    private boolean setRecruitment35Action(String name) {
        int castleValue, templeValue, theatreValue;
        //System.out.printf("  %s\n", name);
        for (Province province : provinces) {
            castleValue = 0;
            templeValue = 0;
            theatreValue = 0;
            for (Place place : province.getBuildings()) {
                if (place.getType().equals(Building.CASTLE)) {
                    castleValue = 4;
                } else if (place.getType().equals(Building.TEMPLE)) {
                    templeValue = 3;
                } else if (place.getType().equals(Building.THEATRE)) {
                    templeValue = 2;
                }
            }
            if (actionNr(name) < moveActionNr()) {
                if (GameStep3Game.turnNr < 3) {
                    province.setFit(3 * province.getDanger() + 3 * max(province.getRice() - 3, 0) + 2 * max(province.getGold() - 4, 0) + 2 * (castleValue + templeValue + theatreValue) + province.getBuildings().size());
                } else {
                    if (province.getRevolt() > 0) {
                        province.setFit(2 * province.getDanger() + max(province.getRice() - 3, 0) + max(province.getGold() - 4, 0) + 2 * (castleValue + templeValue + theatreValue) + province.getBuildings().size());
                    }
                }
            } else {
                if (province.getDanger() > 0) {
                    if (GameStep3Game.turnNr < 3) {
                        province.setFit(-3 * province.getDanger() + 3 * max(province.getRice() - 3, 0) + 2 * max(province.getGold() - 4, 0) + 2 * (castleValue + templeValue + theatreValue) + province.getBuildings().size());
                    } else {
                        if (province.getRevolt() > 0) {
                            province.setFit(-2 * province.getDanger() + max(province.getRice() - 3, 0) + max(province.getGold() - 4, 0) + 2 * (castleValue + templeValue + theatreValue) + province.getBuildings().size());
                        }
                    }
                } else {
                    if (GameStep3Game.turnNr < 3) {
                        province.setFit(3 * max(province.getRice() - 3, 0) + 2 * max(province.getGold() - 4, 0) + 2 * (castleValue + templeValue + theatreValue) + province.getBuildings().size());
                    } else {
                        if (province.getRevolt() > 0) {
                            province.setFit(max(province.getRice() - 3, 0) + max(province.getGold() - 4, 0) + 2 * (castleValue + templeValue + theatreValue) + province.getBuildings().size());
                        }
                    }
                }
            }
            if (isBack(province)) {
                province.setFit(province.getFit() - 6);
            }
            //System.out.printf("    %s: %4.2fl, Rice: %d, Gold: %d, Dan: %d,\n", province.getName(), province.getFit(), province.getRice(), province.getGold(), province.getDanger());
        }
        Map<Province, Double> pomProvinces = new HashMap<>();
        Double attackValue, defenseValue;
        int danger;
        for (Province province : provinces) {
            attackValue = 0.0;
            defenseValue = 0.0;
            for (String neighbour : province.getNeighbour().keySet()) {
                if (province.getNeighbour().get(neighbour).getOwner() == null || !province.getNeighbour().get(neighbour).getOwner().equals(daimyo)) {
                    castleValue = 0;
                    templeValue = 0;
                    theatreValue = 0;
                    danger = province.getArmies() - province.getNeighbour().get(neighbour).getArmies();
                    for (Place place : province.getNeighbour().get(neighbour).getBuildings()) {
                        if (place.getType().equals(Building.CASTLE)) {
                            castleValue = 4;
                        } else if (place.getType().equals(Building.TEMPLE)) {
                            templeValue = 3;
                        } else if (place.getType().equals(Building.THEATRE)) {
                            templeValue = 2;
                        }
                    }
                    if (danger < 0) {
                        attackValue = max(attackValue, -3 * danger + 3 * max(province.getNeighbour().get(neighbour).getRice() - 3, 0) + 2 * max(province.getNeighbour().get(neighbour).getGold() - 4, 0) + 2 * (castleValue + templeValue + theatreValue) + province.getBuildings().size());
                    }
                } else {
                    defenseValue = max(defenseValue, province.getNeighbour().get(neighbour).getFit());
                }
            }
            pomProvinces.put(province, province.getFit() + max(0.71 * attackValue, 0.81 * defenseValue));
        }
        for (Province province : pomProvinces.keySet()) {
            provinces.get(provinces.indexOf(province)).setFit(provinces.get(provinces.indexOf(province)).getFit() + pomProvinces.get(province));
        }
        if (!provinces.isEmpty()) {
            System.out.printf("%s:\n", name);
            Collections.sort(provinces);
            for (Province province : provinces) {
                System.out.printf("  %s: %4.2fl, Rice: %d, Gold: %d, Dan: %d,\n", province.getName(), province.getFit(), province.getRice(), province.getGold(), province.getDanger());
            }
            for (Province province : provinces) {
                if (provincesToGold.size() > 1 || !provincesToGold.contains(province)) {
                    province.setAction(Shogun.getAction(name));
                    actions.remove(Shogun.getAction(name));
                    provinces.remove(province);
                    if (provincesToGold.contains(province)) {
                        provincesToGold.remove(province);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private boolean setRecruitment1Action() {
        boolean possibleMove;
        for (Province province : provinces) {
            /*if (actionNr("Recruitment1") < moveActionNr()) {
             province.setFit(1);
             } else {
             if (province.getDanger() > 0) {
             province.setFit(1);
             } else {
             province.setFit(1);
             }
             }*/
            if (isBack(province)) {
                province.setFit(5 * province.getArmies());
            } else {
                province.setFit(-province.getDanger());
            }
            possibleMove = false;
            for (String neighbour : province.getNeighbour().keySet()) {
                if (province.getNeighbour().get(neighbour).getOwner() != null && province.getNeighbour().get(neighbour).getOwner().equals(daimyo)) {
                    if (!isBack(province.getNeighbour().get(neighbour))) {
                        possibleMove = true;
                    }
                }
            }
            if (!possibleMove) {
                province.setFit(province.getFit() - 300);
            }
        }
        if (!provinces.isEmpty()) {
            System.out.println("Recruitment1:");
            Collections.sort(provinces);
            for (Province province : provinces) {
                System.out.printf("  %s: %4.2fl, Rice: %d, Gold: %d,\n", province.getName(), province.getFit(), province.getRice(), province.getGold());
            }
            for (Province province : provinces) {
                if (province.getFit() >= 10 && province.getArmies() >= 2) {
                    if (provincesToGold.size() > 1 || !provincesToGold.contains(province)) {
                        province.setAction(Shogun.getAction("Recruitment1"));
                        actions.remove(Shogun.getAction("Recruitment1"));
                        provinces.remove(province);
                        if (provincesToGold.contains(province)) {
                            provincesToGold.remove(province);
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean setCastleAction() {
        for (Province province : provinces) {
            if (province.getDanger() > 0) {
                province.setFit(-province.getDanger());
            } else {
                province.setFit(5);
            }
            for (Place place : province.getBuildings()) {
                if (place.getType().equals(Building.CASTLE)) {
                    province.setFit(province.getFit() - 100);
                }
            }
            boolean hasPlase = false;
            for (Place place : province.getBuildings()) {
                if (place.getType().equals(Building.EMPTY)) {
                    hasPlase = true;
                    break;
                }
            }
            if (!hasPlase) {
                province.setFit(province.getFit() - 100);
            }
        }
        if (!provinces.isEmpty()) {
            System.out.println("Castle:");
            Collections.sort(provinces);
            for (Province province : provinces) {
                System.out.printf("  %s: %4.2fl, Rice: %d, Gold: %d,\n", province.getName(), province.getFit(), province.getRice(), province.getGold());
            }
            for (Province province : provinces) {
                if (provincesToGold.size() > 1 || !provincesToGold.contains(province) && province.getFit() > -50) {
                    province.setAction(Shogun.getAction("Castle"));
                    actions.remove(Shogun.getAction("Castle"));
                    provinces.remove(province);
                    if (provincesToGold.contains(province)) {
                        provincesToGold.remove(province);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private boolean setTempleAction() {
        for (Province province : provinces) {
            if (province.getDanger() > 0) {
                province.setFit(-province.getDanger());
            } else {
                province.setFit(5);
            }
            for (Place place : province.getBuildings()) {
                if (place.getType().equals(Building.TEMPLE)) {
                    province.setFit(province.getFit() - 100);
                }
            }
            boolean hasPlase = false;
            for (Place place : province.getBuildings()) {
                if (place.getType().equals(Building.EMPTY)) {
                    hasPlase = true;
                    break;
                }
            }
            if (!hasPlase) {
                province.setFit(province.getFit() - 100);
            }
        }
        if (!provinces.isEmpty()) {
            System.out.println("Temple:");
            Collections.sort(provinces);
            for (Province province : provinces) {
                System.out.printf("  %s: %4.2fl, Rice: %d, Gold: %d,\n", province.getName(), province.getFit(), province.getRice(), province.getGold());
            }
            for (Province province : provinces) {
                if (provincesToGold.size() > 1 || !provincesToGold.contains(province) && province.getFit() > -50) {
                    province.setAction(Shogun.getAction("Temple"));
                    actions.remove(Shogun.getAction("Temple"));
                    provinces.remove(province);
                    if (provincesToGold.contains(province)) {
                        provincesToGold.remove(province);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private boolean setTheatreAction() {
        for (Province province : provinces) {
            if (province.getDanger() > 0) {
                province.setFit(-province.getDanger());
            } else {
                province.setFit(5);
            }
            for (Place place : province.getBuildings()) {
                if (place.getType().equals(Building.THEATRE)) {
                    province.setFit(province.getFit() - 100);
                }
            }
            boolean hasPlase = false;
            for (Place place : province.getBuildings()) {
                if (place.getType().equals(Building.EMPTY)) {
                    hasPlase = true;
                    break;
                }
            }
            if (!hasPlase) {
                province.setFit(province.getFit() - 100);
            }
        }
        if (!provinces.isEmpty()) {
            System.out.println("Theatre:");
            Collections.sort(provinces);
            for (Province province : provinces) {
                System.out.printf("  %s: %4.2fl, Rice: %d, Gold: %d,\n", province.getName(), province.getFit(), province.getRice(), province.getGold());
            }
            for (Province province : provinces) {
                if (provincesToGold.size() > 1 || !provincesToGold.contains(province) && province.getFit() > -50) {
                    province.setAction(Shogun.getAction("Theatre"));
                    actions.remove(Shogun.getAction("Theatre"));
                    provinces.remove(province);
                    if (provincesToGold.contains(province)) {
                        provincesToGold.remove(province);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private boolean setMoveAction(String name) {
        for (Province province : provinces) {
            province.setFit(-3 * province.getDanger() + province.getArmies());
            if (isBack(province)) {
                province.setFit(province.getFit() + max(province.getArmies() - 2, 0));
            }
        }
        if (!provinces.isEmpty()) {
            System.out.printf("%s:\n", name);
            Collections.sort(provinces);
            for (Province province : provinces) {
                System.out.printf("  %s: %4.2fl, Rice: %d, Gold: %d,\n", province.getName(), province.getFit(), province.getRice(), province.getGold());
            }
            for (Province province : provinces) {
                if (province.getArmies() >= 3 && province.getDanger() <= new Random().nextInt(4) + 1) {
                    if (provincesToGold.size() > 1 || !provincesToGold.contains(province)) {
                        province.setAction(Shogun.getAction(name));
                        actions.remove(Shogun.getAction(name));
                        provinces.remove(province);
                        if (provincesToGold.contains(province)) {
                            provincesToGold.remove(province);
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void setRestActions() {
        Collections.shuffle(actions);
        Collections.shuffle(chests);
        Action toAdd = null;
        while (!actions.isEmpty()) {
            if (!actions.get(0).getName().equals("Gold") || provincesToGold.isEmpty()) {
                if (!setAction(actions.get(0).getName())) {
                    if (!chests.isEmpty()) {
                        chests.get(0).setAction(actions.get(0));
                        chests.remove(0);
                    }
                    actions.remove(0);
                }
            } else {
                toAdd = actions.get(0);
                actions.remove(0);
            }
        }
        if (toAdd != null) {
            actions.add(toAdd);
        }
        if (daimyo.getAction(Shogun.actionBid.getName()) == null && !chests.isEmpty()) {
            chests.get(0).setAction(Shogun.actionBid);
            chests.remove(0);
        }
    }

    boolean isBack(Province province) {
        for (String neighbour : province.getNeighbour().keySet()) {
            if (province.getNeighbour().get(neighbour).getOwner() != null && !province.getNeighbour().get(neighbour).getOwner().equals(province.getOwner()) && province.getArmies() < province.getNeighbour().get(neighbour).getArmies() + 3) {
                return false;
            }
        }
        return true;
    }

    boolean isSafe(Province province) {
        for (String neighbour : province.getNeighbour().keySet()) {
            if (province.getNeighbour().get(neighbour).getOwner() != null && !province.getNeighbour().get(neighbour).getOwner().equals(province.getOwner()) && province.getArmies() < province.getNeighbour().get(neighbour).getArmies()) {
                return false;
            }
        }
        return true;
    }

    private Province chooseProvince(List<Province> provinces) {
        double FitnessSum = 0, actFit = 0;
        Collections.sort(provinces);
        for (int i = 0; i < 3 && i < provinces.size(); i++) {
            if (provinces.get(i).getFit() > 0) {
                FitnessSum += provinces.get(i).getFit() * provinces.get(i).getFit();
            }
        }
        double generated = new Random().nextDouble() * FitnessSum;
        for (int i = 0; i < 3 && i < provinces.size(); i++) {
            if (provinces.get(i).getFit() > 0) {
                if (actFit + provinces.get(i).getFit() * provinces.get(i).getFit() > generated) {
                    return provinces.get(i);
                }
                actFit += provinces.get(i).getFit() * provinces.get(i).getFit();
            }
        }
        return null;
    }

    private boolean setAction(String name) {
        switch (name) {
            case "Castle":
                return setCastleAction();
            case "Temple":
                return setTempleAction();
            case "Theatre":
                return setTheatreAction();
            case "Rice":
                return setRiceAction();
            case "Gold":
                return setGoldAction();
            case "Recruitment5":
                return setRecruitment35Action(name);
            case "Recruitment3":
                return setRecruitment35Action(name);
            case "Recruitment1":
                return setRecruitment1Action();
            case "MoveA":
                return setMoveAction(name);
            case "MoveB":
                return setMoveAction(name);
            default:
                return false;
        }
    }

    private int distance(Province province0, Province province1) {
        Map<Province, Integer> distance = new HashMap<>();
        List<Province> queue = new ArrayList<>();
        Province actual;
        distance.put(province0, 0);
        queue.add(province0);
        while (!queue.isEmpty()) {
            actual = queue.get(0);
            queue.remove(0);
            for (String province : actual.getNeighbour().keySet()) {
                if (!distance.containsKey(actual.getNeighbour().get(province))) {
                    distance.put(actual.getNeighbour().get(province), distance.get(actual) + 1);
                    queue.add(actual.getNeighbour().get(province));
                }
            }
        }
        if (distance.containsKey(province1)) {
            return distance.get(province1);
        } else {
            System.err.println("Podano nieistniejącą prowincję!");
            return Integer.MAX_VALUE;
        }
    }
}
