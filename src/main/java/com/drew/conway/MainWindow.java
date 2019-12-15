package com.drew.conway;

/*
 * This class handles all events which take place in the user interface
 * and reacts with the appropriate functions in all other classes.
 */

import com.drew.conway.controller.ConwayController;
import com.drew.conway.models.LifeField;
import com.drew.conway.util.PatternDataUtils;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

public class MainWindow extends Application {

    private PatternDataUtils patternDataUtils = new PatternDataUtils();
    private LifeField lifeField;
    //    private CellCollection cellCollection;
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            StepEvolution();
        }
    };
    Timer timer = new Timer();

    //Gets the lifeField object from the CellCollection class
    //Sets up a timer for the evolution
    //Initialises the UI grid by adding rows and columns
    //Adds visual rectangles into the grid and initialises them
    //Mouse handler
//    private void MouseLeft_Click(object sender, MouseButtonEventArgs e) {
//        Rectangle rect = sender as Rectangle;
//        int row = (int) rect.GetValue(Grid.RowProperty);
//        int column = (int) rect.GetValue(Grid.ColumnProperty);
//        if (cellCollection._cells[row,column].isAlive)
//        {
//            lifeField.PopulationCount--;
//            cellCollection._cells[row, column].isAlive = false;
//            cellCollection._nextGeneration[row, column] =false;
//        }
//            else
//        {
//            lifeField.PopulationCount++;
//            cellCollection._cells[row, column].isAlive = true;
//            cellCollection._nextGeneration[row, column] =true;
//            cellCollection.AddNeighborhoodToQueue(cellCollection.ActiveSet, row, column);
//        }

    //Start button handler to begin simulation
    private void startButtonClick() {
        timer = new Timer();
        timer.schedule(timerTask, 1000);
    }

    //Stop button handler to stop simulation
    private void stopButtonClick() {
        timer.cancel();
    }

    private void createFieldCollection() {
//        cellCollection = new CellCollection(maxGridSize);
//        lifeField = cellCollection.field;
    }

    //Performs a single evolution
    private void StepEvolution() {
//        cellCollection.UpdateLife();
//        lifeField.ActiveCellCount = cellCollection.ActiveSet.Count;
//        lifeField.Iterations++;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                MainWindow.class.getClassLoader().getResource("ConwayController.fxml"));
        GridPane page = loader.load();
        Scene scene = new Scene(page);
        primaryStage.setTitle("Conway's Game of Life");
        primaryStage.setScene(scene);
        primaryStage.show();
        ConwayController conwayController = loader.getController();
        //Why use an Animation Timer and not a standard Thread and while loop?
        //https://stackoverflow.com/questions/35544985/java-update-stage-in-a-for-loop
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                conwayController.runConway();
            }
        }.start();
    }

    public static void main(String[] args) {
        launch(args);
    }

//    }

    //Clear button handler to clear simulator grid
//    private void ClearButton_Click(object sender, RoutedEventArgs e) {
//        ClearGrid();
//        Timer.Stop();
//        DescriptionTextBlock.Text = "";
//        lifeField.PopulationCount = 0;
//        lifeField.Iterations = 0;
//        lifeField.ActiveCellCount = 0;
//    }

    //Clears the grid and sets all cells to be dead
//    private void ClearGrid() {
//        cellCollection.ClearField();
//    }

    //Random button handler to generate a random grid
//    private void RandomButton_Click(object sender, RoutedEventArgs e) {
//        cellCollection.RandomField();
//    }

    ////GridSize handler to change the size of the grid
//    private void GridSizeSelectionChanged(object sender, SelectionChangedEventArgs e) {
//        var Combo = sender as ComboBox;
//        int NewGridSize = int.Parse(Combo.SelectedItem.ToString());
//        maxGridSize = NewGridSize;
//        createFieldCollection();
//        initGrid();
//        PopulateGrid();
//        Iterations.DataContext = lifeField;
//        ActiveCellCount.DataContext = lifeField;
//        PopulationCount.DataContext = lifeField;
//        StableText.DataContext = lifeField;
//    }

    //Adds the options for the grid sizes
//    private void GridSizeComboBox_Loaded(object sender, RoutedEventArgs e) {
//        List<string> data = new List<string>();
//        data.Add("25");
//        data.Add("50");
//        data.Add("60");
//        data.Add("70");
//        data.Add("80");
//        data.Add("90");
//        data.Add("100");
//        data.Add("110");
//        data.Add("120");
//        data.Add("130");
//        data.Add("140");
//        data.Add("150");
//        var comboBox = sender as ComboBox;
//        comboBox.ItemsSource = data;
//        comboBox.SelectedIndex = 6;
//    }

//    //Saves the current grid on the simulator
//    private void SavePatternButton_Click(object sender, RoutedEventArgs e) {
//        string PatternEncoding = cellCollection.GetPatternEncoding();
//        patternDataUtils.SavePatternToFile(PatternEncoding);
//    }

//    //Step button handler to do one single evolution
//    private void StepButton_Click(object sender, RoutedEventArgs e) {
//        StepEvolution();
//    }


    //Opens a .cells file
//    private void OpenPatternFile_Click(object sender, RoutedEventArgs e) {
//        Timer.Stop();
//        string[] PatternData = patternDataUtils.OpenPattern();
//        int startDrawRow;
//        int startDrawCol;
//        startDrawRow = 0;
//        startDrawCol = 0;
//        if (PatternData != null) {
//            DescriptionTextBlock.Text = PatternData[0];
//            cellCollection.DrawFullPattern(PatternData[1], startDrawRow, startDrawCol);
//        }
//    }
}
