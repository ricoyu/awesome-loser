package com.loserico.common.lang;

import com.loserico.common.lang.utils.ObjectUtils;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ObjectUtilsTest {

    @Test
    public void testIsEqual() {
    	Integer i = new Integer(300);
        Integer j = new Integer(300);
        assertTrue(ObjectUtils.equalTo(i, j));

        String s = "300";
        assertFalse(ObjectUtils.equalTo(i, s));
    }
}
