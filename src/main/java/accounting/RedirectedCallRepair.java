package accounting;

import DAO.AcmeCdrDao;
import DAO.EnrichedCdrDao;
import DTO.AcmeCdr;
import DTO.EnrichedCdr;
import java.sql.*;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RedirectedCallRepair {
    private EnrichedCdrDao enrichedDao;
    private AcmeCdrDao acmeDao;

    public RedirectedCallRepair() {
        enrichedDao = new EnrichedCdrDao();
        acmeDao = new AcmeCdrDao();
    }

    public void startRepair() {
        // Get broken enriched calls
        // Get acme calls list
        // Repair
        List<EnrichedCdr> brokenCalls = getBrokenCalls();
        System.out.println(brokenCalls.size() + " broken calls received");
        String[] dates = getLastMonthDates();
        List<AcmeCdr> acmeCalls = acmeDao.getAllRowsByDate(dates[0], dates[1]);
        List<EnrichedCdr> repairedCalls = repairCalls(brokenCalls, acmeCalls);
        updateDb(repairedCalls);
    }

    private List<EnrichedCdr> repairCalls(List<EnrichedCdr> brokenCalls, List<AcmeCdr> acmeCalls) {
        List<EnrichedCdr> repairedCalls = new ArrayList<>();
        int callCount = 0;
        for (EnrichedCdr brokenCall : brokenCalls) {
            for (AcmeCdr acmeCall : acmeCalls) {
                long deltaStartTime = Math.abs(brokenCall.getStart_time().toEpochSecond(ZoneOffset.UTC) - acmeCall.getConnection_time().toEpochSecond(ZoneOffset.UTC));
                long deltaEndTime = Math.abs(brokenCall.getEnd_time().toEpochSecond(ZoneOffset.UTC) - acmeCall.getRelease_time().toEpochSecond(ZoneOffset.UTC));
                int deltaDuration = Math.abs(brokenCall.getDuration() - acmeCall.getCall_duration());
                boolean sameANumbers = acmeCall.getCaller_number().contains(brokenCall.getA_number());
                boolean differentInTrunk = !brokenCall.getIn_sip_peer().equals(acmeCall.getIn_sip_peer());

                if (sameANumbers && differentInTrunk && deltaStartTime <= 1 && deltaEndTime <= 1 && deltaDuration <= 1) {
                    //repairBrokenANumber(brokenCall, call.getCalled_number());
                    callCount++;
                    brokenCall.setA_number(cleanANumber(acmeCall.getCalled_number()));
                    repairedCalls.add(brokenCall);
                }
            }
        }
        System.out.println(callCount + " calls repaired");
        return repairedCalls;
    }

    private void updateDb(List<EnrichedCdr> repairedCalls) {
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection("****", "****", "****")) {
                conn.setAutoCommit(false);
                try (PreparedStatement stmt = conn.prepareStatement("UPDATE enriched_cdr SET a_number=? WHERE id=?")) {
                    for (int i = 0; i < repairedCalls.size(); i++) {
                        stmt.setString(1, repairedCalls.get(i).getA_number());
                        stmt.setLong(2, repairedCalls.get(i).getId());
                        stmt.addBatch();
                        if (i % 100 == 0) {
                            stmt.executeBatch();
                            conn.commit();
                        }
                    }
                    stmt.executeBatch();
                    conn.commit();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<EnrichedCdr> getBrokenCalls() {
        List<EnrichedCdr> brokenCalls = new ArrayList<>();
        // String queryString = "select a_number, b_number, duration, in_sip_peer, start_time, end_time, id from enriched_cdr ec where in_sip_peer in (select ip from trunks) and a_number not similar to '69[0-6]%' and a_number not in (select a_number from phone)";
        String queryString = "select a_number, b_number, duration, in_sip_peer, start_time, end_time, id from enriched_cdr ec where in_sip_peer in (select ip from trunks) and a_number not in (select a_number from phone)";
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection("****", "****", "****")) {
                try (Statement stmt = conn.createStatement()) {
                    ResultSet rs = stmt.executeQuery(queryString);
                    while (rs.next()) {
                        EnrichedCdr singleCall = new EnrichedCdr();
                        singleCall.setA_number(rs.getString(1));
                        singleCall.setB_number(rs.getString(2));
                        singleCall.setDuration(rs.getInt(3));
                        singleCall.setIn_sip_peer(rs.getString(4));
                        singleCall.setStart_time(rs.getTimestamp(5).toLocalDateTime());
                        singleCall.setEnd_time(rs.getTimestamp(6).toLocalDateTime());
                        singleCall.setId(rs.getLong(7));
                        brokenCalls.add(singleCall);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return brokenCalls;
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

    private String cleanANumber(String originalNumber) {
        List<Pattern> patterns = Arrays.asList(
                Pattern.compile("^\\+372981\\d{4}"),
                Pattern.compile("^\\+372372"),
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
}
