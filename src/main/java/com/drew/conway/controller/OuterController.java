package com.drew.conway.controller;

import javafx.fxml.FXML;

public class OuterController {

    @FXML
    ConwayController conwayController;

    public void runConway() {
        conwayController.runConway();
    }
}
