package JVE;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipIO {


    public static byte[] fileToBytes(File file) {
        byte[] bytes = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            bytes = new byte[(int)(file.length())];
            fis.read(bytes);
            return bytes;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            try {
                if (fis !=null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    public static File bytesToFile(byte[] data, String name) {
        Path path = Paths.get(name);
        try {
            Files.createDirectories(path.getParent());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Files.createFile(path);
        } catch (IOException e) {
            try {
                Files.delete(path);
                Files.createFile(path);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        File output = new File(name);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(output);
            fos.write(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return output;
    }


    public static void zip(ArrayList<File> files, String outputArchive) {
        ZipOutputStream zipOutputStream = null;
        try {
            zipOutputStream = new ZipOutputStream(new FileOutputStream(outputArchive));
            for (File file : files) {
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zipOutputStream.putNextEntry(zipEntry);
                FileInputStream fileInputStream = new FileInputStream(file);
                for (int c = fileInputStream.read(); c!=-1; c = fileInputStream.read())
                    zipOutputStream.write(c);
                zipOutputStream.closeEntry();


                /**  Это просто чтобы сделать всё заметным
                 *
                 *
                 *
                 *
                 *
                 *
                 *
                 *
                 *
                 *
                 *
                 *
                 *
                 *
                 */
                //System.out.println("File " + file.getName() + " was packaged!");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (zipOutputStream!=null)
                    zipOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String toBase64(File fileToBase) {
        return Base64.encode(fileToBytes(fileToBase));
    }

    public static File fromBase64(String base64, String tempName) {
        byte[] decoded = new byte[0];
        try {
            decoded = Base64.decode(base64);
        } catch (Base64DecodingException e) {
            e.printStackTrace();
        }
        return bytesToFile(decoded, tempName);
    }
    public static ArrayList<File> unzip(File zipped, String base) {
        ArrayList<File> output = new ArrayList<File>();
        ZipInputStream zipInputStream = null;
        try {
            zipInputStream = new ZipInputStream(new FileInputStream(zipped));
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                String name = zipEntry.getName();
                Path path = Paths.get(base+name);
                try {
                    Files.createDirectories(path.getParent());
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Files.createFile(path);
                } catch (IOException e) {
                    try {
                        Files.delete(path);
                        Files.createFile(path);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                File file = new File(base+name);
                FileOutputStream fout = new FileOutputStream(file);
                for (int c = zipInputStream.read(); c!=-1; c = zipInputStream.read())
                {
                    fout.write(c);
                }
                /**  Это просто чтобы сделать всё заметным
                 *
                 *
                 *
                 *
                 *
                 *
                 *
                 *
                 *
                 *
                 *
                 *
                 *
                 *
                 */
                //System.out.println("File " + file.getName() + " was unpackaged!");
                output.add(file);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (zipInputStream!=null)
                    zipInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return output;
    }

    private static ArrayList<File> getFiles(File dir) {
        String[] files = dir.list();
        ArrayList<File> fs=new ArrayList<>();
        for (String file : files) {
            fs.add(new File(dir, file));
        }
        return fs;
    }

    public ZipIO() {

        String basePath = "/home/nameless/Desktop/temp2/";
        ArrayList<File> files = getFiles(new File(basePath));
        System.out.println("Files created, size="+files.size());

        zip(files, "/home/nameless/archive.jzf");
        System.out.println("Zipped");

        String base64 = toBase64(new File("/home/nameless/archive.jzf"));
        try {
            Files.write(Paths.get("/home/nameless/base64.txt"), base64.getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Base64 written!");
        File fromBase64 = fromBase64(base64, "/home/nameless/fromBase.jzf");
        System.out.println("File from Base64 have been created");
        unzip(fromBase64, "/home/nameless/archive/");
        System.out.println("File from Base64 was unpackaged");


    }

    public static void main(String[] args) {
        // write your code here
        new ZipIO();
    }
}