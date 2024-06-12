import java.util.ArrayList;

public class test {
    public static void main(String[] args) {
        ArrayList<double[]> center = new ArrayList<double[]>();
        double[] a1 = {5, 7};
        double[] a2 = {1, 4};
        center.add(a1);
        center.add(a2);
        double[][] values = new double[center.size()][2];
        for(int i = 0; i < center.size(); i++) {
            values[i] = center.get(i);
        }
        System.out.println(values.length);
        
    }
}