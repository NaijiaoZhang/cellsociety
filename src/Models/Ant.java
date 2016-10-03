package Models;
import CellContent.ForagingAntsContent;

public class Ant {
    private boolean hasFood;
    private int orientation; //N,NE,E,SE,S,SW,W,NW = 0,1,2,3,4,5,6,7
    private int antLife;
    private boolean moved;
    
    public Ant(int a){
        hasFood = false;
        orientation = 0;
        antLife = a;
        moved = false;
    }
    
    public boolean hasFoodItem(){
        return hasFood;
    }
    
    public void hasFoodItem(boolean food){
        hasFood = food;
    }
    
    public void setOrientation(int o){
        orientation = o;
    }
    
    public int getOrientation(){
        return orientation;
    }
    
    public int getLifeLeft(){
        return antLife;
    }
    
    public void setLifeLeft(int a){
        antLife = a;
    }
    
    public void decreaseLifeLeft(){
        antLife--;
    }
    public boolean hasMoved(){
        return moved;
    }
    public void setMoved(boolean b){
        moved = b;
    }
}


