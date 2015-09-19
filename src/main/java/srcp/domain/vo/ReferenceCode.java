package srcp.domain.vo;

import javax.persistence.Embeddable;
import java.util.regex.Pattern;

@Embeddable
public class ReferenceCode extends Base24Code {
    public static final short SES_ID_IDENTIFIER_PART_LEN = (short) 3;
    public static final short SES_ID_UNIQUE_PART_LEN = (short) 7;

    // The reference code given to the applicant
    private String referenceCode;

    @SuppressWarnings("unused")
    private ReferenceCode() {
    }

    // ReferenceCodes must match this regular expression
    private static final String matchingRegex = "[A-Z]{"
            + SES_ID_IDENTIFIER_PART_LEN + "}-?" + sesCodeRegexMatch + "{"
            + SES_ID_UNIQUE_PART_LEN + "}";

    private static volatile Pattern matchingPatt = null;

    public ReferenceCode(String referenceCode) {
        if (!getRegex().matcher(referenceCode).matches()) {
            throw new IllegalArgumentException(referenceCode
                    + " is not a valid reference code.");
        }

        // if a dash (-) was provided before the prefix and the sequence, then
        // remove the dash
        this.referenceCode = referenceCode.replace("-", "");
    }

    public ReferenceCode(String idPrefix, long uniquePart) {
        StringBuilder buf = new StringBuilder();

        buf.append(idPrefix);
        toBase24Field(buf, uniquePart, SES_ID_UNIQUE_PART_LEN);

        this.referenceCode = buf.toString();

        if (!getRegex().matcher(this.referenceCode).matches()) {
            throw new IllegalArgumentException(
                    "bad reference code [not] created " + "from " + idPrefix
                            + " and " + "unique = " + uniquePart);
        }
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public String getDisplayValue() {
        return referenceCode.substring(0, SES_ID_IDENTIFIER_PART_LEN) + "-"
                + referenceCode.substring(SES_ID_IDENTIFIER_PART_LEN);
    }

    public static Pattern getRegex() {
        if (matchingPatt == null) {
            synchronized (ReferenceCode.class) {
                if (matchingPatt == null) {
                    matchingPatt = Pattern.compile(matchingRegex);
                }
            }
        }

        return matchingPatt;
    }

    @Override
    public String toString() {
        return (getDisplayValue());
    }
}
