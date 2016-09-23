import java.util.ResourceBundle;

import Scene.MainMenu;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static final String DEFAULT_RESOURCE_PACKAGE = "names";
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 600; 
	
	private ResourceBundle myResources; 
	
	
    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start (Stage primaryStage) throws Exception {
        myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE);
    	primaryStage.setTitle(myResources.getString("AppTitle"));
    	MainMenu menu = new MainMenu();
        Scene menuScene = menu.init(WIDTH,HEIGHT,myResources); 
        primaryStage.setScene(menuScene);
        primaryStage.show(); 
        
    }

}
