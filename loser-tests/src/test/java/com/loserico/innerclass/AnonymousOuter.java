package com.loserico.innerclass;

public class AnonymousOuter {
	
	private void test(final int i) {
		new Service() {
			public void method() {
				for (int j = 0; j < i; j++) {
					System.out.println("匿名内部类");
				}
			}
		}.method();
	}
}

//匿名内部类必须继承或实现一个已有的接口
interface Service {
	void method();
}
