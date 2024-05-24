import java.io.File;
import java.awt.image.BufferedImage;

public class Main {
    public static void main(String[] args) {
        String path = "D:\\tool\\preprocessing\\lib";              // input path of file
        String newPath = "D:\\preprocessingImg\\";                        //output path of file
        
        File file = new File(path);
        String[] arr = file.list();
        BufferedImage img, newImg;
        int data[][][], threshold, binaryData[][][], grayScaleData[][][], maskData[][][];
        int[] thresholds;

        for (String fileName: arr) {
            String filePath = path + "\\" + fileName;
            img = Util.loadImg(filePath);
            data = Util.makeRGBData(img);

            grayScaleData = Util.RGB2GrayScale(data);   //every pixel transmit RGB to GrayScale

            threshold = Ostu_Thresholding.otsu_threshold(Util.summation(grayScaleData));  //find threshold through (between-class variance)
            binaryData = Filter.threshold_filter_UP(grayScaleData, threshold);   //if value less than threshold, turn to 0x0, if bigger or equal than threshold, turn to 0xff.
            maskData = Util.threshold_mask(binaryData, grayScaleData);  //masking object pixel and blocking background pixel
            
            /*for (int t1 = 70; t1 < 150; t1++) {
                for (int t2 = 60; t2 < 150; t2++) {
                    binaryData = Filter.threshold_filter_UP_AND_DOWN(maskData, t1, t2);
                    newImg = Util.makeImg(binaryData);
                    Util.outputImg(newImg, newPath, "multiLevel_threshold"+Integer.toString(t1)+"-"+Integer.toString(t2)+fileName);

                }
            }*/
            for (int i = 1; i < 256; i++) {
                binaryData = Filter.threshold_filter_UP(maskData, i);
                newImg = Util.makeImg(binaryData);
                Util.outputImg(newImg, newPath, "UP-"+Integer.toString(i) +"-"+fileName);

                binaryData = Filter.threshold_filter_DOWN(maskData, i);
                newImg = Util.makeImg(binaryData);
                Util.outputImg(newImg, newPath, "DOWN-"+Integer.toString(i) +"-"+fileName);
            }
            thresholds = Ostu_Thresholding.multiLevel_threshold(Util.summation(maskData));
            threshold = Ostu_Thresholding.otsu_threshold(Util.summation(maskData));
            binaryData = Filter.threshold_filter_UP(maskData, threshold);

            newImg = Util.makeImg(binaryData);
            //Util.outputImg(newImg, newPath, "UP"+fileName);
            /*threshold = Ostu_Thresholding.otsu_threshold(Util.summation(maskData));
            binaryData = Filter.threshold_filter_UP(maskData, threshold);

            newImg = Util.makeImg(binaryData);
            Util.outputImg(newImg, newPath, fileName);*/

            System.out.printf("fileName: %s  threshold: %d\n", fileName, threshold);
            System.out.printf("threshold 1: %d  threshold 2: %d\n", thresholds[0], thresholds[1]);
            binaryData = Filter.threshold_filter_UP_AND_DOWN(maskData, thresholds[0], thresholds[1]);
            newImg = Util.makeImg(binaryData);
            //Util.outputImg(newImg, newPath, "multiLevel_threshold"+fileName);
        }
        System.out.println(arr.length);
    }
}