package JVE;

import JVE.Parsers.ParseUtils;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipIO {


    public static void zip(ArrayList<String> files, String outputArchive) throws Exception {
        ZipOutputStream zipOutputStream;
        zipOutputStream = new ZipOutputStream(new FileOutputStream(outputArchive));
        for (String fileURL : files) {
            File file = ParseUtils.getFile(fileURL);
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zipOutputStream.putNextEntry(zipEntry);
            FileInputStream fileInputStream = new FileInputStream(file);
            for (int c = fileInputStream.read(); c != -1; c = fileInputStream.read())
                zipOutputStream.write(c);
            zipOutputStream.closeEntry();
        }
        zipOutputStream.close();
    }

    public static ArrayList<File> unzip(File zipped, String base) throws Exception {
        ArrayList<File> output = new ArrayList<File>();
        ZipInputStream zipInputStream;
        zipInputStream = new ZipInputStream(new FileInputStream(zipped));
        ZipEntry zipEntry;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            String name = zipEntry.getName();
            Path path = Paths.get(base + name);
            Files.createDirectories(path.getParent());
            Files.createFile(path);
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

}