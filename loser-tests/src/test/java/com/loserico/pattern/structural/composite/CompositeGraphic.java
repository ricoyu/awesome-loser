package com.loserico.pattern.structural.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * 复合图形类（CompositeGraphic），它也实现了Graphic接口，可以包含其他Graphic对象，无论是简单的还是复杂的
 * <p>
 * Copyright: Copyright (c) 2024-03-29 17:11
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>

 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class CompositeGraphic implements Graphic {
    // 集合用于存储图形集合
    private List<Graphic> childGraphics = new ArrayList<>();

    // 添加图形到组合中
    public void add(Graphic graphic) {
        childGraphics.add(graphic);
    }

    // 从组合中移除图形
    public void remove(Graphic graphic) {
        childGraphics.remove(graphic);
    }

    // 打印图形到控制台
    @Override
    public void draw() {
        for (Graphic graphic : childGraphics) {
            graphic.draw(); // 调用每个子对象的绘制方法
        }
    }
}
