package CellularAutomata;

import java.util.*;
import Cell.Cell;
import Cell.HexagonalCell;
import Cell.SquareCell;
import Cell.TriangleCell;
import CellContent.ForagingAntsContent;
import Models.Ant;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.paint.Color;


public class ForagingAntsRules extends CellularAutomata {
    private Color empty;
    private Color home;
    private Color food;
    private int foodX;
    private int foodY;
    private int homeX;
    private int homeY;
    private int maxAnts;
    private int maxAntsPerCell;
    private int antLife;
    private int initialAnts;
    private int antsSpawned;
    private int maxPheromone;
    private int n;
    private int antsTotal;
    private double k;
    private double evap;
    private double diffuse;
    private int neighborCount;
    private int steps;

    public ForagingAntsRules (int rows,
                              int columns,
                              int wid,
                              int hei,
                              int fX,
                              int fY,
                              int hX,
                              int hY,
                              int maxA,
                              int maxAPC,
                              int aLife,
                              int initAnts,
                              int aSpawn,
                              int maxPh,
                              int nVal,
                              int kVal,
                              int ev,
                              int diff,
                              String tile,
                              String edge,                              
                              Color emptyColor,
                              Color foodColor,
                              Color homeColor,
                              String dir) {
        height = hei;
        width = wid;
        rowCount = rows;
        colCount = columns;
        tileType = tile;
        useProbType = false;
        useProbEmpty = false;
        neighborDirection=dir;
        empty=emptyColor;
        food=foodColor;
        home=homeColor;
        foodX = fX;
        foodY = fY;
        homeX = hX;
        homeY = hY;
        maxAnts = maxA;
        maxAntsPerCell = maxAPC;
        antLife = aLife;
        initialAnts = initAnts;
        antsSpawned = aSpawn;
        maxPheromone = maxPh;
        n = nVal;
        k = 1.0*kVal/1000000;
        evap = 1.0*ev/1000;
        diffuse = 1.0*diff/1000;
        antsTotal = 0;
        edgeType = edge;
        neighborCount = 8;
        if(tileType.equals("hexagonal")){
            neighborCount = 6;
        }
        steps = 0;
        placeCells();
    }

    @Override
    public void update () {
        steps++;
        for (int i = 0; i < grid.keySet().size(); i++) {
            Cell cell = grid.get(i);
            if (!((ForagingAntsContent) cell.getContent()).getIsHome() &&
                !((ForagingAntsContent) cell.getContent()).getIsFoodSource()) {
                updatePheromones(cell);
            }else if(((ForagingAntsContent) cell.getContent()).getIsHome()){
                ((ForagingAntsContent) cell.getContent()).setHomePheromone(maxPheromone);
            }else{
                ((ForagingAntsContent) cell.getContent()).setFoodPheromone(maxPheromone);
            }
            for(Ant a : ((ForagingAntsContent) cell.getContent()).getAnts()){
                a.setMoved(false);
            }
        }
        Map<Integer, Cell> gridClone = cloneGrid(grid);
        for (int i = 0; i < gridClone.keySet().size(); i++) {
            Cell cell = grid.get(i);
            ForagingAntsContent fac = (ForagingAntsContent) cell.getContent();
            if (!fac.getAnts().isEmpty()) {
                fac.setColor(Color.color(1 - (1.0 * fac.getAnts().size()) / maxAntsPerCell,
                                         1 - (1.0 * fac.getAnts().size()) / maxAntsPerCell,
                                         1));
            }
            else {
                fac.setColor(Color.color(1 - (1.0 * fac.getHomePheromone()) / maxPheromone,
                                         1 - (1.0 * fac.getFoodPheromone()) / maxPheromone,
                                         1 - Math.max((1.0 * fac.getHomePheromone()) /
                                                      maxPheromone, (1.0 * fac.getFoodPheromone()) /
                                                                    maxPheromone)));
            }
        }
        for (int i = 0; i < gridClone.keySet().size(); i++) {
            Cell cell = grid.get(i);
            ForagingAntsContent fac = (ForagingAntsContent) cell.getContent();
            List<Ant> removeAnts = new ArrayList<Ant>();
            for (Ant a : fac.getAnts()) {
                if (a.getLifeLeft() <= 0) {
                    removeAnts.add(a);
                    antsTotal--;
                    a.decreaseLifeLeft();
                }
                else if (!a.hasMoved() && a.hasFoodItem() && antReturntoNest(grid.get(i), a) != null) {
                        removeAnts.add(a);
                        a.decreaseLifeLeft();
                }
                else if(!a.hasMoved() && antFindFoodSource(grid.get(i), a) != null) {
                        removeAnts.add(a);
                        a.decreaseLifeLeft();
                }
                a.setMoved(true);
            }
            List<Ant> newAnts = new ArrayList<Ant>();
            for (Ant a : fac.getAnts()) {
                if (!removeAnts.contains(a)) {
                    newAnts.add(a);
                }
            }
            ((ForagingAntsContent) fac).setAnts(newAnts);
            if (fac.getIsHome() && fac.getAnts().size() < maxAntsPerCell - antsSpawned &&
                antsSpawned + antsTotal <= maxAnts) {
                List<Ant> ants = fac.getAnts();
                for (int s = 0; s < antsSpawned; s++) {
                    Ant ant1 = new Ant(antLife);
                    ants.add(ant1);
                }
                antsTotal += antsSpawned;
                fac.setAnts(ants);
            }
        }
        // System.out.println(antsTotal);
    }

    public void updatePheromones (Cell cell) {
        ForagingAntsContent facCell = (ForagingAntsContent) cell.getContent();
        List<Cell> neighbors = checkNeighbors(cell, true);
        Cell h = maxPheromones(neighbors, true);
        Cell f = maxPheromones(neighbors, false);
        if (h != null) {
            ForagingAntsContent maxHome = (ForagingAntsContent) h.getContent();
            // System.out.println(maxHome.getHomePheromone());
            facCell.setHomePheromone(Math
                    .min(facCell.getHomePheromone() + maxHome.getHomePheromone() * diffuse,
                         maxPheromone));
        }
        if (f != null) {
            ForagingAntsContent maxFood = (ForagingAntsContent) f.getContent();
            facCell.setFoodPheromone(Math
                    .min(facCell.getFoodPheromone() + maxFood.getFoodPheromone() * diffuse,
                         maxPheromone));
        }
        facCell.setFoodPheromone(Math
                .max(facCell.getFoodPheromone() - facCell.getFoodPheromone() * evap, 0));
        facCell.setHomePheromone(Math
                .max(facCell.getHomePheromone() - facCell.getHomePheromone() * evap, 0));
    }

    private Ant antReturntoNest (Cell cell, Ant ant) {
        List<Cell> neighbors = checkNeighbors(cell, true);
        Cell maxX = maxPheromones(neighbors, ant.hasFoodItem());
        if (maxX != null && ((ForagingAntsContent) cell.getContent()).getIsFoodSource()) {
            ant.setOrientation(findOrientation(maxX, cell));
        }
        Cell x = maxPheromones(forwardCells(ant.getOrientation(), neighbors), ant.hasFoodItem());
        if (x == null) {
            x = maxX;
        }
        if (x != null) {
            dropFoodPheromones(x, neighbors);
            ant.setOrientation(findOrientation(x, cell));
            Ant a = moveAnt(ant, (ForagingAntsContent) x.getContent());
            if (((ForagingAntsContent) x.getContent()).getIsHome()) {
                ant.hasFoodItem(false);
            }
            return a;
        }
        return null;
    }

    private Ant antFindFoodSource (Cell cell, Ant ant) {
        List<Cell> neighbors = checkNeighbors(cell, true);
        Cell maxX = maxPheromones(neighbors, ant.hasFoodItem());
        if (maxX != null && ((ForagingAntsContent) cell.getContent()).getIsHome()) {
            ant.setOrientation(findOrientation(maxX, cell));
        }
        Cell x = selectLocation(forwardCells(ant.getOrientation(), neighbors));
        if (x == null) {
            x = selectLocation(neighbors);
        }
        if (x != null) {
            dropHomePheromones(x, neighbors);
            //System.out.println(findOrientation(x, cell));
            ant.setOrientation(findOrientation(x, cell));
            Ant a = moveAnt(ant, (ForagingAntsContent) x.getContent());
            if (((ForagingAntsContent) x.getContent()).getIsFoodSource()) {
                ant.hasFoodItem(true);
            }
            return a;
        }
        return null;
    }

    private int findOrientation (Cell neighbor, Cell cell) {
        int[] a = { 1, 1, 0, -1, -1, -1, 0, 1 };
        int[] b = { 0, 1, 1, 1, 0, -1, -1, -1 };
        for (int z = 0; z < 8; z++) {
            if (neighbor.getRow() == cell.getRow() + a[z] &&
                neighbor.getCol() == cell.getCol() + b[z]) {
                return z;
            }
        }
        return 0;
    }

    private Cell maxPheromones (List<Cell> cells, boolean hasFood) {
        double m = 0;
        Cell cell = null;
        Collections.shuffle(cells);
        for (Cell c : cells) {
            if (c != null && ((ForagingAntsContent) c.getContent()).getPheromone(hasFood) >= m &&
                ((ForagingAntsContent) c.getContent()).getAnts().size() < maxAntsPerCell) {
                m = ((ForagingAntsContent) c.getContent()).getPheromone(hasFood);
                cell = c;
            }
        }
        return cell;
    }

    private List<Cell> forwardCells (int n, List<Cell> neighbors) {
        List<Cell> cells = new ArrayList<Cell>();
        for (int i = n; i < n + 3; i++) {
            cells.add(neighbors.get(i % neighborCount));
        }
        return cells;
    }

    private void dropHomePheromones (Cell cell, List<Cell> neighbors) {
        ForagingAntsContent fac = ((ForagingAntsContent) cell.getContent());
        if (fac.getIsHome()) {
            fac.setMaxPheromone(true);
        }
        else {
            double des =
                    ((ForagingAntsContent) maxPheromones(neighbors, true).getContent())
                            .getHomePheromone() -
                         2;
            if (des > fac.getHomePheromone()) {
                fac.setHomePheromone(Math.min(des, maxPheromone));
            }
        }
    }

    private void dropFoodPheromones (Cell cell, List<Cell> neighbors) {
        ForagingAntsContent fac = ((ForagingAntsContent) cell.getContent());
        if (fac.getIsFoodSource()) {
            fac.setMaxPheromone(false);
        }
        else {
            double des =
                    ((ForagingAntsContent) maxPheromones(neighbors, false).getContent())
                            .getFoodPheromone() -
                         2;
            if (des > fac.getFoodPheromone()) {
                fac.setFoodPheromone(Math.min(des, maxPheromone));
            }
        }
    }

    private Cell selectLocation (List<Cell> cells) {
        List<Cell> locs = new ArrayList<Cell>();
        for (Cell c : cells) {
            if (c != null &&
                ((ForagingAntsContent) c.getContent()).getAnts().size() < maxAntsPerCell) {
                locs.add(c);
            }
        }
        if (locs.isEmpty()) {
            return null;
        }
        List<Double> probs = setProbs(locs);
        Random r = new Random();
        double probsSum = 0;
        for (int j = 0; j < probs.size(); j++) {
            probsSum += probs.get(j);
        }
        double d = probsSum * r.nextDouble();
        //System.out.println("This is it " + d);
        for (int j = 0; j < probs.size(); j++) {
            if (d < probs.get(j)) {
                return locs.get(j);
            }
        }
        return locs.get(0);

    }

    private List<Double> setProbs (List<Cell> locs) {
        List<Double> probs = new ArrayList<Double>();
        //System.out.println("locs size " + locs.size());
        double sumd = 0;
        for (Cell c : locs) {
            sumd += Math.pow(k + ((ForagingAntsContent) c.getContent()).getFoodPheromone(), n);
            probs.add(sumd);
            //System.out.println(Math.pow(k + ((ForagingAntsContent) c.getContent()).getFoodPheromone(), n));
        }
        return probs;
    }

    private Ant moveAnt (Ant ant, ForagingAntsContent to) {
        if (to.getAnts().size() == maxAntsPerCell) {
            return null;
        }
        List<Ant> antsTo = to.getAnts();
        antsTo.add(ant);
        to.setAnts(antsTo);
        return ant;
    }

    @Override
    public void createSquareGrid () {
        int w = width / colCount;
        int h = height / rowCount;
        grid = new TreeMap<Integer, Cell>();
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                int k = i + rowCount * j;
                if (i == homeX && j == homeY) {
                    grid.put(k,
                             new SquareCell(i * w, j * h, i, j,
                                            new ForagingAntsContent(0, home, true, false,
                                                                    maxPheromone, 0, maxPheromone,
                                                                    new ArrayList<Ant>())));
                    ((ForagingAntsContent) grid.get(k).getContent()).setHomePheromone(maxPheromone);
                    List<Ant> initAnts = new ArrayList<Ant>();
                    for (int a = 0; a < initialAnts; a++) {
                        initAnts.add(new Ant(antLife));
                    }
                    ((ForagingAntsContent) grid.get(k).getContent()).setAnts(initAnts);
                }
                else if (i == foodX && j == foodY) {
                    grid.put(k,
                             new SquareCell(i * w, j * h, i, j,
                                            new ForagingAntsContent(0, food, false, true,
                                                                    maxPheromone, maxPheromone, 0,
                                                                    new ArrayList<Ant>())));
                    ((ForagingAntsContent) grid.get(k).getContent()).setFoodPheromone(maxPheromone);
                }
                else {
                    grid.put(k,
                             new SquareCell(i * w, j * h, i,
                                            j, new ForagingAntsContent(0, empty, false,
                                                                       false, maxPheromone, 0, 0,
                                                                       new ArrayList<Ant>())));
                }
            }
        }
    }

    @Override
    public void createTriangleGrid () {
        grid = new TreeMap<Integer, Cell>();
        int w = width / colCount;
        int h = height / rowCount;
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                int k = i + rowCount * j;
                if (i == homeX && j == homeY) {
                    grid.put(k,
                             new TriangleCell(i * w, j * h, w, h, i, j,
                                              new ForagingAntsContent(0, home, true, false,
                                                                      maxPheromone, 0, maxPheromone,
                                                                      new ArrayList<Ant>())));
                    ((ForagingAntsContent) grid.get(k).getContent()).setHomePheromone(maxPheromone);
                }
                else if (i == foodX && j == foodY) {
                    grid.put(k,
                             new TriangleCell(i * w, j * h, w, h, i, j,
                                              new ForagingAntsContent(0, food, false, true,
                                                                      maxPheromone, maxPheromone, 0,
                                                                      new ArrayList<Ant>())));
                    ((ForagingAntsContent) grid.get(k).getContent()).setFoodPheromone(maxPheromone);
                }
                else {
                    grid.put(k,
                             new TriangleCell(i * w, j * h, w, h, i,
                                              j, new ForagingAntsContent(0, empty, false,
                                                                         false, maxPheromone, 0, 0,
                                                                         new ArrayList<Ant>())));
                }
            }
        }
    }

    @Override
    public void createHexagonalGrid () {
        // TODO Auto-generated method stub
        grid = new TreeMap<Integer, Cell>();
        double side = 0;
        if (rowCount % 2 == 0 && colCount % 2 == 0) {
            side = height / (rowCount) / Math.sqrt(3);
        }
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                int k = i + rowCount * j;
                if (i == homeX && j == homeY) {
                    grid.put(k,
                             new HexagonalCell(i, j, side,
                                               new ForagingAntsContent(0, home, true, false,
                                                                       maxPheromone, 0,
                                                                       maxPheromone,
                                                                       new ArrayList<Ant>())));
                    ((ForagingAntsContent) grid.get(k).getContent()).setHomePheromone(maxPheromone);
                }
                else if (i == foodX && j == foodY) {
                    grid.put(k,
                             new HexagonalCell(i, j, side,
                                               new ForagingAntsContent(0, food, false, true,
                                                                       maxPheromone, maxPheromone,
                                                                       0, new ArrayList<Ant>())));
                    ((ForagingAntsContent) grid.get(k).getContent()).setFoodPheromone(maxPheromone);
                }
                else {
                    grid.put(k,
                             new HexagonalCell(i, j, side,
                                               new ForagingAntsContent(0, empty, false, false,
                                                                       maxPheromone, 0, 0,
                                                                       new ArrayList<Ant>())));
                }
            }
        }
    }

    @Override
    public void graphStats (Series<Number, Number> graphLocation) {
        // TODO Auto-generated method stub
        graphLocation.getData().add(new XYChart.Data(steps,antsTotal));
        
    }

}