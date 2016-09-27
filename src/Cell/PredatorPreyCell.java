package Cell;

import javafx.scene.paint.Color;

public class PredatorPreyCell extends Cell {

        private int maxsE;
	private int sharkEnergy;
	private int moveNum; 
	private boolean hasMoved;
	
	public PredatorPreyCell(int state, Color clr, int xPos, int yPos, int r, int c, int sE) {
		super(state, clr, xPos, yPos, r, c);	
	        maxsE=sE;
		initializeByState();
	}
	
	public PredatorPreyCell(int state, Color clr, int sE){
		super(state,clr);
		maxsE=sE;
		initializeByState();
	}
	
	public int getMoveNum() {
		return moveNum;
	}

	public void setMoveNum(int moveNum) {
		this.moveNum = moveNum;
	}
	
	public boolean getHasMoved(){
		return hasMoved; 
	}
	
	public void setHasMoved(boolean b){
		hasMoved = b; 
	}
	
	public void moved(){
		moveNum +=1; 
	}
	
	public void ate(){
	        sharkEnergy=maxsE;
	}
	
	public void decreaseEnergy(){
	        sharkEnergy-=1;
	}

	public int getSharkEnergy(){
		return sharkEnergy;
	}
		
	private void initializeByState(){
		if(getState()==0){
			sharkEnergy = -1; 
			moveNum = 0;
			hasMoved=false; 
		}
		else if(getState()==1){
			sharkEnergy = maxsE; 
			moveNum =0;
			hasMoved=false;
		}
		else if(getState()==2){
			sharkEnergy =-1; 
			moveNum =0;
			hasMoved=false; 
		}
	}

	
	
}
