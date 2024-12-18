/**
 * Name: Parser
 * Author: Felix Neutal
 * Description: Abstract class for common parsing methods
 */
package parser;

import DTO.Cdr;
import org.slf4j.Logger;
import schemas.AcmeSchema;
import schemas.Schema;
import utils.FileUtil;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public abstract class Parser <T extends Cdr> {
    protected int totalLines = 0;
    protected Properties properties;
    protected Method[] methods;
    protected Cdr cdrType;
    protected List<String> currentFileNames = new ArrayList<>();
    protected Logger logger;

    public Parser(Properties properties, T c) {
        this.properties = properties;
        this.cdrType = c;
        methods = c.getClass().getDeclaredMethods();
    }

    public Parser() {
        methods = cdrType.getClass().getDeclaredMethods();
    }

    protected void setLogger(Logger logger) {
        this.logger = logger;
    }

    protected abstract List<T> parseFile(String filepath);

    public List<T> parseAllFilesRecursively(String root) {
        List<T> cdrFiles = new ArrayList<>();
        File rootDirectory = new File(root);
        File[] contents = rootDirectory.listFiles();
        for (File c : contents) {
            if (c.isDirectory()) {
                cdrFiles.addAll(parseAllFilesRecursively(c.getAbsolutePath()));
            } else {
                cdrFiles.addAll(parseFile(c.getAbsolutePath()));
            }
        }
        return cdrFiles;
    }

    protected int getNumberOfLines(String root) {
        int numberOfLines = 0;
        File rootDirectory = new File(root);
        File[] contents = rootDirectory.listFiles();
        for (File c : contents) {
            if (c.isDirectory()) {
                numberOfLines += getNumberOfLines(c.getAbsolutePath());
            } else {
                numberOfLines += getLines(c.getAbsolutePath());
            }
        }
        return numberOfLines;
    }

    protected int getLines(String filepath) {
        int startOffset = 9;
        int endOffset = 19 ;
        int index = 1;
        int lineCount = 0;
        Cdr cdr;
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = reader.readLine()) != null && !line.stripLeading().startsWith("CLOSE")) {
                lineCount++;
                if (index >= startOffset && !line.equals("")) {
                    lineCount++;
                }
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lineCount;
    }

    public List<T> parseAllFiles() {
        List<T> cdrFiles = new ArrayList<>();
        List<File> files = FileUtil.getListOfFiles(properties.getProperty("FilesPath"));
        files = filesExistInDb(files);
        for (File c : files) {
            if (c.isFile() && !c.getName().endsWith(".gz")) {
                cdrFiles.addAll(parseFile(c.getAbsolutePath()));
            }
        }
        writeParsedFilesToDb(files);
        logger.info(files.size() + " files and " + totalLines + " lines parsed");
        //FileUtil.archiveFiles(properties.getProperty("ArchiveFilesPath"), files);
        return cdrFiles;
    }

    public List<T> forceParseAllFiles() {
        List<T> cdrFiles = new ArrayList<>();
        List<File> files = FileUtil.getListOfFiles(properties.getProperty("FilesPath"));
        for (File c : files) {
            if (c.isFile() && !c.getName().endsWith(".gz")) {
                cdrFiles.addAll(parseFile(c.getAbsolutePath()));
            }
        }
        //writeParsedFilesToDb(files);
        logger.info(files.size() + " files and " + totalLines + " lines parsed");
        //FileUtil.archiveFiles(properties.getProperty("ArchiveFilesPath"), files);
        return cdrFiles;
    }

    protected abstract T parseLine(String line, T cdr);

    protected String parseCleanPhoneNumber() {
        String realNumber = "";


        return realNumber;
    }

    protected Schema getSchema() {
        String path = properties.getProperty("Schema");
        Schema schema = null;
        schema = new AcmeSchema(path);
        return schema;
    }

    protected boolean containsMethod(String methodName) {
        for (Method m : methods) {
            if (m.getName().equals(methodName)) {
                return true;
            }
        }
        return false;
    }

    protected Method getMethod(String methodName) {
        for (Method m : methods) {
            if (m.getName().equals(methodName)) {
                return m;
            }
        }
        return null;
    }

    public List<File> filesExistInDb(List<File> files) {
        List<String> existingStrings = new ArrayList<>();
        List<File> modifiedFiles = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
        }
        try (Connection connection = DriverManager.getConnection("****", "****", "****")) {
            String sql = "SELECT file_name FROM PARSED_FILES WHERE file_name IN (" +
                    files.stream().map(f -> "?").collect(Collectors.joining(",")) + ")";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (int i = 0; i < files.size(); i++) {
                    statement.setString(i + 1, files.get(i).getName());
                }
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String existingString = resultSet.getString("file_name");
                        existingStrings.add(existingString);
                    }
                }
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }

            //files.removeAll(existingStrings);
            //files.removeIf(file -> existingStrings.contains(file.getName()));
            modifiedFiles = files.stream().filter(file -> !existingStrings.contains(file.getName()) && !file.getName().endsWith(".gz")).collect(Collectors.toList());
        } catch (SQLException e) {
            System.out.println("filesExistInDb");
            logger.error(e.getMessage());
        }
        return modifiedFiles;
    }

    public void writeParsedFilesToDb(List<File> files) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
        }
        try (Connection connection = DriverManager.getConnection("****", "****", "****")) {
            String insertSql = "INSERT INTO PARSED_FILES (file_name) VALUES (?)";
            try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
                for (File f : files) {
                    statement.setString(1, f.getName());
                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println("writeParsedFilesToDb");
            logger.error(e.getMessage());
        }
        //System.out.println("Files Written");
    }

    public void deleteFilesFromDb(List<File> files) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
        }
        try (Connection connection = DriverManager.getConnection("****", "****", "****")) {
            String insertSql = "DELETE FROM PARSED_FILES WHERE file_name = (?)";
            try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
                for (File f : files) {
                    statement.setString(1, f.getName());
                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
}
