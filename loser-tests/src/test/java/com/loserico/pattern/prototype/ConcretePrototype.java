package com.loserico.pattern.prototype;

public class ConcretePrototype {
    private String id;
    private String category;

    // 标准构造函数
    public ConcretePrototype(String id, String category) {
        this.id = id;
        this.category = category;
    }

    // 复制构造函数
    public ConcretePrototype(ConcretePrototype prototype) {
        // 可以在这里选择性地进行深拷贝
        this.id = prototype.id;
        this.category = prototype.category;
    }

    // 获取ID
    public String getId() {
        return id;
    }

    // 获取分类
    public String getCategory() {
        return category;
    }

    // 提供一个方法来克隆对象
    public ConcretePrototype clone() {
        return new ConcretePrototype(this);
    }

    // 重写toString方法以便输出对象信息
    @Override
    public String toString() {
        return "ConcretePrototype{" +
                "id='" + id + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}