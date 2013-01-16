package uk.org.mcdonnell.fuelaccount.util.common.reflection;

import java.io.File;
import java.lang.reflect.Method;

import uk.org.mcdonnell.fuelaccount.util.common.ClassReflection;

import junit.framework.TestCase;

public class TestReflection extends TestCase {
    public void testIsMethodOfClass() {
        try {
            final String TEST_METHOD_NAME_SUFFIX = "name";
            final String TEST_METHOD_NAME = ClassReflection
                    .deriveGetterMethodName(TEST_METHOD_NAME_SUFFIX);

            File testObject = new File("test_filename");

            Method method = ClassReflection.isMethodOfClass(
                    testObject.getClass(), TEST_METHOD_NAME);

            System.out.println(String.format(
                    "Method \"%s\"%s found in Class \"%s\".", TEST_METHOD_NAME,
                    (method == null ? " not" : ""), testObject.getClass()
                            .getName()));

            if (method != null) {
                // Execute the Method.
                System.out.println(String.format("Method output:%s.",
                        method.invoke(testObject, new Object[] {})));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
