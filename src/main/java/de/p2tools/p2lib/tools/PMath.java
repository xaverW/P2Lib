/*
 * P2Tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de/
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */


package de.p2tools.p2lib.tools;

public class PMath {
    private PMath() {

    }

    public static long divideAndRoundUp(long num, long divisor) {
        return divideRoundUp(num, divisor);
    }

    public static int divideAndRoundUp(int num, int divisor) {
        return (int) divideRoundUp(num, divisor);
    }

    private static long divideRoundUp(long num, long divisor) {
        if (num == 0 || divisor == 0) {
            return 0;
        }

        int sign = (num > 0 ? 1 : -1) * (divisor > 0 ? 1 : -1);

        if (sign > 0) {
            return (num + divisor - 1) / divisor;
        } else {
            return (num / divisor);
        }
    }
}
