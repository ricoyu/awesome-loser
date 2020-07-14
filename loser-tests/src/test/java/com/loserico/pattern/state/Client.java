package com.loserico.pattern.state;

public class Client {

	public static void main(String[] args) {
		Context context = new Context();
		context.setBill(5.50);
		System.out.println("当前话费余额：" + context.getBill() + "$");
		context.Handle();

		context.setBill(-1.50);
		System.out.println("当前话费余额：" + context.getBill() + "$");
		context.Handle();

		context.setBill(50.00);
		System.out.println("当前话费余额：" + context.getBill() + "$");
		context.Handle();
	}
}