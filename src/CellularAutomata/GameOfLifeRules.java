package CellularAutomata;
import Cell.Cell;
import java.util.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class GameOfLifeRules extends CellularAutomata{
    public static final int SPAWN=3;
    private final Color death = Color.BLACK; 
    private final Color alive = Color.WHITE; 
    private int probAlive;
        
    public GameOfLifeRules (int rows,int columns,int wid,int hei,int aliveProb){
        height=hei;
        width=wid;
        rowCount=rows;
        colCount=columns;
        probAlive=aliveProb;
        Randomize();
        startGrid=cloneGrid(grid);
    }

    @Override
    public void update () {
        Cell[][] gridClone = cloneGrid(grid);
        for (int i = 0; i < grid.length; i++){
            for(int j = 0; j<grid[0].length; j++){
                int live = liveNeighbors(grid[i][j]);
                if(live == SPAWN && grid[i][j].getState() == 0){
                    gridClone[i][j].setStateandColor(1, alive);
                }
                if(live > SPAWN && grid[i][j].getState() == 1){
                    gridClone[i][j].setStateandColor(0, death);
                }
                if(live < SPAWN-1 && grid[i][j].getState() == 1){
                    gridClone[i][j].setStateandColor(0, death);
                }
            }
        }
        grid = cloneGrid(gridClone);
       
    }
    
    
    /* (non-Javadoc)
     * Override as rerandomize for game of life
     * @see CellularAutomata.CellularAutomata#reset()
     */
    @Override
    public void reset(){
        Randomize();
    }
    
    private void Randomize(){
        Random r = new Random();
        grid = new Cell[rowCount][colCount];
        int w = width/colCount;
        int h = height/rowCount;
        for (int i = 0; i < grid.length; i++){
            for(int j = 0; j<grid[0].length; j++){
                int randNum = r.nextInt(100);
                if(randNum < probAlive){
                    grid[i][j] = new Cell(0,death,i*w,j*h,i,j);
                }else{
                    grid[i][j] = new Cell(1,alive,i*w,j*h,i,j);
                }
            }
        }
    }
    
    private int liveNeighbors(Cell cell){
        int live = 0;
        List<Cell> neighbors = checkNeighborsAll(cell);
        for(Cell c : neighbors){
            if(c.getState() == 1){
                live++;
            }
        }
        return live;
    }

}
