package DTO;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Enriched_Cdr")
public class EnrichedCdr implements Cdr {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (length = 30)
    private String a_number;
    @Column (length = 30)
    private String original_a_number;
    @Column (length = 30)
    private String b_number;

    @Column (length = 30)
    private String original_b_number;
    @Column (length = 30)
    private String area_code;
    private int duration;
    @Column (length = 30)
    private String product_code;
    private double price;
    @Column (length = 6)
    private LocalDateTime start_time; // connection time
    @Column (length = 6)
    private LocalDateTime end_time; // release time
    @Column (length = 20)
    private String in_peer_name;
    @Column (length = 20)
    private String in_sip_peer;
    @Column (length = 20)
    private String out_peer_name;
    @Column (length = 20)
    private String out_sip_peer;
    @Column (length = 20)
    private String direction;

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

    public String getOriginal_a_number() {
        return original_a_number;
    }

    public void setOriginal_a_number(String original_a_number) {
        this.original_a_number = original_a_number;
    }

    public String getB_number() {
        return b_number;
    }

    public void setB_number(String b_number) {
        this.b_number = b_number;
    }

    public String getOriginal_b_number() {
        return original_b_number;
    }

    public void setOriginal_b_number(String original_b_number) {
        this.original_b_number = original_b_number;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getStart_time() {
        return start_time;
    }

    public void setStart_time(LocalDateTime start_time) {
        this.start_time = start_time;
    }

    public LocalDateTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(LocalDateTime end_time) {
        this.end_time = end_time;
    }

    public String getOut_peer_name() {
        return out_peer_name;
    }

    public void setOut_peer_name(String out_peer_name) {
        this.out_peer_name = out_peer_name;
    }

    public String getIn_peer_name() {
        return in_peer_name;
    }

    public void setIn_peer_name(String in_peer_name) {
        this.in_peer_name = in_peer_name;
    }

    public String getIn_sip_peer() {
        return in_sip_peer;
    }

    public void setIn_sip_peer(String in_sip_peer) {
        this.in_sip_peer = in_sip_peer;
    }

    public String getOut_sip_peer() {
        return out_sip_peer;
    }

    public void setOut_sip_peer(String out_sip_peer) {
        this.out_sip_peer = out_sip_peer;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "EnrichedCdr{" +
                "id=" + id +
                ", a_number='" + a_number + '\'' +
                ", original_a_number='" + original_a_number + '\'' +
                ", b_number='" + b_number + '\'' +
                ", original_b_number='" + original_b_number + '\'' +
                ", area_code='" + area_code + '\'' +
                ", duration=" + duration +
                ", product_code='" + product_code + '\'' +
                ", price=" + price +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                ", in_peer_name='" + in_peer_name + '\'' +
                ", in_sip_peer='" + in_sip_peer + '\'' +
                ", out_peer_name='" + out_peer_name + '\'' +
                ", out_sip_peer='" + out_sip_peer + '\'' +
                ", direction='" + direction + '\'' +
                '}';
    }
}
