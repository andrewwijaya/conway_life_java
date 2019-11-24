/*
 * This class contains information about the current simulation such as
 * current number of live cells (population), current number of active cells,
 * whether or not the simulation is stable. A lot of these fields are attached
 * by a binding to the main user interface.
 */
package com.drew;

//INotifyPropertyChanged
//Data in this class needs to be bound to the UI
public class LifeField {
    public int Columns;
    public int Rows;

    //How many evolutions has been performed
    private int iterations;

    //Number of active cells in the grid
    private int activeCellCount;

    //Number of live cells in the grid
    private int populationCount;

    //Flag to determine whether or not a grid is stable
    private boolean stable;

    public LifeField(int size) {
        Columns = size;
        Rows = size;
    }

    public LifeField(int columns, int rows) {
        Columns = columns;
        Rows = rows;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
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
}
