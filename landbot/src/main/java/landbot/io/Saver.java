package landbot.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Saver {

    public static void saveAppend (String path , String save)
    {
        try {
            File f = new File(path);
            Scanner s = new Scanner(f);
            String old = "";

            while (s.hasNextLine())
                old += s.nextLine() + "\n";

            FileWriter fw = new FileWriter(path);
            fw.write(old);
            fw.append(save);

            fw.close();
            s.close();

        } catch (IOException e) {
            System.err.println("an erorr occured while appending to " + path);
            e.printStackTrace();
        }
    }

    public static void saveOverwite (String path , String[] save)
    {
        try {
            FileWriter fw = new FileWriter(path);
            fw.write("");
            
            for (String string : save) 
                fw.append(string + "\n");

            fw.close();

        } catch (IOException e) 
        {
            System.err.println("an error occutred while saving to " + path);
            e.printStackTrace();
        }
    }
    

    /**
     * 
     * @param path
     * @return true if a new file is created
     */
    public static boolean saveNewFile (String path)
    {
        try {
            File file = new File(path);
            if (file.createNewFile())
                return true;
            else
                return false;
        } catch (IOException e) {
           System.err.println("an error has ooccured");
           e.printStackTrace();
        }
        return false;
    }

    public static void deleteFiles(String path) 
    {
        File f = new File(path);
        for (File f2 : f.listFiles()) 
        {
            if (!f2.delete())
                deleteFiles(f2.getPath());
        }
        f.delete();
    }

    public static void copyFrom(String path1, String path2) 
    {
        String[] out = FileReader.read(path1);
        saveOverwite(path2, out);
	}

    public static boolean newFolder(String path) 
    {
        File dir = new File(path);
        if (!dir.exists())
        {
            dir.mkdir();
            return true;
        }
        return false;
	}

    public static void saveNewFile(String path, String[] save) 
    {
        saveNewFile(path);
        saveOverwite(path, save);
	}
}
