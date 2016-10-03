package CellularAutomata;

import java.util.*;
import Cell.Cell;
import Cell.HexagonalCell;
import Cell.SquareCell;
import Cell.TriangleCell;
import CellContent.CellContent;
import CellContent.FireContent;
import CellContent.ForagingAntsContent;
import CellContent.GameOfLifeContent;
import CellContent.PredatorPreyContent;
import CellContent.SegregationContent;
import CellContent.SugarScapeContent;
import javafx.scene.chart.XYChart.Series;

public abstract class CellularAutomata {
	public static final int[] X_HEX_COORD = new int[] { -1, -2, -1, 1, 2, 1 };
	public static final int[] Y_HEX_ODDROW_COORD = new int[] { 0, 0, 1, 1, 0, 0 };
	public static final int[] Y_HEX_EVENROW_COORD = new int[] { -1, 0, 0, 0, 0, -1 };
	public static final int[] X_TRI_CARD_COORD = new int[] { 0, 1, -1 };
	public static final int[] Y_TRI_CARD_DOWN_COORD = new int[] { -1, 0, 0 };
	public static final int[] Y_TRI_CARD_UP_COORD = new int[] { 1, 0, 0 };
	public static final int[] X_SQ_CARD_COORD = new int[] { -1, 0, 0, 1 };
	public static final int[] Y_SQ_CARD_COORD = new int[] { 0, -1, 1, 0 };
	public static final int[] X_SQ_DIAG_COORD = new int[] { -1, -1, 1, 1 };
	public static final int[] Y_SQ_DIAG_COORD = new int[] { -1, 1, -1, 1 };
	public static final int[] X_SQ_COORD = new int[] { -1, -1, -1, 0, 0, 1, 1, 1 };
	public static final int[] Y_SQ_COORD = new int[] { -1, 0, 1, -1, 1, -1, 0, 1 };
	protected Map<Integer, Cell> grid;
	protected int width;
	protected int height;
	protected int rowCount;
	protected int colCount;
	protected String tileType, edgeType;
	protected int probType, probEmpty;
	protected boolean useProbType, useProbEmpty;
	protected String neighborDirection;

	public abstract void update();

	public abstract void createSquareGrid();

	public abstract void createTriangleGrid();

	public abstract void createHexagonalGrid();

	public abstract void graphStats(Series<Number, Number> graphLocation);

	public void reset() {
		placeCells();
	}

	public void setGrid(Map<Integer, Cell> g) {
		grid = g;
	}

	public Map<Integer, Cell> getGrid() {
		return grid;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getRows() {
		return rowCount;
	}

	public int getCol() {
		return colCount;
	}

	public String returntileType() {
		return tileType;
	}

	public String getEdgeType() {
		return edgeType;
	}

	public void setProbType(int prob) {
		probType = prob;
	}

	public int getProbType() {
		return probType;
	}

	public void setProbEmpty(int prob) {
		probEmpty = prob;
	}

	public int getProbEmpty() {
		return probEmpty;
	}

	public boolean getUseProbType() {
		return useProbType;
	}

	public boolean getUseProbEmpty() {
		return useProbEmpty;
	}

	public void placeCells() {
		if (tileType.equals("square")) {
			createSquareGrid();
		} else if (tileType.equals("triangle")) {
			createTriangleGrid();
		} else if (tileType.equals("hexagonal")) {
			createHexagonalGrid();
		}
	}

	public List<Cell> checkNeighbors(Cell cell, boolean needNull) {
		int cellRow = cell.getRow();
		int cellCol = cell.getCol();
		int[][] NeighborCoordinates = getNeighborCoordinates(cell, needNull);

		List<Cell> neighbors = new ArrayList<Cell>();
		for (int z = 0; z < NeighborCoordinates[0].length; z++) {
			int x = NeighborCoordinates[0][z] + cellRow;
			int y = NeighborCoordinates[1][z] + cellCol;

			if (edgeType.equals("toroidal")) {
				if (x < 0) {
					x = rowCount + x;
				}
				if (y < 0) {
					y = colCount + y;
				}
				if (x >= rowCount) {
					x = x - rowCount;
				}
				if (y >= colCount) {
					y = y - colCount;
				}
				neighbors.add(grid.get(x + y * rowCount));
			} else {
				if (x >= 0 && y >= 0 && y < colCount && x < rowCount) {
					neighbors.add(grid.get(x + y * rowCount));
				} else if (needNull) {
					neighbors.add(null);
				}
			}
		}
		return neighbors;
	}

	// used to create copy of original board and reset
	public Map<Integer, Cell> cloneGrid(Map<Integer, Cell> a) {
		if (a.get(0) instanceof HexagonalCell) {
			return cloneHexagonal(a);
		} else if (a.get(0) instanceof TriangleCell) {
			return cloneTriangle(a);
		} else {
			return cloneSquare(a);
		}
	}

	private int[][] getNeighborCoordinates(Cell cell, boolean needNull) {
		int cellRow = cell.getRow();
		int cellCol = cell.getCol();
		int[] xNeighbors;
		int[] yNeighbors;

		if (cell instanceof HexagonalCell) {
			xNeighbors = X_HEX_COORD;
			if (cellRow % 2 == 1) {
				yNeighbors = Y_HEX_ODDROW_COORD;
			} else {
				yNeighbors = Y_HEX_EVENROW_COORD;
			}
		} else if (cell instanceof TriangleCell && neighborDirection.equals("cardinal")) {
			xNeighbors = X_TRI_CARD_COORD;
			if ((cellRow + cellCol) % 2 == 0) {// downward triangle
				yNeighbors = Y_TRI_CARD_DOWN_COORD;
			} else {
				yNeighbors = Y_TRI_CARD_UP_COORD;
			}
		} else if (!(cell instanceof TriangleCell) && neighborDirection.equals("cardinal")) {
			xNeighbors = X_SQ_CARD_COORD;
			yNeighbors = Y_SQ_CARD_COORD;
		} else if (!(cell instanceof TriangleCell) && neighborDirection.equals("diagonal")) {
			xNeighbors = X_SQ_DIAG_COORD;
			yNeighbors = Y_SQ_DIAG_COORD;
		} else {
			xNeighbors = X_SQ_COORD;
			yNeighbors = Y_SQ_COORD;
		}
		return new int[][] { xNeighbors, yNeighbors };
	}

	private Map<Integer, Cell> cloneHexagonal(Map<Integer, Cell> a) {
		Map<Integer, Cell> b = new TreeMap<Integer, Cell>();
		for (int i = 0; i < a.keySet().size(); i++) {
			HexagonalCell tempCell = (HexagonalCell) a.get(i);
			b.put(i, new HexagonalCell(tempCell.getRow(), tempCell.getCol(), tempCell.getSide(),
					cloneContent(tempCell)));

		}
		return b;
	}

	private Map<Integer, Cell> cloneTriangle(Map<Integer, Cell> a) {
		Map<Integer, Cell> b = new TreeMap<Integer, Cell>();
		for (int i = 0; i < a.keySet().size(); i++) {
			TriangleCell tempCell = (TriangleCell) a.get(i);
			b.put(i, new TriangleCell(tempCell.getX(), tempCell.getY(), tempCell.getWidth(), tempCell.getHeight(),
					tempCell.getRow(), tempCell.getCol(), cloneContent(tempCell)));
		}
		return b;
	}

	private Map<Integer, Cell> cloneSquare(Map<Integer, Cell> a) {
		Map<Integer, Cell> b = new TreeMap<Integer, Cell>();
		for (int i = 0; i < a.keySet().size(); i++) {
			Cell tempCell = a.get(i);
			b.put(i, new SquareCell(tempCell.getX(), tempCell.getY(), tempCell.getRow(), tempCell.getCol(),
					cloneContent(tempCell)));

		}
		return b;
	}

	private CellContent cloneContent(Cell cell) {
		CellContent content = cell.getContent();
		if (content instanceof GameOfLifeContent) {
			return new GameOfLifeContent(cell.getContent().getState(), cell.getContent().getColor());
		} else if (content instanceof FireContent) {
			return new FireContent(cell.getContent().getState(), cell.getContent().getColor());
		} else if (content instanceof ForagingAntsContent) {
			ForagingAntsContent fac = ((ForagingAntsContent) cell.getContent());
			return new ForagingAntsContent(cell.getContent().getState(), cell.getContent().getColor(), fac.getIsHome(),
					fac.getIsFoodSource(), fac.getMaxPheromone(), fac.getFoodPheromone(), fac.getHomePheromone(),
					fac.getAntsClone());
		} else if (content instanceof PredatorPreyContent) {
			return new PredatorPreyContent(cell.getContent().getState(), cell.getContent().getColor(),
					((PredatorPreyContent) cell.getContent()).getSharkEnergy());
		} else if (content instanceof SugarScapeContent) {
			return new SugarScapeContent(cell.getContent().getState(), cell.getContent().getColor(),
					((SugarScapeContent) cell.getContent()).getSugar(),
					((SugarScapeContent) cell.getContent()).getMaxSugar(),
					((SugarScapeContent) cell.getContent()).getColorDivision(),
					((SugarScapeContent) cell.getContent()).getGrowRate(),
					((SugarScapeContent) cell.getContent()).getAgent());
		} else {
			return new SegregationContent(cell.getContent().getState(), cell.getContent().getColor());
		}
	}
}
