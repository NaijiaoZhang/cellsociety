package CellularAutomata;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import Cell.Cell;
import Cell.HexagonalCell;
import Cell.SquareCell;
import Cell.TriangleCell;
import CellContent.GameOfLifeContent;
import CellContent.SugarScapeContent;
import Models.Agent;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.paint.Color;

public class SugarScapeRules extends CellularAutomata {

	private int sugarMax, agentSpawnProb;
	private int steps, agentPopulation; 
	private Color agentColor;

	public SugarScapeRules(int rows, int columns, int wid, int hei, int sugarmax, int agentSpawn, String tile,
			String edge, Color agentCol) {
		height = hei;
		width = wid;
		rowCount = rows;
		colCount = columns;
		tileType = tile;
		edgeType = edge;
		sugarMax = sugarmax;
		agentSpawnProb = agentSpawn;
		agentColor=agentCol;
		useProbType = false;
		useProbEmpty = false;
		steps = 0;
		agentPopulation = 0;
		placeCells();
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		Map<Integer, Cell> gridClone = cloneGrid(grid);
		steps++;
		for (int i = 0; i < grid.keySet().size(); i++) {
			if (((SugarScapeContent) grid.get(i).getContent()).getAgent() != null) {
				int vision = ((SugarScapeContent) grid.get(i).getContent()).getAgent().getVision();
				int leftBound = (int) (i / colCount) * colCount;
				int rightBound = ((int) (i / colCount) + 1) * colCount - 1;
				int topBound = i % colCount;
				int botBound = grid.keySet().size()-1;

				int maxSugarKey = decideMaxSugar(vision, leftBound, rightBound, topBound, botBound, i,gridClone);
				((SugarScapeContent) gridClone.get(maxSugarKey).getContent())
						.setAgent(((SugarScapeContent) grid.get(i).getContent()).getAgent());
				((SugarScapeContent) gridClone.get(i).getContent()).setAgent(null);
				((SugarScapeContent) gridClone.get(maxSugarKey).getContent()).getAgent()
						.consumeSugar(((SugarScapeContent) gridClone.get(maxSugarKey).getContent()).consumed());
				((SugarScapeContent) gridClone.get(maxSugarKey).getContent()).getAgent().moved(); 
				if(((SugarScapeContent) gridClone.get(maxSugarKey).getContent()).getAgent().getSugar()<=0){
					((SugarScapeContent)gridClone.get(maxSugarKey).getContent()).setAgent(null);
					agentPopulation--;
				}
				((SugarScapeContent)gridClone.get(i).getContent()).adjustColor();
				((SugarScapeContent)gridClone.get(maxSugarKey).getContent()).adjustColor();
			}
		}
			
		for(int i=0;i<gridClone.keySet().size();i++){
			if (((SugarScapeContent) gridClone.get(i).getContent()).getAgent() == null) {
				((SugarScapeContent)gridClone.get(i).getContent()).growBack();
			}
		}
		
		grid = gridClone;

	}

	private int decideMaxSugar(int vision, int leftBound, int rightBound, int topBound, int botBound, int i,Map<Integer, Cell> gridClone) {
		int maxSugarKey = -1;
		int maxSugar = -1;
		for (int k = 1; k <= vision; k++) {
			if (i - k >= leftBound) {
				if (((SugarScapeContent) gridClone.get(i - k).getContent()).getSugar() > maxSugar
						&& ((SugarScapeContent) gridClone.get(i - k).getContent()).getAgent() == null) {
					maxSugar = ((SugarScapeContent) gridClone.get(i - k).getContent()).getSugar();
					maxSugarKey = i - k;
				}
			}
		}
		for (int k = 1; k <= vision; k++) {
			if (i + k <= rightBound) {
				if (i + k <= rightBound) {
					if (((SugarScapeContent) gridClone.get(i + k).getContent()).getSugar() > maxSugar
							&& ((SugarScapeContent) gridClone.get(i + k).getContent()).getAgent() == null) {
						maxSugar = ((SugarScapeContent) gridClone.get(i + k).getContent()).getSugar();
						maxSugarKey = i + k;
					}
				}
			}
		}
		for (int k = 1; k <= vision; k++) {
			if (i + colCount * k <= botBound) {
				if (((SugarScapeContent) gridClone.get(i + k * colCount).getContent()).getSugar() > maxSugar
						&& ((SugarScapeContent) gridClone.get(i + k * colCount).getContent()).getAgent() == null) {
					maxSugar = ((SugarScapeContent) gridClone.get(i + k * colCount).getContent()).getSugar();
					maxSugarKey = i + k * colCount;
				}
			}
		}
		for (int k = 1; k <= vision; k++) {
			if (i - colCount * k >= topBound) {
				if (((SugarScapeContent) gridClone.get(i - k * colCount).getContent()).getSugar() > maxSugar
						&& ((SugarScapeContent) gridClone.get(i - k * colCount).getContent()).getAgent() == null) {
					maxSugar = ((SugarScapeContent) gridClone.get(i - k * colCount).getContent()).getSugar();
					maxSugarKey = i - k * colCount;
				}
			}
		}
		if (maxSugarKey != -1)
			return maxSugarKey;

		return i;

	}
	
	@Override
	public void createSquareGrid() {
		// TODO Auto-generated method stub
		int colorDivision = 255 / sugarMax;
		Random r = new Random();
		grid = new TreeMap<Integer, Cell>();
		int w = width / colCount;
		int h = height / rowCount;
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < colCount; j++) {
				int k = i + j * rowCount;
				int randNum = r.nextInt(sugarMax + 1);
				int randNum2 = r.nextInt(101);
				if (randNum2 > agentSpawnProb) {
					grid.put(k, new SquareCell(i * w, j * h, i, j, new SugarScapeContent(-1,
							Color.rgb(0, 255 - randNum * colorDivision, 255), randNum,randNum, colorDivision,1, null)));
				} else {
					grid.put(k, new SquareCell(i * w, j * h, i, j,
							new SugarScapeContent(-1, agentColor, randNum,randNum, colorDivision, 1, new Agent(8, 2, 4))));
					agentPopulation++;
				}
			}
		}
	}

	@Override
	public void createTriangleGrid() {
		// TODO Auto-generated method stub
		int colorDivision = 255 / sugarMax;
		Random r = new Random();
		grid = new TreeMap<Integer, Cell>();
		int w = width / colCount;
		int h = height / rowCount;
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < colCount; j++) {
				int k = i + j * rowCount;
				int randNum = r.nextInt(sugarMax + 1);
				int randNum2 = r.nextInt(101);
				if (randNum2 > agentSpawnProb) {
					grid.put(k, new TriangleCell(i*w,j*h,w,h,i,j,new SugarScapeContent(-1,
							Color.rgb(0, 255 - randNum * colorDivision, 255), randNum,randNum, colorDivision,1, null)));
				} else {
					grid.put(k, new TriangleCell(i*w, j*h,w,h, i, j,
							new SugarScapeContent(-1, agentColor, randNum,randNum, colorDivision, 1, new Agent(8, 2, 4))));
					agentPopulation++;
				}
			}
		}

	}

	@Override
	public void createHexagonalGrid() {
		// TODO Auto-generated method stub
		Random r = new Random();
        grid = new TreeMap<Integer,Cell>();
        double side = 0;
        int colorDivision = 255 / sugarMax;
    	if(rowCount%2==0&&colCount%2==0){
    		side = height/(rowCount)/Math.sqrt(3);
    	}
    	for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < colCount; j++) {
				int k = i + j * rowCount;
				int randNum = r.nextInt(sugarMax + 1);
				int randNum2 = r.nextInt(101);
				if (randNum2 > agentSpawnProb) {
					grid.put(k, new HexagonalCell(i,j,side,new SugarScapeContent(-1,
							Color.rgb(0, 255 - randNum * colorDivision, 255), randNum,randNum, colorDivision,1, null)));
				} else {
					grid.put(k, new HexagonalCell(i, j,side,
							new SugarScapeContent(-1, agentColor, randNum,randNum, colorDivision, 1, new Agent(8, 2, 4))));
					agentPopulation++;
				}
			}
		}

	}

	@Override
	public void graphStats(Series<Number, Number> graphLocation) {
		graphLocation.getData().add(new XYChart.Data(steps,agentPopulation));
	}

}
