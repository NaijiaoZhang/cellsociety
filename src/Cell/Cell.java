package Cell;

import javafx.scene.paint.Color;

public class Cell {
    protected int xPosition,yPosition;
    protected int row;
    protected int col;
    protected int state;
    protected Color color;
    
    public Cell (int i, Color clr, int x, int y, int r, int c) {
        state = i;
        color = clr;
        xPosition = x;
        yPosition = y;
        row = r;
        col = c;
    }
    
    public Cell(int i,Color c){
        this(i,c,0,0,0,0);
    }
    
    public void setStateandColor(int i, Color c){
        state = i;
        color = c;
    }
    
    public int getX(){
        return xPosition;
    }
    
    public int getY(){
        return yPosition;
    }
    
    public int getState(){
        return state;
    }
    
    public void setState(int i){
        state = i;
    }
    
    public void increaseState(int i){
        state += i;
    }
    
    public void increaseState(){
        increaseState(1);
    }
    
    public Color getColor(){
        return color;
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