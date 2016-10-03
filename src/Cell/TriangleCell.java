package Cell;

import CellContent.CellContent;
import javafx.scene.paint.Color;

public class TriangleCell extends Cell{

    private double[]xCoordinates; 
    private double[]yCoordinates;
    private int width;
    private int height;
    
    public TriangleCell(int x, int y, int wid, int hei, int r, int c, CellContent cellContent){
            super(x,y,r,c,cellContent);
            width=wid;
            height=hei;
            createBasedOnPosition();
    }
    
    public void createBasedOnPosition(){
        xCoordinates = new double[3];
        yCoordinates = new double[3];
        if((row+col)%2==0){
            //downward triangles
                xCoordinates[0]=xPosition/2;
                yCoordinates[0]=yPosition; 
                xCoordinates[1]=xPosition/2+width; 
                yCoordinates[1]=yPosition;
                xCoordinates[2]=xPosition/2+width/2;
                yCoordinates[2]=yPosition+height;
        }
        else if((row+col)%2==1){
            //upwards triangles
                xCoordinates[0]=xPosition/2;
                yCoordinates[0]=yPosition+height; 
                xCoordinates[1]=xPosition/2+width/2; 
                yCoordinates[1]=yPosition;
                xCoordinates[2]=xPosition/2+width;
                yCoordinates[2]=yPosition+height;
        }
    }
    
    public double[] getXCoordinates(){
        return xCoordinates; 
    }
    
    public double[] getYCoordinates(){
            return yCoordinates;
    }
    
    public int getWidth(){
        return width;
    }
    
    public int getHeight(){
        return height;
    }
    
    @Override
    public void setXandY(int x,int y){
        xPosition = x;
        yPosition = y;
        createBasedOnPosition();
    }
}
