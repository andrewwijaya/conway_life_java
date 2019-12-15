package com.drew.conway.models;

public class LifeCell {
    //Used to help with recursive methods in CellCollection class
    public boolean visited;

    //Used to help with recursive methods in CellCollection class
    public boolean lcmcolored;

    //Determines whether this cell is alive or not
    public boolean isAlive;

    //Determines the period of this cell
    public int colorState;
}
