package com.rumpel.rumpeldesktop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Launcher {
    private static final Logger logger = LogManager.getLogger();
    public static void main(String[] args) {
        logger.info("Launcher class started");
        App.main(args);
    }
}
