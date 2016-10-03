package CellContent;

import java.util.*;
import Cell.Cell;
import Models.Ant;
import javafx.scene.paint.Color;

public class ForagingAntsContent extends CellContent{
    List<Ant>antsHere;
    double homePheromone, foodPheromone, maxPheromone;
    boolean isFoodSource, isHome;
    
    public ForagingAntsContent (int i, Color col,boolean home, boolean food, double maxPh, double foodPh, double homePh, List<Ant> ants) {
        super(i, col);
        isHome = home;
        isFoodSource = food;
        antsHere = ants;
        homePheromone = homePh;
        foodPheromone = foodPh;
        // TODO Auto-generated constructor stub
    }
    
    public List<Ant> getAnts(){
        return antsHere;
    }
    
    public void setAnts(List<Ant> ants){
        antsHere = ants;
    }


    public void setIsFoodSource(boolean food){
        isFoodSource = food;
    }
    
    public boolean getIsFoodSource(){
        return isFoodSource;
    }
    
    public void setIsHome(boolean home){
        isHome = home;
    }
    
    public boolean getIsHome(){
        return isHome;
    }
    
    public void setHomePheromone(double home){
        homePheromone = home;
    }
    
    public double getHomePheromone(){
        return homePheromone;
    }
    
    public void setFoodPheromone(double food){
        foodPheromone = food;
    }
    
    public double getFoodPheromone(){
        return foodPheromone;
    }
    
    public double getPheromone(boolean goingHome){
        if(goingHome){
            return homePheromone;
        }else{
            return foodPheromone;
        }
    }
    
    public double getMaxPheromone(){
        return maxPheromone;
    }
    
    public void setMaxPheromone(boolean home){
        if(home){
            homePheromone = maxPheromone;
        }else{
            foodPheromone = maxPheromone;
        }
    }
    public List<Ant> getAntsClone(){
        List<Ant> clone = new ArrayList<Ant>();
        for(Ant a : antsHere){
            Ant aClone = new Ant(a.getLifeLeft());
            aClone.setOrientation(a.getOrientation());
            aClone.hasFoodItem(a.hasFoodItem());
            clone.add(aClone);
        }
        return clone;
    }
        
}
