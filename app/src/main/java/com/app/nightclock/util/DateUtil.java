package com.app.nightclock.util;

public class DateUtil {
    @Deprecated
    public static final char QUOTE = '\'';
    @Deprecated
    public static final char SECONDS = 's';

    public static boolean hasSeconds(CharSequence inFormat) {
        return hasDesignator(inFormat, SECONDS);
    }



    public static boolean hasDesignator(CharSequence inFormat, char designator) {
        if (inFormat == null) {
            return false;
        }
        int length = inFormat.length();
        int i = 0;
        while (i < length) {
            int count = 1;
            char c = inFormat.charAt(i);
            if (c == '\'') {
                count = skipQuotedText(inFormat, i, length);
            } else if (c == designator) {
                return true;
            }
            i += count;
        }
        return false;
    }

    private static int skipQuotedText(CharSequence s, int i, int len) {
        if (i + 1 < len && s.charAt(i + 1) == QUOTE) {
            return 2;
        }
        int count = 1;
        i++;
        while (i < len) {
            if (s.charAt(i) == QUOTE) {
                count++;
                if (i + 1 >= len || s.charAt(i + 1) != QUOTE) {
                    break;
                }
                i++;
            } else {
                i++;
                count++;
            }
        }
        return count;
    }
}
