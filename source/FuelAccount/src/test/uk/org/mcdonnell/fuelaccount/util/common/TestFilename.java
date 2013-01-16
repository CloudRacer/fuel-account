package uk.org.mcdonnell.fuelaccount.util.common;

import java.io.File;

import junit.framework.TestCase;

public class TestFilename extends TestCase {

    public void test() {
        File file = new File("/test.test/test.tst");
        System.out.println(String.format("\"%s\" --> \"%s\"", file,
                Filename.FilenameWithoutPathOrExtension(file)));
        file = new File(file.getName());
        System.out.println(String.format("\"%s\" --> \"%s\"", file,
                Filename.FilenameWithoutPathOrExtension(file)));
        file = new File(file.getName()
                .substring(0, file.getName().indexOf(".")));
        System.out.println(String.format("\"%s\" --> \"%s\"", file,
                Filename.FilenameWithoutPathOrExtension(file)));
    }
}
