/*
 * The CellCollection class is a two dimensional container for LifeCell objects.
 * It contains important methods which relate to the evolution of the simulator,
 * applying Conway's Life rules and Oscillator and StillLife coloring.



package com.drew.conway;

import com.drew.conway.models.LifeCell;
import com.drew.conway.models.LifeField;
import javafx.util.Pair;
import java.util.HashSet;
import java.util.Set;

public class CellCollection {
    Set<Pair<Integer, Integer>> _ActiveSet = new HashSet<Pair<Integer, Integer>>();
    Set<Pair<Integer, Integer>> _SecondaryActiveSet = new HashSet<Pair<Integer, Integer>>();
    public HashSet<Pair<Integer, Integer>> ActiveSet;


    public HashSet<Pair<Integer, Integer>> getActiveSet() {
        return ActiveSet;
    }

    public void setActiveSet(HashSet<Pair<Integer, Integer>> activeSet) {
        ActiveSet = activeSet;
    }

    public HashSet<Pair<Integer, Integer>> getSecondaryActiveSet() {
        return SecondaryActiveSet;
    }

    public void setSecondaryActiveSet(HashSet<Pair<Integer, Integer>> secondaryActiveSet) {
        SecondaryActiveSet = secondaryActiveSet;
    }

    public HashSet<Pair<Integer, Integer>> SecondaryActiveSet;


    public LifeField field

    public LifeCell this[
    int row, int column]

    {
        get
        {
            //If out of range throw exception
            if (row < 0 || row >= _size ||
                    column < 0 || column >= _size) {
                throw new ArgumentOutOfRangeException();
            }

            //If not return a CellOfLife element
            return _cells[row,column];
        }
    }

    public int Size;
    private int _size;
    public LifeCell[,]_cells;
    public List<bool[,]>GridHistory;
    int[,]GridPeriodicity;
    int[,]PreviousGridPeriods;
    public bool[,]_nextGeneration;

    public CellCollection(int size) {
        field = new LifeField(size);
        _size = size;
        _cells = new LifeCell[_size, _size];
        _nextGeneration = new bool[_size, _size];
        GridHistory = new List<bool[,]>();
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                _cells[row, column] =new LifeCell();
                _cells[row, column].ColorState = 0;
                _cells[row, column].Visited = false;
                _cells[row, column].LCMColored = false;
            }
        }
        RandomField();
    }

    //Method responsible for evolving the logical grid.
    public void UpdateLife() {
        //Fills the GridHistory untill 32 grids have been stacked
        if (GridHistory.Count < 32) {
            bool[,]newGrid = new bool[_size, _size];
            newGrid = (bool[,])_nextGeneration.Clone();
            GridHistory.Add(newGrid);
        }
        //This is when 32 grids have been stacked and is ready to detect periodic behavior
        else {
            string[,]strhistory = new string[_size, _size];
            //every 32 steps, set all colorStates to 0
            foreach(LifeCell cell in _cells)
            {
                cell.ColorState = 0;
            }

            //Check every cell in the grid
            GridPeriodicity = new int[_size, _size];
            for (int row = 0; row < _size; row++) {
                for (int column = 0; column < _size; column++) {
                    StringBuilder history = new StringBuilder();
                    foreach(bool[,]snapshot in GridHistory)
                    {
                        if (snapshot[row,column] ==true)
                        {
                            history.Append("1");
                        }
                            else
                        {
                            history.Append("0");
                        }
                    }
                    string cellHistory = history.ToString();
                    strhistory[row, column] =cellHistory;
                    int period = Utilities.DeterminePeriodicity(cellHistory);
                    GridPeriodicity[row, column] =period;
                    string strperiod = period.ToString();
                }
            }

            //Check for stability using periodicity of grid
            if (PreviousGridPeriods != null) {
                int[,]emptyGrid = new int[_size, _size];

                //Check if the grid is equal to each other
                var equalArrays = PreviousGridPeriods.Rank == GridPeriodicity.Rank &&
                        Enumerable.Range(0, PreviousGridPeriods.Rank).All(dimension = > PreviousGridPeriods.GetLength(dimension) == GridPeriodicity.GetLength(dimension)) &&
                PreviousGridPeriods.Cast<int> ().SequenceEqual(GridPeriodicity.Cast < int>());

                //Make sure that the grid is not an empty grid (unknown periods)
                var isEmpty = PreviousGridPeriods.Rank == emptyGrid.Rank &&
                        Enumerable.Range(0, PreviousGridPeriods.Rank).All(dimension = > PreviousGridPeriods.GetLength(dimension) == emptyGrid.GetLength(dimension)) &&
                PreviousGridPeriods.Cast<int> ().SequenceEqual(emptyGrid.Cast < int>());

                if (equalArrays && !isEmpty) {
                    field.Stable = true;
                } else {
                    field.Stable = false;
                }
            }
            PreviousGridPeriods = new int[_size, _size];
            PreviousGridPeriods = ( int[,])GridPeriodicity.Clone();

            //At this point we have the periods of every cell in the grid. We now need to apply LCM to every cell
            //whose neighbor has a periodicity of > 0
            for (int row = 0; row < _size; row++) {
                for (int column = 0; column < _size; column++) {
                    if (GridPeriodicity[row,column] >0)
                    {
                        //If current cell is not visited, then visit neighbors and get LCM
                        if (!_cells[row,column].Visited)
                        {
                            List<int> test = NeighboringPeriods(row, column);
                            int[] PeriodCollection = test.ToArray();
                            int LCMPeriod;
                            if (PeriodCollection.Length > 1) {
                                LCMPeriod = Utilities.lowestCommonMultiple(PeriodCollection);
                                ColorOscillator(row, column, LCMPeriod);
                            }
                        }
                    }
                }
            }
            GridPeriodicity = new int[_size, _size];
            foreach(LifeCell cell in _cells)
            {
                cell.Visited = false;
                cell.LCMColored = false;
            }
            GridHistory.Clear();
        }

        //This is where only active cells are evolved
        foreach(Tuple < int,int>coord in ActiveSet)
        {
            int row = coord.Item1;
            int column = coord.Item2;
            int neighbors = CountNeighbors(row, column);

            //Conway's Life rules applied here
            if (!_cells[row,column].IsAlive && neighbors == 3)
            {
                // a dead cell with three neighbors comes alive
                _nextGeneration[row, column] =true;
                AddNeighborhoodToQueue(SecondaryActiveSet, row, column);
                field.PopulationCount++;
            }
                else if (_cells[row,column].IsAlive && (neighbors == 3 || neighbors == 2))
            {
                // a live cell with two,three neighbors stay alive
                _nextGeneration[row, column] =true;
            }
                else if (_cells[row,column].IsAlive)
            {
                // a cell dies because of loneliness or overcrowding
                _nextGeneration[row, column] =false;
                AddNeighborhoodToQueue(SecondaryActiveSet, row, column);
                field.PopulationCount--;
            }
        }

        foreach(Tuple < int,int>coord in ActiveSet)
        {
            int row = coord.Item1;
            int column = coord.Item2;
            if (_nextGeneration[row,column] ==true)
            {
                _cells[row, column].IsAlive = true;
            }
                else
            {
                _cells[row, column].IsAlive = false;
            }
        }
        ActiveSet.Clear();
        ActiveSet = new HashSet<Tuple<int, int>>(SecondaryActiveSet);
        SecondaryActiveSet.Clear();
    }

   //This method colors the patterns recursively
    private void ColorOscillator(int row, int column, int Period) {
        LifeCell Current = _cells[row, column];
        LifeCell N = _cells[WrapMinusOne(row), column];
        LifeCell S = _cells[WrapPlusOne(row), column];
        LifeCell W = _cells[row, WrapMinusOne (column)];
        LifeCell E = _cells[row, WrapPlusOne (column)];
        LifeCell NE = _cells[WrapMinusOne(row), WrapPlusOne (column)];
        LifeCell SE = _cells[WrapPlusOne(row), WrapPlusOne (column)];
        LifeCell SW = _cells[WrapPlusOne(row), WrapMinusOne (column)];
        LifeCell NW = _cells[WrapMinusOne(row), WrapMinusOne (column)];

        Current.lcmcolored = true;
        _cells[row, column].ColorState = Period;

        // check cell to upper left
        if (!NW.lcmcolored && GridPeriodicity[WrapMinusOne(row),WrapMinusOne(column)] >0)
        {
            ColorOscillator(WrapMinusOne(row), WrapMinusOne(column), Period);
        }

        // check cell to upper right
        if (!NE.lcmcolored && GridPeriodicity[WrapMinusOne(row),WrapPlusOne(column)] >0)
        {
            ColorOscillator(WrapMinusOne(row), WrapPlusOne(column), Period);
        }

        //check cell above
        if (!N.lcmcolored && GridPeriodicity[WrapMinusOne(row),column] >0)
        {
            ColorOscillator(WrapMinusOne(row), column, Period);
        }

        // check cell below
        if (!S.lcmcolored && GridPeriodicity[WrapPlusOne(row),column] >0)
        {
            ColorOscillator(WrapPlusOne(row), column, Period);
        }

        // check cell to left
        if (!W.lcmcolored && GridPeriodicity[row,WrapMinusOne(column)] >0)
        {
            ColorOscillator(row, WrapMinusOne(column), Period);
        }

        // check cell to right
        if (!E.lcmcolored && GridPeriodicity[row,WrapPlusOne(column)] >0)
        {
            ColorOscillator(row, WrapPlusOne(column), Period);
        }

        // check cell to bottom left
        if (!SW.lcmcolored && GridPeriodicity[WrapPlusOne(row),WrapMinusOne(column)] >0)
        {
            ColorOscillator(WrapPlusOne(row), WrapMinusOne(column), Period);
        }

        // check cell to bottom right
        if (!SE.lcmcolored && GridPeriodicity[WrapPlusOne(row),WrapPlusOne(column)] >0)
        {
            ColorOscillator(WrapPlusOne(row), WrapPlusOne(column), Period);
        }
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


    //Gets all the states of the cells on the grid and returns an encoded string
    public String GetPatternEncoding() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < _size; row++) {
            for (int col = 0; col < _size; col++) {
                LifeCell cell = _cells[row, col];
                if (cell.isAlive) {
                    sb.Append('O');
                } else {
                    sb.Append('.');
                }
            }
            sb.Append("\n");
        }
        return sb.ToString();
    }


    //Draws a pattern using an encoded string
    public void DrawFullPattern(string encoding, int startDrawRow, int startDrawCol) {
        ClearField();
        field.Iterations = 0;
        int CurrentIndex = 0;

        int StartRow = startDrawRow;
        int StartColumn = startDrawCol;
        try {
            while (CurrentIndex < encoding.Length) {
                if (encoding[CurrentIndex].Equals('O')) {
                    _cells[StartRow, StartColumn].IsAlive = true;
                    _nextGeneration[StartRow, StartColumn] =true;
                    AddNeighborhoodToQueue(ActiveSet, StartRow, StartColumn);
                    field.PopulationCount++;
                    StartColumn++;
                } else if (encoding[CurrentIndex].Equals('.')) {
                    StartColumn++;
                } else {
                    StartRow++;
                    StartColumn = startDrawCol;
                }
                CurrentIndex++;
            }
        } catch (Exception e) {

        }
    }


    //Adds a neighborhood to a HashSet, this is to support the evolution of active cells
    public void AddNeighborhoodToQueue(HashSet<Tuple<int, int>> Set, int row, int column) {
        Set.Add(new Tuple<int, int>(row, column));
        Set.Add(new Tuple<int, int>(row, WrapPlusOne(column)));
        Set.Add(new Tuple<int, int>(row, WrapMinusOne(column)));

        Set.Add(new Tuple<int, int>(WrapPlusOne(row), column));
        Set.Add(new Tuple<int, int>(WrapMinusOne(row), column));
        Set.Add(new Tuple<int, int>(WrapPlusOne(row), WrapPlusOne(column)));

        Set.Add(new Tuple<int, int>(WrapMinusOne(row), WrapMinusOne(column)));
        Set.Add(new Tuple<int, int>(WrapPlusOne(row), WrapMinusOne(column)));
        Set.Add(new Tuple<int, int>(WrapMinusOne(row), WrapPlusOne(column)));
    }
}
*/
