package com.drew.conway.models;

import com.drew.conway.util.Utilities;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.logging.Logger;

public class LifeField {

    private static final Logger log = Logger.getLogger(LifeField.class.getName());
    private int columns;
    private int rows;
    private int gridSize;
    public LifeCell[][] lifeGrid;
    private boolean[][][] GridHistory;
    private int[][] PreviousGridPeriods;
    private int[][] GridPeriodicity;
    private int gridHistoryCounter = 0;
    public Set<Point> activeSet = new HashSet<>();
    private Set<Point> secondarySet = new HashSet<>();

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
        GridPeriodicity = new int[rows][columns];
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

    public void printTwoDimensionalGrid(boolean[][] grid){
        System.out.println();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                String i = grid[row][col] ? "X" : " ";
                System.out.print("[" + i + "]");
            }
            System.out.println();
        }
    }

    public void printTwoDimensionalIntGrid(int[][] grid){
        System.out.println();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                System.out.print("[" + grid[row][col] + "]");
            }
            System.out.println();
        }
    }

    /**
     * Method to update the grid using Conway's GOL rules
     */
    public void updateConwayField() {
        //Creates an intermediary two dimensional boolean array called nextGeneration, this is to prevent overwriting
        //the current grid
        boolean[][] nextGeneration = new boolean[rows][columns];
        for (Point point : activeSet) {
            int row = point.x;
            int col = point.y;
            int neighbors = CountNeighbors(row, col);
            if (!lifeGrid[row][col].isAlive.get() && neighbors == 3) {
                nextGeneration[row][col] = true;
                AddNeighborhoodToQueue(secondarySet, row, col);
                populationCount++;
            } else if (lifeGrid[row][col].isAlive.get() && (neighbors == 3 || neighbors == 2)) {
                nextGeneration[row][col] = true;
            } else if (lifeGrid[row][col].isAlive.get()) {
                nextGeneration[row][col] = false;
                AddNeighborhoodToQueue(secondarySet, row, col);
                populationCount--;
            }

        }
        //Once the nextGeneration grid has been generated, use the new values to update the actual UI model
        for (Point point : activeSet) {
            int row = point.x;
            int col = point.y;
            if (nextGeneration[row][col]) {
                lifeGrid[row][col].isAlive.set(true);
            } else {
                lifeGrid[row][col].isAlive.set(false);
            }
        }
        activeSet.clear();
        activeSet = new HashSet<>(secondarySet);
        secondarySet.clear();
        //Fills the GridHistory untill 32 grids have been stacked
        if (gridHistoryCounter < 32) {
//            log.info("Adding to grid history..., counter: " + gridHistoryCounter);
            GridHistory[gridHistoryCounter] = nextGeneration;
            gridHistoryCounter++;
//            printTwoDimensionalGrid(nextGeneration);
        } else {
            log.info("Checking periodicity...");
            //TODO: is this needed?
//            String[][] strhistory = new String[rows][columns];
            //every 32 steps, set all colorStates to 0, which determines the colour of periodicity (not liveliness)
            for (int row = 0; row < rows ; row++){
                for (int col = 0; col < columns ; col++){
                    lifeGrid[row][col].colorState.setValue(0);
                }
            }

            //Check every cell in the grid
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
//                    strhistory[row][column] = cellHistory;
                    int period = Utilities.DeterminePeriodicity(cellHistory);
                    GridPeriodicity[row][column] = period;
                    //???
                    String strperiod = String.valueOf(period);
                }
            }

            //Check for stability using periodicity of grid
            if (PreviousGridPeriods != null) {
                int[][] emptyGrid = new int[rows][columns];
                log.info("Checking previous and current periodicity...");
                boolean isArraysEqual = Arrays.deepEquals(PreviousGridPeriods, GridPeriodicity);
//                boolean isArraysEmpty = Arrays.deepEquals(PreviousGridPeriods, emptyGrid);
                printTwoDimensionalIntGrid(PreviousGridPeriods);
                printTwoDimensionalIntGrid(GridPeriodicity);

//                if (isArraysEqual && !isArraysEmpty) {
                if (isArraysEqual) {
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
                            List<Integer> test = NeighboringPeriods(row, column);
                            int[] PeriodCollection = test.stream().mapToInt(i -> i).toArray();
                            //TODO: is this initialisation correct?
                            int LCMPeriod = 0;
                            if (PeriodCollection.length > 1) {
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
            gridHistoryCounter = 0;
            GridHistory = new boolean[32][rows][columns];
        }
        iterations.setValue(iterations.get() + 1);
    }

    //This method colors the patterns recursively
    private void ColorOscillator(int row, int column, int Period) {
        LifeCell Current = lifeGrid[row][column];
        LifeCell N = lifeGrid[WrapMinusOne(row, rows)][column];
        LifeCell S = lifeGrid[WrapPlusOne(row, rows)][column];
        LifeCell W = lifeGrid[row][ WrapMinusOne (column, columns)];
        LifeCell E = lifeGrid[row][ WrapPlusOne (column, columns)];
        LifeCell NE = lifeGrid[WrapMinusOne(row, rows)][ WrapPlusOne (column, columns)];
        LifeCell SE = lifeGrid[WrapPlusOne(row, rows)][ WrapPlusOne (column, columns)];
        LifeCell SW = lifeGrid[WrapPlusOne(row, rows)][ WrapMinusOne (column, columns)];
        LifeCell NW = lifeGrid[WrapMinusOne(row, rows)][ WrapMinusOne (column, columns)];

        Current.lcmcolored.setValue(true);
//        log.info("Setting period: " + Period);
        lifeGrid[row][ column].colorState.setValue(Period);

        // check cell to upper left
        if (!NW.lcmcolored.get() && GridPeriodicity[WrapMinusOne(row, rows)][WrapMinusOne(column, columns)] >0)
        {
            ColorOscillator(WrapMinusOne(row, rows), WrapMinusOne(column, columns), Period);
        }

        // check cell to upper right
        if (!NE.lcmcolored.get() && GridPeriodicity[WrapMinusOne(row,rows)][WrapPlusOne(column, columns)] >0)
        {
            ColorOscillator(WrapMinusOne(row, rows), WrapPlusOne(column, columns), Period);
        }

        //check cell above
        if (!N.lcmcolored.get() && GridPeriodicity[WrapMinusOne(row, rows)][column] >0)
        {
            ColorOscillator(WrapMinusOne(row, rows), column, Period);
        }

        // check cell below
        if (!S.lcmcolored.get() && GridPeriodicity[WrapPlusOne(row, rows)][column] >0)
        {
            ColorOscillator(WrapPlusOne(row, rows), column, Period);
        }

        // check cell to left
        if (!W.lcmcolored.get() && GridPeriodicity[row][WrapMinusOne(column, columns)] >0)
        {
            ColorOscillator(row, WrapMinusOne(column, columns), Period);
        }

        // check cell to right
        if (!E.lcmcolored.get() && GridPeriodicity[row][WrapPlusOne(column, columns)] >0)
        {
            ColorOscillator(row, WrapPlusOne(column, columns), Period);
        }

        // check cell to bottom left
        if (!SW.lcmcolored.get() && GridPeriodicity[WrapPlusOne(row, rows)][WrapMinusOne(column, columns)] >0)
        {
            ColorOscillator(WrapPlusOne(row, rows), WrapMinusOne(column, columns), Period);
        }

        // check cell to bottom right
        if (!SE.lcmcolored.get() && GridPeriodicity[WrapPlusOne(row, rows)][WrapPlusOne(column, columns)] >0)
        {
            ColorOscillator(WrapPlusOne(row, rows), WrapPlusOne(column, columns), Period);
        }
    }

    //This method returns a list of the periods of the neighbors
    private List<Integer> NeighboringPeriods(int row, int column) {
        if (GridPeriodicity[row][column] > 0) {
            LifeCell Current = lifeGrid[row][column];
            LifeCell N = lifeGrid[WrapMinusOne(row, rows)][column];
            LifeCell S = lifeGrid[WrapPlusOne(row, rows)][column];
            LifeCell W = lifeGrid[row][WrapMinusOne(column, columns)];
            LifeCell E = lifeGrid[row][WrapPlusOne(column, columns)];
            LifeCell NE = lifeGrid[WrapMinusOne(row, rows)][WrapPlusOne(column, columns)];
            LifeCell SE = lifeGrid[WrapPlusOne(row, rows)][WrapPlusOne(column, columns)];
            LifeCell SW = lifeGrid[WrapPlusOne(row, rows)][WrapMinusOne(column, columns)];
            LifeCell NW = lifeGrid[WrapMinusOne(row, rows)][WrapMinusOne(column, columns)];

            List<Integer> PeriodArray = new ArrayList<>();
            //Current

            PeriodArray.add(GridPeriodicity[row][column]);
            Current.visited.setValue(true);


            // check cell to upper left
            if ((!NW.visited.get()) && GridPeriodicity[WrapMinusOne(row, rows)][WrapMinusOne(column, columns)] >0)
            {
                PeriodArray.addAll(NeighboringPeriods(WrapMinusOne(row,rows), WrapMinusOne(column,columns)));
            }

            // check cell to upper right
            if ((!NE.visited.get()) && GridPeriodicity[WrapMinusOne(row,rows)][WrapPlusOne(column, columns)] >0)
            {
                PeriodArray.addAll(NeighboringPeriods(WrapMinusOne(row,rows), WrapPlusOne(column, columns)));
            }

            //check cell above
            if ((!N.visited.get()) && GridPeriodicity[WrapMinusOne(row, rows)][column] >0)
            {
                PeriodArray.addAll(NeighboringPeriods(WrapMinusOne(row,rows), column));
            }

            // check cell below
            if ((!S.visited.get()) && GridPeriodicity[WrapPlusOne(row, rows)][column] >0)
            {
                PeriodArray.addAll(NeighboringPeriods(WrapPlusOne(row, rows), column));
            }

            // check cell to left
            if ((!W.visited.get()) && GridPeriodicity[row][WrapMinusOne(column, columns)] >0)
            {
                PeriodArray.addAll(NeighboringPeriods(row, WrapMinusOne(column, columns)));
            }

            // check cell to right
            if ((!E.visited.get()) && GridPeriodicity[row][WrapPlusOne(column,columns)] >0)
            {
                PeriodArray.addAll(NeighboringPeriods(row, WrapPlusOne(column, columns)));
            }

            // check cell to bottom left
            if ((!SW.visited.get()) && GridPeriodicity[WrapPlusOne(row, rows)][WrapMinusOne(column, columns)] >0)
            {
                PeriodArray.addAll(NeighboringPeriods(WrapPlusOne(row, rows) ,WrapMinusOne(column, columns)));
            }

            // check cell to bottom right
            if ((!SE.visited.get()) && GridPeriodicity[WrapPlusOne(row, rows)][WrapPlusOne(column, columns)] >0)
            {
                PeriodArray.addAll(NeighboringPeriods(WrapPlusOne(row, rows), WrapPlusOne(column, columns)));
            }

            return PeriodArray;
        }
            else
        {
            return new ArrayList<>();
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

    //Adds a neighborhood to a HashSet, this is to support the evolution of active cells
    public void AddNeighborhoodToQueue(Set<Point> set, int row, int column) {
        set.add(new Point(row, column));
        set.add(new Point(row, WrapPlusOne(column, columns)));
        set.add(new Point(row, WrapMinusOne(column, columns)));

        set.add(new Point(WrapPlusOne(row, rows), column));
        set.add(new Point(WrapMinusOne(row, rows), column));
        set.add(new Point(WrapPlusOne(row, rows), WrapPlusOne(column, columns)));

        set.add(new Point(WrapMinusOne(row, rows), WrapMinusOne(column, columns)));
        set.add(new Point(WrapPlusOne(row, rows), WrapMinusOne(column, columns)));
        set.add(new Point(WrapMinusOne(row, rows), WrapPlusOne(column, columns)));
    }
}
