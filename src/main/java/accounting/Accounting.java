package accounting;

import DAO.AcmeCdrDao;
import DAO.EnrichedCdrDao;
import DAO.OsvCdrDao;
import DTO.*;
import java.sql.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Accounting {
    private static final Long TIME_DIFFERENCE = 1000000000L;
    private static final int DURATION_DIFFERENCE = 1;
    AcmeCdrDao acmeDao;
    OsvCdrDao osvDao;
    EnrichedCdrDao enrichedCdrDao;
    List<AcmeCdr> acmeCalls;
    List<OsvCdr> osvCalls;
    List<EnrichedCdr> enrichedCalls;

    public Accounting() {
        acmeDao = new AcmeCdrDao();
        osvDao = new OsvCdrDao();
        enrichedCdrDao = new EnrichedCdrDao();
    }

    private HashMap<String, String> getAreaCodesFromDb() {
        HashMap<String, String> areaCodes = new HashMap<>();
        String queryString = "SELECT area_code, product_code from product_codes";
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection("****", "****", "****")) {
                try (Statement stmt = conn.createStatement()) {
                    ResultSet rs = stmt.executeQuery(queryString);
                    while (rs.next()) {
                        areaCodes.put(rs.getString(1), rs.getString(2));
                    }
                } catch (SQLException e) {
                    System.out.println("i have an error!!!");
                    e.printStackTrace();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
        return areaCodes;
    }

    List<AcmeCdr> getAcmeCallsByDate(String startDate, String endDate) {
        return acmeDao.getAllRowsByDate(startDate, endDate);
    }

    List<OsvCdr> getOsvCallsByDate(String startDate, String endDate) {
        List<OsvCdr> osvList = new ArrayList<>();
        return osvList;
    }

    List<AcmeCdr> fixRedirectedCalls() {
        List<AcmeCdr> fixedAcmeList = new ArrayList<>();
        return fixedAcmeList;
    }

    public void createEnrichedTable() {
        String[] dates = getLastMonthDates();
        acmeCalls = acmeDao.getAllRowsByDate(dates[0], dates[1]);
        List<EnrichedCdr> enrichedCdrList = new ArrayList<>();
        List<ProductCode> productCodesList = enrichedCdrDao.getAllRows(ProductCode.class);
        List<AreaCode> areaCodesList = enrichedCdrDao.getAllRows(AreaCode.class);
        cleanANumbers(acmeCalls);
        //(acmeCalls);
        //cleanANumbers(acmeCalls);
        for (AcmeCdr cdr : acmeCalls) {
            EnrichedCdr newCdr = new EnrichedCdr();
            newCdr.setA_number(cleanANumber(cdr.getCaller_number()));
            newCdr.setOriginal_a_number(cdr.getCaller_number());
            if (cdr.getRouting_number() != null || !cdr.getRouting_number().isEmpty()) {
                newCdr.setB_number(cleanBNumber(cdr.getRouting_number()));
            } else {
                newCdr.setB_number(cleanBNumber(cdr.getCalled_number()));
            }
//            newCdr.setB_number(cdr.getCalled_number().replace("+", ""));
            newCdr.setOriginal_b_number(cdr.getCalled_number());
            setAreaAndProductCode(newCdr, productCodesList, areaCodesList);
            newCdr.setDirection(setDirection(cdr));
            newCdr.setDuration(cdr.getCall_duration());
            newCdr.setStart_time(cdr.getConnection_time());
            newCdr.setEnd_time(cdr.getRelease_time());
            newCdr.setOut_peer_name(cdr.getOut_peer_name());
            newCdr.setIn_peer_name(cdr.getIn_peer_name());
            newCdr.setOut_sip_peer(cdr.getOut_sip_peer());
            newCdr.setIn_sip_peer(cdr.getIn_sip_peer());
            setPrice(newCdr, productCodesList);
            enrichedCdrList.add(newCdr);
        }
        enrichedCdrDao.saveBatchData(enrichedCdrList);

        //RedirectedCallRepair repair = new RedirectedCallRepair();
        //repair.startRepair(enrichedCdrList);
        //repairCalls(acmeList);
        //enrichedCdrDao.saveBatchData(enrichedCdrList);
    }

    private void setPrice(EnrichedCdr cdr, List<ProductCode> productCodes) {
        double minutePrice = 0.0;
        double minutes = cdr.getDuration() / 60.0;
        for (ProductCode code : productCodes) {
            if (code.getProduct_code() != null && cdr.getProduct_code() != null) {
                if (code.getProduct_code().toLowerCase().equals(cdr.getProduct_code().toLowerCase())) {
                    //System.out.println(minutes + "  " + code.getPrice());
                    cdr.setPrice(Math.round(minutes * code.getPrice() * 100.0) / 100.0);
                    break;
                }
            }
        }
    }

    private void setAreaAndProductCode(EnrichedCdr newCdr, List<ProductCode> productCodes, List<AreaCode> areaCodes) {
        // newCdr.setArea_code();
        // newCdr.setProduct_code();
        String areaCode = "";
        String productCode = "";
        StringBuilder bNumber = new StringBuilder(newCdr.getB_number());
        for (int i = bNumber.length() - 1; i >= 0; i--) {
            areaCode = bNumber.substring(0, i + 1);
            for (AreaCode code : areaCodes) {
                if (code.getArea_code().equals(areaCode)) {
                    newCdr.setArea_code(code.getArea_code());
                    newCdr.setProduct_code(getInvoiceProductCode(code.getProduct_code(), productCodes));
                    break;
                }
            }
        }
    }

    private String getInvoiceProductCode(String code, List<ProductCode> productCodes) {
        for (ProductCode pCode : productCodes) {
            if (pCode.getProduct_code().equals(code) && pCode.getProduct_code_for_invoice() != null) {
                code = pCode.getProduct_code_for_invoice();
            }
        }
        return code;
    }

    private void setPricing() {
        // 7303_00_000 KAT
        // 0,0312
    }

    private boolean isAreaCode(String code) {
        return false;
    }

    private String setDirection(AcmeCdr cdr) {
//        String direction = "incoming";
//        if (cdr.getOut_peer_name().toLowerCase().contains("public")) {
//            direction = "outgoing";
//        }
        return "";
    }

    private String cleanANumber(String originalNumber) {
        List<Pattern> patterns = Arrays.asList(
                Pattern.compile("^\\+372372"),
                Pattern.compile("^\\+372981\\d{4}"),
                Pattern.compile("^372372"),
                Pattern.compile("^\\+372"),
                Pattern.compile("^372"),
                Pattern.compile("^\\+"),
                Pattern.compile("^00372"),
                Pattern.compile("^00"),
                Pattern.compile("^981\\d{4}"));

        String cleanedNumber = originalNumber;

        for (Pattern p : patterns) {
            Matcher matcher = p.matcher(cleanedNumber);
            if (matcher.find()) {
                cleanedNumber = matcher.replaceFirst("");
                break;
            }
        }
        return cleanedNumber;
    }

    private String cleanBNumber(String originalNumber) {
        List<Pattern> patterns = Arrays.asList(
                Pattern.compile("^\\+"),
                Pattern.compile("^00372"),
                Pattern.compile("^912\\d{3}"),
                Pattern.compile("^981\\d{4}"));


        String cleanedNumber = originalNumber;

        for (Pattern p : patterns) {
            Matcher matcher = p.matcher(cleanedNumber);
            if (matcher.find()) {
                cleanedNumber = matcher.replaceFirst("");
                break;
            }
        }
        return cleanedNumber;
    }

    private static String[] getLastMonthDates() {
        // Olegi sapimport programmist on see meetod pärit
        // kuna ma ei taha kuupäevi ja kellaaegu enam näha ka
        String[] results = new String[2];
        YearMonth month = YearMonth.now();
        results[0] = month.atDay(1).minusMonths(1L).toString(); // minusMonths must be 1L
        results[1] = month.minusMonths(1L).atEndOfMonth().toString();
        return results;
    }

    private static String[] getMonthDates(long months) {
        // Olegi sapimport programmist on see meetod pärit
        // kuna ma ei taha kuupäevi ja kellaaegu enam näha ka
        String[] results = new String[2];
        YearMonth month = YearMonth.now();
        results[0] = month.atDay(1).minusMonths(months).toString(); // minusMonths must be 1L
        results[1] = month.minusMonths(months).atEndOfMonth().toString();
        return results;
    }

    public void cleanANumbers(List<AcmeCdr> cdrList) {
        for (AcmeCdr cdr : cdrList) {
            String aoNumber = cdr.getCaller_number();
            cdr.setCaller_number(cleanANumber(cdr.getCaller_number()));
        }
    }

    public void createCallBillAll() {
        String queryString = "select a_number, duration, ct.call_type_id , price from enriched_cdr ec left join call_types ct on ct.product_code=ec.product_code where duration > 0 and out_sip_peer in (select ip from trunks)";
    }

    public List<String> getInSipPeer() {
        return Arrays.asList("****", "****", "****");
    }
}
