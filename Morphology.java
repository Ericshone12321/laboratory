/**
 * Morphology
 */
public class Morphology {

    static int[][][] dilation(int data[][][]) {
		int[][][] rData = new int[data.length][data[0].length][3];
		int[][] dxy = new int[][] {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 0}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				if (data[i][j][0] == 0xff) {
					for (int d = 0; d < 9; d++) {
						for (int c = 0; c < 3; c++) {
							rData[Util.checkImageBounds(dxy[d][0]+i, data.length)][Util.checkImageBounds(dxy[d][1]+j, data[0].length)][c] = 0xff;
						}
					}
				}
				
			}
		}
		return rData;
	}

	static int[][][] erosion(int data[][][]) {
		int[][][] rData = new int[data.length][data[0].length][3];
		int[][] dxy = new int[][] {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 0}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
		boolean pass;

		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				pass = true;
				if (data[i][j][0] == 0xff) {
					for (int d = 0; d < 9; d++) {
						if (data[Util.checkImageBounds(dxy[d][0]+i, data.length)][Util.checkImageBounds(dxy[d][1]+j, data[0].length)][0] == 0x0) {
							pass = false;
							break;
						}
					}
				}else {
					pass = false;
				}
				for (int c = 0; c < 3; c++) {
					rData[i][j][c] = (pass ? 0xff : 0x0);
				}
			}
		}
		return rData;
	}
	static int[][][] opening(int data[][][]) {
		return dilation(erosion(data));
	}

	static int[][][] closing(int data[][][]) {
		return erosion(dilation(data));
	}
}