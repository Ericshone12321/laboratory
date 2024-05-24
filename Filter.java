public class Filter {
    static int[][][] threshold_filter_UP(int data[][][], int threshold) {
		int[][][] newData = new int[data.length][data[0].length][3];
		
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				for (int c = 0; c < 3; c++) {
					newData[i][j][c] = ((data[i][j][c] >= threshold) ? 0xff : 0x00);

				}
			}
		}
		return newData;
	}
    static int[][][] threshold_filter_DOWN(int data[][][], int threshold) {
		int[][][] newData = new int[data.length][data[0].length][3];
		
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				for (int c = 0; c < 3; c++) {
					newData[i][j][c] = ((data[i][j][c] <= threshold) ? 0xff : 0x00);

				}
			}
		}
		return newData;
	}
    static int[][][] threshold_filter_UP_AND_DOWN(int data[][][], int threshold_Left, int threshold_Right) {
        int [][][] newData = new int[data.length][data[0].length][3];

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                for (int c = 0; c < 3; c++) {
                    if (data[i][j][c] <= threshold_Left && data[i][j][c] > 0x0) {
                        newData[i][j][c] = 0xff;

                    }else if (data[i][j][c] >= threshold_Right) {
                        newData[i][j][c] = 0xff;

                    }else {
                        newData[i][j][c] = 0x0;

                    }
                }
            }
        }
        return newData;
    }
}
