package com.loserico.algorithm_msb;

import com.loserico.codec.RedixUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ExclusiveOr {

    @Test
    public void testExchangeTwoNum() {
        int a = 11;
        int b = 201;

        a = a ^ b;
        b = a ^ b; // a ^ b ^ b = a ^ 0 = a
        a = a ^ b; // a ^ a ^ b = 0 ^ b = b

        assertThat(a).isEqualTo(201);
        assertThat(b).isEqualTo(11);
    }

    /**
     * 一个数组中有一种数出现了奇数次, 其他数都出现了偶数次, 怎么找到并打印这种数?
     */
    @Test
    public void testOddTimeNum() {
        int[] arr = {13, 53, 16, 13, 96, 27, 13, 14, 57, 42, 64, 96, 16, 57, 13, 27, 53, 14, 42, 77, 77};
        int num = 0;
        for (int i = 0; i < arr.length; i++) {
            num = num ^ arr[i];
        }
        System.out.println(num);
    }

    /**
     * 提取一个int类型数的最右侧1
     * 其他位都不要, 只要最右边的一个位上的1
     */
    @Test
    public void testMustRightOne() {
        int i = 666;
        String binary1 = RedixUtils.int2BinaryStr(i);
        String binary2 = RedixUtils.int2BinaryStr(i & (-i));
        System.out.println(~i + 1);
        System.out.println(i & (-i));
        System.out.println(binary1);
        System.out.println(binary2);
    }

    /**
     * 一个数组中有两种数(a, b)出现了奇数次, 其他数都出现了偶数次, 怎么找到并打印这两种数
     *
     */
    @Test
    public void testTwoOddNumInArray() {
        //下面数组有两种数（10和69）出现了奇数次, 其他数都出现了偶数次。
    	int[] arr = new int[]{199, 10, 128, 184, 145, 10, 145, 69, 128, 145, 145, 10, 184, 145, 145, 199, 184, 145, 10, 135, 10, 128, 199, 135, 199, 184, 199, 69, 128, 184, 145, 128, 69, 69, 128, 128, 128, 69, 184, 199};
        int eor = 0;
        for (int i = 0; i < arr.length; i++) {
            eor = eor ^ arr[i]; //最后得到的eor=a^b
        }

        int lastOneNum = eor & (-eor); //找到最后一位是1的数

        List<Integer> oneNums = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            if ((lastOneNum & arr[i]) != 0) {
                oneNums.add(arr[i]);
            }
        }

        int eor2 = 0;
        for (Integer num : oneNums) {
            eor2 = eor2 ^ num;
        }

        int a = eor2;
        int b = eor ^ a;
        System.out.println("这两个数是: "+a +" " + b);
    }
}
