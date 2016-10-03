package CellularAutomata;

import java.util.*;
import Cell.Cell;
import Cell.HexagonalCell;
import Cell.SquareCell;
import Cell.TriangleCell;
import CellContent.PredatorPreyContent;
import CellContent.SegregationContent;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.paint.Color;


public class PredatorPreyRules extends CellularAutomata {
    private int sharkReproduce, fishReproduce, maxsE;

    private Color shark;
    private Color free;
    private Color fish;
    private List<PredatorPreyContent> cellStart;
    private int steps,fishPopulationSize,sharkPopulationSize; 

    public PredatorPreyRules (int rows,int columns,int wid,int hei,int sE,int sR, int fR, int empty, int sharkProb, String tile, String edge, Color sharkColor, Color fishColor, Color waterColor, String dir) {
        height=hei;
        width=wid;
        rowCount=rows;
        colCount=columns;
        sharkReproduce = sR;
        fishReproduce = fR;
        maxsE=sE;
        tileType=tile;
        edgeType=edge;
        shark=sharkColor;
        fish=fishColor;
        free=waterColor;
        neighborDirection=dir;
        
        probEmpty=empty;
        probType=sharkProb;
        useProbType=true;
        useProbEmpty=true;
        steps = 0;
        fishPopulationSize=0;
        sharkPopulationSize=0;
        
        cellStart=setupCell();
        placeCells();
    }

    @Override
    public void update () {
    	steps++;
    	resetMovement(); 
    	moveAllFish();
    	moveAllSharks();    	
    }    
    
    @Override
    public void reset () {
        cellStart = setupCell();
        placeCells();
    }

    public List<PredatorPreyContent> setupCell () {
        int emptyCount = probEmpty * rowCount * colCount / 100;
        int sharkProbCount = probType * (rowCount * colCount - emptyCount) / 100;

        List<PredatorPreyContent> cells = new ArrayList<PredatorPreyContent>();

        for(int i = 0; i < rowCount*colCount; i++){
            if(i<emptyCount){
                cells.add(new PredatorPreyContent(0,free,-1));
            }else if(i < emptyCount + sharkProbCount){
                cells.add(new PredatorPreyContent(1,shark,maxsE));
                sharkPopulationSize++;
            }else{      
                cells.add(new PredatorPreyContent(2,fish,-1));
                fishPopulationSize++;
            }
        }
        return cells;
    }

    public void createSquareGrid () {
        int w = width / colCount;
        int h = height / rowCount;
        Random r = new Random();
        grid = new TreeMap<Integer, Cell>();
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                int randNum = r.nextInt(cellStart.size());
                SquareCell c = new SquareCell(i * w, j * h, i, j, cellStart.get(randNum));
                grid.put(i + j * rowCount, c);
                cellStart.remove(randNum);
            }
        }

    }

    public void createTriangleGrid () {
        int w = width / colCount;
        int h = height / rowCount;
        Random r = new Random();
        grid = new TreeMap<Integer, Cell>();
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                int k = i + j * rowCount;
                int randNum = r.nextInt(cellStart.size());
                PredatorPreyContent c = cellStart.get(randNum);
                grid.put(k, new TriangleCell(i * w, j * h, w, h, i, j, c));
                cellStart.remove(randNum);
            }
        }
    }

    public void createHexagonalGrid () {
        double side = 0;
        if (rowCount % 2 == 0 && colCount % 2 == 0) {
            side = height / (rowCount) / Math.sqrt(3);
        }
        Random r = new Random();
        grid = new TreeMap<Integer, Cell>();
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                int k = i + j * rowCount;
                int randNum = r.nextInt(cellStart.size());
                PredatorPreyContent c = cellStart.get(randNum);
                grid.put(k, new HexagonalCell(i, j, side, c));
                cellStart.remove(randNum);
            }
        }
    }
    
    public int getSharkReproduce(){
        return sharkReproduce;
    }
    
    public void setSharkReproduce(int sR){
        sharkReproduce=sR;
    }
    
    public int getFishReproduce(){
        return fishReproduce;
    }
    
    public void setFishReproduce(int fR){
        fishReproduce=fR;
    }
    
    public int getSharkEnergy(){
        return maxsE;
    }
    
    public void setSharkEnergy(int sE){
        maxsE=sE;
    }

    private void moveAllFish () {
        for (int i = 0; i < grid.keySet().size(); i++) {
            if (grid.get(i).getContent().getState() == 2 &&
                !(((PredatorPreyContent) grid.get(i).getContent()).getHasMoved())) {
                moveFish(grid.get(i));
            }
        }
    }

    private void moveAllSharks () {
        for (int i = 0; i < grid.keySet().size(); i++) {
            if (grid.get(i).getContent().getState() == 1 &&
                !(((PredatorPreyContent) grid.get(i).getContent()).getHasMoved())) {
                moveSharks(grid.get(i));
            }

        }
    }

    private void resetMovement () {
        for (int i = 0; i < grid.keySet().size(); i++) {
            if (grid.get(i).getContent().getState() != 0) {
                ((PredatorPreyContent) grid.get(i).getContent()).setHasMoved(false);
            }

        }
    }
    private void moveFish(Cell cell){
    	((PredatorPreyContent)cell.getContent()).setHasMoved(true);
    	List<Cell>neighbors = checkNeighbors(cell,false);
    	List<Cell>empty = new ArrayList<>();
    	for(int i=0;i<neighbors.size();i++){
    		if(neighbors.get(i).getContent().getState()==0){
    			empty.add(neighbors.get(i));
    		}
    	}
    	if(empty.size()==0){
    	((PredatorPreyContent)cell.getContent()).moved();
    		return;
    	}
    	else{
    	    moveOrBreed(cell,empty);
    	}
    }

    private void moveOrBreed (Cell cell, List<Cell> empty) {
        Random r = new Random();
        Cell selected = empty.get(r.nextInt(empty.size())); 

        if(((PredatorPreyContent)cell.getContent()).getMoveNum()<fishReproduce){
                grid.put(cell.getRow()+cell.getCol()*rowCount,decideShape(cell,0,free,-1));
                ((PredatorPreyContent)grid.get(cell.getRow()+cell.getCol()*rowCount).getContent()).setHasMoved(true);
                grid.put(selected.getRow()+selected.getCol()*rowCount,cell); 
                cell.setRowandCol(selected.getRow(), selected.getCol());
                cell.setXandY(selected.getX(), selected.getY());
                ((PredatorPreyContent)cell.getContent()).moved();
        }
        else if(((PredatorPreyContent)cell.getContent()).getMoveNum()>=fishReproduce){
                ((PredatorPreyContent)cell.getContent()).setMoveNum(0);
                grid.put(cell.getRow()+cell.getCol()*rowCount,decideShape(cell,2,fish,-1));
                fishPopulationSize++;
                grid.put(selected.getRow()+selected.getCol()*rowCount,cell);
                cell.setRowandCol(selected.getRow(), selected.getCol());
                cell.setXandY(selected.getX(), selected.getY());
        }
    }
    
    private void moveSharks(Cell cell){
        if(((PredatorPreyContent)cell.getContent()).getSharkEnergy()<=0){
            grid.put(cell.getRow()+cell.getCol()*rowCount,decideShape(cell,0,free,-1));
            sharkPopulationSize--;
            return;
        }
        ((PredatorPreyContent)cell.getContent()).setHasMoved(true);
        ((PredatorPreyContent)cell.getContent()).decreaseEnergy();
        ((PredatorPreyContent)cell.getContent()).moved();
            
        List<Cell>neighbors = checkNeighbors(cell,false);
        List<Cell>empty = new ArrayList<>();
        List<Cell>fish = new ArrayList<>();
        for(int i=0;i<neighbors.size();i++){
            if(neighbors.get(i).getContent().getState()==0){
                empty.add(neighbors.get(i));
            }
            else if (neighbors.get(i).getContent().getState() == 2) {
                fish.add(neighbors.get(i));
            }
        }

        Random r = new Random();
        if (fish.isEmpty() && !empty.isEmpty()) {// moving
            int randempty = r.nextInt(empty.size());
            Cell selected = empty.get(randempty);
            empty.remove(randempty);
            grid.put(cell.getRow()+cell.getCol()*rowCount,decideShape(cell,0,free,-1));
            empty.add(grid.get(cell.getRow()+cell.getCol()*rowCount));
            grid.put(selected.getRow()+selected.getCol()*rowCount,cell);
            cell.setRowandCol(selected.getRow(), selected.getCol());
            cell.setXandY(selected.getX(), selected.getY());
        }
        else if(!fish.isEmpty()){//eating
        	fishPopulationSize--;
            int randfish=r.nextInt(fish.size());
            Cell eaten=fish.get(randfish);
            grid.put(eaten.getRow()+eaten.getCol()*rowCount,decideShape(eaten,0,free,-1));
            empty.add(grid.get(eaten.getRow()+eaten.getCol()*rowCount));
            ((PredatorPreyContent)cell.getContent()).ate();
        }
        
        //reproduction
        if(((PredatorPreyContent)cell.getContent()).getMoveNum()>=sharkReproduce && !empty.isEmpty()){
            sharkPopulationSize++;
            int randempty=r.nextInt(empty.size());
            Cell selected=neighbors.get(randempty);
            ((PredatorPreyContent)cell.getContent()).setMoveNum(0);
            grid.put(cell.getRow()+cell.getCol()*rowCount,decideShape(cell,1,shark,maxsE));
            ((PredatorPreyContent)grid.get(cell.getRow()+cell.getCol()*rowCount).getContent()).setHasMoved(true);
            grid.put(selected.getRow()+selected.getCol()*rowCount,cell);
            cell.setRowandCol(selected.getRow(), selected.getCol());
            cell.setXandY(selected.getX(), selected.getY());
        }
    }

    private Cell decideShape (Cell cell, int state, Color col, int sharkE) {
        if (cell instanceof SquareCell) {
            return new SquareCell(cell.getX(), cell.getY(), cell.getRow(), cell.getCol(),
                                  new PredatorPreyContent(state, col, sharkE));
        }
        else if (cell instanceof TriangleCell) {
            return new TriangleCell(cell.getX(), cell.getY(), width / rowCount, height / colCount,
                                    cell.getRow(), cell.getCol(),
                                    new PredatorPreyContent(state, col, sharkE));
        }
        else {
            // dont have to do this every time refactor somehow
            double side = 0;
            if (rowCount % 2 == 0 && colCount % 2 == 0) {
                side = height / (rowCount) / Math.sqrt(3);
            }
            return new HexagonalCell(cell.getRow(), cell.getCol(), side,
                                     new PredatorPreyContent(state, col, sharkE));
        }
    }
	@Override
	public void graphStats(Series<Number, Number> graphLocation) {
		// TODO Auto-generated method stub
		graphLocation.getData().add(new XYChart.Data(steps,fishPopulationSize));	
	}
	
        public void graphSharkStats(Series<Number, Number> graphLocation) {
            // TODO Auto-generated method stub
            graphLocation.getData().add(new XYChart.Data(steps,sharkPopulationSize));        
    }
}
