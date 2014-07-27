package JVE;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Utils {

    private static ArrayList<String> pathes = new ArrayList<>();

    public static void cleanPathes() {
        pathes.clear();
    }

    public static void addPath(String s) {
        if (!s.endsWith("/"))
            s += "/";
        if (!pathes.contains(s))
            pathes.add(s);
    }

    public static File getFile(String name) throws Exception {
        File f = new File(name);
        if (f.exists())
            return f;
        for (String s : pathes) {
            f = new File(s + name);
            if (f.exists())
                return f;
        }
        throw new Exception("File " + name + " not fount in path directories");
    }

    public static void cleanDirectory(File dir) throws Exception {
        if (dir.exists()) {
        String[] files = dir.list();
        for (String file : files) {
            File f = new File(dir, file);
            if (!f.isDirectory()) {
                if (!f.delete())
                    throw new Exception("Can't delete file: "+f.getName());
            }
        }        }
        else {
            Utils.printMessage("Directory "+dir.getName()+" does not exist, creating...");
            if (!dir.mkdir())
                throw new Exception("Directory "+dir.getName()+" can not be created");
        }
    }

    public static void cleanByMask(String dir, String suffix) throws Exception {
        String[] files = new File(dir).list();
        for (String file : files) {
            File f = new File(dir, file);
            if (f.getName().endsWith(suffix))
                if (!f.delete())
                    throw new Exception("Can't delete file: "+f.getName());
        }
    }

    public static ArrayList<String> readLines(String fileName) throws Exception {
        ArrayList<String> Lines = new ArrayList<>();
        BufferedReader input;
        input = new BufferedReader(new FileReader(getFile(fileName)));
        String line;
        while ((line = input.readLine()) != null) {
            Lines.add(line);
        }
        input.close();
        return Lines;
    }

    public static String[] getArguments(String s) {
        s = s.substring(s.indexOf('{') + 1);
        while (s.endsWith(" "))
            s = s.substring(0, s.length() - 1);
        s = s.substring(0, s.length() - 1);
        ArrayList<String> res = new ArrayList<>();
        res.add("");

        int brackets = 0;
        boolean saver = false;
        for (int i = 0; i < s.length(); i++) {
            if (saver) {
                if (s.charAt(i) == 'n')
                    res.set(res.size() - 1, res.get(res.size() - 1) + "\n");
                else res.set(res.size() - 1, res.get(res.size() - 1) + s.charAt(i));
                saver = false;
            } else if (s.charAt(i) == '\\') {
                saver = true;
            } else if (s.charAt(i) == '(' || s.charAt(i) == '[' || s.charAt(i) == '{') {
                brackets++;
                res.set(res.size() - 1, res.get(res.size() - 1) + s.charAt(i));
            } else if (s.charAt(i) == ')' || s.charAt(i) == ']' || s.charAt(i) == '}') {
                brackets--;
                res.set(res.size() - 1, res.get(res.size() - 1) + s.charAt(i));
            } else if (brackets == 0 && s.charAt(i) == ',')
                res.add("");
            else
                res.set(res.size() - 1, res.get(res.size() - 1) + s.charAt(i));
        }

        for (int i = 0; i < res.size(); i++) {
            res.set(i, res.get(i).trim());
        }

        return res.toArray(new String[res.size()]);
    }

    public static void zip(ArrayList<String> files, String outputArchive) throws Exception {
        ZipOutputStream zipOutputStream;
        zipOutputStream = new ZipOutputStream(new FileOutputStream(outputArchive));
        for (String fileURL : files) {
            File file = Utils.getFile(fileURL);
            ZipEntry zipEntry = new ZipEntry(file.getName());
            try {
                zipOutputStream.putNextEntry(zipEntry);
            }
            catch (ZipException e) {
                Utils.printMessage("File " + file.getName() + " is already exists, ignoring...");
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            for (int c = fileInputStream.read(); c != -1; c = fileInputStream.read())
                zipOutputStream.write(c);
            zipOutputStream.closeEntry();
        }
        zipOutputStream.close();
    }

    public static ArrayList<File> unzip(File zipped, String base) throws Exception {
        ArrayList<File> output = new ArrayList<>();
        ZipInputStream zipInputStream;
        zipInputStream = new ZipInputStream(new FileInputStream(zipped));
        ZipEntry zipEntry;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            String name = zipEntry.getName();
            Path path = Paths.get(base + name);
            Files.createDirectories(path.getParent());
            try {
                Files.createFile(path); }
            catch (FileAlreadyExistsException e) {
                Utils.printMessage("File " + path + " is already exists, ignoring...");
            }
            File file = new File(base + name);
            FileOutputStream fout = new FileOutputStream(file);
            for (int c = zipInputStream.read(); c != -1; c = zipInputStream.read()) {
                fout.write(c);
            }
            output.add(file);
        }
        zipInputStream.close();
        return output;
    }

    public static void printMessage(String message) {
        System.out.println(message);
    }

    public static void printMessage(int message) {
        System.out.write(message);
    }
}
