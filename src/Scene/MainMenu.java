package Scene;

import java.io.File;
import java.util.ResourceBundle;
import CellularAutomata.BadFileException;
import CellularAutomata.CellularAutomata;
import CellularAutomata.CellularAutomataFactory;
import XMLParser.XMLParser;
import XMLParser.XMLParserException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainMenu {
	public static final String STYLESHEET = "../resources/menustyles.css";

	private File animationXML;
	private GridPane myRoot;
	private Scene myScene;
	private AnimationScene myAnimation;
	private ResourceBundle myResources;
	private boolean strokeBoolean;
	private Color strokeColor;

	public MainMenu() {
		// TODO Auto-generated constructor stub
	}

	public Scene init(int width, int height, ResourceBundle resources) {
		final FileChooser fileChooser = new FileChooser();
		myResources = resources;
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
			public void handle(final ActionEvent event) {
				Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				animationXML = fileChooser.showOpenDialog(mainStage);
				try {
					fetchXML.setText(animationXML.toString());
				} catch (NullPointerException e) {
					animationXML = new File(fetchXML.getText());
				}
			}
		});

		startAnimation.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(final ActionEvent event) {
    			        XMLParser parser;
    			        try {
    					parser = new XMLParser(animationXML.toString());
    	                        } 
    			        catch (XMLParserException | NullPointerException e) {
                                           showError("XMLError");
                                   return;
    	                        }
					try {
						CellularAutomata animationType = createSimulation(parser.getCAType(), parser);
						if (animationType == null) {
							throw new BadFileException();
						}
						myAnimation = new AnimationScene(myScene, animationType, myRoot, myResources, height,strokeBoolean,strokeColor);
						myScene.setRoot(myAnimation.getRoot());
					} catch (BadFileException e) {
					}


			}
		});

		myRoot.add(programName, 1, 1);
		myRoot.add(fetchXML, 1, 2);
		myRoot.add(startAnimation, 1, 3);

		GridPane.setHalignment(programName, HPos.CENTER);
		GridPane.setHalignment(fetchXML, HPos.CENTER);
		GridPane.setHalignment(startAnimation, HPos.CENTER);

		return myScene;
	}

	// selects which simulation to run/which set of rules to follow
	private CellularAutomata createSimulation(String simulationname, XMLParser parser) {
		CellularAutomataFactory simulationCreator = new CellularAutomataFactory(simulationname, parser,myResources);
		strokeBoolean=simulationCreator.getStrokeBoolean();
		strokeColor=simulationCreator.getStrokeColor();
		return simulationCreator.decideAndCreateCA();
	}

	private void showError(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(message);
		alert.setContentText(myResources.getString(message));
		alert.showAndWait();
	}
}
