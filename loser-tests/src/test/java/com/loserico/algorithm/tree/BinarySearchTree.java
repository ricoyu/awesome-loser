package com.loserico.algorithm.tree;

import java.util.Scanner;

/**
 * <p>
 * Copyright: (C), 2022-07-06 16:12
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class BinarySearchTree {
	
	public BinarySearchTree() {
	}
	
	public BinaryNode find(BinaryNode root, int data) {
		BinaryNode current = root;
		while (current != null) {
			if (data < current.data) {
				current = current.left;
			} else if (data > current.data) {
				current = current.right;
			} else {
				return current;
			}
		}
		return null;
	}
	
	/**
	 * 获取节点的层数, 节点的深度+1
	 *
	 * @param root
	 * @return
	 */
	public int getTreeDepth(BinaryNode root) {
		return root == null ? 0 : (1 + Math.max(getTreeDepth(root.left), getTreeDepth(root.right)));
	}
	
	private void writeArray(BinaryNode currNode, int rowIndex, int columnIndex, String[][] res, int treeDepth) {
		//保证输入的树不为空
		if (currNode == null) {
			return;
		}
		//先将当前节点保存到二维数组中
		res[rowIndex][columnIndex] = String.valueOf(currNode.data);
		//计算当前位于树的第几层
		int currentLevel = ((rowIndex + 1) / 2);
		//若到了最后一层，则返回
		if (currentLevel == treeDepth) {
			return;
		}
		//计算当前行到下一行，每个元素之间的间隔（下一行的列索引与当前元素的列索引之间的间隔）
		int gap = treeDepth - currentLevel - 1;
		
		//对左儿子进行判断，若有左儿子，则记录相应的"/"与左儿子的值
		if (currNode.left != null) {
			res[rowIndex + 1][columnIndex - gap] = "/";
			writeArray(currNode.left, rowIndex + 2, columnIndex - gap * 2, res, treeDepth);
		}
		
		//对右儿子进行判断，若有右儿子，则记录相应的"\"与右儿子的值
		if (currNode.right != null) {
			res[rowIndex + 1][columnIndex + gap] = "\\";
			writeArray(currNode.right, rowIndex + 2, columnIndex + gap * 2, res, treeDepth);
		}
	}
	
	public void show(BinaryNode root) {
		if (root == null) {
			System.out.println("EMPTY!");
			return;
		}
		//得到树的深度
		int treeDepth = getTreeDepth(root);
		//最后一行的宽度为2的（n - 1）次方乘3，再加1
		//作为整个二维数组的宽度
		int arrayHeight = treeDepth * 2 - 1;
		int arrayWidth = (2 << (treeDepth - 2)) * 3 + 1;
		// 用一个字符串数组来存储每个位置应显示的元素
		String[][] res = new String[arrayHeight][arrayWidth];
		//对数组进行初始化，默认为一个空格
		for (int i = 0; i < arrayHeight; i++) {
			for (int j = 0; j < arrayWidth; j++) {
				res[i][j] = " ";
			}
		}
		
		// 从根节点开始，递归处理整个树
		writeArray(root, 0, arrayWidth / 2, res, treeDepth);
		
		//此时，已经将所有需要显示的元素储存到了二维数组中，将其拼接并打印即可
		for (String[] line : res) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < line.length; i++) {
				sb.append(line[i]);
				if (line[i].length() > 1 && i <= line.length - 1) {
					i += line[i].length() > 4 ? 2 : line[i].length() - 1;
				}
			}
			System.out.println(sb.toString());
		}
	}
	
	public void insert(BinaryNode root, int data) {
		if (data < root.data) {
			if (root.left == null) {
				BinaryNode node = new BinaryNode(data);
				root.left = node;
				node.parent = root;
			} else {
				insert(root.left, data);
			}
		} else {
			if (root.right == null) {
				BinaryNode node = new BinaryNode(data);
				root.right = node;
				node.parent = root;
			} else {
				insert(root.right, data);
			}
		}
	}
	
	public BinaryNode remove(BinaryNode root, int data) {
		BinaryNode delNode = find(root, data);
		if (delNode == null) {
			System.out.println("要删除的值不在树中");
			return root;
		}
		//删除的点没有左右子树的情况
		if (delNode.left == null && delNode.right == null) {
			if (delNode == root) {
				root = null;
			} else if (delNode.parent.data < delNode.data) {// 说明删除的点是右子节点
				delNode.parent.right = null;
			} else {
				delNode.parent.left = null;
			}
		} else if (delNode.left != null && delNode.right != null) {// 2.删除的节点有两颗子节点
			BinaryNode successor = findSuccessor(delNode); // 先找的后继节点
			//后继节点和删除节点进行交换，首先后继节点的左节点是肯定为空的
			successor.left = delNode.left; // 后继的左边变为删除的左边
			successor.left.parent = successor; // 删除点的左边parent指向后继节点
			// 再来看后继节点的右边
			if (successor.right != null && successor.parent != delNode) { // 后继节点有右边,这其实就是下面情况3的第一种
				successor.parent.left = successor.right;
				successor.right = delNode.right;
				successor.right.parent = successor;
			} else if (successor.right == null) {//如果后继节点没有右边,那其实就是情况1，没有左右子树
				if (successor.parent != delNode) {//如果后继节点的parent不等于删除的点 那么就需要把删除的右子树赋值给后继节点
					successor.parent.left = null;        //注意原来的后继节点上的引用要删掉,否则会死循环
					successor.right = delNode.right;
					successor.right.parent = successor;
				}
			}
			// 替换做完接下来就要删除节点了
			if (delNode == root) {
				successor.parent = null;
				root = successor;
				return root;
			}
			successor.parent = delNode.parent;
			if (delNode.data > delNode.parent.data) { // 删除的点在右边，关联右子树
				delNode.parent.right = successor;
			} else {
				delNode.parent.left = successor;
			}
		} else {// 3.删除点有一个子节点
			if (delNode.right != null) {// 有右节点
				if (delNode == root) {
					root = delNode.right;
					return root;
				}
				delNode.right.parent = delNode.parent;// 把右节点的parent指向删除点的parent
				// 关联父节点的左右子树
				if (delNode.data < delNode.parent.data) {// 删除的点在左边
					delNode.parent.left = delNode.right;
				} else {
					delNode.parent.right = delNode.right;
				}
			} else {
				if (delNode == root) {
					root = delNode.left;
					return root;
				}
				delNode.left.parent = delNode.parent;
				if (delNode.data < delNode.parent.data) {
					delNode.parent.left = delNode.left;
				} else {
					delNode.parent.right = delNode.left;
				}
			}
		}
		
		return root;
	}
	
	/**
	 * 查找node的后继节点, 后继节点的为该二叉树的中序遍历后, 该节点后面一个结点即该节点的后继结点。
	 *
	 * @param node
	 * @return BinaryNode
	 */
	public BinaryNode findSuccessor(BinaryNode node) {
		//表示没有右边 那就没有后继
		if (node.right == null) {
			return null;
		}
		BinaryNode cur = node.right;
		// 开一个额外的空间 用来返回后继节点，因为我们要找到为空的时候，那么其实返回的是上一个节点
		BinaryNode pre = node.right;
		while (cur != null) {
			pre = cur;
			// 注意后继节点是要往左边找，因为右边的肯定比左边的大，我们要找的是第一个比根节点小的，所以只能往左边
			cur = cur.left;
		}
		return pre; // 因为cur会变成null，实际我们是要cur的上一个点，所以就是pre来代替
	}
	
	/**
	 * 中序是按左根右的顺序
	 *
	 * @param root
	 */
	public void midOrder(BinaryNode root) {
		if (root == null) {
			return;
		}
		midOrder(root.left);
		System.out.println(root.data);
		midOrder(root.right);
	}
	
	static class BinaryNode {
		private int data;
		private BinaryNode left;
		private BinaryNode right;
		private BinaryNode parent;
		
		public BinaryNode(int data) {
			this.data = data;
		}
	}
	
	/**
	 * 测试用例
	 * 15
	 * 10
	 * 19
	 * 8
	 * 13
	 * 16
	 * 28
	 * 5
	 * 9
	 * 12
	 * 14
	 * 20
	 * 30
	 * -1
	 * 删除：15 8 5 10 12 19 16 14 30 9 13 20 28
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		BinarySearchTree binarySearchTree = new BinarySearchTree();
		BinaryNode root = null;
		Scanner cin = new Scanner(System.in);
		int t = 1;
		System.out.println("二叉搜索树假定不存重复的子节点,重复可用链表处理，请注意~~");
		System.out.println("请输入根节点:");
		int rootData = cin.nextInt();
		root = new BinaryNode(rootData);
		System.out.println("请输入第" + t + "个点:输入-1表示结束");
		while (true) {
			int data = cin.nextInt();
			if (data == -1) {
				break;
			}
			binarySearchTree.insert(root, data);
			t++;
			System.out.println("请输入第" + t + "个点:输入-1表示结束");
		}
		binarySearchTree.show(root);
		while(true) {
			System.out.println("请输入要删除的点：-1表示结束");
			int key = cin.nextInt();
			root = binarySearchTree.remove(root, key);
			binarySearchTree.show(root);
			if(root == null) {
				System.out.println("树已经没有数据了~~");
				break;
			}
		}
	}
}
