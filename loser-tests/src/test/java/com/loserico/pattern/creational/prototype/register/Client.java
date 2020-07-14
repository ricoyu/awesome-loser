package com.loserico.pattern.creational.prototype.register;

public class Client {
	public static void main(String[] args) {
		try {
			Prototype p1 = new ConcretePrototype1();
			PrototypeManager.setPrototype("p1", p1);
			//获取原型来创建对象
			Prototype p3 = PrototypeManager.getPrototype("p1").clone();
			p3.setName("张三");
			System.out.println("第一个实例：" + p3);
			//有人动态的切换了实现
			Prototype p2 = new ConcretePrototype2();
			PrototypeManager.setPrototype("p1", p2);
			//重新获取原型来创建对象
			Prototype p4 = PrototypeManager.getPrototype("p1").clone();
			p4.setName("李四");
			System.out.println("第二个实例：" + p4);
			//有人注销了这个原型
			PrototypeManager.removePrototype("p1");
			//再次获取原型来创建对象
			Prototype p5 = PrototypeManager.getPrototype("p1").clone();
			p5.setName("王五");
			System.out.println("第三个实例：" + p5);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}