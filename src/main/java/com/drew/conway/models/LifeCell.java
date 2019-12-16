package com.drew.conway.models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class LifeCell {
    //Used to help with recursive methods in CellCollection class
    public BooleanProperty visited;

    //Used to help with recursive methods in CellCollection class
    public BooleanProperty lcmcolored;

    //Determines whether this cell is alive or not
    public BooleanProperty isAlive;

    //Determines the period of this cell
    public IntegerProperty colorState;

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
        cellColor.addListener(new ChangeListener<Paint>() {
            @Override
            public void changed(ObservableValue<? extends Paint> observable, Paint oldValue, Paint newValue) {

            }
        });
        cellColor.setValue(Color.WHITE);
        visited = new SimpleBooleanProperty();
        visited.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

            }
        });
    }
}
