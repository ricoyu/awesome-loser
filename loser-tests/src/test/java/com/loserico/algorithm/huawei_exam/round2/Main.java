package com.loserico.algorithm.huawei_exam.round2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine().trim();
		String[] parts = input.split(" ");
		int headAddress = Integer.parseInt(parts[0]); //首节点地址
		int n = Integer.parseInt(parts[1]); //节点数
		int nextAddress = 0;
		List<Node> originalNodes = new ArrayList<>();
		List<Node> nodes = new ArrayList<>();
		Map<Integer, Node> nodeMap = new HashMap<>();

		for (int i = 0; i < n; i++) {
			String line = scanner.nextLine().trim();
			String[] datas = line.split(" ");
			int address = Integer.parseInt(datas[0]);
			int data = Integer.parseInt(datas[1]);
			int next = Integer.parseInt(datas[2]);

			Node node = new Node(address,
					data,
					next);
			nodeMap.put(address, node);
			if (node.isHead(headAddress)) {
				nodes.add(node);
				nextAddress = node.next;
			}
			originalNodes.add(node);
		}


		for (Node node : originalNodes) {
			Node node1 = nodeMap.get(node.next);
			if (node1 != null) {
				nodes.add(node);
			}
		}
		/*
		for (int i = 0; i < n; i++) {
			for (Node node : originalNodes) {
				if (node.isNext(nextAddress)) {
					nextAddress = node.next;
					nodes.add(node);
					break;
				}
			}
		}
		*/

		int midIndex = -1;
		if (nodes.size() %2==1) {
			midIndex = nodes.size()/2;
		}else {
			midIndex = nodes.size()/2;
		}

		System.out.println(nodes.get(midIndex).data);
	}
}

class Node {
	int address;
	int data;
	int next;

	public Node(int address, int data, int next) {
		this.address = address;
		this.data = data;
		this.next = next;
	}

	/**
	 * 判断这个节点是否为诶首节点
	 * @param headAddress
	 * @return
	 */
	public boolean isHead(int headAddress) {
		return this.address == headAddress;
	}

	/**
	 * 判断当前节点是否是耨ge节点的next
	 * @param nextAddress
	 * @return
	 */
	public boolean isNext(int nextAddress) {
		return this.address == nextAddress;
	}

	@Override
	public String toString() {
		return address+","+data+","+next;
	}
}
