package com.szaumoor.rumple.model.utils;

import java.math.BigDecimal;

public enum Numbers {
    ;

    public static boolean limitsValid(final BigDecimal softLimit, final BigDecimal hardLimit) {
        return Types.nonNull(softLimit, hardLimit) &&
                softLimit.compareTo(BigDecimal.ZERO) > 0 &&
                hardLimit.compareTo(softLimit) > 0;
    }
}
