/*
 * P2tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2Lib.configFile.pData;

/**
 * this is a list of the "same" configurationdata
 * for example a list of "Persons" with the *DATA* fields:
 * NAME, SIZE, ...
 * <p>
 * Das verwendete PDate muss das dann zurückliefern:
 * public String getTag() {
 * return PDataListMeta.META_KEY;
 * }
 *
 * @param <E>
 */
public interface PDataListMeta<E extends PData> extends PDataList<E> {

    String META_KEY = "META";
    PData meta = null;

    PData getMeta();
}
