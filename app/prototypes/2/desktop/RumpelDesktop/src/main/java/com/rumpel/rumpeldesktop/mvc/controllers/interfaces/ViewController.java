package com.rumpel.rumpeldesktop.mvc.controllers.interfaces;

import javafx.fxml.Initializable;

public interface ViewController<T extends Initializable> {
    T getController();
}
