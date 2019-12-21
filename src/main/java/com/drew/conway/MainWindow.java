package com.drew.conway;

/*
 * This class handles all events which take place in the user interface
 * and reacts with the appropriate functions in all other classes.
 */

import com.drew.conway.controller.OuterController;
import com.drew.conway.models.LifeField;
import com.drew.conway.util.PatternDataUtils;
import com.drew.conway.util.ConfigManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MainWindow extends Application {

    private static Logger log = Logger.getLogger(MainWindow.class.getName());

    static {
        InputStream stream = MainWindow.class.getClassLoader().
                getResourceAsStream("logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(stream);
            log = Logger.getLogger(MainWindow.class.getName());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PatternDataUtils patternDataUtils = new PatternDataUtils();
    private LifeField lifeField;
    //    private CellCollection cellCollection;

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


    private void createFieldCollection() {
//        cellCollection = new CellCollection(maxGridSize);
//        lifeField = cellCollection.field;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        log.info("Starting Conway's Game of Life application...");
        log.info("Loading properties...");
        Properties properties = ConfigManager.getProperties("conway.properties");
        log.info("Firing up GUI...");
        FXMLLoader loader = new FXMLLoader(
                MainWindow.class.getClassLoader().getResource("OuterController.fxml"));
        HBox page = loader.load();
        Scene scene = new Scene(page);
        primaryStage.setTitle("Conway's Game of Life");
        primaryStage.setScene(scene);
        primaryStage.show();
        OuterController outerController = loader.getController();
        //Why use an Animation Timer and not a standard Thread and while loop?
        //https://stackoverflow.com/questions/35544985/java-update-stage-in-a-for-loop

        outerController.runConway();
    }

    public static void main(String[] args) {
        launch(args);
    }

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

}
