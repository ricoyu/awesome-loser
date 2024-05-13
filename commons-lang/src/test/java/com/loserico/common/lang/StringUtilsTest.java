package com.loserico.common.lang;

import com.loserico.common.lang.utils.StringUtils;
import org.junit.Test;

public class StringUtilsTest {

    @Test
    public void test() {
        String cod = "FFFFFFFFFF FF00250030 FFFF003000 2400010123 456789ABCD EFFFFF0018 2430001000 2000030009 0020008001 2000200060 FFFFFFFFFF FFFFF";
        cod = StringUtils.trimAll(cod);
        System.out.println(cod.length());
        String str = cod.substring(96, 100);
        System.out.println(str);
    }
}
