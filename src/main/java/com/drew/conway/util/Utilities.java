/*
 * Contains helper methods which are used by all other classes in the project
 */

package com.drew.conway.util;

public class Utilities {
    //Determines the periodic value of a cell's history which is encoded as a 32 bit string
    public static int DeterminePeriodicity(String CellHistory) {
        if (CellHistory.contains("1")) {
            for (int period = 1; period <= CellHistory.length() / 2; period++) {
                boolean CurrentPeriod = true;
                for (int cellIndex = 0; cellIndex < CellHistory.length(); cellIndex++) {
                    int offset = cellIndex + period;
                    if (offset < CellHistory.length()) {
                        if (CellHistory.charAt(cellIndex) != CellHistory.charAt(offset)) {
                            CurrentPeriod = false;
                            break;
                        }
                    }
                }
                if (CurrentPeriod) {
                    return period;
                }
            }
            return 0;
        } else {
            return 0;
        }

    }

    //Calculates the lowest common multiple in an array of integers
    public static int lowestCommonMultiple(int[] x) throws Exception {
        if (x.length < 2) {
            throw new Exception("Do not use this method if there are less than two numbers.");
        }
        int tmp = lowestCommonMultiple(x[x.length - 1], x[x.length - 2]);
        for (int i = x.length - 3; i >= 0; i--) {
            if (x[i] < 0) {
                throw new Exception("Cannot compute the least common multiple of several numbers where one, at least, is negative.");
            }
            tmp = lowestCommonMultiple(tmp, x[i]);
        }
        return tmp;
    }

    private static int lowestCommonMultiple(int x1, int x2) {
        int a;
        int b;
        if (x1 < 0 || x2 < 0) {
            throw new IllegalArgumentException("Cannot compute the Greatest Common Divisor if one integer is negative.");
        }
        if (x1 > x2) {
            a = x1;
            b = x2;
        } else {
            a = x2;
            b = x1;
        }

        for (int i = 1; i <= b; i++) {
            if ((a * i) % b == 0) {
                return i * a;
            }
        }
        return b;
    }
}
