package CellContent;

import javafx.scene.paint.Color;

public abstract class CellContent {
	
	protected int state;
	protected Color color;

	public CellContent(int s, Color c) {
		state = s;
		color = c;
	}

	public void setStateandColor(int i, Color c) {
		state = i;
		color = c;
	}

	public void setColor(Color c) {
		color = c;
	}

	public void setState(int i) {
		state = i;
	}

	public void increaseState(int i) {
		state += i;
	}

	public void increaseState() {
		increaseState(1);
	}

	public Color getColor() {
		return color;
	}
	
	public int getState(){
        return state;
    }

	
}
