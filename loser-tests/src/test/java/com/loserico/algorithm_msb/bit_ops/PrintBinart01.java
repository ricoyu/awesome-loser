package com.loserico.algorithm_msb.bit_ops;

import org.junit.Test;

public class PrintBinart01 {

    @Test
    public void testPrintBinary() {
    	printBinary(1);
        printBinary(6);
        printBinary(7);
    }
    public static void printBinary(int num) {
        for (int i = 31; i >= 0; i--) {
            if ((num & 1 << i) == 0) {
                System.out.print("0");
            } else {
                System.out.print("1");
            }
        }
        System.out.println();
    }
}
