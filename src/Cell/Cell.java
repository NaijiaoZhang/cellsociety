package Cell;

import CellContent.CellContent;
import CellContent.GameOfLifeContent;
import CellContent.PredatorPreyContent;
import javafx.scene.paint.Color;

public abstract class Cell {
    protected int xPosition,yPosition;
    protected int row;
    protected int col;
    protected CellContent cellContent; 
   
    public Cell (int x, int y, int r, int c, CellContent insideCell) {
        xPosition = x;
        yPosition = y;
        row = r;
        col = c;
        cellContent = insideCell ;
    }
    
    public CellContent getContent(){
    	return cellContent;
    }
  
    public int getX(){
        return xPosition;
    }
    
    public int getY(){
        return yPosition;
    }
    
    public int getRow(){
        return row;
    }
    
    public int getCol(){
        return col;
    }

    public void setXandY(int x,int y){
        xPosition = x;
        yPosition = y;
    }
    
    public void setRowandCol(int r,int c){
        row = r;
        col = c;
    }
    
}