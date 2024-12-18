package sftp;

import com.jcraft.jsch.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class Sftp {
    private final Logger logger;
    private int REMOTE_PORT;
    private int SESSION_TIMEOUT;
    private int CHANNEL_TIMEOUT;
    private String username;
    private String password;
    private String hostname;
    private JSch jSch;
    private Session jschSession;
    private Channel channel;
    private ChannelSftp channelSftp;
    private Properties properties;
    private String sourceFilepath;
    private String destinationFilepath;
    private String knownHostsPath;
    private String identity;
    private String regex;
    public Sftp(Properties properties) {
        logger = LoggerFactory.getLogger("sftp");
        this.properties = properties;
        username = properties.getProperty("username");
        //password = properties.getProperty("password");
        hostname = properties.getProperty("hostname");
        sourceFilepath = properties.getProperty("source_path");
        destinationFilepath = properties.getProperty("destination_path");
        knownHostsPath = properties.getProperty("known_hosts");
        REMOTE_PORT = Integer.valueOf(properties.getProperty("remote_port"));
        SESSION_TIMEOUT = Integer.valueOf(properties.getProperty("session_timeout"));
        CHANNEL_TIMEOUT = Integer.valueOf(properties.getProperty("channel_timeout"));
        identity = properties.getProperty("identity");
        regex = properties.getProperty("regex");
    }

    public void connect() {
        jSch = new JSch();
        //System.out.println("Known hosts file is: " + knownHostsPath);
        try {
            jSch.addIdentity(identity);
            jSch.setKnownHosts(knownHostsPath);
            Properties config = new Properties();
            config.put("PubkeyAcceptedKeyTypes", "ssh-rsa");
            config.put("HostKeyAlgorithms", "ssh-rsa");
            config.put("PreferredAuthentications", "publickey");
            jschSession = jSch.getSession(username, hostname, REMOTE_PORT);
            jschSession.setConfig(config);
            jschSession.setPassword(password);
            jschSession.connect(SESSION_TIMEOUT);
            channel = jschSession.openChannel("sftp");
            channel.connect(CHANNEL_TIMEOUT);
            channelSftp = (ChannelSftp) channel;
            logger.info("Connected to " + hostname);
        } catch (JSchException e) {
            logger.error(e.toString());
        }
    }

    public void closeConnection() {
        jschSession.disconnect();
        channel.disconnect();
        channelSftp.disconnect();
        logger.info("Connection to " + hostname + " closed");
    }

    public void retrieveSingleFile(String filename) {
        try {
            channelSftp.get(sourceFilepath + filename, destinationFilepath + filename);
            channelSftp.exit();
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }

    public void retrieveAllFiles(List<String> filenames) {
        int retrievedFilesCount = 0;
        try {
            if (!Files.exists(Path.of(destinationFilepath))) {
                Files.createDirectory(Path.of(destinationFilepath));
            }
            for (String f : filenames) {
                //if (!fileAlreadyTransfered(f)) {
                    //System.out.println(sourceFilepath + File.separator + f + " ## " + destinationFilepath + File.separator + f);
                    channelSftp.get(sourceFilepath + f, destinationFilepath + f);
                    //setFileTransferredInDb(f);
                    retrievedFilesCount++;
                //}
            }
            channelSftp.exit();
        } catch (IOException | SftpException e) {
            logger.error(e.getMessage());
        }
        logger.info(retrievedFilesCount + " files retrieved");
        deleteRemoteFiles(filenames);
        //System.out.println(retrievedFilesCount + " files retrieved+");
    }

    public List<String> getListOfFiles() {
        List<String> files = new ArrayList<>();
        try {
            ChannelExec execChannel = (ChannelExec) jschSession.openChannel("exec");
            execChannel.setCommand("ls " + sourceFilepath);
            execChannel.connect();
            InputStream input = execChannel.getInputStream();
            files = IOUtils.readLines(input, "UTF-8");
            execChannel.disconnect();
        } catch (JSchException | IOException e) {
            logger.error(e.getMessage());
        }
        return files;
    }

    private boolean fileAlreadyTransfered(String fileName) {
        try (Connection conn = DriverManager.getConnection("****", "****", "****")) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM SFPT_FILES WHERE FILE_NAME=?")) {
                stmt.setString(1, fileName);
                ResultSet rs = stmt.executeQuery();
                return rs.next();
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return false;
    }

    private void setFileTransferredInDb(String fileName) {
        try (Connection conn = DriverManager.getConnection("****", "****", "****")) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO SFPT_FILES (FILE_NAME) VALUES (?)")) {
                stmt.setString(1, fileName);
                stmt.executeUpdate();
                conn.commit();
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public void deleteRemoteFiles(List<String> files) {
        try {
            //System.out.println("Deleting files");
            files = files.stream().map(f -> sourceFilepath + f).collect(Collectors.toList());
            //System.out.println(files.get(1));
            ChannelExec execChannel = (ChannelExec) jschSession.openChannel("exec");
            //execChannel.setCommand("rm " + sourceFilepath);
            execChannel.setCommand("rm " + String.join(" ", files));
            execChannel.connect();
            while (!execChannel.isClosed()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }
            }
            int exitStatus = execChannel.getExitStatus();
            if (exitStatus != 0) {
                logger.error("Error deleting files. Exit status: " + exitStatus);
            }
            execChannel.disconnect();
        } catch (JSchException e) {
            logger.error(e.getMessage());
        }
    }

    public void deleteRemoteFile(String file) {
        String filePath = "felixiTest/";
        try {
            ChannelExec execChannel = (ChannelExec) jschSession.openChannel("exec");
            execChannel.setCommand("rm " + filePath + file);
            execChannel.connect();
            execChannel.disconnect();
        } catch (JSchException e) {
            //logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
