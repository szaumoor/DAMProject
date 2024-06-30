package com.rumpel.rumpeldesktop.fxutils;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.time.LocalDate;
import java.time.Year;

import static com.szaumoor.utils.Strings.removeNonDecimalChars;

/**
 * Utility class for setting up spinners
 */
public final class Spinners {
    private static final int START_YEAR = 1970; // beginning of time for computers

    /**
     * Private constructor to prevent instantiation
     */
    private Spinners() {
        throw new AssertionError("Utility classes cannot be instantiated");
    }

    /**
     * Sets up the given spinner for numbers within the specified range.
     *
     * @param spinner the spinner to be set up
     * @param min     the minimum value for the spinner
     * @param max     the maximum value for the spinner
     */
    public static void setForNumbers(final Spinner<Double> spinner, double min, double max) {
        spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max));
        setListenersForNumbers(spinner);
    }

    /**
     * Sets up a spinner for numbers with the given minimum, maximum, and starting values.
     *
     * @param spinner the spinner widget to set up
     * @param min     the minimum value for the spinner
     * @param max     the maximum value for the spinner
     * @param start   the starting value for the spinner
     */
    public static void setForNumbers(final Spinner<Integer> spinner, int min, int max, int start) {
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, start));
        setListenersForNumbers(spinner);
    }

    /**
     * Sets the spinner to display years starting from the specified start year up to the current year.
     *
     * @param spinner the Spinner control to set the values for
     * @param start   the start year for the spinner
     */
    public static void setForYears(final Spinner<Integer> spinner, int start) {
        int max = Year.now().getValue();
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(START_YEAR, max, start));
        setListenersForNumbers(spinner);
    }

    /**
     * Sets the spinner to display years, using the current year as the start.
     *
     * @param spinner the spinner to set
     */
    public static void setForYears(final Spinner<Integer> spinner) {
        setForYears(spinner, Year.now().getValue());
    }

    /**
     * Sets the value and listeners for a Spinner representing months.
     *
     * @param spinner the Spinner to set the value and listeners for
     * @param start   the initial value for the Spinner
     */
    public static void setForMonths(final Spinner<Integer> spinner, int start) {
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, start));
        setListenersForNumbers(spinner);
    }

    /**
     * Set the spinner for the months, using the current month as the start.
     *
     * @param spinner the spinner to set
     */
    public static void setForMonths(final Spinner<Integer> spinner) {
        setForMonths(spinner, LocalDate.now().getMonthValue());
    }

    /**
     * Prepares the spinners to reject non-numeric characters
     *
     * @param spinner Spinner to set up
     */
    private static void setListenersForNumbers(final Spinner<?> spinner) {
        var editor = spinner.getEditor();
        editor.setOnKeyTyped(event -> {
            var code = event.getCode();
            if (!code.isDigitKey()) {
                int caretPosition = editor.getCaretPosition();
                editor.setText(removeNonDecimalChars(editor.getText()));
                editor.positionCaret(caretPosition);
            }
        });
        editor.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!editor.isFocused()) {
                var text = editor.getText();
                if (text.length() > 1 && text.charAt(0) == '0' && text.charAt(1) != '.') {
                    editor.setText(text.substring(1));
                }
            }
        });
    }
}
