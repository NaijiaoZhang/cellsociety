package CellularAutomata;

import Cell.Cell;
import Cell.HexagonalCell;
import Cell.SquareCell;
import Cell.TriangleCell;
import CellContent.FireContent;

import java.util.*;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.paint.Color;

public class FireRules extends CellularAutomata {
	private int probCatchesFire;
	private Color burning;
	private Color burnt;
	private Color burnable;
	private int steps, treesAlive;

	public FireRules(int rows, int columns, int wid, int hei, int probCatch, String tile, String edge, Color burningColor,Color burnableColor,Color burntColor, String dir) {
		height = hei;
		width = wid;
		rowCount = rows;
		colCount = columns;
		tileType = tile;
		edgeType = edge;
		probCatchesFire = probCatch;
		useProbType = false;
		useProbEmpty = false;
		burning=burningColor;
		burnt=burntColor;
		burnable=burnableColor;
		neighborDirection=dir;
		steps = 0;
		treesAlive = 0;
		placeCells();
	}

	@Override
	public void update() {
		steps++;
		Map<Integer, Cell> gridClone = cloneGrid(grid);
		Random r = new Random();
		for (int i = 0; i < grid.keySet().size(); i++) {
			if (grid.get(i).getContent().getState() == 2) {
				gridClone.get(i).getContent().setStateandColor(0, burnt);
			} else if (grid.get(i).getContent().getState() == 1 && fireNeighbors(grid.get(i)) > 0
					&& r.nextInt(100) < probCatchesFire) {
				gridClone.get(i).getContent().setStateandColor(2, burning);

			}
		}
		updatePopulationSize();
		grid = gridClone;
	}

	public void createSquareGrid() {
		int w = width / colCount;
		int h = height / rowCount;
		grid = new TreeMap<Integer, Cell>();
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < colCount; j++) {
				int k = i + j * rowCount;
				if (j == 0 || j == colCount - 1 || i == 0 || i == rowCount - 1) {
					grid.put(k, new SquareCell(i * w, j * h, i, j, new FireContent(0, burnt)));
				} else if (i == rowCount / 2 && j == colCount / 2) {
					grid.put(k, new SquareCell(i * w, j * h, i, j, new FireContent(2, burning)));
				} else {
					grid.put(k, new SquareCell(i * w, j * h, i, j, new FireContent(1, burnable)));
					treesAlive++;
				}
			}
		}
	}

	@Override
	public void createTriangleGrid() {
		int w = width / colCount;
		int h = height / rowCount;
		grid = new TreeMap<Integer, Cell>();
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < colCount; j++) {
				int k = i + j * rowCount;
				if ((j == 0 && i % 2 == 0) || (j == colCount - 1 && (rowCount - i) % 2 == 0) || i == 0
						|| i == rowCount - 1) {
					grid.put(k, new TriangleCell(i * w, j * h, w, h, i, j, new FireContent(0, burnt)));
				} else if (i == rowCount / 2 && j == colCount / 2) {
					grid.put(k, new TriangleCell(i * w, j * h, w, h, i, j, new FireContent(2, burning)));
				} else {
					grid.put(k, new TriangleCell(i * w, j * h, w, h, i, j, new FireContent(1, burnable)));
					treesAlive++;
				}
			}
		}
	}

	@Override
	public void createHexagonalGrid() {
		double side = 0;
		if (rowCount % 2 == 0 && colCount % 2 == 0) {
			side = height / (rowCount) / Math.sqrt(3);
		}
		grid = new TreeMap<Integer, Cell>();
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < colCount; j++) {
				int k = i + j * rowCount;
				if ((j == 0 && i % 2 == 0) || (j == colCount - 1 && (rowCount - i) % 2 == 1) || i <= 1
						|| i >= rowCount - 2) {
					grid.put(k, new HexagonalCell(i, j, side, new FireContent(0, burnt)));
				} else if (i == rowCount / 2 && j == colCount / 2) {
					grid.put(k, new HexagonalCell(i, j, side, new FireContent(2, burning)));
				} else {
					grid.put(k, new HexagonalCell(i, j, side, new FireContent(1, burnable)));
					treesAlive++;
				}
			}
		}
	}

	private void updatePopulationSize() {
		int count = 0;
		for (int i = 0; i < grid.keySet().size(); i++) {
			if (grid.get(i).getContent().getState() == 1) {
				count++;
			}
		}
		treesAlive = count;
	}

	private int fireNeighbors(Cell cell) {
		int fire = 0;
		List<Cell> neighbors = checkNeighbors(cell, false);
		for (Cell c : neighbors) {
			if (c.getContent().getState() == 2) {
				fire++;
			}
		}
		return fire;
	}

    public int getProbCatchesFire(){
        return probCatchesFire;
    }
    public void setProbCatchesFire(int f){
        probCatchesFire = f;
    }
	@Override
	public void graphStats(Series<Number, Number> graphLocation) {
		// TODO Auto-generated method stub
		graphLocation.getData().add(new XYChart.Data(steps, treesAlive));
	}

}
