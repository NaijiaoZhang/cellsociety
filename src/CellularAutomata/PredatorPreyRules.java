package CellularAutomata;

import java.util.*;
import Cell.Cell;
import Cell.PredatorPreyCell;
import javafx.scene.paint.Color;


public class PredatorPreyRules extends CellularAutomata {
    private int sharkReproduce; 
    private int fishReproduce;
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
       
        List<Cell> cellStart = new ArrayList<Cell>();
        int w = width/columns;
        int h = height/rows;
        empty = empty*rows*columns/100;
        sharkProb = sharkProb*(rows*columns -empty)/100;
        for(int i = 0; i < rows*columns; i++){
            if(i<empty){
                cellStart.add(new PredatorPreyCell(0,free,sE));
            }else if(i < empty + sharkProb){
                cellStart.add(new PredatorPreyCell(1,shark,sE));
            }else{	
                cellStart.add(new PredatorPreyCell(2,fish,sE));
            }
        }
        
        Random r = new Random();
        grid = new Cell[rows][columns];
        for (int i = 0; i < rows; i++){
            for(int j = 0; j<columns; j++){
                int randNum = r.nextInt(cellStart.size());
                Cell c = cellStart.get(randNum);
                c.setRowandCol(i, j);
                c.setXandY(i*w, j*h);
                grid[i][j] = c;
                cellStart.remove(randNum);
            }
        }
        startGrid=cloneGrid(grid);
    }

    @Override
    public void update () {
    	resetMovement(); 
    	moveAllFish();
    	
    }
    	
    	
        //Cell[][] gridClone = cloneGrid(grid);

//        for (int i = 0; i < grid.length; i++){
//            for(int j = 0; j<grid[0].length; j++){
//                Cell[] fishAndFree = adjFish(grid[i][j]);
//                if(fishAndFree[1] != null &&  (grid[i][j].getState() != sharkEnergy || 
//                                               grid[i][j].getState() != 0 || 
//                                               grid[i][j].getState() != sharkEnergy + fishReproduce )){
//                    grid[i][j].setStateandColor(0,free);
//                    grid[fishAndFree[1].getRow()][fishAndFree[1].getCol()].setStateandColor(grid[i][j].getState(),grid[i][j].getColor());
//                }else if(grid[i][j].getState() == sharkEnergy){
//                    grid[i][j].setStateandColor(0,free);
//                }else if(grid[i][j].getState() == sharkEnergy+fishReproduce && fishAndFree[1] != null){
//                    grid[i][j].setStateandColor(sharkEnergy+1,fish);
//                    grid[fishAndFree[1].getRow()][fishAndFree[1].getCol()].setStateandColor(sharkEnergy+1,fish);
//                }else if(fishAndFree[0] != null && grid[i][j].getState() <= sharkEnergy && grid[i][j].getState() != 0){
//                    grid[fishAndFree[0].getRow()][fishAndFree[0].getCol()].setStateandColor(0,free);
//                }else if(grid[i][j].getState() != 0){
//                    grid[i][j].increaseState();
//                }
//            }
//        }
    

    private void moveAllFish(){
    	for(int i=0;i<grid.length;i++){
    		for(int j=0;j<grid[0].length;j++){
    			if(grid[i][j].getState()==2&&!((PredatorPreyCell)grid[i][j]).getHasMoved()){
    				moveFish(grid[i][j]);
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
    
//    private Cell[] adjFish(Cell cell){
//        List<Cell> neighbors = checkNeighborsCardinal(cell);
//        Random r = new Random();
//        Cell[] fishAndFree = new Cell[2];
//        List<Cell> freeSpace = new ArrayList<Cell>();
//        for(Cell c : neighbors){
//            if(c.getState() > sharkEnergy){
//                fishAndFree[0] = c;
//            }else if(c.getState() == 0){
//                freeSpace.add(c);
//            }
//        }
//        if(!freeSpace.isEmpty()){
//            fishAndFree[1] = freeSpace.get(r.nextInt(freeSpace.size()));
//        }
//        return fishAndFree;
//    }
}
