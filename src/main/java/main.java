import DAO.OsvCdrDao;
import DTO.*;
import DAO.AcmeCdrDao;
import accounting.Accounting;
import accounting.MakeInvoices;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import parser.AcmeParser;
import parser.OsvParser;
import parser.Parser;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import sftp.Sftp;

import static utils.Utils.getParserProperties;


public class main {
    //private static String ACTIVE_PROFILE = "development";
    private static String ACTIVE_PROFILE = "development"; // development, production
    private static Properties osvSftpProperties = getParserProperties("osv_sftp-" + ACTIVE_PROFILE + ".properties");
    private static Properties acmeSftpProperties = getParserProperties("acme_sftp-" + ACTIVE_PROFILE + ".properties");
    private static Properties osvParserProperties = getParserProperties("osvParser-" + ACTIVE_PROFILE + ".properties");
    private static Properties acmeParserProperties = getParserProperties("acmeParser-" + ACTIVE_PROFILE + ".properties");

    public static void main(String[] args) {
        //2023-01-10
        //yyyy-mm-dd
        //select * from osv_cdr where (record_id between '2023-01-10%' and '2023-03-11%');
        //select count(*) from osv_cdr where (start_time between DATE'2023-10-01' and DATE'2023-11-01') and record_id like '2023-02-10%' order by start_time desc;
        //        if (args.length > 0) {
//            if (args[0].equals("test")) {
//                System.out.println("Parser works!!!");
//            } else if (args[0].equals("osv")) {
//                runOsvParser(false);
//            } else if (args[0].equals("acme")) {
//                runSftp(acmeSftpProperties);
//                FileUtil.uncompressAllGZipFiles(acmeParserProperties.getProperty("FilesPath"), acmeParserProperties.getProperty("FilesPath"));
//                runAcmeParser(false);
//            } else if (args[0].equals("osvr")) {
//                runOsvParser(true);
//            } else if (args[0].equals("acmer")) {
//                runAcmeParser(true);
//            } else if (args[0].equals("sftp")) {
//                runSftp(osvSftpProperties);
//                runSftp(acmeSftpProperties);
//            } else if (args[0].equals("runall")) {
//                runSftp(osvSftpProperties);
//                runSftp(acmeSftpProperties);
//                FileUtil.uncompressAllGZipFiles(acmeParserProperties.getProperty("FilesPath"), acmeParserProperties.getProperty("FilesPath"));
//                //FileUtil.moveFiles(acmeParserProperties.getProperty("FilesPath"), acmeParserProperties.getProperty("ArchiveFilesPath")); //Make changes in script so gz files are not read
//                runAcmeParser(false);
//                FileUtil.deleteAllUncompressedFiles(acmeParserProperties.getProperty("FilesPath"));
//                FileUtil.moveFiles(acmeParserProperties.getProperty("FilesPath"), acmeParserProperties.getProperty("ArchiveFilesPath")); //Make changes in script so non gz files are not read
//                runOsvParser(false);
//                FileUtil.compressAllFiles(osvParserProperties.getProperty("FilesPath"), osvParserProperties.getProperty("ArchiveFilesPath"));
//        }
//        }
        //Accounting acc = new Accounting();
        //acc.createEnrichedTable();
        MakeInvoices makeInvoices = new MakeInvoices();

        Accounting acc = new Accounting();
        acc.createEnrichedTable();
        //makeInvoices.makeInvoices();
        //RedirectedCallRepair repair = new RedirectedCallRepair();
        //compareCallBillAll();
        //repair.startRepair();
    }

    private static void enrichData() {

    }

    private static void repairTable() {
        List<AcmeCdr> parsedFiles;
        List<AcmeCdr> dbFiles;
        AcmeCdrDao dao = new AcmeCdrDao();
        AcmeParser parser = new AcmeParser(acmeParserProperties, new AcmeCdr());

        parsedFiles = parser.forceParseAllFiles();
        dbFiles = dao.getAllRows();

        // Use a Map to quickly find parsedFiles by the start and release time
        Map<String, AcmeCdr> parsedFilesMap = new HashMap<>();
        for (AcmeCdr cdr : parsedFiles) {
            String key = cdr.getStart_time() + "-" + cdr.getRelease_time();
            parsedFilesMap.put(key, cdr);
        }

        for (AcmeCdr cdr : dbFiles) {
            String key = cdr.getStart_time() + "-" + cdr.getRelease_time();
            AcmeCdr acdr = parsedFilesMap.get(key);
            if (acdr != null) {
                //System.out.println(cdr.getCalled_num_out());
                //cdr.setCalled_num_out(acdr.getCalled_num_out());
                //cdr.setCaller_num_out(acdr.getCaller_num_out());
                //System.out.println(cdr.getCalled_num_out());
                // Ensure the primary key (ID) is set correctly if needed
                cdr.setId(acdr.getId()); // Assuming getId() returns the primary key
            }
        }

        dao.updateBatchData(dbFiles); // Use dbFiles, which now has updated data
    }

    private static void runSftp(Properties properties) {
            Sftp sftp = new Sftp(properties);
            sftp.connect();
            List<String> files = sftp.getListOfFiles();
            sftp.retrieveAllFiles(files);
            sftp.closeConnection();
    }

    private static void runOsvParser(boolean recursively) {
            List<OsvCdr> files;
            Parser parser = new OsvParser(osvParserProperties, new OsvCdr());
            if (!recursively) {
                files = parser.parseAllFiles();
            } else {
                files = parser.parseAllFilesRecursively(osvParserProperties.getProperty("FilesPath"));
            }
            System.out.println(files.size() + " rows inserted");

            OsvCdrDao dao = new OsvCdrDao();
            dao.saveBatchData(files);
    }

    private static void runAcmeParser(boolean recursively) {
            List<AcmeCdr> files;
            AcmeParser parser = new AcmeParser(acmeParserProperties, new AcmeCdr());
            if (!recursively) {
                files = parser.parseAllFiles();
            } else {
                // Parser instance already has property file inside
                files = parser.parseAllFilesRecursively(acmeParserProperties.getProperty("FilesPath"));
            }
            AcmeCdrDao dao = new AcmeCdrDao();
            dao.saveBatchData(files);
    }

    private static void testRecordIds() {
        List<Cdr> files;
        List<String> oldRecordIds = new ArrayList<>();
        List<String> newRecordIds = new ArrayList<>();
        String line;
        Parser parser = new OsvParser(osvParserProperties, new OsvCdr());
        files = parser.parseAllFiles();
        newRecordIds = files.stream()
                .map(cdr -> (OsvCdr)cdr)
                .map(osvCdr -> osvCdr.getRecord_id())
                .collect(Collectors.toList());
        //##################################################################################################################################################################

        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\felix\\Desktop\\Git\\CdrParser\\src\\main\\resources\\osv_record_id.txt"))) {
            while ((line = reader.readLine()) != null) {
                oldRecordIds.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> commonElements = new ArrayList<>(oldRecordIds);
        commonElements.retainAll(newRecordIds);

        oldRecordIds.removeAll(commonElements);
        newRecordIds.removeAll(commonElements);
        System.out.println("oldrecordid size: " + oldRecordIds.size());
        System.out.println("newrecordid size: " + newRecordIds.size());
        for (String s : oldRecordIds) {
            System.out.println(s);
        }
    }

    private static void compareCallBillAll() {
        List<CallBillAll> oracleFiles = new ArrayList<>();
        List<CallBillAll> postgresFiles = new ArrayList<>();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@db0:1521:orcl", "ewsd", "vibalik")) {
                try (Statement stmt = conn.createStatement()) {
                    ResultSet rs = stmt.executeQuery("select a_number, call_type, price, duration, call_count from call_bill_all where start_date=date'2024-08-01'");
                    while (rs.next()) {
                        CallBillAll cba = new CallBillAll();
                        cba.setA_number(rs.getString(1));
                        cba.setCall_type(rs.getString(2));
                        cba.setPrice(rs.getDouble(3));
                        cba.setDuration(rs.getInt(4));
                        cba.setCall_count(rs.getInt(5));
                        oracleFiles.add(cba);
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Oracle size is: " + oracleFiles.size());
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection("jdbc:postgresql://10.3.3.41:5432/billdb", "felix", "KakaKun1ngas77!")) {
                try (Statement stmt = conn.createStatement()) {
                    ResultSet rs = stmt.executeQuery("select ec.a_number, ct.call_type_id as call_type, sum(ec.price) as price, sum(ec.duration) as duration, count(*) as call_count from enriched_cdr ec left join call_types ct on ct.product_code = ec.product_code where in_sip_peer in (select ip from trunks) and a_number in (select a_number from phone) and duration > 0 group by a_number, call_type_id order by a_number");
                    while (rs.next()) {
                        CallBillAll cba = new CallBillAll();
                        cba.setA_number(rs.getString(1));
                        cba.setCall_type(rs.getString(2));
                        cba.setPrice(rs.getDouble(3));
                        cba.setDuration(rs.getInt(4));
                        cba.setCall_count(rs.getInt(5));
                        postgresFiles.add(cba);
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Postgres size is: " + postgresFiles.size());
        boolean isFound = false;
        for (CallBillAll oracle : oracleFiles) {
            isFound = false;
            for (CallBillAll postgres : postgresFiles) {
                if (oracle.getA_number().equals(postgres.getA_number()) && oracle.getCall_type().equals(postgres.getCall_type())) {
                    isFound = true;
                    oracle.setCall_count(oracle.getCall_count() - postgres.getCall_count());
                    oracle.setDuration(oracle.getDuration() - postgres.getDuration());
                    oracle.setPrice(oracle.getPrice() - postgres.getPrice());
                    System.out.println(oracle);
                }
            }

        }
//        System.out.println("===============================================================================");
//        for (CallBillAll postgres : postgresFiles) {
//            isFound = false;
//            for (CallBillAll oracle : oracleFiles) {
//                if (oracle.getA_number().equals(postgres.getA_number())) {
//                    isFound = true;
//                }
//            }
//            if (!isFound) {
//                System.out.println(postgres + " not found in oracle");
//            }
//        }
    }

    private static void importPhone() {
        List<Phone> phoneNumbers = new ArrayList<>();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@db0:1521:orcl", "ewsd", "vibalik")) {
                try (Statement stmt = conn.createStatement()) {
                    ResultSet rs = stmt.executeQuery("select * from phone");
                    while (rs.next()) {
                        Phone phoneNumber = new Phone();
                        phoneNumber.setA_number(rs.getString(1));
                        phoneNumber.setAa_number(rs.getString(2));
                        phoneNumber.setIn_trunk(rs.getString(3));
                        phoneNumber.setCustomer_id(rs.getLong(4));
                        phoneNumber.setPhone_type(rs.getString(5));
                        phoneNumber.setOwner_info(rs.getString(6));
                        phoneNumber.setCategory(rs.getString(7));
                        phoneNumber.setPen(rs.getString(8));
                        phoneNumber.setB_date(rs.getDate(9));
                        phoneNumber.setB_basis(rs.getString(10));
                        phoneNumber.setPrivate(rs.getString(11));
                        phoneNumber.setEristus(rs.getString(12));
                        phoneNumber.setClosed(rs.getDate(13));
                        phoneNumber.setNotes(rs.getString(14));
                        phoneNumber.setC_date(rs.getDate(15));
                        phoneNumber.setAddress(rs.getString(16));
                        phoneNumber.setGroups_id(rs.getLong(17));
                        phoneNumbers.add(phoneNumber);
                    }
                } catch (SQLException e) {
                    System.out.println("i have an error!!!");
                    e.printStackTrace();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }

        SessionFactory sessionFactory = null;
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().build();
        try {
            sessionFactory = new MetadataSources(registry).
                    addAnnotatedClass(Phone.class)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            System.out.println("\n\nERRORRRRR!!!!\n\n");
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            for (int i = 0; i < phoneNumbers.size(); i++) {
                session.save(phoneNumbers.get(i));
                if (i % 50 == 0) {
                    session.flush();
                    session.clear();
                }
                //Sometimes referred to as Pokémon Exception Handling from the TV's catchphrase: Gotta catch em all.
            }
            session.flush();
            session.clear();
            tx.commit();
        }
    }
}
