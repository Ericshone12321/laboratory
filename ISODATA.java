import java.util.ArrayList;
import java.util.Arrays;

public class ISODATA {
    static int[][] isoData(int[] histogram, int k, int totalPixel) {
		ArrayList<double[]> center = new ArrayList<double[]>();
		ArrayList<int[]> finalMeans = new ArrayList<int[]>();
        
		double[] c = new double[k];
        int Nmin = totalPixel / (k + 2);
		
		for(int i = 0; i < k; i++) {
			c[i] = Math.random() * 256;
		}
		Arrays.sort(c);

		for(int i = 0; i < k; i++) {
            double[] c1 = new double[2];
			c1[0] = c[i];
			c1[1] = 100;
            center.add(c1);
		}

        int[][] Means = classification(center, histogram, k);

        for(int i = 0; i < Means.length; i++) {
            finalMeans.add(Means[i]);
        }
        
		for(int t = 0; t < 5; t++) {

            int Nfm = finalMeans.size();                        //檢查pixel數是否小於門檻
            for(int l = 0; l < Nfm; l++) {
                int sum_pixel = totalPixel(finalMeans.get(l), histogram);

                if(sum_pixel < Nmin) {
                    center.remove(l);
                    int[] removeMean = finalMeans.get(l);
                    finalMeans.remove(l);
                    
                    for(int i = 0; i < 256; i++) {
                        if(removeMean[i] == 1) {
                            double[] result = new double[center.size()];
                            for(int j = 0; j < center.size(); j++) {
                                result[j] = squareMinus(i, center.get(j)[0], histogram[i], center.get(j)[1]);                               
                            }

                            int min_index = 0;
                            double min_result = result[0];
                            for(int j = 1; j < result.length; j++) {
                                if(min_result > result[j]) {
                                    min_result = result[j];
                                    min_index = j;
                                }                                                 
					        }
                            finalMeans.get(min_index)[i] = 1;
				        }                                
                    }
                    l -= 1;
                    Nfm -= 1;
                }
            }                                                   //檢查pixel數是否小於門檻


            for(int i = 0; i < finalMeans.size(); i++) {
                double[] c1 = find_center(finalMeans.get(i), histogram);
                center.get(i)[0] = c1[0];
                center.get(i)[1] = c1[1];
            }


            if(finalMeans.size() < k) {                 //如果符合條件就進行分裂
                int fm = finalMeans.size();
                double[] squareMinus = new double[fm];
                
                for(int i = 0; i < fm; i++) {
                    int total = 0;
                    for(int j = 0; j < 256; j++) {
                        if(finalMeans.get(i)[j] == 1) {
                            squareMinus[i] += squareMinus(center.get(i)[0], j, center.get(i)[1], histogram[j]);
                            total += 1;
                        }
                    }
                    squareMinus[i] /= total;
                }

                int max_index = 0;
				double max_result = squareMinus[0];
				for(int j = 1; j < squareMinus.length; j++) {
					if(max_result < squareMinus[j]) {
						max_result = squareMinus[j];
						max_index = j;
					}
				}

                int[] new1 = new int[256];
                int[] new2 = new int[256];
                for(int i = 0; i < 256; i++) {
                    if(finalMeans.get(max_index)[i] == 1) {
                        double result = squareMinus(center.get(max_index)[0], i, center.get(max_index)[1], histogram[i]);
                        if(result > squareMinus[max_index]) {
                            new2[i] = 1;
                        }
                        else {
                            new1[i] = 1;
                        }
                    }                     
                }
                center.remove(max_index);
                finalMeans.remove(max_index);
                finalMeans.add(new1);
                finalMeans.add(new2);
                center.add(find_center(new1, histogram));
                center.add(find_center(new2, histogram));
                
            }                                               //如果符合條件就進行分裂

            if(finalMeans.size() > k) {                 //如果符合條件就進行合併
                for(int i = finalMeans.size(); i > 2; i--) {
                    int start = 0;
                    for(int j = 1; j < i; j++) {
                        if(squareMinus(center.get(start)[0], center.get(start + j)[0], center.get(start)[1], center.get(start + j)[1]) > 20) {
                            int[] merge_set = finalMeans.get(start);
                            for(int n = 0; n < 256; n++) {
                                if(finalMeans.get(start + j)[n] == 1) {
                                    merge_set[n] = 1;
                                }
                            }
                            double[] new_center = find_center(merge_set, histogram);
                            finalMeans.remove(start);
                            finalMeans.remove(start + j - 1);
                            center.remove(start);
                            center.remove(start + j - 1);
                            finalMeans.add(merge_set);
                            center.add(new_center);
                            break;
                        }
                        else {
                            start += 1;
                        }
                    }
                }
            }                                               //如果符合條件就進行合併

		}   //總共做t次

        int[][] returnMeans = new int[finalMeans.size()][256];
        for(int i = 0; i < finalMeans.size(); i++) {
            returnMeans[i] = finalMeans.get(i);
        }
        return returnMeans;	
	}

    static int totalPixel(int[] data, int[] histogram) {
        int sum = 0;
        for(int i = 0; i < data.length; i++) {
            if(data[i] == 1) {
                sum += histogram[i];
            }
        }
        return sum;
    }

    static double squareMinus(double x1, double x2, double y1, double y2) {
        double result = Math.pow(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2), 0.5);
        return result;
    }

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

    static double[] find_center(int[] data, int[] histogram) {
        double[] ans = new double[2];
        double sum_x = 0, sum_y = 0;
        int total = 0;
        for(int j = 0; j < 256; j++) {
            if(data[j] == 1) {
                sum_x += j;
                sum_y += histogram[j];
                total += 1;
            }
        }
        ans[0] = (double)sum_x / total;
        ans[1] = (double)sum_y / total;
        return ans;
    }

    static int check1(int[] data) {
        int sum = 0;
        for(int i: data) {
            if(i == 1) {
                sum += 1;
            }
        }
        return sum;
    }

    static int[][] classification(ArrayList<double[]> center, int[] histogram, int k) {
        int[][] means = new int[k][256];

			for(int i = 0; i < 256; i++) {
				double[] result = new double[center.size()];

				for(int j = 0; j < center.size(); j++) {
					double x = Math.pow((i - center.get(j)[0]), 2);
					double y = Math.pow((histogram[i] - center.get(j)[1]), 2);
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
        return means;
    }

    static double[][] ArrayListToArray(ArrayList<double[]> center) {
        double[][] values = new double[center.size()][2];
        for(int i = 0; i < center.size(); i++) {
            values[i] = center.get(i);
        }
        return values;
    }
}