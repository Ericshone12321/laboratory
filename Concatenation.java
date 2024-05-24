import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Concatenation
 */
public class Concatenation {

    static void concatenate(String folderPath, String targetFolderPath) {
        File file = new File(folderPath);
        String[] arr = file.list();
        BufferedImage img, newImg;
        int  data[][][], newData[][][];
                    
        for (int t = 0; t < arr.length; t += 9) {
            newData = new int[256*3][256*3][3];

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {

                    String filePath = folderPath + "\\" + arr[t + i*3 + j];
                    
                    img = Util.loadImg(filePath);
                    data = Util.makeRGBData(img);

                    for (int x = 0; x < 256; x++) {
                        for (int y = 0; y < 256; y++) {
                            for (int c = 0; c < 3; c++) {
                                newData[x + i*256][y + j*256][c] = data[x][y][c];

                            }
                        }
                    }
                }
            }
            newImg = Util.makeImg(newData);
            String subName[] = arr[t].split("_");
            String fileName = subName[0] + "_" + subName[1] + ".png";
            System.out.println(targetFolderPath+fileName);

            Util.outputImg(newImg, targetFolderPath, fileName);
        }
    }
}