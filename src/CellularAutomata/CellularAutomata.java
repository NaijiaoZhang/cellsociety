package CellularAutomata;

import java.util.*;
import Cell.Cell;

public abstract class CellularAutomata {
	private Cell[][] Grid, startGrid;
	private int width;
	private int height;
	private int rowCount;
	private int colCount;

	public CellularAutomata(int rows, int columns, int wid, int hei) {
		Grid = new Cell[rows][columns];
		width = wid;
		height = hei;
		rowCount = rows;
		colCount = columns;
		// TODO Auto-generated constructor stub
	}

	public CellularAutomata() {
		this(25, 25, 500, 500);
	}

	public Cell[][] step() {
		return Grid;
	}

	public List<Cell> checkNeighborsDiagonal(Cell cell) {
		int i = cell.getRow();
		int j = cell.getCol();
		List<Cell> neighbors = new ArrayList<Cell>();
		if (i > 0) {
			neighbors.add(Grid[i - 1][j]);
			if (j > 0) {
				neighbors.add(Grid[i - 1][j - 1]);
			}
			if (j < rowCount - 1) {
				neighbors.add(Grid[i - 1][j + 1]);
			}
		}
		if (j > 0) {
			neighbors.add(Grid[i][j - 1]);
		}
		if (j < rowCount - 1) {
			neighbors.add(Grid[i][j + 1]);
		}
		if (i < colCount - 1) {
			neighbors.add(Grid[i + 1][j]);
			if (j > 0) {
				neighbors.add(Grid[i + 1][j - 1]);
			}
			if (j < rowCount - 1) {
				neighbors.add(Grid[i + 1][j + 1]);
			}
		}
		return neighbors;
	}

	public List<Cell> checkNeighborsCardinal(Cell cell) {
		int i = cell.getRow();
		int j = cell.getCol();
		List<Cell> neighbors = new ArrayList<Cell>();
		if (i > 0) {
			neighbors.add(Grid[i - 1][j]);
		}
		if (j > 0) {
			neighbors.add(Grid[i][j - 1]);
		}
		if (j < rowCount - 1) {
			neighbors.add(Grid[i][j + 1]);
		}
		if (i < colCount - 1) {
			neighbors.add(Grid[i + 1][j]);
		}
		return neighbors;
	}

	public abstract void update();

	public void setGrid(Cell[][] g) {
		Grid = g;
	}

	public Cell[][] getGrid() {
		return Grid;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
