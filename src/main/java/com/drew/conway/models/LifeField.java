package com.drew.conway.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.logging.Logger;

public class LifeField {

    private static final Logger logger = Logger.getLogger(LifeField.class.getName());
    private int columns;
    private int rows;
    private int gridSize;
    public LifeCell[][] lifeGrid;

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


    public void updateConwayField() {
//        logger.info("Updating Conway field...");
        boolean[][] nextGeneration = new boolean[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                int neighbors = CountNeighbors(row, col);
                //Conway's Life rules applied here
                if (!lifeGrid[row][col].isAlive.get() && neighbors == 3) {
                    // a dead cell with three neighbors comes alive
                    nextGeneration[row][col] = true;
//                    AddNeighborhoodToQueue(SecondaryActiveSet, row, column);
                    populationCount++;
                } else if (lifeGrid[row][col].isAlive.get() && (neighbors == 3 || neighbors == 2)) {
                    // a live cell with two,three neighbors stay alive
                    nextGeneration[row][col] = true;
                } else if (lifeGrid[row][col].isAlive.get()) {
                    // a cell dies because of loneliness or overcrowding
                    nextGeneration[row][col] = false;
//                    AddNeighborhoodToQueue(SecondaryActiveSet, row, column);
                    populationCount--;
                }
            }
        }
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                if (nextGeneration[row][col]) {
                    lifeGrid[row][col].isAlive.set(true);
                } else {
                    lifeGrid[row][col].isAlive.set(false);
                }
            }
        }
        iterations.setValue(iterations.get() + 1);
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
