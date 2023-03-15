package com.loserico.innerclass;

public class Outer2 {

  private static int radius = 1;
  private int count =2;

  class Inner {
    public void visit() {
      System.out.println("visit outer static variable:" + radius);
      System.out.println("visit outer variable:" + count);
    }
  }
  
  public static void main(String[] args) {
    Outer2 outer = new Outer2();
    Outer2.Inner inner = outer.new Inner();
    inner.visit();
  }
}
