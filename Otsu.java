public class Otsu {
    //算出群間變異數;
	static int variance(int[] histogram) {
		double w1 = 0, u1 = 0, w2 = 0, u2 = 0, max_variance = 0;
		int[] accumulated_histo = new int[256];
		double[] accumulated_histo_µ = new double[256];
		int threshold = 0;

		for(int i = 1; i < 256; i++) {
			accumulated_histo[i] = accumulated_histo[i - 1] + histogram[i];
			accumulated_histo_µ[i] = accumulated_histo_µ[i - 1] + i * histogram[i];			//µ(L) = ∑ i*pi, i=1 to L-1
		}

		for(int i = 1; i < 256; i++) {
			w1 = (double)accumulated_histo[i] / accumulated_histo[255];
			w2 = 1 - w1;

			if ( w1 == 0 || w2 == 0 ) {
				continue;
			}

			u1 = accumulated_histo_µ[i] / w1;
			u2 = (accumulated_histo_µ[255] - accumulated_histo_µ[i]) / w2;
			//double v = w1 * w2 * Math.pow((u1 - u2), 2);
			double v = w1 * (u1 - accumulated_histo_µ[255]) * (u1 - accumulated_histo_µ[255]) + 
			w2 * (u2 - accumulated_histo_µ[255]) * (u2 - accumulated_histo_µ[255]);

			if(v > max_variance) {
				max_variance = v;
				threshold = i;
			}
		}
		return threshold;
	}

	//把圖片二值化
	static int[][][] toBinary(int variance, int[][][] grayData) {
		int height = grayData.length;
		int width = grayData[0].length;
		int[][][] binaryImg = new int[height][width][3];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if(grayData[i][j][0] >= variance) {
                    binaryImg[i][j][0] = 255;
                    binaryImg[i][j][1] = 255;
                    binaryImg[i][j][2] = 255;
                }
                else {
                    binaryImg[i][j][0] = 0;
                    binaryImg[i][j][1] = 0;
                    binaryImg[i][j][2] = 0;
                }
            }
        }
		return binaryImg;
	}

	//對圖片做遮罩
	static int[][][] mask(int[][][] grayData, int[][][] binary_data) {
		int[][][] mask_data = new int[grayData.length][grayData[0].length][3];
		for(int i = 0; i < grayData.length; i++) {
			for(int j = 0; j < grayData[0].length; j++) {
				if(binary_data[i][j][0] == 0) {
					mask_data[i][j][0] = grayData[i][j][0];
					mask_data[i][j][1] = grayData[i][j][0];
					mask_data[i][j][2] = grayData[i][j][0];
				}
				else {
					mask_data[i][j][0] = 0;
					mask_data[i][j][1] = 0;
					mask_data[i][j][2] = 0;
				}
			}
		}
		return mask_data;
	}
}
