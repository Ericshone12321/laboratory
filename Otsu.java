package ImageBinarization;
/**
 * Ostu_Thresholding
 */
public class Otsu {

	int[] histogram;
	int[] thresholds;
	int[] counter;

	float[] means;
	float[] weights;
	float prefix_his[]; 
	float prefix_weight_his[];

	int k = 2;
	int max_var = Integer.MIN_VALUE;

	public Otsu(int[] histogram) {
		this.histogram = histogram;
		
	}

	public Otsu(int[] histogram, int k) {
		this.histogram = histogram;
		this.k = k;
	}

	private void prefix_sum_of_his() {
		prefix_his = new float[256];
		prefix_weight_his = new float[256];

		for (int i = 1; i < 256; ++i ) {
			this.prefix_his[i] = prefix_his[i-1] + histogram[i];
			this.prefix_weight_his[i] = prefix_weight_his[i-1] + i*histogram[i];		//µ(L) = ∑ i*pi, i=1 to L-1

		}
		
	}

    public int otsu_threshold() {
		
		float w1 = 0.0f, w2 = 0.0f;
		float m2 = 0.0f, m1 = 0.0f;
		float max_var = 0.0f, inter_var = 0.0f;
		int threshold = 0;

		prefix_sum_of_his();

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

	public int[] multiLevel_threshold() {
		float m1 = 0.0f, m2 = 0.0f, m3 = 0.0f;
		float w1 = 0.0f, w2 = 0.0f, w3 = 0.0f;
		
		int threshold_1 = 0, threshold_2 = 0;

		means = new float[k];
		weights = new float[k];

		float variance = 0.0f; 
		float max_var = 0.0f;

		thresholds = new int[k];
		counter = new int[k-1];

		prefix_sum_of_his();
		
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
	public int[] test_rec() {
		prefix_sum_of_his();
		
		counter = new int[k];
		thresholds = new int[k-1];
		means = new float[k];
		weights = new float[k];

		var(0, 1);
		return thresholds;
	}

	private void var(int i, int m) {		//i = 0~k-1, m = 1~0xff
		if (i == k) {		//分完群了 有k個 m,w
			float var = 0.0f;
			for (int u = 0; u < k; u++) {
				var += weights[u] * (means[u]-prefix_weight_his[0xff]) * (means[u]-prefix_weight_his[0xff]);
			}
			//System.out.printf("%f ", var);
			if (var > max_var) {
				for (int u = 1; u < k; u++) {
					thresholds[u] = counter[u];		//counter have every m
					System.out.printf("%d ", counter[u]);
				}
				System.out.println();
			}
			return;
		}
		if (m > 0xff) {		//某一層for到底了
			return;
		}
		counter[i] = m;
		
		for (; m < 256; m++) {
			if (i == 0) {		//first for
				weights[i] = (prefix_his[m] ) / prefix_his[0xff];
				means[i] = prefix_weight_his[m] / weights[i];
				
	
			}else if (i == k-1) {		//last for
				weights[i] = (prefix_his[0xff]-prefix_his[counter[i-1]]) / prefix_his[0xff];
				means[i] = (prefix_weight_his[0xff]-prefix_weight_his[counter[i-1]]) / weights[i];
				counter[i] = 0xff;
	
			}else {
				weights[i] = (prefix_his[m]-prefix_his[counter[i-1]]) / prefix_his[0xff];
				means[i] = (prefix_weight_his[m]-prefix_weight_his[counter[i-1]]) / weights[i];
			
			}
			if (weights[i] != 0.0f) {
				var(i+1, m+1);	//go to next nested for i是下一層 下一層從u+1開始切割
			}
		}
		return;
	}
}