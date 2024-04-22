package com.loserico.algorithm_msb.sort;

import com.loserico.common.lang.utils.AlgorithmUtils;

public class QuickSort {

    public static void main(String[] args) {
        int[] nums = AlgorithmUtils.randomArr(10, 20);
        nums = new int[] {1, 13, 19, 15, 5};
        AlgorithmUtils.printArray(nums);
        quickSort2(nums);
        AlgorithmUtils.printArray(nums);
    }

    /**
     * 快读排序版本1
     * @param nums
     */
    private static void quickSort1(int[] nums) {
       if (nums== null || nums.length< 2) {
           return;
       }
       process1(nums, 0, nums.length-1);
    }

    /**
     * 快排2.0版本
     * @param nums
     */
    private static void quickSort2(int[] nums) {
       if (nums== null || nums.length< 2) {
           return;
       }
       process2(nums, 0, nums.length-1);
    }

    /**
     * 快排3.0版本
     * @param nums
     */
    private static void quickSort3(int[] nums) {
       if (nums== null || nums.length< 2) {
           return;
       }
       process3(nums, 0, nums.length-1);
    }

    public static void process1(int[] nums, int l, int r) {
        if (l>=r) {
            return;
        }
        int m = partition(nums, l, r);
        process1(nums, l, m-1);
        process1(nums, m+1, r);
    }

    public static void process2(int[] nums, int l, int r) {
        if (l>=r) {
            return;
        }
        int[] arr = partition2(nums, l, r);
        process2(nums, l, arr[0]-1);
        process2(nums, arr[1]+1, r);
    }

    public static void process3(int[] nums, int l, int r) {
        if (l>=r) {
            return;
        }
        int[] arr = partition3(nums, l, r);
        process3(nums, l, arr[0]-1);
        process3(nums, arr[1]+1, r);
    }

    /**
     * arr[L..R]上，以arr[R]位置的数做划分值
     * <=nums[R]的放左边
     * >的放右边
     * @param nums
     * @param l
     * @param r
     * @return
     */
    private static int partition(int[] nums, int l, int r) {
        if (l > r) {
            return -1;
        }
        if (l ==r) {
            return l;
        }
        int target = nums[r];
        int lessIndex = l - 1;
        int index = l;
        while (index < r) {
            if (nums[index] <= target) {
                swap(nums, ++lessIndex, index++);
            } else {
                index++;
            }
        }
        swap(nums, ++lessIndex, r);
        //partition(nums, l, lessIndex);
        //partition(nums, lessIndex + 1, r);
        return lessIndex;
    }

    /**
     * arr[L..R]上，以arr[R]位置的数做划分值
     * <nums[R]的放左边
     * =nums[R]的放中间
     * >的放右边
     * @param nums
     * @param l
     * @param r
     * @return
     */
    private static int[] partition2(int[] nums, int l, int r) {
        if (l > r) {
            return new int[]{-1, -1};
        }
        if (l ==r) {
            return  new int[]{l, l};
        }
        int target = nums[r];
        int lessIndex = l - 1;
        int index = l;
        int moreIndex = r+1;
        while (index < moreIndex) {
            if (nums[index] < target) {
                swap(nums, ++lessIndex, index++);
            } else if(nums[index] == target){
                index++;
            } else {
                swap(nums, index, --moreIndex);
            }
        }
        swap(nums, moreIndex, r);
        //partition(nums, l, lessIndex);
        //partition(nums, lessIndex + 1, r);
        return new int[]{lessIndex, moreIndex};
    }

    /**
     * arr[L..R]上，以arr[R]位置的数做划分值
     * <nums[R]的放左边
     * =nums[R]的放中间
     * >的放右边
     * @param nums
     * @param l
     * @param r
     * @return
     */
    private static int[] partition3(int[] nums, int l, int r) {
        if (l > r) {
            return new int[]{-1, -1};
        }
        if (l ==r) {
            return  new int[]{l, l};
        }
        swap(nums, (int)(Math.random() * (r-l+1)), r); //最忌选一个数跟r交换
        int target = nums[r];
        int lessIndex = l - 1;
        int index = l;
        int moreIndex = r+1;
        while (index < moreIndex) {
            if (nums[index] < target) {
                swap(nums, ++lessIndex, index++);
            } else if(nums[index] == target){
                index++;
            } else {
                swap(nums, index, --moreIndex);
            }
        }
        swap(nums, moreIndex, r);
        //partition(nums, l, lessIndex);
        //partition(nums, lessIndex + 1, r);
        return new int[]{lessIndex, moreIndex};
    }

    public static void swap(int[] nums, int i, int j) {
        if (i == j) {
            return;
        }
        nums[i] = nums[i] ^ nums[j];
        nums[j] = nums[i] ^ nums[j];
        nums[i] = nums[i] ^ nums[j];
    }
}
