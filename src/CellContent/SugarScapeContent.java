package CellContent;

import Models.Agent;
import javafx.scene.paint.Color;

public class SugarScapeContent extends CellContent {

	private int sugarHere,maxSugarHere,colorBasedOnSugar,sugarGrowBackRate; 
	
	private Agent agentHere; 
	
	public SugarScapeContent(int s, Color c, int amountHeld,int maxSugar, int colorDivision, int growBack, Agent agent) {
		super(s, c);
		colorBasedOnSugar = colorDivision;
		agentHere=agent;
		sugarHere = amountHeld;
		maxSugarHere= maxSugar;
		sugarGrowBackRate=growBack;
	}
	
	public void adjustColor(){
		if(agentHere!=null){
			setColor(Color.RED);
		}
		else{
			setColor(Color.rgb(0, 255 - sugarHere * colorBasedOnSugar, 255));
		}
	}
	
	public void growBack(){
		if(sugarHere+sugarGrowBackRate>maxSugarHere){
			sugarHere=maxSugarHere;
		}
		else{
			sugarHere+=sugarGrowBackRate;
		}
		setColor(Color.rgb(0, 255 - sugarHere * colorBasedOnSugar, 255));
	}
	
	public int consumed(){
		int temp = sugarHere; 
		sugarHere = 0 ;
		return temp;
	}
	
	public int getMaxSugar(){
		return maxSugarHere;
	}
	
	public int getGrowRate(){
		return sugarGrowBackRate; 
	}
	
	public int getSugar(){
		return sugarHere; 
	}
	
	public int getColorDivision(){
		return colorBasedOnSugar; 
	}
	
	public Agent getAgent(){
		return agentHere; 
	}
	
	public void setAgent(Agent a){
		agentHere = a; 
	}


}
