/**
 * Name: AcmeCdr
 * Author: Felix Neutal
 * Description: Data transfer object for holding Acme cdr data
 */
package DTO;

import jakarta.persistence.*;
import utils.DateTimeUtils;

import java.time.LocalDateTime;

@Entity
@Table (name = "acme_cdr_raw")
public class AcmeCdr implements Cdr {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private int call_duration;
    @Column (length = 6)
    private LocalDateTime start_time;
    @Column (length = 6)
    private LocalDateTime connection_time;
    @Column (length = 6)
    private LocalDateTime release_time;
    @Column (length = 30)
    private String called_number;
    @Column (length = 30)
    private String egress_routing_number;
    @Column (length = 30)
    private String caller_number;
    @Column (length = 30)
    private String p_asserted_id;
    private int release_cause;
    @Column (length = 50)
    private String release_description;
    @Column (length = 20)
    private String out_peer_name;
    @Column (length = 20)
    private String in_peer_name;
    @Column (length = 20)
    private String in_sip_peer;
    @Column (length = 20)
    private String out_sip_peer;
    @Column (length = 20)
    private String in_type;
    @Column (length = 30)
    private String routing_number;

    @Column (length = 30)
    private String sip_diversion;

    public AcmeCdr() {
    }

    public String getSip_diversion() {
        return sip_diversion;
    }

    public void setSip_diversion(String sip_diversion) {
        this.sip_diversion = sip_diversion;
    }

    public String getRouting_number(){
        return routing_number;
    }

    public void setRouting_number(String routing_number) {
        this.routing_number = routing_number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCall_duration() {
        return call_duration;
    }

    public void setCall_duration(String call_duration) {
        this.call_duration = Integer.valueOf(call_duration);
    }

    public LocalDateTime getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = DateTimeUtils.parseAcme(start_time);
    }

    public LocalDateTime getConnection_time() {
        return connection_time;
    }

    public void setConnection_time(String connection_time) {
        this.connection_time = DateTimeUtils.parseAcme(connection_time);
    }

    public LocalDateTime getRelease_time() {
        return release_time;
    }

    public void setRelease_time(String release_time) {
        this.release_time = DateTimeUtils.parseAcme(release_time);
    }

    public String getCalled_number() {
        return called_number;
    }

    public void setCalled_number(String called_number) {
        this.called_number = called_number;
    }

    public String getEgress_routing_number() {
        return egress_routing_number;
    }

    public void setEgress_routing_number(String egress_routing_number) {
        this.egress_routing_number = egress_routing_number;
    }

    public String getCaller_number() {
        return caller_number;
    }

    public void setCaller_number(String caller_number) {
        this.caller_number = caller_number;
    }

    public String getP_asserted_id() {
        return p_asserted_id;
    }

    public void setP_asserted_id(String p_asserted_id) {
        this.p_asserted_id = p_asserted_id;
    }

    public int getRelease_cause() {
        return release_cause;
    }

    public void setRelease_cause(String release_cause) {
        this.release_cause = Integer.valueOf(release_cause);
    }

    public String getRelease_description() {
        return release_description;
    }

    public void setRelease_description(String release_description) {
        this.release_description = release_description;
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

    public String getIn_type() {
        return in_type;
    }

    public void setIn_type(String in_type) {
        this.in_type = in_type;
    }

    @Override
    public String toString() {
        return "AcmeCdr{" +
                "id=" + id +
                ", call_duration=" + call_duration +
                ", start_time=" + start_time +
                ", connection_time=" + connection_time +
                ", release_time=" + release_time +
                ", called_number='" + called_number + '\'' +
                ", egress_routing_number='" + egress_routing_number + '\'' +
                ", caller_number='" + caller_number + '\'' +
                ", p_asserted_id='" + p_asserted_id + '\'' +
                ", release_cause=" + release_cause +
                ", release_description='" + release_description + '\'' +
                ", out_peer_name='" + out_peer_name + '\'' +
                ", in_peer_name='" + in_peer_name + '\'' +
                ", in_sip_peer='" + in_sip_peer + '\'' +
                ", out_sip_peer='" + out_sip_peer + '\'' +
                ", in_type='" + in_type + '\'' +
                ", routing_number='" + routing_number + '\'' +
                ", sip_diversion='" + sip_diversion + '\'' +
                '}';
    }
}