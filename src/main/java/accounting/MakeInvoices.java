package accounting;

import DAO.CallBillAllDao;
import DTO.CallBillAll;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MakeInvoices {
    CallBillAllDao dao;

    public MakeInvoices() {
        dao = new CallBillAllDao();
    }

    public void makeInvoices() {
        List<CallBillAll> invoices = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection("****", "****", "****")) {
                try (Statement stmt = conn.createStatement()) {
                    ResultSet rs = stmt.executeQuery("select ec.a_number, ct.call_type_id as call_type, sum(ec.price) as price, sum(ec.duration) as duration, count(*) as call_count from enriched_cdr ec left join call_types ct on ct.product_code = ec.product_code where in_sip_peer in (select ip from trunks) and a_number in (select a_number from phone) and duration > 0 group by a_number, call_type_id order by a_number;");
                    while (rs.next()) {
                        CallBillAll cba = new CallBillAll();
                        cba.setA_number(rs.getString(1));
                        cba.setCall_type(rs.getString(2));
                        cba.setPrice(rs.getDouble(3));
                        cba.setDuration(rs.getInt(4));
                        cba.setCall_count(rs.getInt(5));
                        invoices.add(cba);
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        dao.saveBatchData(invoices);
    }
}