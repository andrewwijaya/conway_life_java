package com.drew.conway.models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class LifeCell {
    //Used to help with recursive methods in CellCollection class
    BooleanProperty visited;

    //Used to help with recursive methods in CellCollection class
    BooleanProperty lcmcolored;

    //Determines whether this cell is alive or not
    public BooleanProperty isAlive;

    //Determines the period of this cell
    IntegerProperty colorState;

    public ObjectProperty<Paint> cellColor;

    public LifeCell(){
        isAlive = new SimpleBooleanProperty();
        isAlive.addListener((observable, oldValue, newValue) -> {
            if(newValue){
                cellColor.setValue(Color.BLACK);
            }else{
                cellColor.setValue(Color.WHITE);
            }
        });
        cellColor = new SimpleObjectProperty<>();
        cellColor.addListener((observable, oldValue, newValue) -> {

        });
        cellColor.setValue(Color.WHITE);
        visited = new SimpleBooleanProperty();
        visited.addListener((observable, oldValue, newValue) -> {

        });
        colorState = new SimpleIntegerProperty();
        colorState.addListener((observable, oldValue, newValue) -> {
            switch (newValue.intValue()){
                case 1:
                    cellColor.setValue(Color.RED);
                    break;
                case 2:
                    cellColor.setValue(Color.YELLOW);
                    break;
                case 3:
                    cellColor.setValue(Color.BROWN);
                    break;
            }
        });
        lcmcolored = new SimpleBooleanProperty();
    }
}
