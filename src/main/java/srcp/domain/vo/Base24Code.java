package srcp.domain.vo;

import java.util.Arrays;

/**
 * Created by Josh on 9/19/2015.
 */
public class Base24Code {
    // public final static char[] codeChars = "123456789BFGHJKNQRSTVXYZ"
    // .toCharArray();
    public final static char[] codeChars = "B81KZ2J6FG39NHQV74TRXYS5"
            .toCharArray();
    public final static String codeRegexMatch = "[1-9BFGHJKNQRSTVXYZ]";

    public final static short CODE_CHARS_CT = (short) (codeChars.length);

    /***
     * Convert a field to base 24
     *
     * @param dest
     *            a {@link StringBuilder} to write to
     * @param value
     *            the numerical value to encode
     * @param fieldWidth
     *            number of digits
     *
     * @return the modified <code>StringBuilder</code>
     */
    public static final StringBuilder toBase24Field(StringBuilder dest,
                                                    long value, int fieldWidth) {
        int startPos = dest.length();
        int endPos = startPos + fieldWidth - 1;

        while ((value >= CODE_CHARS_CT) && (fieldWidth > 0)) {
            dest.append(codeChars[(int) (value % CODE_CHARS_CT)]);
            fieldWidth--;
            value /= CODE_CHARS_CT;
        }

        while (fieldWidth > 0) {
            dest.append(codeChars[(int) value]);
            value = 0;
            fieldWidth--;
        }

        // Reverse substring to get correct order
        while (endPos > startPos) {
            char tmp;

            tmp = dest.charAt(startPos);
            dest.setCharAt(startPos, dest.charAt(endPos));
            dest.setCharAt(endPos, tmp);
            endPos--;
            startPos++;
        }

        return dest;
    }

    /***
     * Convert base24 back to number
     *
     * @param chSeq
     *            the {@link CharSequence} to read from
     * @param offs
     *            offset to begin
     * @param len
     *            length of field to read
     *
     * @return the value as a <code>long</code>
     *
     * @throws
     */
    public static final long fromBase24Field(CharSequence chSeq, int offs,
                                             int len) {
        long accum = 0L;
        int endBefore = offs + len;

        for (; offs < endBefore; offs++) {
            int chValue;

            accum *= CODE_CHARS_CT;
            chValue = Arrays.binarySearch(codeChars, chSeq.charAt(offs));
            if (chValue < 0) {
                throw new IllegalArgumentException("unparseable code");
            }

            accum += chValue;
        }

        return accum;
    }
}
