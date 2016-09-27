package Scene;

import java.util.ResourceBundle;


import Cell.Cell;
import Cell.HexagonalCell;
import CellularAutomata.CellularAutomata;
import CellularAutomata.GameOfLifeRules;
import CellularAutomata.PredatorPreyRules;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * The class that displays the simulation being run.
 * Also contains buttons that control simulation settings (play, step, stop, reset) and to go back to main menu.
 * @author Vincent
 *
 */
public class AnimationScene {
    public static final String MENUSTYLESHEET="../resources/menustyles.css";
    public static final String STYLESHEET="../resources/animationstyles.css";

    public static final int FRAMES_PER_SECOND = 50;
    public static final int SPEED_CHANGE=1000;
    public static final int MIN_SPEED=1000;
    public static final int MAX_SPEED=20000;

    
    private ResourceBundle myResources;
    private KeyFrame frame;
    private Timeline anim;
    private CellularAutomata myAutomata;
    private Canvas myCanvas;
    private GraphicsContext gc;
    private GridPane myRoot,mainroot;
    private Scene myScene;
    private HBox controls;
    private Button backButton, resetButton, playButton, stepButton, stopButton, speedButton, slowButton;
    private int gridHeight, gridWidth, cellRows, cellCols, cellWidth, cellHeight, mainHeight;
    private Cell [][] myGrid;
    private int ms;
    private int msDelay;
    private int runAnimation; //0 for don't run, 1 for keep running, 2 for run once

    
    /**
     * Create the animation scene that shows the simulation and controls it.
     * @param scene
     * @param rules
     * @param root
     * @param resource
     * @param h
     */
    public AnimationScene (Scene scene, CellularAutomata rules,GridPane root,ResourceBundle resource, int h) {
    	//input variables
        myResources = resource; 
        myScene=scene;
        mainroot=root;
        myAutomata=rules;
        mainHeight=h;

        //grid parameters
        setGridParameters();
        
        //other variables to initialize
        ms=10000;
        msDelay= ms / FRAMES_PER_SECOND;
        runAnimation=0;
        
        //set up gridpane and canvas
        myRoot=new GridPane();
        myRoot.getRowConstraints().add(new RowConstraints(mainHeight-33));        
        myRoot.setAlignment(Pos.TOP_CENTER);
        
        myCanvas=new Canvas(gridHeight,gridWidth);
        gc = myCanvas.getGraphicsContext2D();
        myRoot.add(myCanvas, 1, 0);      
        GridPane.setHalignment(myCanvas,HPos.CENTER);
        
        //create buttons
        createButtons();  
        
        //show board
        display();      
        
        //create keyframe and start timeline
        createKeyFrame();

        //change root
        myScene.setRoot(myRoot);
        
        //css
        myScene.getStylesheets().clear();
        myScene.getStylesheets().add(STYLESHEET);
    }
    
    /**
     * Returns root to be able to move between main menu and animation scene.
     * @return
     */
    public GridPane getRoot(){
        return myRoot;
    }
    
    //draws grid on canvas
    private void display(){
    	if(myGrid[0][0] instanceof HexagonalCell){
    		displayHexagonalTiles(); 
    	}
    	else if(myGrid[0][0] instanceof Cell){
    		displaySquareTiles(); 
    	}
    }
    
    private void displayHexagonalTiles(){
    	gc.setStroke(Color.BLUE);
    	gc.setFill(myGrid[0][0].getColor());
    	for(int i=0;i<myGrid.length;i++){
    		for(int j=0;j<myGrid[0].length;j++){
    			if(i%2==0){
    				gc.strokePolygon(((HexagonalCell)myGrid[i][j]).getXCoordinates(), ((HexagonalCell)myGrid[i][j]).getYCoordinates(), 6);
    				gc.fillPolygon(((HexagonalCell)myGrid[i][j]).getXCoordinates(), ((HexagonalCell)myGrid[i][j]).getYCoordinates(), 6);
    			}
    			}
    		}
    }
    
    private void displaySquareTiles(){
    	for (int i=0; i<cellCols;i++)
        {
            for (int h=0; h<cellRows; h++){
                Cell tempCell=myGrid[i][h];
                gc.setFill(tempCell.getColor());
                gc.fillRect(tempCell.getX(),tempCell.getY(),cellWidth,cellHeight);
            }
        }
    }
    
    /*
     * The method that is set by the animation to constantly run.
     * Uses the runAnimation parameter (controlled by User through buttons) to determine when to move forward.
     */
    private void stepFunction(){
        //keeps going through, like step in game
        switch(runAnimation){
            case 0:
                //before any buttons have been pressed, after stop pressed, or after step has called update once
                break;
                
            case 1:
                //after start has been pressed
                update();
                break;
                
            case 2:
                //after step has been pressed
                update();
                runAnimation=0;
                break;
                
            default:
                break;
        }            
    }
    
    //Moves one step forward in simulation
    private void update(){
        myAutomata.update();
        myGrid = myAutomata.getGrid();
        display();
    }   

    /*
     * BUTTON FUNCTIONALITY
     */
    
    //stop simulation from moving forward
    private void stop(){
            runAnimation=0;
    }
    
    //set simulation to continuously go forward
    private void play(){
            runAnimation=1;
    }
    
    //go forward one step in simulation
    private void step(){
            runAnimation=2;
    }  
    
    //speedup
    private void faster(){
            if(ms>MIN_SPEED){
                ms-=SPEED_CHANGE;
                changeSpeed();
            }                
    }  
    
    //slowdown
    private void slower(){
            if(ms<MAX_SPEED){
                ms+=SPEED_CHANGE;
                changeSpeed();
            }        
    }
    
    //change speed
    private void changeSpeed(){
        msDelay=ms/FRAMES_PER_SECOND;
        anim.stop();
        anim.getKeyFrames().remove(0);
        frame = new KeyFrame(Duration.millis(msDelay),
                             e -> stepFunction());
        anim.getKeyFrames().add(frame);
        anim.play();
    }
    ;
    //reset grid
    private void reset(){
            runAnimation=0;
            myAutomata.reset();
            myGrid=myAutomata.getGrid();
            display();
    }
    
    //go back to main menu
    private void goBack(){
            runAnimation=0;
            myScene.getStylesheets().clear();
            myScene.getStylesheets().add(MENUSTYLESHEET);
            myScene.setRoot(mainroot);
    }
    
    /*
     * INITIALIZATION METHODS
     */
    
    //sets initialization state and variables of grid
    private void setGridParameters(){
    	System.out.println("here2");
        myGrid=myAutomata.getGrid();
        cellRows=myGrid.length;
        cellCols=myGrid[0].length;
        gridHeight=myAutomata.getHeight();
        gridWidth=myAutomata.getWidth();
        cellWidth=gridWidth/cellCols;
        cellHeight=gridHeight/cellRows;
    }
    
    //creates and starts keyframe for animation
    private void createKeyFrame(){
        frame = new KeyFrame(Duration.millis(msDelay),
                             e -> stepFunction());
        anim = new Timeline();
        anim.setCycleCount(Timeline.INDEFINITE);
        anim.getKeyFrames().add(frame);
        anim.play();
    }
    
    //Buttons and alignment
    private void createButtons(){
        controls=new HBox();
        
        playButton= makeButton(myResources.getString("Play"),event -> play());
        controls.getChildren().add(playButton);
        stopButton= makeButton(myResources.getString("Stop"),event -> stop());
        controls.getChildren().add(stopButton);
        stepButton= makeButton(myResources.getString("Step"),event -> step());
        controls.getChildren().add(stepButton);
        speedButton= makeButton(myResources.getString("Faster"),event -> faster());
        controls.getChildren().add(speedButton);
        slowButton= makeButton(myResources.getString("Slower"),event -> slower());
        controls.getChildren().add(slowButton);
        resetButton= makeButton(myResources.getString("Reset"),event -> reset());
        controls.getChildren().add(resetButton);
        backButton= makeButton(myResources.getString("Back"),event -> goBack());
        controls.getChildren().add(backButton);
        
        myRoot.add(controls, 1, 1);
    }
    
    //create a new button
    private Button makeButton (String label, EventHandler<ActionEvent> handler) {
        Button result = new Button(label);
        result.setOnAction(handler);
        return result;
    }
}
    