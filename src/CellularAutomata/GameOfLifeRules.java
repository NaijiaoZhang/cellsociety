package CellularAutomata;
import Cell.Cell;
import java.util.*;
import Cell.StateColor;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class GameOfLifeRules extends CellularAutomata{
    private final Color death = Color.BLACK; 
    private final Color alive = Color.WHITE; 
    
    private Cell[][] grid;
    private int spawnNum;
    
    public GameOfLifeRules (int rows,int columns,int width,int height,int aliveProb, int spawn) {
        int w = width/columns;
        int h = height/rows;
        Random r = new Random();
        grid = getGrid();
        spawnNum = spawn;
        for (int i = 0; i < grid.length; i++){
            for(int j = 0; j<grid[0].length; j++){
                int randNum = r.nextInt(100);
                if(randNum < aliveProb){
                    grid[i][j] = new Cell(new StateColor(1,Color.BLACK),i*w,j*h,i,j);
                }else{
                    grid[i][j] = new Cell(new StateColor(0,Color.WHITE),i*w,j*h,i,j);
                }
            }
        }
        setGrid(grid);
    }

    @Override
    public void update () {
        grid = getGrid();
        for (int i = 0; i < grid.length; i++){
            for(int j = 0; j<grid[0].length; j++){
                int live = liveNeighbors(grid[i][j]);
                if(live == spawnNum && grid[i][j].getState() == 0){
                    grid[i][j].setStateandColor(1, alive);
                }
                if(live > spawnNum && grid[i][j].getState() == 1){
                    grid[i][j].setStateandColor(0, death);
                }
                if(live < spawnNum-1 && grid[i][j].getState() == 1){
                    grid[i][j].setStateandColor(0, death);
                }
            }
        }
        setGrid(grid);
    }
    
    private int liveNeighbors(Cell cell){
        int live = 0;
        List<Cell> neighbors = checkNeighborsDiagonal(cell);
        for(Cell c : neighbors){
            if(c.getState() == 1){
                live++;
            }
        }
        return live;
    }

}
