package utils;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipException;

public class FileUtil {
    public static void deleteAllUncompressedFiles(String fileSource) {
        for (File file : getListOfFiles(fileSource)) {
            if (!file.getName().endsWith(".gz")) {
                try {
                    Files.delete(file.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void uncompressGZipFile(String fileSource, String fileDestination) {
        try {
            FileInputStream fis = new FileInputStream(fileSource);
            GZIPInputStream gis = new GZIPInputStream(fis);
            FileOutputStream fos = new FileOutputStream(fileDestination);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            gis.close();
            fis.close();
        } catch (IOException e) {
            System.out.println("Something went wrong");
        }
    }

    public static void uncompressAllGZipFiles(String fileSource, String fileDestination) {
        //fileDestination += File.separator;
        //for (File file : getListOfFiles(fileSource)) {
        //    uncompressGZipFile(file.getAbsolutePath(), fileDestination + file.getName().substring(0, file.getName().length() - 3));
        //}
        //fileDestination += File.separator;
        for (File file : getListOfFiles(fileSource)) {
            uncompressGZipFile(file.getAbsolutePath(), fileDestination + file.getName().substring(0, file.getName().length() - 3));
            //System.out.println(file.getName());
            //uncompressGZipFile(file.getAbsolutePath(), fileDestination + file.getName().substring(0, file.getName().length() - 3));
        }
        //System.out.println(fileDestination);
    }

    public static  void compressGZipFile(String fileSource, String fileDestination) {
        try (FileInputStream fis = new FileInputStream(fileSource);
            FileOutputStream fos = new FileOutputStream(fileDestination);
            GZIPOutputStream gos = new GZIPOutputStream(fos)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                gos.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void compressAllFiles(String fileSource, String fileDestination) {
        fileDestination += File.separator;
        for (File file : getListOfFiles(fileSource)) {
            compressGZipFile(file.getAbsolutePath(), fileDestination + file.getName() + ".gz");
            try {
                Files.delete(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<File> getListOfFiles(String root) {
        List<File> files = new ArrayList<>();
        File rootDirectory = new File(root);
        File[] contents = rootDirectory.listFiles();
        for (File c : contents) {
                if (c.isDirectory()) {
                    files.addAll(getListOfFiles(c.getAbsolutePath()));
                } else {
                    files.add(c);
                }
        }
        return files;
    }

    public static void archiveFiles(String folder, List<File> files) {
        try {
            Path destinationPath = Path.of(folder);
            if (!Files.exists(destinationPath)) {
                Files.createDirectory(destinationPath);
            }
            for (File f : files) {
                Files.move(f.toPath(), destinationPath.resolve(f.getName()));
            }
        } catch (IOException e) {
                e.printStackTrace();
        }
    }

    public static boolean fileExistsInDb(String filename) {
        return fileExistsInList(filename);
    }

    public static boolean fileExistsInList(String filename) {
        return  false;
    }

    public static void moveFiles(String source, String target) {
        //ArchiveFilesPath
        Path destination = Path.of(target);
        List<File> files = getListOfFiles(source);
        for (File c : files) {
            if (c.isFile()) {
                try {
                    Files.move(c.toPath(), Path.of(destination + File.separator + c.getName()), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
