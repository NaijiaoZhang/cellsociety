package Cell;

import javafx.scene.paint.Color;

public class Cell {
    private StateColor stateColor;
    private int xPosition,yPosition;
    private int row;
    private int col;
    
    public Cell (StateColor sc, int x, int y, int r, int c) {
        setStateandColor(sc);
        xPosition = x;
        yPosition = y;
        row = r;
        col = c;
    }
    
    public Cell(StateColor sc){
        this(sc,0,0,0,0);
    }
    
    public void setStateandColor(int i, Color c){
        stateColor.setStateandColor(i, c);        
    }
    
    public void setStateandColor(StateColor sc){
        stateColor = sc;        
    }
    
    public StateColor getStateandColor(){
        return stateColor;
    }
    
    public int getX(){
        return xPosition;
    }
    
    public int getY(){
        return yPosition;
    }

    public int getState(){
        return stateColor.getState();
    }
    
    public Color getColor(){
        return stateColor.getColor();
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