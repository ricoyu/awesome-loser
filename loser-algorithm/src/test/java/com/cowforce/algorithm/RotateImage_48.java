package com.cowforce.algorithm;

/**
 * <p>
 * Copyright: (C), 2023-01-31 19:23
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class RotateImage_48 {
	
	public void rotate(int[][] matrix) {
		if (matrix.length == 0 || matrix.length != matrix[0].length) {
			return;
		}
		int maxIndex = matrix.length - 1;
		//先沿右上 - 左下的对角线镜像翻转
		for (int x = 0; x <= maxIndex; ++x) {
			for (int y = 0; y <= maxIndex - x; ++y) {
				int temp = matrix[x][y];
				matrix[x][y] = matrix[maxIndex - y][maxIndex - x];
				matrix[maxIndex - y][maxIndex - x] = temp;
			}
		}
		//再沿水平中线上下翻转
		for (int i = 0; i <= (maxIndex >> 1); ++i) {
			for (int j = 0; j <= maxIndex; ++j) {
				int temp = matrix[i][j];
				matrix[i][j] = matrix[maxIndex - i][j];
				matrix[maxIndex - i][j] = temp;
			}
		}
	}
}
