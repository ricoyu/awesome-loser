package com.loserico.common.lang;

import com.loserico.common.lang.utils.ReflectionUtils;
import org.junit.Test;

public class ReflectionUtilsTest {

    @Test
    public void test() {
        MsgMonitor monitor = new MsgMonitor();
        Object field = ReflectionUtils.getFieldValue("intField1", monitor);
        Class<?> aClass = field.getClass();
    }

    @Test
    public void testSetStaticField() {
        System.out.println(ReflectionUtils.getFieldValue("name", Banana.class));
        ReflectionUtils.setField("name", Banana.class, "泰国香蕉");
        System.out.println(ReflectionUtils.getFieldValue("name", Banana.class));
    }


}
