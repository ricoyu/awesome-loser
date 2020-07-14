package com.loserico.tree;

import lombok.Data;

/**
 * 红黑树是一种含有红黑节点并能自平衡的二叉查找树
 * 红黑树是非完美平衡二叉查找树, 是完美黑色平衡二叉查找树
 * 1: 每个节点要么是黑的, 要么是红的
 * 2: 根节点是黑色的
 * 3: 叶子节点是黑的(叶子节点是虚拟节点, 值为NIL)
 * 4: 每个红色节点的两个子节点一定是黑色的(根据第2,4点可以推论出红色节点一定有父节点)
 * 5: 任意节点到每个叶子节点的路径都包含数量相同的黑节点
 * (如果把红黑树的所有红色节点去掉, 剩下的黑色节点是平衡的)
 * <p>
 * 新节点加入的时候, 默认是红色的
 * <p>
 * 红黑树的自平衡每次只需考虑CPGU三代即可, 其余部分无需考虑
 * 祖父 G
 * 父母 P
 * 叔叔 U
 * 兄弟 B
 * 根   R
 * 当前新增的节点 C
 * <p>
 * Copyright: (C), 2019/11/26 17:46
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Data
public class RBTree {
	
	/**
	 * 新增加点默认是红色的
	 */
	private boolean red = true;
	
	private RBTree left;
	
	private RBTree right;
	
	private Integer value;
}
