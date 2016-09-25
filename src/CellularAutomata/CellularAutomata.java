package CellularAutomata;

import java.util.*;
import Cell.Cell;

public abstract class CellularAutomata {
	protected Cell[][] grid,startGrid;
	protected int width;
	protected int height;
	protected int rowCount;
	protected int colCount;

	public abstract void update();
	
        public void reset(){
            grid=cloneGrid(startGrid);
        }    

        public void setGrid(Cell[][] g) {
                grid = g;
        }

        public Cell[][] getGrid() {
                return grid;
        }

        public int getWidth() {
                return width;
        }

        public int getHeight() {
                return height;
        }
	       
	public List<Cell> checkNeighborsAll(Cell cell) {
		int i = cell.getRow();
		int j = cell.getCol();
		int[] a = {-1,-1,-1,0,0,1,1,1};
		int[] b = {-1,0,1,-1,1,-1,0,1};
		List<Cell> neighbors = new ArrayList<Cell>();
		for(int z = 0; z<8; z++){
		    int x = a[z]+i;
		    int y = b[z]+j;
		    if(x>=0 && y>=0 && y<colCount && x<rowCount){
		        neighbors.add(grid[x][y]);
		    }
		}
		return neighbors;
	}

	public List<Cell> checkNeighborsCardinal(Cell cell) {
		int i = cell.getRow();
		int j = cell.getCol();
		List<Cell> neighbors = new ArrayList<Cell>();
		if (i > 0) {
			neighbors.add(grid[i - 1][j]);
		}
		if (j > 0) {
			neighbors.add(grid[i][j - 1]);
		}
		if (j < rowCount - 1) {
			neighbors.add(grid[i][j + 1]);
		}
		if (i < colCount - 1) {
			neighbors.add(grid[i + 1][j]);
		}
		return neighbors;
	}

	
	//used to create copy of original board and reset
        public Cell[][] cloneGrid(Cell[][] a){
            Cell[][] b = new Cell[a.length][a[0].length];
            for (int i = 0; i < a.length; i++) {
                for (int j = 0; j < a[0].length; j++) {
                    Cell tempCell=a[i][j];
                    b[i][j]=new Cell(tempCell.getState(),tempCell.getColor(),tempCell.getX(),tempCell.getY(),tempCell.getRow(),tempCell.getCol());
                }
            }
            return b;
        }
}
