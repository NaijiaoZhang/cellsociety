package Scene;

import java.io.File;
import java.util.ResourceBundle;

import CellularAutomata.CellularAutomata;
import CellularAutomata.CellularAutomataFactory;
import XMLParser.XMLParser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainMenu {
	public static final String STYLESHEET="../resources/menustyles.css";
	
	private File animationXML; 
	private GridPane myRoot;
	private Scene myScene;
    private AnimationScene myAnimation;
    private ResourceBundle myResources; 
	
    public MainMenu () {
        // TODO Auto-generated constructor stub
    }

    public Scene init(int width,int height,ResourceBundle resources){
    	final FileChooser fileChooser = new FileChooser(); 
    	myResources=resources; 
    	myRoot = new GridPane();
		myRoot.setAlignment(Pos.CENTER);
		myRoot.setHgap(10);
		myRoot.setVgap(10);
		myRoot.setPadding(new Insets(25, 25, 25, 25));

		myScene = new Scene(myRoot, width, height);
		myScene.getStylesheets().add(STYLESHEET);
		
		Label programName = new Label(myResources.getString("AppTitle"));
		Button fetchXML = new Button(myResources.getString("XMLSelect"));
		Button startAnimation = new Button(myResources.getString("Start"));
		
		fetchXML.setOnAction(new EventHandler<ActionEvent>() {
					
			@Override
			public void handle(final ActionEvent event){
				Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				animationXML = fileChooser.showOpenDialog(mainStage);	
				fetchXML.setText(animationXML.toString());
			}
		});
		
		startAnimation.setOnAction(new EventHandler<ActionEvent>() {
			
            @Override
			public void handle(final ActionEvent event){
				XMLParser parser = new XMLParser(animationXML.toString()); 
				CellularAutomata animationType = createSimulation(parser.getCAType(),parser);
				if(animationType!=null){
					myAnimation=new AnimationScene(myScene,animationType,myRoot,myResources, height);
					myScene.setRoot(myAnimation.getRoot());
				}
				else{
				    System.out.println("error");
				}
			}
		});
		
		
		myRoot.add(programName, 1, 1);
		myRoot.add(fetchXML, 1, 2);
		myRoot.add(startAnimation, 1, 3);
		
	        GridPane.setHalignment(programName,HPos.CENTER);
	        GridPane.setHalignment(fetchXML,HPos.CENTER);
	        GridPane.setHalignment(startAnimation,HPos.CENTER);

		return myScene; 
    }
    
    //selects which simulation to run/which set of rules to follow
    private CellularAutomata createSimulation(String simulationname,XMLParser parser){
       CellularAutomataFactory simulationCreator = new CellularAutomataFactory(simulationname,parser); 
       return simulationCreator.decideAndCreateCA();
    }
}
