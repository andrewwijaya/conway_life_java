package com.drew;/*
 * This class handles all events which take place in the user interface
 * and reacts with the appropriate functions in all other classes.
 */
using Microsoft.Win32;
using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Shapes;
using System.Windows.Threading;
using System.Windows.Media;

namespace RenewConway
{
    public partial class MainWindow : Window
    {
        private int MaxGridSize;
        private LifeField Field;
        private CellCollection Cells;
        private DispatcherTimer Timer = new DispatcherTimer();
        private PatternDataUtils PatternManager = new PatternDataUtils();

        public MainWindow()
        {
            InitializeComponent();
            this.Loaded += new RoutedEventHandler(Window_Loaded);
        }

        //Gets the Field object from the CellCollection class
        private void CreateFieldCollection()
        {
            Cells = new CellCollection(MaxGridSize);
            Field = Cells.field;
        }

        //Sets up a timer for the evolution
        private void Window_Loaded(object sender, RoutedEventArgs e)
        {
            Timer.Tick += TimerTick;
            Timer.Interval = TimeSpan.FromMilliseconds(50);
        }

        //Initialises the UI grid by adding rows and columns
        private void InitGrid()
        {
            World.Children.Clear();
            World.RowDefinitions.Clear();
            World.ColumnDefinitions.Clear();

            for (int i = 0; i < Field.Rows; i++)
            {
                World.RowDefinitions.Add(new RowDefinition());
            }

            for (int i = 0; i < Field.Columns; i++)
            {
                World.ColumnDefinitions.Add(new ColumnDefinition());
            }
        }

        //Adds visual rectangles into the grid and initialises them 
        private void PopulateGrid()
        {
            for (int row = 0; row < MaxGridSize; row++)
            {
                for (int column = 0; column < MaxGridSize; column++)
                {
                    Rectangle rect = new Rectangle();
                    rect.MouseLeftButtonDown += MouseLeft_Click;
                    Grid.SetColumn(rect, column);
                    Grid.SetRow(rect, row);
                    rect.DataContext = Cells[row, column];
                    World.Children.Add(rect);
                    rect.Style = Resources["lifeStyle"] as Style;
                }
            }
        }

        //Mouse handler
        private void MouseLeft_Click(object sender, MouseButtonEventArgs e)
        {
            Rectangle rect = sender as Rectangle;
            int row = (int)rect.GetValue(Grid.RowProperty);
            int column = (int)rect.GetValue(Grid.ColumnProperty);
            if (Cells._cells[row, column].IsAlive)
            {
                Field.PopulationCount--;
                Cells._cells[row, column].IsAlive = false;
                Cells._nextGeneration[row, column] = false;
            }
            else
            {
                Field.PopulationCount++;
                Cells._cells[row, column].IsAlive = true;
                Cells._nextGeneration[row, column] = true;
                Cells.AddNeighborhoodToQueue(Cells.ActiveSet, row, column);
            }
        }

        //Timer handler
        private void TimerTick(object sender, EventArgs e)
        {
            StepEvolution();
        }

        //Start button handler to begin simulation
        private void StartButton_Click(object sender, RoutedEventArgs e)
        {
            Timer.Start();
        }

        //Stop button handler to stop simulation
        private void StopButton_Click(object sender, RoutedEventArgs e)
        {
            Timer.Stop();
        }

        //Clear button handler to clear simulator grid
        private void ClearButton_Click(object sender, RoutedEventArgs e)
        {
            ClearGrid();
            Timer.Stop();
            DescriptionTextBlock.Text = "";
            Field.PopulationCount = 0;
            Field.Iterations = 0;
            Field.ActiveCellCount = 0;
        }

        //Clears the grid and sets all cells to be dead
        private void ClearGrid()
        {
            Cells.ClearField();
        }

        //Random button handler to generate a random grid
        private void RandomButton_Click(object sender, RoutedEventArgs e)
        {
            Cells.RandomField();
        }

        ////GridSize handler to change the size of the grid
        private void GridSizeSelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            var Combo = sender as ComboBox;
            int NewGridSize = int.Parse(Combo.SelectedItem.ToString());
            MaxGridSize = NewGridSize;
            CreateFieldCollection();
            InitGrid();
            PopulateGrid();
            Iterations.DataContext = Field;
            ActiveCellCount.DataContext = Field;
            PopulationCount.DataContext = Field;
            StableText.DataContext = Field;
        }

        //Adds the options for the grid sizes
        private void GridSizeComboBox_Loaded(object sender, RoutedEventArgs e)
        {
            List<string> data = new List<string>();
            data.Add("25");
            data.Add("50");
            data.Add("60");
            data.Add("70");
            data.Add("80");
            data.Add("90");
            data.Add("100");
            data.Add("110");
            data.Add("120");
            data.Add("130");
            data.Add("140");
            data.Add("150");
            var comboBox = sender as ComboBox;
            comboBox.ItemsSource = data;
            comboBox.SelectedIndex = 6;
        }

        //Saves the current grid on the simulator
        private void SavePatternButton_Click(object sender, RoutedEventArgs e)
        {
            string PatternEncoding = Cells.GetPatternEncoding();
            PatternManager.SavePatternToFile(PatternEncoding);
        }

        //Step button handler to do one single evolution
        private void StepButton_Click(object sender, RoutedEventArgs e)
        {
            StepEvolution();
        }

        //Performs a single evolution
        private void StepEvolution()
        {
            Cells.UpdateLife();
            Field.ActiveCellCount = Cells.ActiveSet.Count;
            Field.Iterations++;
        }

        //Opens a .cells file 
        private void OpenPatternFile_Click(object sender, RoutedEventArgs e)
        {
            Timer.Stop();
            string[] PatternData = PatternManager.OpenPattern();
            int startDrawRow;
            int startDrawCol;
            startDrawRow = 0;
            startDrawCol = 0;
            if (PatternData != null)
            {
                DescriptionTextBlock.Text = PatternData[0];
                Cells.DrawFullPattern(PatternData[1], startDrawRow, startDrawCol);
            }
        }
    }
}