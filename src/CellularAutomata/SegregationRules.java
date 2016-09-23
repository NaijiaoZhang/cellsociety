package CellularAutomata;

import java.util.Random;
import com.sun.prism.paint.Color;
import Cell.Cell;
import Cell.StateColor;
import java.util.*;

public class SegregationRules extends CellularAutomata{
    private Cell[][] grid;
    private int similar;
    
    public SegregationRules (int rows,int columns,int redProb,int width,int height,int empty, int sim) {
        List<Cell> cellStart = new ArrayList<Cell>();
        int w = width/columns;
        int h = height/rows;
        empty = empty*rows*columns/100;
        redProb = redProb*(rows*columns -empty)/100;
        for(int i = 0; i < rows*columns; i++){
            if(i<empty){
                cellStart.add(new Cell(new StateColor(0,Color.WHITE)));
            }else if(i < empty + redProb){
                cellStart.add(new Cell(new StateColor(1,Color.RED)));
            }else{
                cellStart.add(new Cell(new StateColor(2,Color.BLUE)));
            }
        }
        Random r = new Random();
        grid = getGrid();
        similar = sim;
        for (int i = 0; i < grid.length; i++){
            for(int j = 0; j<grid[0].length; j++){
                int randNum = r.nextInt(cellStart.size());
                Cell c = cellStart.get(randNum);
                c.setRowandCol(i, j);
                c.setXandY(i*w, j*h);
                grid[i][j] = c;
                cellStart.remove(randNum);
            }
        }
        setGrid(grid);
    }

    @Override
    public void update () {
        
    }

}
