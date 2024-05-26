/**
 * Ostu_Thresholding
 */
public class Ostu_Thresholding {

    static int otsu_threshold( int[] histogram) {
		
		float w1 = 0.0f, w2 = 0.0f;
		float m2 = 0.0f, m1 = 0.0f;
		float max_var = 0.0f, inter_var = 0.0f;
		int threshold = 0;
		float prefix_his[] = new float[256];
		float prefix_weight_his[] = new float[256];

		for (int i = 1; i < 256; ++i ) {
			prefix_his[i] = prefix_his[i-1] + histogram[i];
			prefix_weight_his[i] = prefix_weight_his[i-1] + i*histogram[i];		//µ(L) = ∑ i*pi, i=1 to L-1

		}
		for (int index_histo = 1; index_histo < 256; ++index_histo) {
			w1 = (float)prefix_his[index_histo] / prefix_his[255];		//左邊機率的累加 ω1 (t) = ∑ i* pi, i=1 to t
			w2 = 1.0f - w1;		//右邊機率的累加

			if ( w1 == 0 || w2 == 0 ) {
				continue;

			}
			m1 = prefix_weight_his[index_histo] / w1;		//低於閥值的灰階群µ1 = ∑ i*pi /ω1 (t), i=1 to t
			m2 = (prefix_weight_his[255]-prefix_weight_his[index_histo]) / w2;		//高於閥值的灰階群 µ2 = ∑ i*pi/ω2 (t), i=t+1 to L-1 
			inter_var = w1 * (m1-prefix_weight_his[255]) * (m1-prefix_weight_his[255]) + 
						w2 * (m2-prefix_weight_his[255]) * (m2-prefix_weight_his[255]);

			if ( inter_var >= max_var ) {
				threshold = index_histo;
				max_var = inter_var;

			}
		}

		return threshold;
	}

	static int[] multiLevel_threshold(int[] histogram) {
		float m1 = 0.0f, m2 = 0.0f, m3 = 0.0f;
		float w1 = 0.0f, w2 = 0.0f, w3 = 0.0f;
		float variance = 0.0f, max_var = 0.0f;
		int threshold_1 = 0, threshold_2 = 0;
		float prefix_his[] = new float[256];
		float prefix_weight_his[] = new float[256];

		for (int i = 1; i < 256; ++i ) {
			prefix_his[i] = prefix_his[i-1] + histogram[i];
			prefix_weight_his[i] = prefix_weight_his[i-1] + i*histogram[i];		//µ(L) = ∑ i*pi, i=1 to L-1
			//System.out.printf("index: %d, prefix_his: %d, prefix_w_his: %d, histogram: %d\n", i, prefix_his[i], prefix_weight_his[i], histogram[i]);
		}

		for (int u = 1; u < 256; u++) {
			w1 = prefix_his[u] / prefix_his[255] ;
			m1 = prefix_weight_his[u] / w1;
			if (w1 == 0.0f)
				continue;
			
			for (int v = u+1; v < 256; v++) {
				w2 = (prefix_his[v] - prefix_his[u]) / prefix_his[255];
				w3 = 1.0f - w1 - w2;

				if (w2 == 0 || w3 == 0) 
					continue;
				
				m2 = (prefix_weight_his[v] - prefix_weight_his[u]) / w2;
				m3 = (prefix_weight_his[255] - prefix_weight_his[v]) / w3;
				
				variance = w1 * (m1-prefix_weight_his[255]) * (m1-prefix_weight_his[255]) + 
						   w2 * (m2-prefix_weight_his[255]) * (m2-prefix_weight_his[255]) + 
						   w3 * (m3-prefix_weight_his[255]) * (m3-prefix_weight_his[255]);
						   
				if (variance > max_var) {
					threshold_1 = u;
					threshold_2 = v;
					max_var = variance;
				}
			}
		}
	
		return new int[] {threshold_1, threshold_2};
	}
}