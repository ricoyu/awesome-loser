package com.loserico.algorithm_msb.bit_ops;

import org.junit.Test;

public class FindKTimesNum {

    @Test
    public void test() {
    	int[] nums = new int[]{63, 60, 96, 11, 13, 13, 62, 96, 63, 65, 69, 88, 60, 11, 13, 60, 63, 63, 65, 63, 88, 11, 62, 69, 65, 71, 88, 62, 96, 63, 69};
        int num = testFindKTimesNum(nums, 1, 3);
        System.out.println(num);
    }
    /**
     * 一个数组中有一种数出现K次, 其他数都出现了M次, M>1, K<M
     * 要求找到出现了K次的数, 额外空间复杂度O(1), 时间复杂度O(n)
     */
    public static int testFindKTimesNum(int[] nums, int k, int m) {
        //初始化一个数组，用于存储所有位上1的累加和
        int[] bits = new int[32];
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < 32; j++) {
                //判断当前位是否为1，是则累加到对应的bits数组中
                bits[j] += nums[i]>>j & 1;
            }
        }

        //通过模M的结果，还原出现K次的数
        int kNum = 0;
        for (int i = 0; i < bits.length; i++) {
            if (bits[i] % m != 0) {
                kNum |= (1<<i);
            }
        }

        return kNum;
    }
}
