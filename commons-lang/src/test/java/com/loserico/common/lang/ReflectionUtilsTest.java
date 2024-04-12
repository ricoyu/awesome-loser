package com.loserico.common.lang;

import com.loserico.common.lang.utils.ReflectionUtils;
import org.junit.Test;

public class ReflectionUtilsTest {

    @Test
    public void test() {
        MsgMonitor monitor = new MsgMonitor();
        Object field = ReflectionUtils.getField(monitor, "intField1");
        Class<?> aClass = field.getClass();
    }
}
