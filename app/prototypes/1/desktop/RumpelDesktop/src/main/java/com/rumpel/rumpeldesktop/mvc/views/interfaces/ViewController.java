package com.rumpel.rumpeldesktop.mvc.views.interfaces;

import javafx.fxml.Initializable;

public interface ViewController<T extends Initializable> {
    T getController();
}
