package com.drew.conway.models;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextFormatter;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import sun.java2d.pipe.SpanShapeRenderer;

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
        isAlive.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    cellColor.setValue(Color.BLACK);
                }else{
                    cellColor.setValue(Color.WHITE);
                }
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
