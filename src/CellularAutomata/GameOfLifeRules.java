package CellularAutomata;
import Cell.Cell;
import Cell.HexagonalCell;
import Cell.SquareCell;
import Cell.TriangleCell;
import CellContent.GameOfLifeContent;

import java.util.*;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class GameOfLifeRules extends CellularAutomata{
    public static final int SPAWN=3;
    private Color death; 
    private Color alive; 
    private int steps,populationSize; 
        
    public GameOfLifeRules (int rows,int columns,int wid,int hei,int aliveProb, String tile, String edge,Color deathColor,Color aliveColor, String dir){
        tileType = tile; 
        edgeType=edge;
    	height=hei;
        width=wid;
        rowCount=rows;
        colCount=columns;
        probType=aliveProb;
        death=deathColor;
        alive=aliveColor;
        probEmpty=0;
        neighborDirection=dir;
        useProbType=true;
        useProbEmpty=false;
        placeCells();
    }

    @Override
    public void update () {
    	steps++;
        Map<Integer,Cell> gridClone = cloneGrid(grid);
        for (int i = 0; i < grid.keySet().size(); i++){
                int live = liveNeighbors(grid.get(i));
                if(live == SPAWN && grid.get(i).getContent().getState() == 0){
                    gridClone.get(i).getContent().setStateandColor(1, alive);
                }
                if(live > SPAWN && grid.get(i).getContent().getState() == 1){
                    gridClone.get(i).getContent().setStateandColor(0, death);
                }
                if(live < SPAWN-1 && grid.get(i).getContent().getState() == 1){
                    gridClone.get(i).getContent().setStateandColor(0, death);
            }
        }
        updatePopulationSize(); 
        grid = gridClone;       
    }
    
    /* (non-Javadoc)
     * Override as rerandomize for game of life
     * @see CellularAutomata.CellularAutomata#reset()
     */
    
    public void createHexagonalGrid(){
        Random r = new Random();
        grid = new TreeMap<Integer,Cell>();
        double side = 0;
    	if(rowCount%2==0&&colCount%2==0){
    		side = height/(rowCount)/Math.sqrt(3);
    	}
    	for(int i=0;i<rowCount;i++){
    		for(int j=0;j<colCount;j++){
    		    int k = i + j * rowCount;
                    int randNum = r.nextInt(100);
                    if(randNum < probType){
    			grid.put(k, new HexagonalCell(i,j,side,new GameOfLifeContent(0,death)));
                    }else{
                        grid.put(k, new HexagonalCell(i,j,side, new GameOfLifeContent(1,alive)));
                        populationSize++;
                    }
    		}
    	}
    }
    
    public void createSquareGrid(){
    	Random r = new Random();
        grid = new TreeMap<Integer,Cell>();
        int w = width/colCount;
        int h = height/rowCount;
        for (int i = 0; i < rowCount; i++){
            for(int j = 0; j<colCount; j++){
                int k = i + j * rowCount;
                int randNum = r.nextInt(100);
                if(randNum < probType){
                    grid.put(k, new SquareCell(i*w,j*h,i,j,new GameOfLifeContent(0,death)));
                }else{
                    grid.put(k, new SquareCell(i*w,j*h,i,j,new GameOfLifeContent(1,alive)));
                    populationSize++;
                }
            }
        }
    }
    
    public void createTriangleGrid(){
        Random r = new Random();
        grid = new TreeMap<Integer,Cell>();
        int w = width/colCount;
        int h = height/rowCount;
        for (int i = 0; i < rowCount; i++){
            for(int j = 0; j<colCount; j++){
                int k = i + j * rowCount;
                int randNum = r.nextInt(100);
                if(randNum < probType){
                    grid.put(k, new TriangleCell(i*w,j*h,w,h,i,j,new GameOfLifeContent(0,death)));
                }else{
                    grid.put(k, new TriangleCell(i*w,j*h,w,h,i,j,new GameOfLifeContent(1,alive)));
                }
            }
        }
    }
    
    private void updatePopulationSize(){
    	int count = 0; 
    	for (int i = 0; i < grid.keySet().size(); i++){
             if(grid.get(i).getContent().getState() == 1){
                 count++;
             }
    	}
    	populationSize = count;
    }
    
    private int liveNeighbors(Cell cell){
        int live = 0;
        List<Cell> neighbors = checkNeighbors(cell,false);
        for(Cell c : neighbors){
            if(c.getContent().getState() == 1){
                live++;
            }
        }
        return live;
    }

	@Override
	public void graphStats(Series<Number, Number> graphLocation) {
		// TODO Auto-generated method stub
		graphLocation.getData().add(new XYChart.Data(steps,populationSize));
	}
}
