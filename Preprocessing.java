import java.io.File;

public class Preprocessing {
    public static void main(String[] args) {
        String path = "D:\\tool\\preprocessing\\lib";              // input path of file
        
        //Concatenation.concatenate(path, newPath);
        String[] sets = new String[] {"test_set", "train_set"};
        String[] pics = new String[] {"mask", "pic"};
        String newFolderPath;

        File newFolder;

        for (int i = 1; i <= 7; i++) {
            newFolderPath = "D:\\n" + //
                                "ew7fold\\n" + //
                                "ew7fold\\" + "7f"+Integer.toString(i);
            newFolder = new File(newFolderPath);
            newFolder.mkdir();
            for (int j = 0; j < 2; j++) {
                newFolderPath = "D:\\n" + //
                                "ew7fold\\n" + //
                                "ew7fold\\" + "7f"+Integer.toString(i) + "\\"+sets[j];
                newFolder = new File(newFolderPath);
                newFolder.mkdir();
                for (int k = 0; k < 2; k++) {
                    path = "C:\\Users\\sean\\OneDrive\\文件\\new7fold\\new7fold\\"+"7f"+Integer.toString(i) + "\\"+sets[j]+"\\"+pics[k];
                    newFolderPath = "D:\\n" + //
                                    "ew7fold\\n" + //
                                    "ew7fold\\" + "7f"+Integer.toString(i) + "\\"+sets[j]+"\\"+pics[k];
                    
                    newFolder = new File(newFolderPath);
                    newFolder.mkdir();
                    Concatenation.concatenate(path, newFolderPath+"\\");
                    

                }
            }
            
        }
        
        

    }
}
