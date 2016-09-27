package CellularAutomata;

import XMLParser.XMLParser;

public class CellularAutomataFactory {

	private String simulationname;
	private XMLParser parser;
	
	public CellularAutomataFactory(String simName, XMLParser parse){
		simulationname = simName; 
		parser = parse; 
	}
	
	public CellularAutomata decideAndCreateCA(){
		switch(simulationname){
	        case "GameOfLife":
	            return createGameOfLife(); 
	        case "Fire":
	        	return createFire(); 
	        case "Segregation":
	        	return createSegregation();
	        case "PredatorPrey":
	                return createPredatorPrey();
	        default:
	           return null; 
			}        
	}
	
	private GameOfLifeRules createGameOfLife(){
		return new GameOfLifeRules(Integer.parseInt(parser.getVariableValues("cells_row")),
        		Integer.parseInt(parser.getVariableValues("cells_column")),
        		Integer.parseInt(parser.getVariableValues("width")),
        		Integer.parseInt(parser.getVariableValues("height")),
        		Integer.parseInt(parser.getVariableValues("prob_alive")),
        		parser.getVariableValues("TileType"));
	}
	
	private FireRules createFire(){
		return new FireRules(Integer.parseInt(parser.getVariableValues("cells_row")),
        		Integer.parseInt(parser.getVariableValues("cells_column")),
        		Integer.parseInt(parser.getVariableValues("width")),
        		Integer.parseInt(parser.getVariableValues("height")),
        		Integer.parseInt(parser.getVariableValues("prob_catch"))); 
	}
	
	private SegregationRules createSegregation(){
		return new SegregationRules(Integer.parseInt(parser.getVariableValues("cells_row")),
        		Integer.parseInt(parser.getVariableValues("cells_column")),
        		Integer.parseInt(parser.getVariableValues("width")),
        		Integer.parseInt(parser.getVariableValues("height")),
        		Integer.parseInt(parser.getVariableValues("empty")),
        		Integer.parseInt(parser.getVariableValues("similar")),
        		Integer.parseInt(parser.getVariableValues("red_prob"))); 
	}
	
	private PredatorPreyRules createPredatorPrey(){
		return new PredatorPreyRules(Integer.parseInt(parser.getVariableValues("cells_row")),
        		Integer.parseInt(parser.getVariableValues("cells_column")),
        		Integer.parseInt(parser.getVariableValues("width")),
        		Integer.parseInt(parser.getVariableValues("height")),
        		Integer.parseInt(parser.getVariableValues("shark_energy")),
        		Integer.parseInt(parser.getVariableValues("shark_reproduce")),
        		Integer.parseInt(parser.getVariableValues("fish_reproduce")),
        		Integer.parseInt(parser.getVariableValues("empty")),
        		Integer.parseInt(parser.getVariableValues("shark_prob"))); 

	}
}
