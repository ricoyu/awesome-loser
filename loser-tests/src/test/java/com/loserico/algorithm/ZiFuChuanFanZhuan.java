package com.loserico.algorithm;

import java.util.Scanner;
  
public class ZiFuChuanFanZhuan {
    
    /**
     * 字符串反转
     * 
     * 写出一个程序，接受一个字符串，然后输出该字符串反转后的字符串。（字符串长度不超过1000）
     * @param args
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while(sc.hasNext()){
            String s = sc.nextLine();
            for(int i=s.length()-1 ;i>=0 ;i--){
                System.out.print(s.charAt(i));
            }
        }
    }
}