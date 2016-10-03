package CellularAutomata;

import Cell.Cell;
import Cell.HexagonalCell;
import Cell.SquareCell;
import Cell.TriangleCell;
import CellContent.CellContent;
import CellContent.SegregationContent;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.paint.Color;
import java.util.*;

public class SegregationRules extends CellularAutomata{
    private int similar;
    private Color colorA; 
    private Color free; 
    private Color colorB; 
    private List<SegregationContent> cellStart;
    private int steps, freePopulationSize; 
    
    public SegregationRules (int rows,int columns,int wid,int hei,int empty, int sim,int redProb, String tile, String edge, Color color1, Color color2, Color emptyColor,String dir) {
        height=hei;
        width=wid;
        rowCount=rows;
        colCount=columns;
        similar = sim;
        tileType=tile;
        edgeType=edge;
        colorA=color1;
        colorB=color2;
        free=emptyColor;
        probEmpty=empty;
        probType=redProb;
        neighborDirection=dir;
        
        useProbType=true;
        useProbEmpty=true;
        steps = 0;
        freePopulationSize = 0;
       
        cellStart=setupCell();
        placeCells();
    }
    
    @Override
    public void update () {
    	steps++;
        List<Cell> moveFrom = new ArrayList<Cell>();
        List<Cell> moveTo = new ArrayList<Cell>();
        for (int i = 0; i < grid.keySet().size(); i++){
                int sim = simNeighbors(grid.get(i));
                if(grid.get(i).getContent().getState() != 0 && sim < similar){
                    moveFrom.add(grid.get(i));
                }else if(grid.get(i).getContent().getState() == 0){
                    moveTo.add(grid.get(i)); 
                }
        }
        if(moveFrom.isEmpty() || moveTo.isEmpty()){
            return;
        }
        swapToOpenSpace(moveFrom,moveTo);
    }
    
    public int getSimilar(){
        return similar;
    }
    
    public void setSimilar(int s){
        similar=s;
    }
    
    @Override
    public void reset(){
        cellStart=setupCell();
        placeCells();
    }
    
    private void swapToOpenSpace(List<Cell> moveFrom, List<Cell> moveTo){
        Random r = new Random();
        int count = 0;
        for (Cell c : moveFrom){
            if(moveTo.isEmpty()){
                return;
            }
            int randNum = r.nextInt(moveTo.size());
            moveTo.get(randNum).getContent().setStateandColor(c.getContent().getState(),c.getContent().getColor());
            c.getContent().setStateandColor(0,free);
            count++;
            moveTo.remove(randNum);
            moveTo.add(c);
        }
        freePopulationSize = count; 
    }
    

    
    private List<SegregationContent> setupCell(){
        int emptyCount = probEmpty*rowCount*colCount/100;
        int redProbCount = probType*(rowCount*colCount -emptyCount)/100;
        List<SegregationContent> cells = new ArrayList<SegregationContent>();

        for(int i = 0; i < rowCount*colCount; i++){
            if(i<emptyCount){
                cells.add(new SegregationContent(0,free));
                freePopulationSize++;
            }else if(i < emptyCount + redProbCount){
                cells.add(new SegregationContent(1,colorA));
            }else{
                cells.add(new SegregationContent(2,colorB));
            }
        }
        return cells;
    }
    

    @Override
    public void createSquareGrid(){
        int w = width/colCount;
        int h = height/rowCount;

        Random r = new Random();
        grid = new TreeMap<Integer,Cell>();
        for (int i = 0; i < rowCount; i++){
            for(int j = 0; j<colCount; j++){
                int k = i + j*rowCount;
                int randNum = r.nextInt(cellStart.size());
                SquareCell c = new SquareCell(i*w,j*h,i,j,cellStart.get(randNum));
                grid.put(k, c);
                cellStart.remove(randNum);
            }
        }
    }
    
    @Override
    public void createTriangleGrid () {
        int w = width/colCount;
        int h = height/rowCount;

        Random r = new Random();
        grid = new TreeMap<Integer,Cell>();
        for (int i = 0; i < rowCount; i++){
            for(int j = 0; j<colCount; j++){
                int k = i + j*rowCount;
                int randNum = r.nextInt(cellStart.size());
                SegregationContent c = cellStart.get(randNum);
                grid.put(k, new TriangleCell(i*w,j*h,w,h,i,j,c));
                cellStart.remove(randNum);
            }
        }
        
    }

    @Override
    public void createHexagonalGrid () {
        double side = 0;
        if(rowCount%2==0&&colCount%2==0){
                side = height/(rowCount)/Math.sqrt(3);
        }
        Random r = new Random();
        grid = new TreeMap<Integer,Cell>();
        for (int i = 0; i < rowCount; i++){
            for(int j = 0; j<colCount; j++){
                int k = i + j*rowCount;
                int randNum = r.nextInt(cellStart.size());
                SegregationContent c = cellStart.get(randNum);
                grid.put(k, new HexagonalCell(i,j,side,c));
                cellStart.remove(randNum);
            }
        }        
    }
    
    private int simNeighbors(Cell cell){
        int sim = 0;
        int dif = 0;
        List<Cell> neighbors = checkNeighbors(cell,false);
        for(Cell c : neighbors){
            if(c.getContent().getState() == cell.getContent().getState()){
                sim++;
            }else if(c.getContent().getState() != 0){
                dif++;
            }
        }
        if(sim==0 && dif==0){
            return 0;
        }
        return sim*100/(sim+dif);
    }

	@Override
	public void graphStats(Series<Number, Number> graphLocation) {
		// TODO Auto-generated method stub
		graphLocation.getData().add(new XYChart.Data(steps,freePopulationSize));	
	}

}
