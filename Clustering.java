import java.util.Arrays;
import java.util.Random;

public class Clustering {
    static int[] k_means(int[] data, double[][] means_set) {
        double[][] means_vector;
        int[] clustering_result = new int[data.length];
        int k = means_set.length, belongSet;
        double distance, min_dist;
        int wx = 1, wy = 1, wz = 1;
        
        for (int t = 0; t < 100; t++) {
            means_vector = new double[k][3];
            for (int i = 0; i < data.length; i++) {              
                min_dist = Integer.MAX_VALUE;
                belongSet = 0;
                for (int u = 0; u < k; u++) {
                    distance = (means_set[u][0]-i)*(means_set[u][0]-i) + 
                                (means_set[u][1]-data[i])*(means_set[u][1]-data[i]);
                    if (min_dist > distance) {
                        belongSet = u;
                        min_dist = distance;
                    }
                }
                means_vector[belongSet][0] += i*wx;       //x-weight
                means_vector[belongSet][1] += data[i]*wy;       //y-weight
                means_vector[belongSet][2]++;      //number of point which belongs to this set
                
            }
            double x, y;
            int xor_array = k;
            for (int u = 0; u < k; u++) {       
                x = means_vector[u][0] / means_vector[u][2];
                y = means_vector[u][1] / means_vector[u][2];

                if (x == means_set[u][0] && y == means_set[u][1]) {
                    xor_array--;

                }
                means_set[u] = new double[] {x, y};
            }
            if (xor_array == 0)
                break;
        }
        for (int u = 0; u < means_set.length; u++) {
            System.out.printf("x: %f, y: %f\n", means_set[u][0], means_set[u][1]);
        }
        System.out.println();
        for (int i = 0; i < clustering_result.length; i++) {
            min_dist = Integer.MAX_VALUE;
            belongSet = 0;
            for (int u = 0; u < k; u++) {
                distance = wx*(means_set[u][0]-i)*(means_set[u][0]-i) + 
                            wy*(means_set[u][1]-data[i])*(means_set[u][1]-data[i]);
                if (min_dist > distance) {
                    belongSet = u;
                    min_dist = distance;
                } 
            }
            clustering_result[i] = belongSet;
        }
        return clustering_result;
    }
    static double[][] k_means_plus_plus(int[] data, int k) {
        Random rand = new Random();
        double x, y;
        double min_distance, distance, total, last_value;
        double[][] means_set = new double[k][2];
        double[] point2mean_nearst_dist_sum;
        

        do {
            x = rand.nextInt(data.length);
        }while (x == 0x0);  
        means_set[0] = new double[]{x, data[(int)x]};

        for (int t = 1; t < k; t++) {
            point2mean_nearst_dist_sum = new double[data.length];
            last_value = 0;
            for (int i = 0; i < data.length; i++) {
                min_distance = Integer.MAX_VALUE;
                for (int u = 0; u < t; u++) {       // find the nearest mean
                    distance = (means_set[u][0]-i)*(means_set[u][0]-i) + 
                                (means_set[u][1]-data[i])*(means_set[u][1]-data[i]);

                    min_distance = Math.min(min_distance, distance);

                }
                point2mean_nearst_dist_sum[i] = min_distance + last_value;
                last_value = point2mean_nearst_dist_sum[i];
                
                
            }
            total = last_value;
            
            double rand_p = Math.random(), last_possibility = 0.0, possibility;
            for (int u = 0; u < point2mean_nearst_dist_sum.length; u++) {
                if (u == last_value) {
                    continue;
                }
                possibility = (float)point2mean_nearst_dist_sum[u] / (float)total;
                if (rand_p >= last_possibility && rand_p < possibility) {
                    x = u;
                    y = data[u];
                    means_set[t] = new double[]{x, y};
                    break;
                }
                last_possibility = possibility;
            }
        }
        
        return means_set;
    }
}
