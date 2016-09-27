package Cell;

import javafx.scene.paint.Color;

public class HexagonalCell extends Cell{

	private double[]xCoordinates; 
	private double[]yCoordinates;
	private double sideLength; 
	
	public HexagonalCell(int r, int c, double side){
		super(-1,Color.BLACK,-1,-1,r,c);
		createBasedOnPosition(r,c,side);
		sideLength = side; 
		color = Color.BLACK; 
	}
	
	public void createBasedOnPosition(int r,int c,double side){
		xCoordinates = new double[6];
		yCoordinates = new double[6];
		if(r%2==0){
			yCoordinates[0]=Math.sqrt(3)*side/2+r/2*Math.sqrt(3)*side;
			xCoordinates[0]=c*3*side; 
			yCoordinates[1]=r/2*Math.sqrt(3)*side; 
			xCoordinates[1]=0.5*side+c*3*side;
			yCoordinates[2]=r/2*Math.sqrt(3)*side;
			xCoordinates[2]=1.5*side+c*3*side; 
			yCoordinates[3]=Math.sqrt(3)*side/2+r/2*Math.sqrt(3)*side;
			xCoordinates[3]=2*side+c*3*side; 
			yCoordinates[4]=Math.sqrt(3)*side+r/2*Math.sqrt(3)*side;
			xCoordinates[4]=1.5*side+c*3*side; 
			yCoordinates[5]=Math.sqrt(3)*side+r/2*Math.sqrt(3)*side;
			xCoordinates[5]=0.5*side+c*3*side;
		}
		else if(r%2==1){
			yCoordinates[0]=Math.sqrt(3)*side+(r/2)*Math.sqrt(3)*side; 
			xCoordinates[0]=1.5*side+3*c*side; 
			yCoordinates[1]=Math.sqrt(3)*side/2+(r/2)*Math.sqrt(3)*side; 
			xCoordinates[1]=2*side + 3*c*side; 
			yCoordinates[2]=Math.sqrt(3)*side/2+Math.sqrt(3)*r/2*side; 
			xCoordinates[2]=3*side+3*side*c; 
			yCoordinates[3]=Math.sqrt(3)*side+r/2*Math.sqrt(3)*side; 
			xCoordinates[3]=3.5*side+3*side*c; 
			yCoordinates[4]=Math.sqrt(3)*side/2+Math.sqrt(3)*side+r/2*Math.sqrt(3)*side; 
			xCoordinates[4]=3*side+3*side*c;
			yCoordinates[5]=Math.sqrt(3)*side/2+Math.sqrt(3)*side+r/2*Math.sqrt(3)*side;
			xCoordinates[5]=2*side + 3*c*side; 
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
	
	public Color getColor(){
		return color; 
	}
	
	
}
