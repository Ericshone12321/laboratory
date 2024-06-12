import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class k_means_Main {
    public static void main(String[] args) {
        String path = "C:\\Users\\eric\\Desktop\\originImg\\";              
        String newPath = "C:\\Users\\eric\\Desktop\\aa\\";
        File file = new File(path);
        String[] arr = file.list();
        int done = 0;
        for(String fileName: arr) {
            BufferedImage img, newImg;
            int data[][][], grayData[][][], histogram[], width, height;
            String filePath = path + "\\" + fileName;
            img = Util.loadImg(filePath);
            data = Util.makeRGBData(img);
            height = img.getHeight();
            width = img.getWidth();
            grayData = new int[height][width][3];
            grayData = Util.toGray(data);
            histogram = Util.histogram(grayData);
            int[][] means = K_means.k_means(histogram, 4);
            int[][][] cluster = K_means.cluster(means, grayData);

            newImg = Util.makeImg(cluster);
            try {
                File newFile = new File(newPath + fileName);              
                ImageIO.write(newImg, "png", newFile);
                System.out.println("已完成:" + fileName);
                done += 1;
            } catch(IOException e) {
                System.out.println("IO exception");
            }                                                         //輸出處理好的圖檔
        }
        System.out.println("共完成:" + done + "項");
    }
}
