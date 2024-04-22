package com.loserico.java8.methodreference;

import java.lang.reflect.Method;
import java.util.function.Function;

public class MethodReferenceUtils {

    public static void main(String[] args) {
        //columnName(Department::getDeptName);
    }

    public static String columnName(Function<?, ?> methodReference) {
        try {
            Method method = methodReference.getClass().getMethod("apply", Object.class);
            String methodName = method.getName();
            System.out.println(methodName);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
