package com.loserico.algorithm_msb.search;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 二分查找
 * <p>
 * Copyright: Copyright (c) 2024-03-25 9:17
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 *
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class BinarySearch {

    public boolean exists(int[] sortedArr, int num) {
        if ((sortedArr == null || sortedArr.length == 0)) {
            return false;
        }

        int l = 0;
        int r = sortedArr.length - 1;
        int mid = 0;
        int i = 1;
        while (l < r) {
            log.info("第{}次查找", i++);
            mid = l + ((r - l) >> 1);
            if (sortedArr[mid] == num) {
                return true;
            } else if (num > sortedArr[mid]) {
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }
        return sortedArr[l] == num;
    }

    @Test
    public void testBinarySearch() {
        int[] sortedArr = new int[]{1, 3, 5, 7, 9};
        int[] searchTarget = new int[]{3, 9, 4};
        for (int i = 0; i < searchTarget.length; i++) {
            int num = searchTarget[i];
            boolean exists = exists(sortedArr, num);
            log.info((exists ? "找到" : "找不到") + "数字{}", num);
        }
    }
}
