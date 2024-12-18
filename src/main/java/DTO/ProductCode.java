package DTO;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "product_codes")
public class ProductCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (length = 30)
    private String product_code;
    @Column (length = 30)
    private String product_code_for_invoice;
    @Column (length = 30)
    private String saf_code;
    @Column (length = 30)
    private String price_listing;
    @Column (length = 30)
    private String unit;
    @Column (length = 30)
    private double price;
    @Column (length = 120)
    private String description;
    @Column (length = 6)
    private Timestamp start_date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_code_for_invoice() {
        return product_code_for_invoice;
    }

    public void setProduct_code_for_invoice(String product_code_for_invoice) {
        this.product_code_for_invoice = product_code_for_invoice;
    }

    public String getSaf_code() {
        return saf_code;
    }

    public void setSaf_code(String saf_code) {
        this.saf_code = saf_code;
    }

    public String getPrice_listing() {
        return price_listing;
    }

    public void setPrice_listing(String price_listing) {
        this.price_listing = price_listing;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getStart_date() {
        return start_date;
    }

    public void setStart_date(Timestamp start_date) {
        this.start_date = start_date;
    }

    @Override
    public String toString() {
        return "ProductCode{" +
                "id=" + id +
                ", product_code='" + product_code + '\'' +
                ", product_code_for_invoice='" + product_code_for_invoice + '\'' +
                ", saf_code='" + saf_code + '\'' +
                ", price_listing='" + price_listing + '\'' +
                ", unit='" + unit + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", start_date=" + start_date +
                '}';
    }
}
