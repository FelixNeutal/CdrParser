package DTO;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "Phone")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20)
    private String a_number;
    @Column(length = 20)
    private String aa_number;
    @Column(length = 8)
    private String in_trunk;
    private long customer_id;
    @Column(length = 20)
    private String phone_type;
    @Column(length = 128)
    private String owner_info;
    @Column(length = 20)
    private String category;
    @Column(length = 10)
    private String pen;
    @Column (length = 6)
    private Date b_date;
    @Column(length = 30)
    private String b_basis;
    @Column(length = 1)
    private String Private;
    @Column(length = 1)
    private String Eristus;
    @Column (length = 6)
    private Date closed;
    @Column(length = 128)
    private String notes;
    @Column (length = 6)
    private Date c_date;
    @Column(length = 50)
    private String address;
    private long groups_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getA_number() {
        return a_number;
    }

    public void setA_number(String a_number) {
        this.a_number = a_number;
    }

    public String getAa_number() {
        return aa_number;
    }

    public void setAa_number(String aa_number) {
        this.aa_number = aa_number;
    }

    public String getIn_trunk() {
        return in_trunk;
    }

    public void setIn_trunk(String in_trunk) {
        this.in_trunk = in_trunk;
    }

    public long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(long customer_id) {
        this.customer_id = customer_id;
    }

    public String getPhone_type() {
        return phone_type;
    }

    public void setPhone_type(String phone_type) {
        this.phone_type = phone_type;
    }

    public String getOwner_info() {
        return owner_info;
    }

    public void setOwner_info(String owner_info) {
        this.owner_info = owner_info;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPen() {
        return pen;
    }

    public void setPen(String pen) {
        this.pen = pen;
    }

    public Date getB_date() {
        return b_date;
    }

    public void setB_date(Date b_date) {
        this.b_date = b_date;
    }

    public String getB_basis() {
        return b_basis;
    }

    public void setB_basis(String b_basis) {
        this.b_basis = b_basis;
    }

    public String getPrivate() {
        return Private;
    }

    public void setPrivate(String aPrivate) {
        Private = aPrivate;
    }

    public String getEristus() {
        return Eristus;
    }

    public void setEristus(String eristus) {
        Eristus = eristus;
    }

    public Date getClosed() {
        return closed;
    }

    public void setClosed(Date closed) {
        this.closed = closed;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getC_date() {
        return c_date;
    }

    public void setC_date(Date c_date) {
        this.c_date = c_date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getGroups_id() {
        return groups_id;
    }

    public void setGroups_id(long groups_id) {
        this.groups_id = groups_id;
    }
}
