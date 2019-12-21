package com.drew.conway.controller;

import com.drew.conway.models.LifeCell;
import com.drew.conway.models.LifeField;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;
import java.util.logging.Logger;

public class ConwayController {

    private static final Logger logger = Logger.getLogger(ConwayController.class.getName());

    private int gridWidth = 250;
    private int gridHeight = 150;
    private int rectangleSize = 5;

    public LifeField getLifeField() {
        return lifeField;
    }

    //This is the data model for this controller, the LifeField object is a grid model representing the LifeCells
    private LifeField lifeField;

    @FXML
    private GridPane world;
    public volatile boolean isInitialised = false;

    @FXML
    private void initialize() {
        lifeField = new LifeField(gridWidth,gridHeight);
        populateGrid();
        isInitialised = true;
    }

    public void runConway() {
        lifeField.updateConwayField();
    }

    private void populateGrid() {
        for (int row = 0; row < gridHeight; row++) {
            for (int column = 0; column < gridWidth; column++) {
                Rectangle rect = new Rectangle();
                LifeCell lifeCell = new LifeCell();
                lifeField.lifeGrid[row][column] = lifeCell;
                rect.setHeight(rectangleSize);
                rect.setWidth(rectangleSize);
                rect.fillProperty().bindBidirectional(lifeCell.cellColor);
                lifeCell.isAlive.setValue(getRandomCellState());
//                rect.MouseLeftButtonDown += MouseLeft_Click;
                world.add(rect, column, row);
//                rect.DataContext = cellCollection[row, column];
            }
        }
    }

    //Resets the grid and clears necessary fields
    public void clearField() {
        for (int row = 0; row < gridHeight; row++) {
            for (int column = 0; column < gridWidth; column++) {
                lifeField.lifeGrid[row][column].isAlive.setValue(false);
//                _cells[row, column].ColorState = 0;
//                _nextGeneration[row, column] =false;
            }
        }
        resetStatistics();
//        ActiveSet.Clear();
//        SecondaryActiveSet.Clear();
//        field.PopulationCount = 0;
//        field.Stable = false;
//        GridHistory.Clear();
    }

    public void randomField(){
        for (int row = 0; row < gridHeight; row++) {
            for (int column = 0; column < gridWidth; column++) {
                lifeField.lifeGrid[row][column].isAlive.setValue(getRandomCellState());
            }
        }
        resetStatistics();
    }

    private void resetStatistics(){
        lifeField.setIterations(0);
        lifeField.setPopulationCount(0);
    }

    private Color randomColor() {
        Random r = new Random();
        return r.nextBoolean() ? Color.BLACK : Color.WHITE;
    }

    private boolean getRandomCellState() {
        return new Random().nextBoolean();
    }
}
