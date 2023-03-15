package com.loserico.innerclass;

public class Outer {
	
	private int out_a = 12;
	private static int STATIC_b = 34;
	
	public void testFunctionClass() {
		int inner_c = 0;
		class Inner {
			private void fun() {
				System.out.println(out_a);
				System.out.println(STATIC_b);
				System.out.println(inner_c);
			}
		}
		Inner inner = new Inner();
		inner.fun();
	}
	
	public static void testStaticFunctionClass() {
		int d = 0;
		class Inner {
			private void fun() {
				//编译错误，定义在静态方法中的局部类不可以访问外
				// System.out.println(out_a); 
				//部类的实例变量
				System.out.println(STATIC_b);
				System.out.println(d);
			}
		}
		Inner inner = new Inner();
		inner.fun();
	}
	
	public static void main(String[] args) {
		testStaticFunctionClass();
	}
}
