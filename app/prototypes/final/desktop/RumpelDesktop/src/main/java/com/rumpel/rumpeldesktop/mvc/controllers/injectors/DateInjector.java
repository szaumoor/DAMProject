package com.rumpel.rumpeldesktop.mvc.controllers.injectors;

import java.time.Month;
import java.time.Year;

/**
 * Injector to inject date data
 */
public interface DateInjector {
    void setDate(final Year year, final Month month);
}
