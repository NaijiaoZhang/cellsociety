package Scene;

import java.util.ResourceBundle;

import CellularAutomata.CellularAutomata;
import CellularAutomata.FireRules;
import CellularAutomata.ForagingAntsRules;
import CellularAutomata.GameOfLifeRules;

import CellularAutomata.PredatorPreyRules;
import CellularAutomata.SegregationRules;
import CellularAutomata.SugarScapeRules;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

/**
 * The class that displays the simulation being run. Also contains buttons that
 * control simulation settings (play, step, stop, reset) and to go back to main
 * menu.
 * 
 * @author Vincent
 *
 */
public class AnimationScene {

    public static final String MENUSTYLESHEET="../resources/menustyles.css";
    public static final String STYLESHEET="../resources/animationstyles.css";
    public static final int BUTTON_HEIGHT=75;
    public static final int SPINNER_WIDTH=60;

    public static final int FRAMES_PER_SECOND = 50;
    public static final int SPEED_CHANGE=2000;
    public static final int START_SPEED=10000;
    public static final int MIN_SPEED=2000;
    public static final int MAX_SPEED=18000;
    public static final int PROB_CHANGE=10;
    public static final int MIN_PROB=0;
    public static final int MAX_PROB=100;
    public static final int MIN_NUM=1;
    public static final int MAX_NUM=50;    
    
    private ResourceBundle myResources;
    private Display myDisplay;
    private KeyFrame frame;
    private Timeline anim;
    private CellularAutomata myAutomata;
    private GridPane myRoot,mainroot;
    private Scene myScene;
    private int mainHeight;
    private int msDelay;
    private int runAnimation; //0 for don't run, 1 for keep running, 2 for run once
	private XYChart.Series<Number, Number> series,series2;


    
    /**
     * Create the animation scene that shows the simulation and controls it.
     * @param scene
     * @param rules
     * @param root
     * @param resource
     * @param h
     */
    public AnimationScene (Scene scene, CellularAutomata rules,GridPane root,ResourceBundle resource, int h, boolean stroke, Color strokeColor) {
    	//input variables
        myResources = resource; 
        myScene=scene;
        mainroot=root;
        myAutomata=rules;
        mainHeight=h;
        
        runAnimation=0;
        
        myDisplay=new Display(myAutomata,stroke,strokeColor);
        
        myRoot=new GridPane();
        myRoot.setAlignment(Pos.TOP_CENTER);
        
        myRoot.add(myDisplay.getCanvas(), 1, 0);      
        GridPane.setHalignment(myDisplay.getCanvas(),HPos.CENTER);
        myRoot.getRowConstraints().add(new RowConstraints(mainHeight-BUTTON_HEIGHT)); 
        
        //create buttons
        createButtons();       
        
        //create keyframe and start timeline
        anim = new Timeline();
        anim.setCycleCount(Timeline.INDEFINITE);
        setSpeed(START_SPEED);

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
                myDisplay.update();
                break;
                
            case 2:
                //after step has been pressed
                myDisplay.update();
                runAnimation=0;
                break;
                
            default:
                break;
        }            
        if(series!=null ){
        	myAutomata.graphStats(series);
        	if(myAutomata instanceof PredatorPreyRules && series2!=null)
        	{
        	    ((PredatorPreyRules) myAutomata).graphSharkStats(series2);
        	}
        }
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
    
    //change speed
    private void setSpeed(int ms){
        msDelay=ms/FRAMES_PER_SECOND;
        anim.stop();
        if(anim.getKeyFrames().size()>0)
        {
            anim.getKeyFrames().remove(0);
        }
        frame = new KeyFrame(Duration.millis(msDelay),
                             e -> stepFunction());
        anim.getKeyFrames().add(frame);
        anim.play();
    }
    
    //reset grid
    private void reset(){
            runAnimation=0;
            myDisplay.reset();
    }
    
    //go back to main menu
    private void goBack(){
            runAnimation=0;
            myScene.getStylesheets().clear();
            myScene.getStylesheets().add(MENUSTYLESHEET);
            myScene.setRoot(mainroot);
    }
    
    private void graph() {
		Stage graphStage = new Stage();
		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();

		// creating the chart
		final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);

		// defining a series
		series = new XYChart.Series<>();

		Scene scene = new Scene(lineChart, 800, 600);
		lineChart.getData().add(series);

		graphStage.setScene(scene);
		graphStage.show();

		xAxis.setLabel(myResources.getString("XAxisLabel"));

		// Graph Titles
		if (myAutomata instanceof GameOfLifeRules) {
			lineChart.setTitle(myResources.getString("GameOfLifeTitle"));
			series.setName(myResources.getString("GameOfLifeYAxisLabel"));
		} else if (myAutomata instanceof FireRules) {
			lineChart.setTitle(myResources.getString("FireTitle"));
			series.setName(myResources.getString("FireYAxisLabel"));
		} else if (myAutomata instanceof SegregationRules) {
			lineChart.setTitle(myResources.getString("SegregationTitle"));
			series.setName(myResources.getString("SegregationYAxisLabel"));
		}else if(myAutomata instanceof SugarScapeRules){
			lineChart.setTitle(myResources.getString("SugarScapeTitle"));
			series.setName(myResources.getString("SugarScapeYAxisLabel"));
		} else if(myAutomata instanceof ForagingAntsRules){
                    lineChart.setTitle(myResources.getString("ForagingAntsTitle"));
                    series.setName(myResources.getString("ForagingAntsYAxisLabel"));
		}else if (myAutomata instanceof PredatorPreyRules) {
			lineChart.setTitle(myResources.getString("PredatorPreyTitle"));
			series.setName(myResources.getString("PredatorPreyYAxisFishLabel"));
			series2 = new XYChart.Series<>();
			lineChart.getData().add(series2);
			series2.setName(myResources.getString("PredatorPreyYAxisSharkLabel"));
		}

	}
    
    //Buttons and alignment
    private void createButtons(){
        VBox controls=new VBox();
        HBox sliders=new HBox();
        HBox buttons=new HBox();
        controls.getChildren().add(sliders);
        controls.getChildren().add(buttons);
        
        Button playButton= makeButton(myResources.getString("Play"),event -> play());
        Button stopButton= makeButton(myResources.getString("Stop"),event -> stop());
        Button stepButton= makeButton(myResources.getString("Step"),event -> step());
        Button resetButton= makeButton(myResources.getString("Reset"),event -> reset());
        Button backButton= makeButton(myResources.getString("Back"),event -> goBack());
        Button graphButton = makeButton(myResources.getString("Graph"), event -> graph());
        
        
        Slider speedSlider = makeSlider(MIN_SPEED,MAX_SPEED,START_SPEED,SPEED_CHANGE,true,myResources.getString("Fast"),myResources.getString("Slow"),(observable, oldValue, newValue) -> {
                    setSpeed(newValue.intValue());
            });
        
        if(myAutomata.getUseProbType()){
            Slider probTypeSlider= makeSlider(MIN_PROB,MAX_PROB,myAutomata.getProbType(),PROB_CHANGE,false,String.valueOf(MIN_PROB),String.valueOf(MAX_PROB),(observable, oldValue, newValue) -> {
                myAutomata.setProbType(newValue.intValue());
                reset();
            });
            sliders.getChildren().add(makeLabel("Cell Type"));
            sliders.getChildren().add(probTypeSlider);
        }
        
        if(myAutomata.getUseProbEmpty()){
            Slider probEmptySlider= makeSlider(MIN_PROB,MAX_PROB,myAutomata.getProbEmpty(),PROB_CHANGE,false,String.valueOf(MIN_PROB),String.valueOf(MAX_PROB),(observable, oldValue, newValue) -> {
                        myAutomata.setProbEmpty(newValue.intValue());
                        reset();
                });
            sliders.getChildren().add(makeLabel("Empty Cells"));        
            sliders.getChildren().add(probEmptySlider);
        }
        
        if(myAutomata instanceof FireRules){
            Slider burnProb= makeSlider(MIN_PROB,MAX_PROB,((FireRules) myAutomata).getProbCatchesFire(),PROB_CHANGE,false,String.valueOf(MIN_PROB),String.valueOf(MAX_PROB),(observable, oldValue, newValue) -> {
                        ((FireRules) myAutomata).setProbCatchesFire(newValue.intValue());
                });
            sliders.getChildren().add(makeLabel("Burn Probability"));
            sliders.getChildren().add(burnProb);
        }
        if(myAutomata instanceof SegregationRules){
            Slider similarSlider= makeSlider(MIN_PROB,MAX_PROB,((SegregationRules) myAutomata).getSimilar(),PROB_CHANGE,false,String.valueOf(MIN_PROB),String.valueOf(MAX_PROB),(observable, oldValue, newValue) -> {
                        ((SegregationRules) myAutomata).setSimilar(newValue.intValue());
                });
            sliders.getChildren().add(makeLabel("Similarity")); 
            sliders.getChildren().add(similarSlider);
        }
        
        if(myAutomata instanceof PredatorPreyRules){
            Spinner<Integer> sharkReproduceSpinner =makeSpinner(MIN_NUM, MAX_NUM,((PredatorPreyRules) myAutomata).getSharkReproduce(),
                                                                (obs, oldValue, newValue) -> ((PredatorPreyRules) myAutomata).setSharkReproduce(newValue.intValue()));
            Spinner<Integer> fishReproduceSpinner =makeSpinner(MIN_NUM, MAX_NUM,((PredatorPreyRules) myAutomata).getFishReproduce(),
                                                                (obs, oldValue, newValue) -> ((PredatorPreyRules) myAutomata).setFishReproduce(newValue.intValue()));
            Spinner<Integer> sharkEnergySpinner =makeSpinner(MIN_NUM, MAX_NUM,((PredatorPreyRules) myAutomata).getSharkEnergy(),
                                                                (obs, oldValue, newValue) -> ((PredatorPreyRules) myAutomata).setSharkEnergy(newValue.intValue()));
          
            sliders.getChildren().add(makeLabel("Shark Breed"));        
            sliders.getChildren().add(sharkReproduceSpinner);
            sliders.getChildren().add(makeLabel("Fish Breed"));        
            sliders.getChildren().add(fishReproduceSpinner);
            sliders.getChildren().add(makeLabel("Shark Lifespan"));        
            sliders.getChildren().add(sharkEnergySpinner);
        }
          
        
        buttons.getChildren().add(playButton);
        buttons.getChildren().add(stopButton);
        buttons.getChildren().add(stepButton);
        buttons.getChildren().add(makeLabel("Speed"));        
        buttons.getChildren().add(speedSlider); 
        buttons.getChildren().add(resetButton);
        buttons.getChildren().add(backButton);
        buttons.getChildren().add(graphButton);
        
        myRoot.add(controls, 1, 1);
        GridPane.setHalignment(controls,HPos.CENTER);
    }
    
    //create a new button
    private Button makeButton (String label, EventHandler<ActionEvent> handler) {
        Button result = new Button(label);
        result.setOnAction(handler);
        return result;
    }
    
    private Slider makeSlider (int min, int max, int start,int majortick, boolean ticksnap, String minLabel, String maxLabel,ChangeListener<Number> listener){
        Slider sl=new Slider(min,max,start);
        sl.setMajorTickUnit(majortick);
        sl.setMinorTickCount(0);
        sl.setShowTickMarks(true);
        sl.setShowTickLabels(true);
        sl.setSnapToTicks(false);
        sl.valueProperty().addListener(listener);
        sliderLabel(sl,min,max, minLabel, maxLabel);
        return sl;
    }
    
    private Spinner<Integer> makeSpinner(int min, int max, int start,ChangeListener<Number> listener){
        Spinner<Integer> spinner =
                new Spinner<>(min, max,start);  
        spinner.valueProperty().addListener(listener);
        spinner.setPrefWidth(SPINNER_WIDTH);
        return spinner;
    }
    
    private Label makeLabel(String text){
        Label label=new Label(text);
        label.setTextFill(Color.WHITE);
        return label;
    }
    
    private void sliderLabel (Slider sl,int min, int max, String minLabel, String maxLabel){
        sl.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double n) {
                if (n ==min) {
                    return minLabel;
                }
                else if (n ==max) {
                    return maxLabel;
                }
                else{
                    return "";
                }
            }

            @Override
            public Double fromString(String s) {
                if(s.equals(minLabel)){
                        return 0d;
                }
                else{
                        return 1d;
                }
            }
        });
    }
}
