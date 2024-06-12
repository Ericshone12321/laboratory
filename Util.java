import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Util {
    final static int checkPixelBounds(int value){
		if (value >255) return 255;
		if (value <0) return 0;
		return value;
 	} 
	
	//get red channel from colorspace (4 bytes)
	final static int getR(int rgb){
		  return checkPixelBounds((rgb & 0x00ff0000)>>>16);	
    }

	//get green channel from colorspace (4 bytes)
	final static int getG(int rgb){
	  return checkPixelBounds((rgb & 0x0000ff00)>>>8);
	}
	
	//get blue channel from colorspace (4 bytes)
	final static int getB(int rgb){
		  return  checkPixelBounds(rgb & 0x000000ff);
	}
	
	final static int makeColor(int r, int g, int b){
		return (255<< 24 | r<<16 | g<<8 | b);
	}
	
	final static int covertToGray(int r, int g, int b){
		return checkPixelBounds((int) (0.2126 * r + 0.7152 * g + 0.0722 * b));		
	}

	
	final static int checkImageBounds(int value, int length){
		 if (value>length-1) return length-1;
		 else if (value <0) return 0;
		 else return value;
	}
	
	static BufferedImage makeImg(int[][][] newData){
		int height = newData.length;
		int width =  newData[0].length;
		BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); 
        for (int y=0; y<height; y++) {
        	for (int x=0; x<width; x++) {
        		int rgb = Util.makeColor(newData[y][x][0],
        								 newData[y][x][1], 
        								 newData[y][x][2]);
        		newImg.setRGB(x, y, rgb);
        	}
        	
        }
        return newImg;
	}

	static int[][][] makeRGBData(BufferedImage img) {
		int height = img.getHeight();
		int width = img.getWidth();
		int[][][] data = new int[height][width][3];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int rgb = img.getRGB(x, y);
				data[y][x][0] = Util.getR(rgb);
				data[y][x][1] = Util.getG(rgb);
				data[y][x][2] = Util.getB(rgb);
			}
		}
		return data;
	}

	static BufferedImage loadImg(String filename) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(filename));

		} catch (IOException e) {
			System.out.println("IO exception");
		}
		return img;
	}

	static int[][][] toGray(int[][][] data) {
		int height = data.length;
		int width = data[0].length;
		int[][][] grayData = new int[height][width][3];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grayScale = Util.covertToGray(data[i][j][0], data[i][j][1], data[i][j][2]);
                grayData[i][j][0] = grayScale;
                grayData[i][j][1] = grayScale;
                grayData[i][j][2] = grayScale;
            }
        }
		return grayData;
	}

	//統計0~255灰階RGB之出現次數
	static int[] histogram(int[][][] data) {
		int height = data.length;
		int width = data[0].length;
		int[] histogram = new int [256];
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                histogram[data[j][i][0]] += 1;
            }
        }
		return histogram;
	}	
}