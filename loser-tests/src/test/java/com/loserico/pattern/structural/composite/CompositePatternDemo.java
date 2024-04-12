package com.loserico.pattern.structural.composite;

public class CompositePatternDemo {

    public static void main(String[] args) {
        // 初始化简单图形
        Graphic circle = new Circle();
        Graphic square = new Square();

        // 创建复合图形
        CompositeGraphic composite = new CompositeGraphic();
        // 将简单图形添加到复合图形中
        composite.add(circle);
        composite.add(square);

        // 创建另一个复合图形
        CompositeGraphic composite2 = new CompositeGraphic();
        // 将一个简单图形和一个复合图形添加到另一个复合图形中
        composite2.add(new Circle());
        composite2.add(composite);

        // 绘制第二个复合图形，它包含一个简单图形和另一个复合图形
        System.out.println("Drawing composite2:");
        composite2.draw();
    }
}
