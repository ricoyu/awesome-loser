package com.loserico.innerclass;

public class MemberInner {

	private static int radius = 1;

	private int count = 2;

	class Inner {
		public void visit() {
			System.out.println("外部类静态变量radius: " + radius);
			System.out.println("外部私有变量radius: " + count);
		}
	}

	public static void main(String[] args) {
		MemberInner memberInner = new MemberInner();
		MemberInner.Inner inner = memberInner.new Inner();
		inner.visit();
	}
}
