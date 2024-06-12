import java.util.Arrays;

public class K_means {
    //把各個RGB做分群
	static int[][] k_means(int[] histogram, int k) {
		double[][] center = new double[k][2];
		int[][] means = new int[k][256];
		double[] c = new double[k];
		
		for(int i = 0; i < k; i++) {
			c[i] = Math.random() * 256;
		}
		Arrays.sort(c);
		for(int i = 0; i < k; i++) {
			center[i][0] = c[i];
			center[i][1] = 100;
		}
	
		for(int t = 0; t < 20; t++) {
			means = new int[k][256];

			for(int i = 0; i < 256; i++) {
				double[] result = new double[k];

				for(int j = 0; j < k; j++) {
					double x = Math.pow((i - center[j][0]), 2);
					double y = Math.pow((histogram[i] - center[j][1]), 2);
					result[j] = x + y;
				}

				int min_index = 0;
				double min_result = result[0];
				for(int j = 1; j < result.length; j++) {
					if(min_result > result[j]) {
						min_result = result[j];
						min_index = j;
					}
				}
				
				means[min_index][i] = 1;
			}

			for(int i = 0; i < means.length; i++) {
				double sum_x = 0, sum_y = 0;
				int total = 0;
				for(int j = 0; j < means[0].length; j++) {
					if(means[i][j] == 1) {
						sum_x += j;
						sum_y += histogram[j];
						total += 1;
					}
				}
				center[i][0] = (double)sum_x / total;
				center[i][1] = (double)sum_y / total;
			}
		}
		return means;
	}

	//把灰階圖檔依據分好的means做分類
	static int[][][] cluster(int[][] means, int[][][] grayData) {
		int color = 255 / means.length;
		int height = grayData.length;
		int width = grayData[0].length;
		int[][][] newData = new int[height][width][3];

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				for(int k = 0; k < means.length; k++) {
					if(means[k][grayData[i][j][0]] == 1) {
						newData[i][j][0] = k * color;
						newData[i][j][1] = k * color;
						newData[i][j][2] = k * color;
						continue;
					}
				}
			}
		}
		return newData;
	}
}
