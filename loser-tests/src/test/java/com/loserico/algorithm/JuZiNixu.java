package com.loserico.algorithm;

import java.util.Scanner;

public class JuZiNixu {
    
    /**
     * 句子逆序
     * 
     * 将一个英文语句以单词为单位逆序排放。例如“I am a boy”，逆序排放后为“boy a am I”
     * 所有单词之间用一个空格隔开，语句中除了英文字母外，不再包含其他字符
     * @param args
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while(in.hasNext()){
            String str = in.nextLine();
            String arr[] = str.split(" ");
            StringBuffer res = new StringBuffer();
            for(int i=arr.length-1; i>0; i--){
                res.append(arr[i]+" ");
            }
            res.append(arr[0]);
            System.out.println(res.toString());
        }
    }
}