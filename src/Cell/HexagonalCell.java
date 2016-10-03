package Cell;

import CellContent.CellContent;
import javafx.scene.paint.Color;

public class HexagonalCell extends Cell{

	private double[]xCoordinates; 
	private double[]yCoordinates;
	private double sideLength; 
	
	public HexagonalCell(int r, int col, double side, CellContent cellContent){
	    super(-1,-1,r,col,cellContent);
	           sideLength = side; 
		createBasedOnPosition();
	}
	
	public void createBasedOnPosition(){
		xCoordinates = new double[6];
		yCoordinates = new double[6];
		if(row%2==0){
			yCoordinates[0]=Math.sqrt(3)*sideLength/2+row/2*Math.sqrt(3)*sideLength;
			xCoordinates[0]=col*3*sideLength; 
			yCoordinates[1]=row/2*Math.sqrt(3)*sideLength; 
			xCoordinates[1]=0.5*sideLength+col*3*sideLength;
			yCoordinates[2]=row/2*Math.sqrt(3)*sideLength;
			xCoordinates[2]=1.5*sideLength+col*3*sideLength; 
			yCoordinates[3]=Math.sqrt(3)*sideLength/2+row/2*Math.sqrt(3)*sideLength;
			xCoordinates[3]=2*sideLength+col*3*sideLength; 
			yCoordinates[4]=Math.sqrt(3)*sideLength+row/2*Math.sqrt(3)*sideLength;
			xCoordinates[4]=1.5*sideLength+col*3*sideLength; 
			yCoordinates[5]=Math.sqrt(3)*sideLength+row/2*Math.sqrt(3)*sideLength;
			xCoordinates[5]=0.5*sideLength+col*3*sideLength;
		}
		else if(row%2==1){
			yCoordinates[0]=Math.sqrt(3)*sideLength+(row/2)*Math.sqrt(3)*sideLength; 
			xCoordinates[0]=1.5*sideLength+3*col*sideLength; 
			yCoordinates[1]=Math.sqrt(3)*sideLength/2+(row/2)*Math.sqrt(3)*sideLength; 
			xCoordinates[1]=2*sideLength + 3*col*sideLength; 
			yCoordinates[2]=Math.sqrt(3)*sideLength/2+(row/2)*Math.sqrt(3)*sideLength; 
			xCoordinates[2]=3*sideLength+3*sideLength*col; 
			yCoordinates[3]=Math.sqrt(3)*sideLength+row/2*Math.sqrt(3)*sideLength; 
			xCoordinates[3]=3.5*sideLength+3*sideLength*col; 
			yCoordinates[4]=Math.sqrt(3)*sideLength/2+Math.sqrt(3)*sideLength+row/2*Math.sqrt(3)*sideLength; 
			xCoordinates[4]=3*sideLength+3*sideLength*col;
			yCoordinates[5]=Math.sqrt(3)*sideLength/2+Math.sqrt(3)*sideLength+row/2*Math.sqrt(3)*sideLength;
			xCoordinates[5]=2*sideLength + 3*col*sideLength; 
		}
	}
	
	public double getSide(){
		return sideLength; 
	}
	
	public double[] getXCoordinates(){
		return xCoordinates; 
	}
	
	public double[] getYCoordinates(){
		return yCoordinates;
	}	
	
	    @Override
	    public void setRowandCol(int r,int c){
	        row = r;
	        col = c;
	        createBasedOnPosition();
	    }
}
