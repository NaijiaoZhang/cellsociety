package CellularAutomata;

import java.util.Random;
import Cell.Cell;
import javafx.scene.paint.Color;
import java.util.*;

public class SegregationRules extends CellularAutomata{
    private int similar;
    private final Color red = Color.RED; 
    private final Color free = Color.WHITE; 
    private final Color blue = Color.BLUE; 
    
    public SegregationRules (int rows,int columns,int wid,int hei,int empty, int sim,int redProb) {
        height=hei;
        width=wid;
        rowCount=rows;
        colCount=columns;
        List<Cell> cellStart = new ArrayList<Cell>();
        int w = width/columns;
        int h = height/rows;
        empty = empty*rows*columns/100;
        redProb = redProb*(rows*columns -empty)/100;
        for(int i = 0; i < rows*columns; i++){
            if(i<empty){
                cellStart.add(new Cell(0,free));
            }else if(i < empty + redProb){
                cellStart.add(new Cell(1,red));
            }else{
                cellStart.add(new Cell(2,blue));
            }
        }
        Random r = new Random();
        similar = sim;
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
        List<Cell> moveFrom = new ArrayList<Cell>();
        List<Cell> moveTo = new ArrayList<Cell>();
        for (int i = 0; i < grid.length; i++){
            for(int j = 0; j<grid[0].length; j++){
                int sim = simNeighbors(grid[i][j]);
                if(grid[i][j].getState() != 0 && sim < similar){
                    moveFrom.add(grid[i][j]);
                }else if(grid[i][j].getState() == 0){
                    moveTo.add(grid[i][j]);                
                }
            }
        }
        if(moveFrom.isEmpty()){
            return;
        }
        Random r = new Random();
        for (Cell c : moveFrom){
            if(moveTo.isEmpty()){
                return;
            }
            int randNum = r.nextInt(moveTo.size());
            moveTo.get(randNum).setStateandColor(c.getState(),c.getColor());
            c.setStateandColor(0,free);
            moveTo.remove(randNum);
        }
    }
    
    private int simNeighbors(Cell cell){
        int sim = 0;
        int dif = 0;
        List<Cell> neighbors = checkNeighborsAll(cell);
        for(Cell c : neighbors){
            if(c.getState() == cell.getState()){
                sim++;
            }else if(c.getState() != 0){
                dif++;
            }
        }
        if(sim==0 && dif==0){
            return 0;
        }
        return sim*100/(sim+dif);
    }

}
