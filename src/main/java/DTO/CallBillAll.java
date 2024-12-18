package DTO;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Call_Bill_All")
public class CallBillAll implements Cdr {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20)
    private String a_number;
    @Column(length = 2)
    private String call_type;
    private double price;
    private int duration;
    private int call_count;
    @Column(length = 20)
    private String in_trunk;
    @Column (length = 6)
    private LocalDateTime start_date;
    @Column (length = 6)
    private LocalDateTime end_date;

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

    public String getCall_type() {
        return call_type;
    }

    public void setCall_type(String call_type) {
        this.call_type = call_type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCall_count() {
        return call_count;
    }

    public void setCall_count(int call_count) {
        this.call_count = call_count;
    }

    public String getIn_trunk() {
        return in_trunk;
    }

    public void setIn_trunk(String in_trunk) {
        this.in_trunk = in_trunk;
    }

    public LocalDateTime getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDateTime start_date) {
        this.start_date = start_date;
    }

    public LocalDateTime getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDateTime end_date) {
        this.end_date = end_date;
    }

    @Override
    public String toString() {
        return a_number +
                ", " + call_type +
                ", " + price +
                ", " + duration +
                ", " + call_count;
    }
}
