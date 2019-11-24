/*
 * This class represents a single cell in the UI.
 */


package com.drew;

    public class LifeCell {
        //Used to help with recursive methods in CellCollection class
        public boolean Visited;

        //Used to help with recursive methods in CellCollection class
        public boolean LCMColored;

        //Determines whether this cell is alive or not
        public boolean _isAlive;

        public boolean IsAlive
        {
            get
            {
                return _isAlive;
            }
            set
            {
                _isAlive = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged(this,
                        new PropertyChangedEventArgs("IsAlive"));
                }
            }
        }

        //Determines the period of this cell
        private int _colorState;

        public int ColorState
        {
            get
            {
                return _colorState;
            }
            set
            {
                _colorState = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged(this,
                        new PropertyChangedEventArgs("ColorState"));
                }
            }
        }

//        public event PropertyChangedEventHandler PropertyChanged;
    }
