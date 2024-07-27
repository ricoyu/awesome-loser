package com.loserico.jdk9;

import org.junit.Test;

import java.io.*;

public class TryDemo {

    @Test
    public void test() {
        InputStreamReader isr = null;
        //传统的 try catch finally语句块
        try {
            isr = new InputStreamReader(new FileInputStream("D:\\Dropbox\\doc\\cssksqn 穿梭时空三千年.txt"));
            isr.read();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void test2() {
        //JAVA8 try语法升级
        try (InputStreamReader isr = new InputStreamReader(new FileInputStream("D:\\Dropbox\\doc\\cssksqn 穿梭时空三千年.txt"))) {
            isr.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //JAVA9 try语法升级
    @Test
    public void test3() throws FileNotFoundException {
        InputStreamReader isr = new InputStreamReader(new FileInputStream("D:\\Dropbox\\doc\\cssksqn 穿梭时空三千年.txt"));
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream("D:/aa.txt"));
        try (isr; osw) {
            isr.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
