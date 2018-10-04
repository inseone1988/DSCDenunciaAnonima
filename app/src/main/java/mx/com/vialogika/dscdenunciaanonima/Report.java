package mx.com.vialogika.dscdenunciaanonima;

import android.databinding.BaseObservable;

public class Report extends BaseObservable {

    private String event_capture_timestamp = "";
    private String event_date;
    private String event_time;
    private String event_type = "Denuncia Express";
    private String event_risk_level = "red";
    private String event_evidence;
    private String event_what;
    private String event_how;
    private String event_when;
    private String event_where;
    private String event_facts = "";
    private String event_UUID;
    private String event_user;
    private String event_user_position;
    private String event_user_site;
    private String event_site_client;
    private String event_name;
    private String event_subject = "";

    public String getUUID() {
        return event_UUID;
    }

    public void setUUID(String UUID) {
        this.event_UUID = UUID;
    }

    public String getDateTime() {
        return event_capture_timestamp;
    }

    public void setDateTime(String dateTime) {
        this.event_capture_timestamp = dateTime;
    }

    public String getTitle() {
        return event_name;
    }

    public void setTitle(String title) {
        this.event_name = title;
    }

    public String getSubject() {
        return event_subject;
    }

    public void setSubject(String subject) {
        this.event_subject = subject;
    }

    public String getWhat() {
        return event_what;
    }

    public void setWhat(String what) {
        this.event_what = what;
    }

    public String getWhen() {
        return event_when;
    }

    public void setWhen(String when) {
        this.event_when = when;
    }

    public String getHow() {
        return event_how;
    }

    public void setHow(String how) {
        this.event_how = how;
    }

    public String getWhere() {
        return event_where;
    }

    public void setWhere(String where) {
        this.event_where = where;
    }

    public String getDescription() {
        return event_facts;
    }

    public void setDescription(String description) {
        this.event_facts = description;
    }

    public void setName(String name) {
        this.event_user = name;
    }

    public void setPosition(String position) {
        this.event_user_position = position;
    }

    public void setSite(String site) {
        this.event_user_site = site;
    }

    public void setClient(String client) {
        this.event_site_client = client;
    }

    public void setDate(String date) {
        this.event_date = date;
    }

    public void setTime(String time) {
        this.event_time = time;
    }

    public String getName() {
        return event_user;
    }

    public String getPosition() {
        return event_user_position;
    }

    public String getSite() {
        return event_user_site;
    }

    public String getClient() {
        return event_site_client;
    }

    public String getDate() {
        return event_date;
    }

    public String getTime() {
        return event_time;
    }
}
