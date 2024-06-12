import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Otsu_Main {

    public static void main(String[] args) {
        BufferedImage img, newImg;
        String newpath = "";
        String path = "";
        int data[][][], grayData[][][], histogram[];

        img = Util.loadImg(path);
        data = Util.makeRGBData(img);
        int height = img.getHeight();
        int width = img.getWidth();
        grayData = new int[height][width][3];
        grayData = Util.toGray(data);
        histogram = Util.histogram(grayData);
        
        int threshold = Otsu.variance(histogram);
        int[][][] binary_data = Otsu.toBinary(threshold, grayData);
        int[][][] maskData = Otsu.mask(grayData, binary_data);

        threshold = Otsu.variance(Util.histogram(maskData));
        System.out.println(threshold);
        binary_data = Otsu.toBinary(threshold, maskData);

        newImg = Util.makeImg(binary_data);
        try {
            File newFile = new File(newpath + "done.png");              
            ImageIO.write(newImg, "png", newFile);
            //System.out.println("已完成:" + fileName);
        } catch(IOException e) {
            System.out.println("IO exception");
        }                                                         //輸出處理好的圖檔
            
}
}