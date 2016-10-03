package Models;

public class Agent {
	private int sugar;
	private int sugarMetabolism; 
	private int vision;
	
	public Agent(int initSugar, int sugarConsumedStep, int howFar){
		sugar = initSugar;
		sugarMetabolism = sugarConsumedStep;
		vision = howFar; 
	}
	
	public int getSugar(){
		return sugar; 
	}
	
	public void consumeSugar(int amount){
		sugar += amount; 
	}
	
	public void moved(){
		sugar=sugar-sugarMetabolism; 
	}
	
	public int getVision(){
		return vision; 
	}
	
	
	
	
	
}
