package JVE.Parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class ParseUtils {

    private static ArrayList<String> pathes=new ArrayList<>();

    public static void cleanPathes() {
        pathes.clear();
    }

    public static void addPath(String s) {
        if (!s.endsWith("/"))
            s+="/";
        pathes.add(s);
    }

    public static File getFile(String name) throws Exception {
        File f=new File(name);
        if (f.exists())
            return f;
        for (String s: pathes) {
            f=new File(s+name);
            if (f.exists())
                return f;
        }
        exit("File "+name+" not fount in path directories");
        return null;
    }

    public static ArrayList<File> getFiles(File dir) {
        String[] files = dir.list();
        ArrayList<File> fs=new ArrayList<>();
        for (String file : files) {
            fs.add(new File(dir, file));
        }
        return fs;
    }

    public static ArrayList<String> readLines(String fileName) throws Exception {
        ArrayList<String> Lines = new ArrayList<>();

        BufferedReader input = null;
        try {
            input = new BufferedReader(new FileReader(getFile(fileName)));
            String line;
            while ((line = input.readLine()) != null) {
                Lines.add(line);
            }
        } catch (Exception e) {
            exit("Failed to load file " + fileName);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (Exception e) {
                exit("Failed to load file " + fileName);
            }
        }
        return Lines;
    }

    static String[] getArguments(String s) {
        s = s.substring(s.indexOf('{') + 1);
        while (s.endsWith(" "))
        s = s.substring(0, s.length() - 1);
        s = s.substring(0, s.length() - 1);
        ArrayList<String> res=new ArrayList<>();
        res.add("");

        int brackets=0;
        boolean saver=false;
        for (int i = 0; i < s.length(); i++) {
            if (saver) {
                if (s.charAt(i)=='n')
                    res.set(res.size()-1, res.get(res.size()-1)+"\n");
                else res.set(res.size()-1, res.get(res.size()-1)+s.charAt(i));
                saver=false;
            }
            else if (s.charAt(i)=='\\') {
                saver=true;
            }
            else
            if (s.charAt(i)=='(' || s.charAt(i)=='[' || s.charAt(i)=='{') {
                brackets++;
                res.set(res.size()-1, res.get(res.size()-1)+s.charAt(i));
            }
            else if (s.charAt(i)==')' || s.charAt(i)==']' || s.charAt(i)=='}') {
                brackets--;
                res.set(res.size()-1, res.get(res.size()-1)+s.charAt(i));
            }
            else if (brackets==0 && s.charAt(i)==',')
                res.add("");
            else
                res.set(res.size()-1, res.get(res.size()-1)+s.charAt(i));
        }

        for (int i=0; i<res.size(); i++) {
            res.set(i, res.get(i).trim());
        }

        return res.toArray(new String[res.size()]);
    }

    public static void exit(String message) throws Exception {
        System.err.println("Error: " + message);
        throw new Exception(message);
    }

    public static void printMessage(String message) {
        System.out.println(message);
    }

    public static void printMessage(int message) {
        System.out.write(message);
    }
}
