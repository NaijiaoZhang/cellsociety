package Cell;

import javafx.scene.paint.Color;

public class StateColor {
    private int state;
    private Color color;
    public StateColor(int i, Color c){
        state = i;
        color = c;
    }
    
    public void setStateandColor(int i, Color c){
        state = i;
        color = c;
    }
    
    public int getState(){
        return state;
    }
    
    public Color getColor(){
        return color;
    }
}
