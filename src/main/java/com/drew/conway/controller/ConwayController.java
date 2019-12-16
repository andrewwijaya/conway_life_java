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

    private int maxGridSize = 200;
    private int rectangleSize = 3;

    //This is the data model for this controller, the LifeField object is a grid model representing the LifeCells
    private LifeField lifeField;

    @FXML
    private GridPane world;
    public volatile boolean isInitialised = false;

    @FXML
    private void initialize() {
        lifeField = new LifeField(maxGridSize, maxGridSize);
        populateGrid();
        isInitialised = true;
    }

    public void runConway() {
        lifeField.updateConwayField();
    }

    private void populateGrid() {
        for (int row = 0; row < maxGridSize; row++) {
            for (int column = 0; column < maxGridSize; column++) {
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

    private Color randomColor() {
        Random r = new Random();
        return r.nextBoolean() ? Color.BLACK : Color.WHITE;
    }

    private boolean getRandomCellState() {
        return new Random().nextBoolean();
    }
}
