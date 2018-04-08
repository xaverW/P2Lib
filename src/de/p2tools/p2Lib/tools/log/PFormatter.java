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


package de.p2tools.p2Lib.tools.log;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class PFormatter extends SimpleFormatter {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final FastDateFormat HHmmss = FastDateFormat.getInstance("HH:mm:ss");
    private static final int MSG_SIZE = 14;
    private static final int INDENT = 11 + MSG_SIZE;
    final String empty = StringUtils.leftPad("", INDENT);
    final String emptyEx = StringUtils.leftPad("", 10);


    @Override

    public String format(LogRecord record) {
        if (record.getThrown() == null &&
                (record.getMessage().isEmpty() || record.getMessage().trim().equals("\n"))) {
            return "\n";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[" + HHmmss.format(new Date(record.getMillis())) + "]")
                .append(" ")
                .append(StringUtils.rightPad(record.getLevel().getLocalizedName() + ": ", MSG_SIZE));

        if (record.getMessage().contains(LINE_SEPARATOR)) {
            formatMultiLine(record.getMessage(), sb, empty);

        } else {
            sb.append(formatMessage(record))
                    .append(LINE_SEPARATOR);
        }

        if (record.getThrown() != null) {
            try {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                record.getThrown().printStackTrace(pw);
                pw.close();
                sb.append(LINE_SEPARATOR);
                formatMultiLine(emptyEx + sw.toString(), sb, emptyEx);
                sb.append(LINE_SEPARATOR);
                sb.append(LINE_SEPARATOR);
            } catch (Exception ex) {
                // ignore
            }
        }

        return sb.toString();
    }


    private void formatMultiLine(String msg, StringBuilder sb, String before) {
        String[] arr = msg.split("\n");
        if (arr.length == 0) {
            return;
        }

        sb.append(arr[0]);

        for (int i = 1; i < arr.length; ++i) {
            sb.append(LINE_SEPARATOR);
            sb.append(before);
            sb.append(arr[i]);
        }
        sb.append(LINE_SEPARATOR);
    }


}
