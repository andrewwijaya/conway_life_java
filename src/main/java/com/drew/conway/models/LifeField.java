package com.drew.conway.models;

import com.drew.conway.util.Utilities;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.Arrays;
import java.util.logging.Logger;

public class LifeField {

    private static final Logger log = Logger.getLogger(LifeField.class.getName());
    private int columns;
    private int rows;
    private int gridSize;
    public LifeCell[][] lifeGrid;
    private boolean[][][] GridHistory;
    private int[][] PreviousGridPeriods;
    private int gridHistoryCounter = 0;

    //How many evolutions has been performed
    private IntegerProperty iterations;

    //Number of active cells in the grid
    private int activeCellCount;

    //Number of live cells in the grid
    private int populationCount;

    //Flag to determine whether or not a grid is stable
    private boolean stable;

    public LifeField(int size) {
        columns = size;
        rows = size;
    }

    public LifeField(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
        //Grid always has equal number of rows and columns
        this.gridSize = rows;
        lifeGrid = new LifeCell[rows][columns];
        iterations = new SimpleIntegerProperty();
        GridHistory = new boolean[32][rows][columns];
    }

    public int getIterations() {
        return iterations.get();
    }

    public IntegerProperty getIterationProperty() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations.setValue(iterations);
    }

    public int getActiveCellCount() {
        return activeCellCount;
    }

    public void setActiveCellCount(int activeCellCount) {
        this.activeCellCount = activeCellCount;
    }

    public int getPopulationCount() {
        return populationCount;
    }

    public void setPopulationCount(int populationCount) {
        this.populationCount = populationCount;
    }

    public boolean isStable() {
        return stable;
    }

    public void setStable(boolean stable) {
        this.stable = stable;
    }

    public int getGridSize() {
        return columns * rows;
    }

    /**
     * Method to update the grid using Conway's GOL rules
     */
    public void updateConwayField() {
        //Creates an intermediary two dimensional boolean array called nextGeneration, this is to prevent overwriting
        //the current grid
        boolean[][] nextGeneration = new boolean[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                int neighbors = CountNeighbors(row, col);
                if (!lifeGrid[row][col].isAlive.get() && neighbors == 3) {
                    nextGeneration[row][col] = true;
//                    AddNeighborhoodToQueue(SecondaryActiveSet, row, column);
                    populationCount++;
                } else if (lifeGrid[row][col].isAlive.get() && (neighbors == 3 || neighbors == 2)) {
                    nextGeneration[row][col] = true;
                } else if (lifeGrid[row][col].isAlive.get()) {
                    nextGeneration[row][col] = false;
//                    AddNeighborhoodToQueue(SecondaryActiveSet, row, column);
                    populationCount--;
                }
            }
        }
        //Once the nextGeneration grid has been generated, use the new values to update the actual UI model
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                if (nextGeneration[row][col]) {
                    lifeGrid[row][col].isAlive.set(true);
                } else {
                    lifeGrid[row][col].isAlive.set(false);
                }
            }
        }
        //Fills the GridHistory untill 32 grids have been stacked
        if (gridHistoryCounter < 32) {
            log.info("Adding to grid history..., counter: " + gridHistoryCounter);
            boolean[][] newGrid = nextGeneration;
            GridHistory[gridHistoryCounter] = newGrid;
            gridHistoryCounter++;
        } else {
            String[][] strhistory = new String[rows][columns];
            //every 32 steps, set all colorStates to 0, which determines the colour of periodicity (not liveliness)
            for (int row = 0; row < rows ; row++){
                for (int col = 0; col < columns ; col++){
                    lifeGrid[row][col].colorState.setValue(0);
                }
            }

            //Check every cell in the grid
            int[][] GridPeriodicity = new int[rows][columns];
            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {
                    StringBuilder history = new StringBuilder();
                    for (boolean[][] snapshot : GridHistory)
                    {
                        if (snapshot[row][column])
                        {
                            history.append("1");
                        }
                            else {
                            history.append("0");
                        }
                    }
                    String cellHistory = history.toString();
                    strhistory[row][column] = cellHistory;
                    int period = Utilities.DeterminePeriodicity(cellHistory);
                    GridPeriodicity[row][column] = period;
                    //???
                    String strperiod = String.valueOf(period);
                }
            }

            //Check for stability using periodicity of grid
            if (PreviousGridPeriods != null) {
                int[][] emptyGrid = new int[rows][columns];

/*                //Check if the grid is equal to each other
                boolean equalArrays = PreviousGridPeriods.Rank == GridPeriodicity.Rank &&
                        Enumerable.Range(0, PreviousGridPeriods.Rank).All(dimension = > PreviousGridPeriods.GetLength(dimension) == GridPeriodicity.GetLength(dimension)) &&
                PreviousGridPeriods.Cast<int> ().SequenceEqual(GridPeriodicity.Cast < int>());

                //Make sure that the grid is not an empty grid (unknown periods)
                boolean isEmpty = PreviousGridPeriods.Rank == emptyGrid.Rank &&
                        Enumerable.Range(0, PreviousGridPeriods.Rank).All(dimension = > PreviousGridPeriods.GetLength(dimension) == emptyGrid.GetLength(dimension)) &&
                PreviousGridPeriods.Cast<int> ().SequenceEqual(emptyGrid.Cast < int>());*/

                //TODO: not sure if this is equivalent to above
                boolean isArraysEqual = Arrays.equals(PreviousGridPeriods, GridPeriodicity);
                boolean isArraysEmpty = Arrays.equals(PreviousGridPeriods, emptyGrid);

                if (isArraysEqual && !isArraysEmpty) {
                    stable = true;
                } else {
                    stable = false;
                }
            }
            PreviousGridPeriods = new int[rows][columns];
            PreviousGridPeriods = GridPeriodicity;

            //At this point we have the periods of every cell in the grid. We now need to apply LCM to every cell
            //whose neighbor has a periodicity of > 0
            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {
                    if (GridPeriodicity[row][column] > 0)
                    {
                        //If current cell is not visited, then visit neighbors and get LCM
                        if (!lifeGrid[row][column].visited.get())
                        {
                            List<int> test = NeighboringPeriods(row, column);
                            int[] PeriodCollection = test.ToArray();
                            int LCMPeriod;
                            if (PeriodCollection.Length > 1) {
                                try {
                                    LCMPeriod = Utilities.lowestCommonMultiple(PeriodCollection);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                ColorOscillator(row, column, LCMPeriod);
                            }
                        }
                    }
                }
            }
            GridPeriodicity = new int[rows][columns];
            for (int row = 0; row < rows ; row++){
                for (int col = 0; col < columns ; col++){
                    lifeGrid[row][col].visited.set(false);
                    lifeGrid[row][col].lcmcolored.set(false);
                }
            }
            //TODO
//            GridHistory.Clear();
        }
        iterations.setValue(iterations.get() + 1);
    }

    //This method returns a list of the periods of the neighbors
    private List<int> NeighboringPeriods(int row, int column) {
        if (GridPeriodicity[row,column] >0)
        {
            LifeCell Current = _cells[row, column];
            LifeCell N = _cells[WrapMinusOne(row), column];
            LifeCell S = _cells[WrapPlusOne(row), column];
            LifeCell W = _cells[row, WrapMinusOne (column)];
            LifeCell E = _cells[row, WrapPlusOne (column)];
            LifeCell NE = _cells[WrapMinusOne(row), WrapPlusOne (column)];
            LifeCell SE = _cells[WrapPlusOne(row), WrapPlusOne (column)];
            LifeCell SW = _cells[WrapPlusOne(row), WrapMinusOne (column)];
            LifeCell NW = _cells[WrapMinusOne(row), WrapMinusOne (column)];

            List<int> PeriodArray = new List<int>();
            //Current

            PeriodArray.Add(GridPeriodicity[row, column]);
            Current.visited = true;


            // check cell to upper left
            if ((!NW.visited) && GridPeriodicity[WrapMinusOne(row),WrapMinusOne(column)] >0)
            {
                PeriodArray = PeriodArray.Concat(NeighboringPeriods(WrapMinusOne(row), WrapMinusOne(column))).ToList();
            }

            // check cell to upper right
            if ((!NE.visited) && GridPeriodicity[WrapMinusOne(row),WrapPlusOne(column)] >0)
            {
                PeriodArray = PeriodArray.Concat(NeighboringPeriods(WrapMinusOne(row), WrapPlusOne(column))).ToList();
            }

            //check cell above
            if ((!N.visited) && GridPeriodicity[WrapMinusOne(row),column] >0)
            {
                PeriodArray = PeriodArray.Concat(NeighboringPeriods(WrapMinusOne(row), column)).ToList();
            }

            // check cell below
            if ((!S.visited) && GridPeriodicity[WrapPlusOne(row),column] >0)
            {
                PeriodArray = PeriodArray.Concat(NeighboringPeriods(WrapPlusOne(row), column)).ToList();
            }

            // check cell to left
            if ((!W.visited) && GridPeriodicity[row,WrapMinusOne(column)] >0)
            {
                PeriodArray = PeriodArray.Concat(NeighboringPeriods(row, WrapMinusOne(column))).ToList();
            }

            // check cell to right
            if ((!E.visited) && GridPeriodicity[row,WrapPlusOne(column)] >0)
            {
                PeriodArray = PeriodArray.Concat(NeighboringPeriods(row, WrapPlusOne(column))).ToList();
            }

            // check cell to bottom left
            if ((!SW.visited) && GridPeriodicity[WrapPlusOne(row),WrapMinusOne(column)] >0)
            {
                PeriodArray = PeriodArray.Concat(NeighboringPeriods(WrapPlusOne(row), WrapMinusOne(column))).ToList();
            }

            // check cell to bottom right
            if ((!SE.visited) && GridPeriodicity[WrapPlusOne(row),WrapPlusOne(column)] >0)
            {
                PeriodArray = PeriodArray.Concat(NeighboringPeriods(WrapPlusOne(row), WrapPlusOne(column))).ToList();
            }

            return PeriodArray;
        }
            else
        {
            return new List<int>();
        }

    }

    //Counts the number of live neighbors of a particular cell
    private int CountNeighbors(int row, int column) {
        // total up the number of neighbors who are alive
        int neighbors = 0;

        // check cell to upper left
        if (lifeGrid[WrapMinusOne(row, rows)][WrapMinusOne(column, columns)].isAlive.get())
            neighbors++;

        //check cell above
        if (lifeGrid[WrapMinusOne(row, rows)][column].isAlive.get())
            neighbors++;

        // check cell to upper right
        if (lifeGrid[WrapMinusOne(row, rows)][WrapPlusOne(column, columns)].isAlive.get())
            neighbors++;

        // check cell to left
        if (lifeGrid[row][WrapMinusOne(column, columns)].isAlive.get())
            neighbors++;

        // check cell to right
        if (lifeGrid[row][WrapPlusOne(column, columns)].isAlive.get())
            neighbors++;

        // check cell to bottom left
        if (lifeGrid[WrapPlusOne(row, rows)][WrapMinusOne(column, columns)].isAlive.get())
            neighbors++;

        // check cell below
        if (lifeGrid[WrapPlusOne(row, rows)][column].isAlive.get())
            neighbors++;

        // check cell to bottom right
        if (lifeGrid[WrapPlusOne(row, rows)][WrapPlusOne(column, columns)].isAlive.get())
            neighbors++;
        return neighbors;
    }

    private int WrapMinusOne(int value, int max) {
        if (value == 0)
            value = max;
        return value - 1;
    }

    private int WrapPlusOne(int value, int max) {
        if (value >= max - 1)
            value = -1;
        return value + 1;
    }
}
