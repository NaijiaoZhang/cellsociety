package CellularAutomata;

import java.util.ResourceBundle;
import XMLParser.XMLParser;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;

public class CellularAutomataFactory {
        public static final String[] TILE_TYPES={"square","triangle","hexagonal"};
        public static final String[] EDGE_TYPES={"finite","toroidal"};
        public static final String[] BOOL_TYPES={"true","false"};
        public static final String[] DIRECTION_TYPES={"all","diagonal","cardinal"};

	private String simulationname;
	private XMLParser parser;
	private ResourceBundle myResources;
	
	public CellularAutomataFactory(String simName, XMLParser parse,ResourceBundle resources){
	        myResources=resources;
		simulationname = simName; 
		parser = parse; 
	}
	
	public CellularAutomata decideAndCreateCA(){
	    try{
		switch(simulationname){
    	        case "GameOfLife":
    	            return createGameOfLife(); 
    	        case "Fire":
    	        	return createFire(); 
    	        case "Segregation":
    	        	return createSegregation();
    	        case "PredatorPrey":
    	                return createPredatorPrey();
    	        case "ForagingAnts":
    	            return createForagingAnts();
    	        case "SugarScape":
    	                return createSugarScape();
    	        default:
    	           throw new BadFileException();
    		}    
	    }
	    catch(BadFileException e){
                showError("TitleError");
	    }
	    return null;
	}
	
	public boolean getStrokeBoolean(){
	    return (stringCheck("strokeBoolean",BOOL_TYPES)==null || stringCheck("strokeBoolean",BOOL_TYPES).toLowerCase().equals("true"));
	}
	
	public Color getStrokeColor(){
	    if(colorCheck("strokeColor")==null){
	        return Color.BLUE;
	    }
	    return colorCheck("strokeColor");
	}
	
	private GameOfLifeRules createGameOfLife(){
	        try{	  
	        if(colorCheck("death_color")==null || colorCheck("alive_color")==null){
	            return null;
	        }
		return new GameOfLifeRules(boundsCheck("cells_row",1,100000),
		                           boundsCheck("cells_column",1,100000),
		                           boundsCheck("width",100,500),
		                           boundsCheck("height",100,500),
		                           boundsCheck("prob_alive",0,100),
		                           stringCheck("TileType",TILE_TYPES),
		                           stringCheck("EdgeType",EDGE_TYPES),
	                                   colorCheck("death_color"),
	                                   colorCheck("alive_color"),
	                                   stringCheck("neighborDirection",DIRECTION_TYPES));
	        }
	        catch(NullPointerException e){
	            return null;
	        }		
	}
	
	private FireRules createFire(){
	        try{
	        if(colorCheck("burning_color")==null || colorCheck("burnable_color")==null || colorCheck("burnt_color")==null ){
	            return null;
	        }
		return new FireRules(boundsCheck("cells_row",1,1000),
                                     boundsCheck("cells_column",1,1000),
                                     boundsCheck("width",100,500),
                                     boundsCheck("height",100,500),
                                     boundsCheck("prob_catch",0,100),
                                     stringCheck("TileType",TILE_TYPES),
                                     stringCheck("EdgeType",EDGE_TYPES),
                                     colorCheck("burning_color"),
                                     colorCheck("burnable_color"),
                                     colorCheck("burnt_color"),
                                     stringCheck("neighborDirection",DIRECTION_TYPES)); 
                }
                catch(NullPointerException e){
                    return null;
                }       
	}
	
	private SegregationRules createSegregation(){
	        try{

	        if(colorCheck("A_color")==null || colorCheck("B_color")==null || colorCheck("empty_color")==null ){
	            return null;
	        }
		return new SegregationRules(boundsCheck("cells_row",1,1000),
	                                    boundsCheck("cells_column",1,1000),
	                                    boundsCheck("width",100,500),
	                                    boundsCheck("height",100,500),
	                                    boundsCheck("empty",0,100),
	                                    boundsCheck("similar",0,100),
        		                    boundsCheck("red_prob",0,100),
        		                    stringCheck("TileType",TILE_TYPES),
        		                    stringCheck("EdgeType",EDGE_TYPES),
        		                    colorCheck("A_color"),
        		                    colorCheck("B_color"),
        		                    colorCheck("empty_color"),
        		                    stringCheck("neighborDirection",DIRECTION_TYPES)); 
                }
                catch(NullPointerException e){
                    return null;
                }       
	}
	
	private PredatorPreyRules createPredatorPrey(){
	        try{
	        if(colorCheck("shark_color")==null || colorCheck("fish_color")==null || colorCheck("water_color")==null ){
	            return null;
	        }
		return new PredatorPreyRules(boundsCheck("cells_row",1,1000),
	                                     boundsCheck("cells_column",1,1000),
	                                     boundsCheck("width",100,500),
	                                     boundsCheck("height",100,500),
	                                     boundsCheck("shark_energy",1,50),
	                                     boundsCheck("shark_reproduce",1,50),
	                                     boundsCheck("fish_reproduce",1,50),
        		                     boundsCheck("empty",0,100),
        		                     boundsCheck("shark_prob",0,100),
        		                     stringCheck("TileType",TILE_TYPES),
        		                     stringCheck("EdgeType",EDGE_TYPES),
        		                     colorCheck("shark_color"),
        		                     colorCheck("fish_color"),
        		                     colorCheck("water_color"),
        		                     stringCheck("neighborDirection",DIRECTION_TYPES)); 
	        }
                catch(NullPointerException e){
                    return null;
                }       

	}
	private ForagingAntsRules createForagingAnts(){
            try{
            return new ForagingAntsRules(boundsCheck("cells_row",1,1000),
                                       boundsCheck("cells_column",1,1000),
                                       boundsCheck("width",100,500),
                                       boundsCheck("height",100,500),
                                       boundsCheck("food_X",0,99999),
                                       boundsCheck("food_Y",0,99999),
                                       boundsCheck("home_X",0,99999),
                                       boundsCheck("home_Y",0,99999),
                                       boundsCheck("max_ants",1,10000),
                                       boundsCheck("max_ants_per_cell",1,100),
                                       boundsCheck("ant_life",5,2000),
                                       boundsCheck("initial_ants",1,100),
                                       boundsCheck("ants_spawned",1,100),
                                       boundsCheck("max_pheromone",1,100000),
                                       boundsCheck("n",1,1000),
                                       boundsCheck("k",1,1000000),
                                       boundsCheck("evap",1,1000),
                                       boundsCheck("diffuse",1,1000),
                                       stringCheck("TileType",TILE_TYPES),
                                       stringCheck("EdgeType",EDGE_TYPES),
                                       colorCheck("empty_color"),
                                       colorCheck("food_color"),
                                       colorCheck("home_color"),
                                       stringCheck("neighborDirection",DIRECTION_TYPES));
            }
            catch(NullPointerException e){
                return null;
            }               
        }

	
        private SugarScapeRules createSugarScape(){
            try{
            return new SugarScapeRules(boundsCheck("cells_row",1,100000),
                                         boundsCheck("cells_column",1,100000),
                                         boundsCheck("width",100,500),
                                         boundsCheck("height",100,500),
                                         boundsCheck("max_sugar",1,500),
                                         boundsCheck("prob_agent",0,100),
                                         stringCheck("TileType",TILE_TYPES),
                                         stringCheck("EdgeType",EDGE_TYPES),
                                         colorCheck("agent_color")); 
            }
            catch(NullPointerException e){
                return null;
            }       

    }

        private Integer boundsCheck(String variable,int min, int max){
            try{
                Integer val;
                String check=parser.getVariableValues(variable);
                if(check==null){
                    System.out.println("default");
                    val=getDefaultInt(variable);
                }
                else{
                    val=Integer.parseInt(parser.getVariableValues(variable));
                }
                if(val>=min && val<=max){
                    return val;
                }
                throw new BadFileException();
            }
            catch(NullPointerException | NumberFormatException | BadFileException e){
                showError(variable);
                return null;
            }
        }
        
        private String stringCheck(String variable,String[] checkList){            
            try{
                String val=parser.getVariableValues(variable); 
                if(val == null){
                    val=getDefaultString(variable);
                }
                val=val.toLowerCase();
                for(String type: checkList){
                    if(val.equals(type)){
                        return val;
                    }
                }
                throw new BadFileException();
            }
            catch(NullPointerException | BadFileException e){
                showError(variable);
                return null;
            }
        }
        
        private Color colorCheck(String variable){
            Color color;
            try{
                String val=parser.getVariableValues(variable); 
                if(val == null){
                    val=getDefaultString(variable);
                }
                val=val.toUpperCase();
                color = (Color) Color.class.getField(val).get(null);
            }
            catch(NullPointerException | IllegalArgumentException | IllegalAccessException | SecurityException | NoSuchFieldException e ){
                showError("ColorError");
                return null;             
            }
            return color;
        }
	
        private void showError(String message) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle(message);
            alert.setContentText(myResources.getString(message));
            alert.showAndWait();
    }
        
    private Integer getDefaultInt(String variable){
        switch(variable){
            case("cells_row"):
                return 25;
            case("cells_column"):
                return 25;
            case("width"):
                return 500;
            case("height"):
                return 500;
            case("prob_alive"):
                return 75;
            case("prob_catch"):
                return 50;
            case("empty"):
                return 50;
            case("similar"):
                return 50;
            case("red_prob"):
                return 50;
            case("shark_energy"):
                return 5;
            case("shark_reproduce"):
                return 7;
            case("fish_reproduce"):
                return 5;
            case("shark_prob"):
                return 30;   
            case("max_sugar"):
                return 5;
            case("prob_agent"):
                return 5;
            case("food_X"):
                return 4;
            case("food_Y"):
                return 4;
            case("home_X"):
                return 25;
            case("home_Y"):
                return 25;
            case("max_ants"):
                return 1000;
            case("max_ants_per_cell"):
                return 10;
            case("ant_life"):
                return 500;
            case("initial_ants"):
                return 2;
            case("ants_spawned"):
                return 2;
            case("max_pheromone"):
                return 1000;
            case("n"):
                return 10;
            case("k"):
                return 100;
            case("evap"):
                return 1;
            case("diffuse"):
                return 1;
            default:
                return null;
        }
    }
    
    private String getDefaultString(String variable){
        switch(variable){
            case("EdgeType"):
                return "finite";
            case("TileType"):
                return "square";
            case("burning_color"):
                return "RED";
            case("burnt_color"):
                return "YELLOW";
            case("burnable_color"):
                return "GREEN";
            case("death_color"):
                return "BLACK";
            case("alive_color"):
                return "WHITE";
            case("A_color"):
                return "RED";
            case("B_color"):
                return "BLUE";
            case("empty_color"):
                return "WHITE";
            case("shark_color"):
                return "RED";
            case("fish_color"):
                return "GREEN";
            case("water_color"):
                return "BLUE";
            case("strokeBoolean"):
                return "true";
            case("strokeColor"):
                return "BLUE";
            case("food_color"):
                return "RED";
            case("home_color"):
                return "GREEN";
            case("agent_color"):
                return "RED";
            case("neighborDirection"):
                return "all";
            default:
                return null;
        }
    }
}
