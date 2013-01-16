package uk.org.mcdonnell.fuelaccount.util.common;

import java.io.File;

public class Filename {

    private Filename() {
    }

    public static String FilenameWithoutPathOrExtension(File file) {
        String filenameWithoutPathOrExtension = null;

        // Remove the path.
        filenameWithoutPathOrExtension = file.getName();
        // Remove the extension.
        if (filenameWithoutPathOrExtension.indexOf(".") != -1) {
            filenameWithoutPathOrExtension = filenameWithoutPathOrExtension
                    .substring(0, filenameWithoutPathOrExtension.indexOf("."));
        }

        return filenameWithoutPathOrExtension;
    }

}
