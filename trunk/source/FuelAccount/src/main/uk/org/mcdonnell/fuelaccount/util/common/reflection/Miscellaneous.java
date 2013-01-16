package uk.org.mcdonnell.fuelaccount.util.common.reflection;

import java.lang.reflect.Method;
import java.util.Locale;

public class Miscellaneous {

    public static Method isMethodOfClass(Class<?> classToExamine,
            String methodName) {
        Method method = null;
        Method[] methods = classToExamine.getMethods();

        int index = 0;
        while (method == null && (index < methods.length)) {
            if (methods[index].getName().equals(methodName)) {
                method = methods[index];
            }

            index++;
        }

        return method;
    }

    public static String deriveGetterMethodName(String methodNameSuffix) {
        final String TEST_METHOD_NAME_GETTER_PREFIX = "get";
        final String TEST_METHOD_NAME = String.format("%s%s%s",
                TEST_METHOD_NAME_GETTER_PREFIX,
                methodNameSuffix.toUpperCase(Locale.getDefault()).charAt(0),
                methodNameSuffix.substring(1));

        return TEST_METHOD_NAME;
    }
}
