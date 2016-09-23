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
	    /*    
	        case "Predator-Prey":
	            myAutomata=new PredatorPreyRules();
	            break;
	        case "Fire":
	            myAutomata=new FireRules();
	            break;
	        case "Segregation":
	            myAutomata=new SegregationRules();
	            break;
	     */
	        default:
	           return null; 
			}        
	}
	
	private GameOfLifeRules createGameOfLife(){
		System.out.println(parser.getVariableValues("cells_column"));
		System.out.println(parser.getVariableValues("cells_row"));
		return new GameOfLifeRules(Integer.parseInt(parser.getVariableValues("cells_row")),
        		Integer.parseInt(parser.getVariableValues("cells_column")),
        		Integer.parseInt(parser.getVariableValues("width")),
        		Integer.parseInt(parser.getVariableValues("height")),
        		Integer.parseInt(parser.getVariableValues("prob_alive")),
        		3);
	}
}
