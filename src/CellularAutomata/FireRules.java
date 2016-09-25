package CellularAutomata;
import Cell.Cell;

import java.util.*;
import javafx.scene.paint.Color;

public class FireRules extends CellularAutomata{
    private int probCatchesFire;
    private final Color burning = Color.RED; 
    private final Color burnt = Color.YELLOW; 
    private final Color burnable = Color.GREEN; 
    
   public FireRules (int rows, int columns, int wid,int hei, int probCatch) {
       height=hei;
       width=wid;
       rowCount=rows;
       colCount=columns;
        int w = width/columns;
        int h = height/rows;
        probCatchesFire = probCatch;
        grid = new Cell[rows][columns];
        for (int i = 0; i < rows; i++){
            for(int j = 0; j<columns; j++){
                if(j == 0 || j == columns - 1 || i == 0 || i == rows - 1){
                    grid[i][j] = new Cell(0,burnt,i*w,j*h,i,j);
                }else if(i == rows/2 && j == columns/2){
                    grid[i][j] = new Cell(2,burning,i*w,j*h,i,j);
                }else{
                    grid[i][j] = new Cell(1,burnable,i*w,j*h,i,j);
                }
            }
        }
        startGrid=cloneGrid(grid);
    }

    @Override
    public void update () {
        Cell[][] gridClone = cloneGrid(grid);
        Random r = new Random();
        for (int i = 0; i < grid.length; i++){
            for(int j = 0; j<grid[0].length; j++){
                if(grid[i][j].getState() == 2){
                    gridClone[i][j].setStateandColor(0,burnt);
                }else if(grid[i][j].getState() == 1 && fireNeighbors(grid[i][j]) > 0 && r.nextInt(100) < probCatchesFire){
                    gridClone[i][j].setStateandColor(2,burning);
                }
            }
        }
        grid = gridClone;
    }
    
    private int fireNeighbors(Cell cell){
        int fire = 0;
        List<Cell> neighbors = checkNeighborsCardinal(cell);
        for(Cell c : neighbors){
            if(c.getState() == 2){
                fire++;
            }
        }
        return fire;
    }

}
