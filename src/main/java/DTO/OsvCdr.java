/**
 * Name: OsvCdr
 * Author: Felix Neutal
 * Description: Data transfer object for holding OSV cdr data
 */
package DTO;

import jakarta.persistence.*;
import utils.DateTimeUtils;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@Entity
@Table (name = "osv_cdr_raw")
public class OsvCdr implements Cdr {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int sequential_record_number;
    private int record_type;
    @Column (length = 6)
    private LocalDateTime start_time;
    private int duration;
    @Column (length = 30)
    private String switch_id;
    @Column (length = 70)
    private String record_id;
    @Column (length = 16)
    private String customer_id;
    private int call_segment_duration;
    @Column (length = 128)
    private String called_num;
    //private String cleaned_called_num;
    @Column (length = 128)
    private String calling_num;
    //private String cleaned_calling_num;
    @Column (length = 128)
    private String paying_party;
    private int attempt_indicator;
    private int release_cause;
    private int orig_party_id;
    private int term_party_id;
    @Column (length = 6)
    private LocalDateTime answer_time;
    @Column (length = 6)
    private LocalDateTime release_time;
    @Column (length = 6)
    private LocalDateTime incoming_leg_connect_time;
    @Column (length = 6)
    private LocalDateTime incoming_leg_release_time;
    @Column (length = 6)
    private LocalDateTime outcoming_leg_connect_time;
    @Column (length = 6)
    private LocalDateTime outcoming_leg_release_time;
    @Column (length = 100)
    private String per_call_feature;
    @Column (length = 16)
    private String forw_num;
    @Column (length = 100)
    private String orig_codec;
    @Column (length = 100)
    private String term_codec;
    @Column (length = 100)
    private String bg_orig_customer_id;
    @Column (length = 100)
    private String bg_account_code;
    @Column (length = 20)
    private String bg_orig_facility_type;
    @Column (length = 20)
    private String bg_term_facility_type;
    @Column (length = 50)
    private String bg_orig_facility_id;
    @Column (length = 50)
    private String bg_term_facility_id;
    @Column (length = 10)
    private String bg_call_completion_code;
    @Column (length = 128)
    private String original_dialled_digits;
    //private String cleaned_original_dialled_digits;
    @Column (length = 20)
    private String bg_dept_name;
    @Column (length = 10)
    private String media_type;
    @Column (length = 10)
    private String inc_phone_context;
    @Column (length = 20)
    private String call_event;
    private int secure_rtp;
    @Column (length = 64)
    private String orig_domain;
    @Column (length = 64)
    private String term_domain;
    private int traffic_type;
    private int gcid_number;
    private int gtid_number;
    @Column (length = 100)
    private String ingress_sip_endpoint;
    @Column (length = 100)
    private String egress_sip_endpoint;
    @Column (length = 128)
    private String dest_party_num;

    public OsvCdr() {
    }

    //public String getCleaned_called_num() {
    //    return cleaned_called_num;
    //}

    //public void setCleaned_called_num(String cleaned_called_num) {
    //    this.cleaned_called_num = cleaned_called_num;
    //}

    //public String getCleaned_calling_num() {
    //    return cleaned_calling_num;
    //}

    //public void setCleaned_calling_num(String cleaned_calling_num) {
    //    this.cleaned_calling_num = cleaned_calling_num;
    //}

    //public String getCleaned_original_dialled_digits() {
    //    return cleaned_original_dialled_digits;
    //}

    //public void setCleaned_original_dialled_digits(String cleaned_original_dialled_digits) {
    //    this.cleaned_original_dialled_digits = cleaned_original_dialled_digits;
    //}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSequential_record_number() {
        return sequential_record_number;
    }

    public void setSequential_record_number(String sequential_record_number) throws NumberFormatException {
        if (!sequential_record_number.equals(""))
            this.sequential_record_number = Integer.valueOf(sequential_record_number);
    }

    public int getRecord_type() {
        return record_type;
    }

    public void setRecord_type(String record_type) throws NumberFormatException {
        if (!record_type.equals(""))
            this.record_type = Integer.valueOf(record_type);
    }

    public LocalDateTime getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) throws DateTimeParseException {
        if (!start_time.equals(""))
            this.start_time = DateTimeUtils.parseOsv(start_time);
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(String duration) throws NumberFormatException {
        if (!duration.equals(""))
            this.duration = Integer.valueOf(duration);
    }

    public String getSwitch_id() {
        return switch_id;
    }

    public void setSwitch_id(String switch_id) {
        this.switch_id = switch_id;
    }

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public int getCall_segment_duration() {
        return call_segment_duration;
    }

    public void setCall_segment_duration(String call_segment_duration) throws NumberFormatException {
        if (!call_segment_duration.equals(""))
            this.call_segment_duration = Integer.valueOf(call_segment_duration);
    }

    public String getCalled_num() {
        return called_num;
    }

    public void setCalled_num(String called_num) {
        this.called_num = called_num;
       // setCleaned_called_num(cleanNumber(called_num));
    }

    public String getCalling_num() {
        return calling_num;
    }

    public void setCalling_num(String calling_num) {
        this.calling_num = calling_num;
        //setCleaned_calling_num(cleanNumber(calling_num));
    }

    public String getPaying_party() {
        return paying_party;
    }

    public void setPaying_party(String paying_party) {
        this.paying_party = paying_party;
    }

    public int getAttempt_indicator() {
        return attempt_indicator;
    }

    public void setAttempt_indicator(String attempt_indicator) throws NumberFormatException {
        if (!attempt_indicator.equals(""))
            this.attempt_indicator = Integer.valueOf(attempt_indicator);
    }

    public int getRelease_cause() {
        return release_cause;
    }

    public void setRelease_cause(String release_cause) throws NumberFormatException {
        if (!release_cause.equals(""))
            this.release_cause = Integer.valueOf(release_cause);
    }

    public int getOrig_party_id() {
        return orig_party_id;
    }

    public void setOrig_party_id(String orig_party_id) throws NumberFormatException {
        if (!orig_party_id.equals(""))
            this.orig_party_id = Integer.valueOf(orig_party_id);
    }

    public int getTerm_party_id() {
        return term_party_id;
    }

    public void setTerm_party_id(String term_party_id) throws NumberFormatException {
        if (!term_party_id.equals(""))
            this.term_party_id = Integer.valueOf(term_party_id);
    }

    public LocalDateTime getAnswer_time() {
        return answer_time;
    }

    public void setAnswer_time(String answer_time) throws DateTimeParseException {
        if (!answer_time.equals(""))
            this.answer_time = DateTimeUtils.parseOsv(answer_time);
    }

    public LocalDateTime getRelease_time() {
        return release_time;
    }

    public void setRelease_time(String release_time) throws DateTimeParseException {
        if (!release_time.equals(""))
            this.release_time = DateTimeUtils.parseOsv(release_time);
    }

    public LocalDateTime getIncoming_leg_connect_time() {
        return incoming_leg_connect_time;
    }

    public void setIncoming_leg_connect_time(String incoming_leg_connect_time) throws DateTimeParseException {
        if (!incoming_leg_connect_time.equals(""))
            this.incoming_leg_connect_time = DateTimeUtils.parseOsv(incoming_leg_connect_time);
    }

    public LocalDateTime getIncoming_leg_release_time() {
        return incoming_leg_release_time;
    }

    public void setIncoming_leg_release_time(String incoming_leg_release_time) throws DateTimeParseException {
        if (!incoming_leg_release_time.equals(""))
            this.incoming_leg_release_time = DateTimeUtils.parseOsv(incoming_leg_release_time);
    }

    public LocalDateTime getOutcoming_leg_connect_time() {
        return outcoming_leg_connect_time;
    }

    public void setOutcoming_leg_connect_time(String outcoming_leg_connect_time) throws DateTimeParseException {
        if (!outcoming_leg_connect_time.equals(""))
            this.outcoming_leg_connect_time = DateTimeUtils.parseOsv(outcoming_leg_connect_time);
    }

    public LocalDateTime getOutcoming_leg_release_time() {
        return outcoming_leg_release_time;
    }

    public void setOutcoming_leg_release_time(String outcoming_leg_release_time) throws DateTimeParseException {
        if (!outcoming_leg_release_time.equals(""))
            this.outcoming_leg_release_time = DateTimeUtils.parseOsv(outcoming_leg_release_time);
    }

    public String getPer_call_feature() {
        return per_call_feature;
    }

    public void setPer_call_feature(String per_call_feature) {
        this.per_call_feature = per_call_feature;
    }

    public String getForw_num() {
        return forw_num;
    }

    public void setForw_num(String forw_num) {
        this.forw_num = forw_num;
    }

    public String getOrig_codec() {
        return orig_codec;
    }

    public void setOrig_codec(String orig_codec) {
        this.orig_codec = orig_codec;
    }

    public String getTerm_codec() {
        return term_codec;
    }

    public void setTerm_codec(String term_codec) {
        this.term_codec = term_codec;
    }

    public String getBg_orig_customer_id() {
        return bg_orig_customer_id;
    }

    public void setBg_orig_customer_id(String bg_orig_customer_id) {
        this.bg_orig_customer_id = bg_orig_customer_id;
    }

    public String getBg_account_code() {
        return bg_account_code;
    }

    public void setBg_account_code(String bg_account_code) {
        this.bg_account_code = bg_account_code;
    }

    public String getBg_orig_facility_type() {
        return bg_orig_facility_type;
    }

    public void setBg_orig_facility_type(String bg_orig_facility_type) {
        this.bg_orig_facility_type = bg_orig_facility_type;
    }

    public String getBg_term_facility_type() {
        return bg_term_facility_type;
    }

    public void setBg_term_facility_type(String bg_term_facility_type) {
        this.bg_term_facility_type = bg_term_facility_type;
    }

    public String getBg_orig_facility_id() {
        return bg_orig_facility_id;
    }

    public void setBg_orig_facility_id(String bg_orig_facility_id) {
        this.bg_orig_facility_id = bg_orig_facility_id;
    }

    public String getBg_term_facility_id() {
        return bg_term_facility_id;
    }

    public void setBg_term_facility_id(String bg_term_facility_id) {
        this.bg_term_facility_id = bg_term_facility_id;
    }

    public String getBg_call_completion_code() {
        return bg_call_completion_code;
    }

    public void setBg_call_completion_code(String bg_call_completion_code) {
        this.bg_call_completion_code = bg_call_completion_code;
    }

    public String getOriginal_dialled_digits() {
        return original_dialled_digits;
    }

    public void setOriginal_dialled_digits(String original_dialled_digits) {
        this.original_dialled_digits = original_dialled_digits;
        //setCleaned_original_dialled_digits(cleanNumber(original_dialled_digits));
    }

    public String getBg_dept_name() {
        return bg_dept_name;
    }

    public void setBg_dept_name(String bg_dept_name) {
        this.bg_dept_name = bg_dept_name;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getInc_phone_context() {
        return inc_phone_context;
    }

    public void setInc_phone_context(String inc_phone_context) {
        this.inc_phone_context = inc_phone_context;
    }

    public String getCall_event() {
        return call_event;
    }

    public void setCall_event(String call_event) {
        this.call_event = call_event;
    }

    public int getSecure_rtp() {
        return secure_rtp;
    }

    public void setSecure_rtp(String secure_rtp) throws NumberFormatException {
        if (!secure_rtp.equals(""))
            this.secure_rtp = Integer.valueOf(secure_rtp);
    }

    public String getOrig_domain() {
        return orig_domain;
    }

    public void setOrig_domain(String orig_domain) {
        this.orig_domain = orig_domain;
    }

    public String getTerm_domain() {
        return term_domain;
    }

    public void setTerm_domain(String term_domain) {
        this.term_domain = term_domain;
    }

    public int getTraffic_type() {
        return traffic_type;
    }

    public void setTraffic_type(String traffic_type) throws NumberFormatException {
        if (!traffic_type.equals(""))
            this.traffic_type = Integer.valueOf(traffic_type);
    }

    public int getGcid_number() {
        return gcid_number;
    }

    public void setGcid_number(String gcid_number) throws NumberFormatException {
        if (!gcid_number.equals(""))
            this.gcid_number = Integer.valueOf(gcid_number);
    }

    public int getGtid_number() {
        return gtid_number;
    }

    public void setGtid_number(String gtid_number) throws NumberFormatException {
        if (!gtid_number.equals(""))
            this.gtid_number = Integer.valueOf(gtid_number);
    }

    public String getIngress_sip_endpoint() {
        return ingress_sip_endpoint;
    }

    public void setIngress_sip_endpoint(String ingress_sip_endpoint) {
        this.ingress_sip_endpoint = ingress_sip_endpoint;
    }

    public String getEgress_sip_endpoint() {
        return egress_sip_endpoint;
    }

    public void setEgress_sip_endpoint(String egress_sip_endpoint) {
        this.egress_sip_endpoint = egress_sip_endpoint;
    }

    public String getDest_party_num() {
        return dest_party_num;
    }

    public void setDest_party_num(String dest_party_num) {
        this.dest_party_num = dest_party_num;
    }

    @Override
    public String toString() {
        return "OsvCdr{" +
                "id=" + id +
                ", sequential_record_number=" + sequential_record_number +
                ", record_type=" + record_type +
                ", start_time=" + start_time +
                ", duration=" + duration +
                ", switch_id='" + switch_id + '\'' +
                ", record_id='" + record_id + '\'' +
                ", customer_id='" + customer_id + '\'' +
                ", call_segment_duration=" + call_segment_duration +
                ", called_num=" + called_num +
                ", calling_num=" + calling_num +
                ", paying_party=" + paying_party +
                ", attempt_indicator=" + attempt_indicator +
                ", release_cause=" + release_cause +
                ", orig_party_id=" + orig_party_id +
                ", term_party_id=" + term_party_id +
                ", answer_time=" + answer_time +
                ", release_time=" + release_time +
                ", incoming_leg_connect_time=" + incoming_leg_connect_time +
                ", incoming_leg_release_time=" + incoming_leg_release_time +
                ", outcoming_leg_connect_time=" + outcoming_leg_connect_time +
                ", outcoming_leg_release_time=" + outcoming_leg_release_time +
                ", per_call_feature='" + per_call_feature + '\'' +
                ", forw_num='" + forw_num + '\'' +
                ", orig_codec='" + orig_codec + '\'' +
                ", term_codec='" + term_codec + '\'' +
                ", bg_orig_customer_id='" + bg_orig_customer_id + '\'' +
                ", bg_account_code='" + bg_account_code + '\'' +
                ", bg_orig_facility_type='" + bg_orig_facility_type + '\'' +
                ", bg_term_facility_type='" + bg_term_facility_type + '\'' +
                ", bg_orig_facility_id='" + bg_orig_facility_id + '\'' +
                ", bg_term_facility_id='" + bg_term_facility_id + '\'' +
                ", bg_call_completion_code='" + bg_call_completion_code + '\'' +
                ", original_dialled_digits='" + original_dialled_digits + '\'' +
                ", bg_dept_name='" + bg_dept_name + '\'' +
                ", media_type='" + media_type + '\'' +
                ", inc_phone_context='" + inc_phone_context + '\'' +
                ", call_event='" + call_event + '\'' +
                ", secure_rtp=" + secure_rtp +
                ", orig_domain='" + orig_domain + '\'' +
                ", term_domain='" + term_domain + '\'' +
                ", traffic_type=" + traffic_type +
                ", gcid_number=" + gcid_number +
                ", gtid_number=" + gtid_number +
                ", ingress_sip_endpoint='" + ingress_sip_endpoint + '\'' +
                ", egress_sip_endpoint='" + egress_sip_endpoint + '\'' +
                ", dest_party_num='" + dest_party_num + '\'' +
                '}';
    }
}
