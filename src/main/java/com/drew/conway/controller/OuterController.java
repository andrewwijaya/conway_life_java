package com.drew.conway.controller;

import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Timer;

public class OuterController {

    @FXML
    private ConwayController conwayController;

    @FXML
    private Label stepCount;

    private AnimationTimer animationTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            conwayController.runConway();
        }
    };

    public void runConway() {
        animationTimer.start();
        stepCount.textProperty().bind(Bindings.convert(conwayController.getLifeField().getIterationProperty()));
    }


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

    //Clear button handler to clear simulator grid
//    private void ClearButton_Click(object sender, RoutedEventArgs e) {
//        ClearGrid();
//        Timer.Stop();
//        DescriptionTextBlock.Text = "";
//        lifeField.PopulationCount = 0;
//        lifeField.Iterations = 0;
//        lifeField.ActiveCellCount = 0;
//    }

//    Clears the grid and sets all cells to be dead
    public void clearField() {
        conwayController.clearField();
    }

    //Random button handler to generate a random grid
    public void randomButton() {
        conwayController.randomField();
    }

    //Start button handler to begin simulation
    public void startButton() {
        animationTimer.start();
    }

    //Stop button handler to stop simulation
    public void stopButton() {
        animationTimer.stop();
    }

    //Performs a single evolution
    public void stepButton() {
        conwayController.runConway();
    }

    public void exit(){
        System.exit(0);
    }
}
