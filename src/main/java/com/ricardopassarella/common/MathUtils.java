package com.ricardopassarella.common;

import java.math.BigDecimal;
import java.util.List;

import static java.math.RoundingMode.DOWN;

public class MathUtils {

    public static BigDecimal divide(long a, long b) {
        return BigDecimal.valueOf(a)
                         .divide(BigDecimal.valueOf(b), 2, DOWN);
    }

    /*
        Requirements: list is already ordered
     */
    public static BigDecimal getMedian(List<Long> list) {
        int size = list.size();

        BigDecimal value = BigDecimal.valueOf(list.get(size / 2))
                                     .setScale(2, DOWN);

        if (size % 2 == 0) {
            return value.add(BigDecimal.valueOf(list.get(size / 2 - 1)))
                        .divide(BigDecimal.valueOf(2), 2, DOWN);
        }

        return value;
    }
}
