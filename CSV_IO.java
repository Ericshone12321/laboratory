package CSV;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class CSV_IO {
    public static int [][][] CSV_Reader(String filePath) {
        String row = null;
        int x = 0, y = 0, width = 768, height = 768;
        int[][][] data = new int[width][height][3];

        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(filePath));
            BufferedReader csv_reader = new BufferedReader(isr);

            try {
                row = csv_reader.readLine();
                while ((row = csv_reader.readLine()) != null) {
                    String row_data[] = row.split(",");
                    x = Integer.parseInt(row_data[0]);
                    y = Integer.parseInt(row_data[1]);
                    
                    for (int c = 0; c < 3; c++) {
                        data[x][y][c] = Integer.parseInt(row_data[c+2]);
                    }
                }
                csv_reader.close();
            }catch (IOException e) {

            }
        }catch (FileNotFoundException e) {
            System.out.println(filePath + "is not found");
        }
        
        return data; 
    } 
    public static void CSV_Writer(int[] histogram, String fileName) {
        String filePath = "D:\\tool\\ImageProcessing\\data_csv\\" + fileName;
        try {
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(filePath));
            BufferedWriter csv_writer = new BufferedWriter(osw);
            try {
                csv_writer.write("grayscale,value");
                csv_writer.newLine();
                
                for (int i = 0; i < 256; i++) {
                    csv_writer.write(Integer.toString(i)+","+Integer.toString(histogram[i]));
                    csv_writer.newLine();
                }
                
                csv_writer.close();

            }catch (IOException e1) {

            }
            
        }catch(FileNotFoundException e) {
            System.out.println(filePath + "is not found");

        }
        return;
    }
}
