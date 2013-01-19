package uk.org.mcdonnell.fuelaccount.util.common;

import java.util.Locale;

public class Miscellaneous {

    private Miscellaneous() {
    }

    public static String initialiseText(String string) {
        return String.format("%s%s",
                string.substring(0, 1).toUpperCase(Locale.getDefault()), string
                        .substring(1).toLowerCase(Locale.getDefault()));
    }
}
