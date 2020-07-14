package com.loserico.reactive;

/**
 * Hot and Cold Observables
 * 
 * 需要说明的时, Hot Observables 和cold Observables并不是严格的概念区分, 它只是对于两类Observable形象的描述
 * 
 * Cold Observables: 指的是那些在订阅之后才开始发送事件的Observable(每个Subscriber都能接收到完整的事件) Hot
 * Observables: 指的是那些在创建了Observable之后, (不管是否订阅)就开始发送事件的Observable
 * 其实也有创建了Observable之后调用诸如publish()方法就可以开始发送事件的, 这里咱们暂且忽略.
 * 
 * 我们一般使用的都是Cold Observable, 除非特殊需求, 才会使用Hot Observable, 在这里, Hot Observable
 * 这一类是不支持背压的, 而是Cold Observable这一类中也有一部分并不支持背压(比如interval, timer等操作符创建的Observable)
 * 
 * Tips: 
 * 	都是Observable, 结果有的支持背压, 有的不支持, 这就是RxJava1.X的一个问题. 在2.0中, 这种问题已经解决了, 以后谈到2.0时再细说.
 * 
 * 在那些不支持背压策略的操作符中使用响应式拉取数据的话，还是会抛出MissingBackpressureException.
 * 
 * 那么, 不支持背压的Observevable如何做流速控制呢?
 * <p>
 * Copyright: Copyright (c) 2018-11-24 22:36
 * <p>
 * Company: DataSense
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class HotColdObservables {

}
