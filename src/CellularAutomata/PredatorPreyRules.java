package CellularAutomata;

import java.util.*;
import Cell.Cell;
import Cell.PredatorPreyCell;
import javafx.scene.paint.Color;


public class PredatorPreyRules extends CellularAutomata {
    private int sharkReproduce, fishReproduce, maxsE, emptyCount, sharkProbCount;
    private final Color shark = Color.RED; 
    private final Color free = Color.BLUE; 
    private final Color fish = Color.LIGHTGREEN; 

    public PredatorPreyRules (int rows,int columns,int wid,int hei,int sE,int sR, int fR, int empty, int sharkProb) {
        height=hei;
        width=wid;
        rowCount=rows;
        colCount=columns;
        sharkReproduce = sR;
        fishReproduce = fR;
        maxsE=sE;
        
        emptyCount = empty*rowCount*colCount/100;
        sharkProbCount = sharkProb*(rowCount*colCount-emptyCount)/100;
        
        placeCells();
        startGrid=cloneGrid(grid);
    }

    @Override
    public void update () {
    	resetMovement(); 
    	moveAllFish();
    	moveAllSharks();    	
    }    
    
    @Override
    public void reset(){
        placeCells();
    }
    
    private void placeCells(){
        List<Cell> cellStart = new ArrayList<Cell>();
        int w = width/colCount;
        int h = height/rowCount;

        for(int i = 0; i < rowCount*colCount; i++){
            if(i<emptyCount){
                cellStart.add(new PredatorPreyCell(0,free,maxsE));
            }else if(i < emptyCount + sharkProbCount){
                cellStart.add(new PredatorPreyCell(1,shark,maxsE));
            }else{      
                cellStart.add(new PredatorPreyCell(2,fish,maxsE));
            }
        }
        
        Random r = new Random();
        grid = new Cell[rowCount][colCount];
        for (int i = 0; i < rowCount; i++){
            for(int j = 0; j<colCount; j++){
                int randNum = r.nextInt(cellStart.size());
                Cell c = cellStart.get(randNum);
                c.setRowandCol(i, j);
                c.setXandY(i*w, j*h);
                grid[i][j] = c;
                cellStart.remove(randNum);
            }
        }
    }

    private void moveAllFish(){
    	for(int i=0;i<grid.length;i++){
    		for(int j=0;j<grid[0].length;j++){
    			if(grid[i][j].getState()==2&&!((PredatorPreyCell)grid[i][j]).getHasMoved()){
    				moveFish(grid[i][j]);
    			}
    		}
    	}
    }
    
    private void moveAllSharks(){
        for(int i=0;i<grid.length;i++){
            for(int j=0;j<grid[0].length;j++){
                    if(grid[i][j].getState()==1&&!((PredatorPreyCell)grid[i][j]).getHasMoved()){
                            moveSharks(grid[i][j]);
                    }
            }
        }
    }    
    
    private void resetMovement(){
    	for(int i=0;i<grid.length;i++){
    		for(int j=0;j<grid[0].length;j++){
    			if(grid[i][j].getState()==1||grid[i][j].getState()==2){
    				((PredatorPreyCell)grid[i][j]).setHasMoved(false);
    			}
    		}
    	}
    }
    
    private void moveFish(Cell cell){
    	((PredatorPreyCell)cell).setHasMoved(true);
    	List<Cell>neighbors = checkNeighborsCardinal(cell);
    	List<Cell>empty = new ArrayList<>();
    	for(int i=0;i<neighbors.size();i++){
    		if(neighbors.get(i).getState()==0){
    			empty.add(neighbors.get(i));
    		}
    	}
    	if(empty.size()==0){
    		((PredatorPreyCell)cell).moved();
    		return;
    	}
    	else{
    		Random r = new Random();
    		Cell selected = empty.get(r.nextInt(empty.size()));
    		if(((PredatorPreyCell)cell).getMoveNum()<fishReproduce){
    			grid[cell.getRow()][cell.getCol()]=new PredatorPreyCell(0,free,cell.getX(),cell.getY(),cell.getRow(),cell.getCol(),-1);
    			((PredatorPreyCell)(grid[cell.getRow()][cell.getCol()])).setHasMoved(true);
    			grid[selected.getRow()][selected.getCol()]=cell; 
    			cell.setXandY(selected.getX(), selected.getY());
    			cell.setRowandCol(selected.getRow(), selected.getCol());
    			((PredatorPreyCell)cell).moved(); 
    		}
    		else if(((PredatorPreyCell)cell).getMoveNum()>=fishReproduce){
    			((PredatorPreyCell)cell).setMoveNum(0);
    			grid[cell.getRow()][cell.getCol()]=new PredatorPreyCell(2,fish,cell.getX(),cell.getY(),cell.getRow(),cell.getCol(),-1);
    			grid[selected.getRow()][selected.getCol()]=cell; 
    			cell.setXandY(selected.getX(), selected.getY());
    			cell.setRowandCol(selected.getRow(), selected.getCol());
    		}
    	}
    }
    
    private void moveSharks(Cell cell){
        //death
        if(((PredatorPreyCell)cell).getSharkEnergy()<=0){
            grid[cell.getRow()][cell.getCol()]=new PredatorPreyCell(0,free,cell.getX(),cell.getY(),cell.getRow(),cell.getCol(),-1);
            return;
        }
        
        ((PredatorPreyCell)cell).setHasMoved(true);
        ((PredatorPreyCell)cell).decreaseEnergy();
        ((PredatorPreyCell)cell).moved();
            
        List<Cell>neighbors = checkNeighborsCardinal(cell);
        List<Cell>empty = new ArrayList<>();
        List<Cell>fish = new ArrayList<>();
        for(int i=0;i<neighbors.size();i++){
            if(neighbors.get(i).getState()==0){
                empty.add(neighbors.get(i));
            }
            else if(neighbors.get(i).getState()==2){
                fish.add(neighbors.get(i));
            }
        }        

        Random r=new Random();          
        if(fish.isEmpty() && !empty.isEmpty()){//moving
            int randempty=r.nextInt(empty.size());
            Cell selected=empty.get(randempty);
            empty.remove(randempty);
            grid[cell.getRow()][cell.getCol()]=new PredatorPreyCell(0,free,cell.getX(),cell.getY(),cell.getRow(),cell.getCol(),-1);
            empty.add(grid[cell.getRow()][cell.getCol()]);
            grid[selected.getRow()][selected.getCol()]=cell; 
            cell.setXandY(selected.getX(), selected.getY());
            cell.setRowandCol(selected.getRow(), selected.getCol());
        }
        else if(!fish.isEmpty()){//eating
            int randfish=r.nextInt(fish.size());
            Cell eaten=fish.get(randfish);
            grid[eaten.getRow()][eaten.getCol()]=new PredatorPreyCell(0,free,eaten.getX(),eaten.getY(),eaten.getRow(),eaten.getCol(),-1);
            empty.add(grid[eaten.getRow()][eaten.getCol()]);
            ((PredatorPreyCell)cell).ate();
        }
        
        //reproduction
        if(((PredatorPreyCell)cell).getMoveNum()>=sharkReproduce && !empty.isEmpty()){
            int randempty=r.nextInt(empty.size());
            Cell selected=neighbors.get(randempty);
            ((PredatorPreyCell)cell).setMoveNum(0);
            grid[cell.getRow()][cell.getCol()]=new PredatorPreyCell(1,shark,cell.getX(),cell.getY(),cell.getRow(),cell.getCol(),maxsE);
            ((PredatorPreyCell)(grid[cell.getRow()][cell.getCol()])).setHasMoved(true);
            grid[selected.getRow()][selected.getCol()]=cell; 
            cell.setXandY(selected.getX(), selected.getY());
            cell.setRowandCol(selected.getRow(), selected.getCol());
        }
    }
}
