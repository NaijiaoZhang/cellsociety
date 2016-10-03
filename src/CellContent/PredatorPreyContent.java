package CellContent;

import Cell.Cell;
import javafx.scene.paint.Color;

public class PredatorPreyContent extends CellContent {

	private int sharkEnergy;
	private int maxSharkEnergy;
	private int moveNum;
	private boolean hasMoved;

	public PredatorPreyContent(int state, Color clr, int sE) {
		super(state, clr);
		maxSharkEnergy = sE;
		initializeByState();
	}

	public int getMoveNum() {
		return moveNum;
	}

	public void setMoveNum(int moveNum) {
		this.moveNum = moveNum;
	}

	public boolean getHasMoved() {
		return hasMoved;
	}

	public void setHasMoved(boolean b) {
		hasMoved = b;
	}

	public void moved() {
		moveNum += 1;
	}

	public void ate() {
		sharkEnergy = maxSharkEnergy;
	}

	public void decreaseEnergy() {
		sharkEnergy -= 1;
	}

	public int getSharkEnergy() {
		return sharkEnergy;
	}

	private void initializeByState() {
		if (getState() == 0) {
			sharkEnergy = -1;
			moveNum = 0;
			hasMoved = false;
		} else if (getState() == 1) {
			sharkEnergy = maxSharkEnergy;
			moveNum = 0;
			hasMoved = false;
		} else if (getState() == 2) {
			sharkEnergy = -1;
			moveNum = 0;
			hasMoved = false;
		}
	}
}
