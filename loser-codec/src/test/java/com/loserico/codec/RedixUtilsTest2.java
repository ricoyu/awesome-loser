package com.loserico.codec;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RedixUtilsTest2 {

    @Test
    public void byte2HexShouldReturnCorrectHexValueForPositiveByte() {
        byte input = 10;
        String expectedOutput = "0A";
        assertEquals(expectedOutput, RedixUtils.byte2Hex(input));
    }

    @Test
    public void byte2HexShouldReturnCorrectHexValueForNegativeByte() {
        byte input = -10;
        String expectedOutput = "F6";
        assertEquals(expectedOutput, RedixUtils.byte2Hex(input));
    }

    @Test
    public void byte2HexShouldReturnCorrectHexValueForZero() {
        byte input = 0;
        String expectedOutput = "00";
        assertEquals(expectedOutput, RedixUtils.byte2Hex(input));
    }

    @Test
    public void byte2HexShouldReturnCorrectHexValueForMaximumByte() {
        byte input = Byte.MAX_VALUE;
        String expectedOutput = "7F";
        assertEquals(expectedOutput, RedixUtils.byte2Hex(input));
    }

    @Test
    public void byte2HexShouldReturnCorrectHexValueForMinimumByte() {
        byte input = Byte.MIN_VALUE;
        String expectedOutput = "80";
        assertEquals(expectedOutput, RedixUtils.byte2Hex(input));
    }
}