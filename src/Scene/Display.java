package Scene;

import Cell.Cell;
import java.util.*;
import Cell.HexagonalCell;
import Cell.TriangleCell;
import CellularAutomata.CellularAutomata;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Display {
    private CellularAutomata myAutomata;
    private Canvas myCanvas;
    private GraphicsContext gc;
    private int gridHeight, gridWidth, cellRows, cellCols, cellWidth, cellHeight;
    private boolean strokeBool;

    public Display (CellularAutomata rules,boolean stroke, Color strokeColor) {
        myAutomata = rules;
        strokeBool=stroke;
        setGridParameters();

        if (myAutomata.returntileType().equals("square")) {
            myCanvas = new Canvas(gridWidth, gridHeight);

        }
        else if (myAutomata.returntileType().equals("triangle")) {
            myCanvas =
                    new Canvas(gridWidth / 2 + myAutomata.getWidth() / (2 * myAutomata.getCol()),
                               gridHeight);
        }
        else if (myAutomata.returntileType().equals("hexagonal")) {
            myCanvas =
                    new Canvas(Math.sqrt(3) * gridWidth * (1 + 1 / (double) myAutomata.getCol()),
                               gridHeight + myAutomata.getHeight() / (myAutomata.getRows() / 2) /
                                            Math.sqrt(3));
        }
        gc = myCanvas.getGraphicsContext2D();
        gc.setStroke(strokeColor);

        show();
    }

    public Canvas getCanvas () {
        return myCanvas;
    }

    // Moves one step forward in simulation
    public void update () {
        myAutomata.update();
        show();
    }  
    
    //draws grid on canvas
    private void show(){
        Cell cell = myAutomata.getGrid().get(0);
        if(cell instanceof HexagonalCell){
                displayHexagonalTiles(); 
        }
        else if(cell instanceof TriangleCell){
                displayTriangleTiles(); 
        }
        else if(cell instanceof Cell){
                displaySquareTiles(); 
        }
        //possibly throw error? if not maybe make the above elseif an else
    }

    // draws grid on canvas


    // reset grid
    public void reset () {
        myAutomata.reset();
        show();
    }

    private void displayHexagonalTiles () {
        Map<Integer,Cell> grid=myAutomata.getGrid();
        for(int i=0;i<grid.keySet().size();i++){
                Cell tempCell=grid.get(i);
                gc.setFill(tempCell.getContent().getColor());
                if(strokeBool){
                    gc.strokePolygon(((HexagonalCell)grid.get(i)).getXCoordinates(), ((HexagonalCell)grid.get(i)).getYCoordinates(), 6);
                }
                gc.fillPolygon(((HexagonalCell)grid.get(i)).getXCoordinates(), ((HexagonalCell)grid.get(i)).getYCoordinates(), 6);
                           
                }

    }

    private void displayTriangleTiles () {
        Map<Integer,Cell> grid=myAutomata.getGrid();
        for(int i=0;i<grid.keySet().size();i++){
            Cell tempCell=grid.get(i);
            gc.setFill(tempCell.getContent().getColor());
            if(strokeBool){
                gc.strokePolygon(((TriangleCell)grid.get(i)).getXCoordinates(), ((TriangleCell)grid.get(i)).getYCoordinates(), 3);
            }
            gc.fillPolygon(((TriangleCell)grid.get(i)).getXCoordinates(), ((TriangleCell)grid.get(i)).getYCoordinates(), 3);
                            
            }
    }
    
    private void displaySquareTiles(){
        Map<Integer,Cell> grid=myAutomata.getGrid();
        for(int i = 0; i < grid.keySet().size(); i++){
            Cell tempCell=grid.get(i);
            gc.setFill(tempCell.getContent().getColor());
            if(strokeBool){
                gc.strokeRect(tempCell.getX(),tempCell.getY(),cellWidth,cellHeight);
            }
            gc.fillRect(tempCell.getX(),tempCell.getY(),cellWidth,cellHeight);
        }
    }
    
    //sets initialization state and variables of grid display
    private void setGridParameters(){
        cellRows=myAutomata.getRows();
        cellCols=myAutomata.getCol();
        gridHeight=myAutomata.getHeight();
        gridWidth=myAutomata.getWidth();
        cellWidth=gridWidth/cellCols;
        cellHeight=gridHeight/cellRows;
    }
}
