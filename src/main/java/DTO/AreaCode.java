package DTO;

import jakarta.persistence.*;
import oracle.sql.TIMESTAMP;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "area_codes")
public class AreaCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (length = 30)
    private String area_code;
    @Column (length = 30)
    private String product_code;
    @Column (length = 120)
    private String name;
    @Column (length = 6)
    private Timestamp start_date; // connection time

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getStart_date() {
        return start_date;
    }

    public void setStart_date(Timestamp start_date) {
        this.start_date = start_date;
    }

    @Override
    public String toString() {
        return "AreaCode{" +
                "id=" + id +
                ", area_code='" + area_code + '\'' +
                ", product_code='" + product_code + '\'' +
                ", name='" + name + '\'' +
                ", start_date=" + start_date +
                '}';
    }
}
